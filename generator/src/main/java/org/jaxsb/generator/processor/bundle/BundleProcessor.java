/* Copyright (c) 2008 JAX-SB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxsb.generator.processor.bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarOutputStream;

import javax.annotation.Generated;
import javax.xml.XMLConstants;

import org.apache.xerces.jaxp.JAXPConstants;
import org.jaxsb.compiler.lang.NamespaceBinding;
import org.jaxsb.compiler.lang.NamespaceURI;
import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.composite.SchemaComposite;
import org.jaxsb.compiler.processor.composite.SchemaModelComposite;
import org.jaxsb.runtime.Binding;
import org.jaxsb.runtime.CompilerFailureException;
import org.libj.jci.CompilationException;
import org.libj.jci.InMemoryCompiler;
import org.libj.net.URLs;
import org.libj.util.CollectionUtil;
import org.libj.util.Paths;
import org.libj.util.function.Throwing;
import org.libj.util.zip.ZipWriter;
import org.openjax.xml.api.ValidationException;
import org.openjax.xml.datatype.HexBinary;
import org.openjax.xml.dom.DOMParsers;
import org.openjax.xml.dom.DOMStyle;
import org.openjax.xml.dom.DOMs;
import org.openjax.xml.dom.Validator;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class BundleProcessor implements PipelineEntity, PipelineProcessor<GeneratorContext,SchemaComposite,Bundle> {
  private static void compile(final Collection<SchemaComposite> documents, final File destDir, final File sourceDir, final Set<File> sourcePath) throws CompilationException, IOException, URISyntaxException {
    final List<File> classpath = sourcePath != null ? new ArrayList<>(sourcePath) : new ArrayList<>(2);
    final Class<?>[] requiredLibs = {Binding.class, CollectionUtil.class, Generated.class, HexBinary.class, JAXPConstants.class, NamespaceBinding.class, ValidationException.class, Validator.class};
    for (final Class<?> cls : requiredLibs) {
      final CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
      if (codeSource != null)
        classpath.add(new File(codeSource.getLocation().toURI()));
    }

    final File[] sources = new File[documents.size()];
    final Iterator<SchemaComposite> iterator = documents.iterator();
    for (int i = 0; i < sources.length; ++i)
      // FIXME: This is duplicated in SchemaReferenceProcessor[62]
      sources[i] = new File(sourceDir, ((SchemaModelComposite)iterator.next()).getSchemaModel().getTargetNamespace().getNamespaceBinding().getClassName().replace('.', File.separatorChar) + ".java");

    final InMemoryCompiler compiler = new InMemoryCompiler();
    for (final File source : sources)
      compiler.addSource(new String(Files.readAllBytes(source.toPath())));

    compiler.compile(classpath, destDir, "-g");
  }

  @SuppressWarnings("resource")
  private static Collection<File> jar(final File destDir, final boolean isJar, final Collection<SchemaComposite> schemaComposites, final Set<NamespaceURI> includes, final Set<NamespaceURI> excludes, final boolean skipXsd) throws IOException, SAXException {
    final Set<NamespaceURI> namespaceURIsAdded = new HashSet<>();
    final Collection<File> jarFiles = new HashSet<>();

    for (final SchemaComposite schemaComposite : schemaComposites) {
      final SchemaModelComposite schemaModelComposite = (SchemaModelComposite)schemaComposite;
      final NamespaceURI namespaceURI = schemaModelComposite.getSchemaDocument().getSchemaReference().getNamespaceURI();
      if ((includes == null || includes.contains(namespaceURI)) && (excludes == null || !excludes.contains(namespaceURI))) {
        final String packageName = namespaceURI.getNamespaceBinding().getPackageName();
        final String packagePath = packageName.replace('.', '/');
        final ZipWriter destJar;
        if (isJar) {
          final File jarFile = new File(destDir, packageName + ".jar");
          if (jarFile.exists() && !jarFile.delete())
            throw new IOException("Unable to delete existing jar: " + jarFile.getAbsolutePath());

          if (!jarFile.getParentFile().exists())
            jarFile.getParentFile().mkdirs();

          destJar = new ZipWriter(new JarOutputStream(new FileOutputStream(jarFile)));
          jarFiles.add(jarFile);

          if (!namespaceURIsAdded.contains(namespaceURI)) {
            namespaceURIsAdded.add(namespaceURI);

            Files.walk(new File(destDir, packagePath).toPath()).forEach(Throwing.rethrow(p -> {
              final File file = p.toFile();
              if (!file.isDirectory() && (file.getName().endsWith(".java") || file.getName().endsWith(".class")))
                destJar.write(destDir.toPath().relativize(file.toPath()).toString(), Files.readAllBytes(file.toPath()));
            }));
          }
        }
        else {
          destJar = null;
        }

        if (!skipXsd && !schemaModelComposite.getSchemaDocument().getSchemaReference().isInclude())
          addXSDs(schemaModelComposite.getSchemaDocument().getSchemaReference().getURL(), packagePath + '/' + packageName + ".xsd", destJar, destDir, 0);

        if (destJar != null)
          destJar.close();
      }
    }

    return jarFiles;
  }

  private static void addXSDs(final URL url, final String filePath, final ZipWriter ZipWriter, final File destDir, int includeCount) throws IOException, SAXException {
    final String baseDir = Paths.getCanonicalParent(filePath);
    String relativeRootPath = null;
    final Element element = DOMParsers.newDocumentBuilder().parse(url.openStream()).getDocumentElement();
    final NodeList children = element.getChildNodes();
    for (int i = 0; i < children.getLength(); ++i) {
      final Node node = children.item(i);
      if (!XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(node.getNamespaceURI()))
        continue;

      if ("import".equals(node.getLocalName())) {
        final NamedNodeMap attributes = node.getAttributes();
        Node namespace = null;
        Node schemaLocation = null;
        for (int j = 0; j < attributes.getLength(); ++j) {
          final Node attribute = attributes.item(j);
          if ("namespace".equals(attribute.getLocalName()))
            namespace = attribute;
          else if ("schemaLocation".equals(attribute.getLocalName()))
            schemaLocation = attribute;

          if (namespace != null && schemaLocation != null) {
            final NamespaceURI namespaceURI = NamespaceURI.getInstance(namespace.getNodeValue());
            final String packagePath = namespaceURI.getNamespaceBinding().getPackageName().replace('.', '/');
            final String schemaPath;
            if (baseDir.equals(packagePath)) {
              schemaPath = baseDir;
            }
            else {
              if (relativeRootPath == null) {
                relativeRootPath = "";
                for (int k = 0; k != -1; k = baseDir.indexOf('/', k + 1))
                  relativeRootPath += "../";
              }

              schemaPath = Paths.canonicalize(relativeRootPath + packagePath);
            }

            schemaLocation.setNodeValue(schemaPath + "/" + namespaceURI.getNamespaceBinding().getPackageName() + ".xsd");
            namespace = null;
            schemaLocation = null;
            break;
          }
        }
      }
      else if ("include".equals(node.getLocalName())) {
        final NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
          final Node attribute = attributes.item(j);
          if ("schemaLocation".equals(attribute.getLocalName())) {
            final String schemaLocation = attribute.getNodeValue();
            final URL includeURL = Paths.isAbsolute(schemaLocation) ? new URL(schemaLocation) : Paths.isAbsoluteLocal(schemaLocation) ? URLs.toCanonicalURL(schemaLocation) : URLs.toURL(URLs.getCanonicalParent(url), schemaLocation);
            final String includePath = filePath.replace(".xsd", "-" + ++includeCount + ".xsd");
            attribute.setNodeValue(Paths.getName(includePath));
            addXSDs(includeURL, includePath, ZipWriter, destDir, includeCount);
          }
        }
      }
    }

    final byte[] bytes = DOMs.domToString(element, DOMStyle.INDENT).getBytes();
    if (ZipWriter != null)
      ZipWriter.write(filePath, bytes);

    Files.write(new File(destDir, filePath).toPath(), bytes);
  }

  private final Set<File> sourcePath;
  private final boolean skipXsd;

  public BundleProcessor(final Set<File> sourcePath, final boolean skipXsd) {
    this.sourcePath = sourcePath;
    this.skipXsd = skipXsd;
  }

  @Override
  public Collection<Bundle> process(final GeneratorContext pipelineContext, final Collection<SchemaComposite> documents, final PipelineDirectory<GeneratorContext,SchemaComposite,Bundle> directory) throws IOException {
    try {
      if (pipelineContext.getCompileDir() != null)
        BundleProcessor.compile(documents, pipelineContext.getCompileDir(), pipelineContext.getDestDir(), sourcePath);

      final Collection<Bundle> bundles = new ArrayList<>();
      final Collection<File> jarFiles = BundleProcessor.jar(pipelineContext.getDestDir(), pipelineContext.getPackage(), documents, pipelineContext.getIncludes(), pipelineContext.getExcludes(), skipXsd);
      for (final File jarFile : jarFiles)
        bundles.add(new Bundle(jarFile));

      return bundles;
    }
    catch (final CompilationException | SAXException | URISyntaxException e) {
      throw new CompilerFailureException(e);
    }
  }
}