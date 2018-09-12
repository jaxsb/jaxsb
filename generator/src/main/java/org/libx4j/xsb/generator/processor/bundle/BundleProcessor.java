/* Copyright (c) 2008 lib4j
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

package org.libx4j.xsb.generator.processor.bundle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.XMLConstants;

import org.lib4j.io.Files;
import org.lib4j.jci.CompilationException;
import org.lib4j.jci.JavaCompiler;
import org.fastjax.net.URLs;
import org.lib4j.pipeline.PipelineDirectory;
import org.lib4j.pipeline.PipelineEntity;
import org.lib4j.pipeline.PipelineProcessor;
import org.fastjax.util.Collections;
import org.fastjax.util.Paths;
import org.fastjax.util.jar.Jar;
import org.lib4j.xml.ValidationException;
import org.lib4j.xml.datatype.HexBinary;
import org.lib4j.xml.dom.DOMParsers;
import org.lib4j.xml.dom.DOMStyle;
import org.lib4j.xml.dom.DOMs;
import org.lib4j.xml.dom.Validator;
import org.libx4j.xsb.compiler.lang.NamespaceBinding;
import org.libx4j.xsb.compiler.lang.NamespaceURI;
import org.libx4j.xsb.compiler.processor.GeneratorContext;
import org.libx4j.xsb.compiler.processor.composite.SchemaComposite;
import org.libx4j.xsb.compiler.processor.composite.SchemaModelComposite;
import org.libx4j.xsb.runtime.Binding;
import org.libx4j.xsb.runtime.CompilerFailureException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class BundleProcessor implements PipelineEntity, PipelineProcessor<GeneratorContext,SchemaComposite,Bundle> {
  private static void compile(final Collection<SchemaComposite> documents, final File destDir, final File sourceDir, final Set<File> sourcePath) throws CompilationException, IOException, URISyntaxException {
    final Collection<File> classpath = sourcePath != null ? sourcePath : new ArrayList<>(2);
    final Class<?>[] requiredLibs = {Binding.class, Collections.class, HexBinary.class, NamespaceBinding.class, ValidationException.class, Validator.class};
    for (final Class<?> file : requiredLibs)
      classpath.add(new File(file.getProtectionDomain().getCodeSource().getLocation().toURI()));

    final File[] sources = new File[documents.size()];
    final Iterator<SchemaComposite> iterator = documents.iterator();
    for (int i = 0; i < sources.length; i++)
      // FIXME: This is duplicated in SchemaReferenceProcessor[62]
      sources[i] = new File(sourceDir, ((SchemaModelComposite)iterator.next()).getSchemaModel().getTargetNamespace().getNamespaceBinding().getClassName().replace('.', File.separatorChar) + ".java");

    new JavaCompiler(destDir, classpath).compile(sources);
  }

  private static Collection<File> jar(final File destDir, final boolean isJar, final Collection<SchemaComposite> schemaComposites, final Set<NamespaceURI> includes, final Set<NamespaceURI> excludes) throws IOException, SAXException {
    final Set<NamespaceURI> namespaceURIsAdded = new HashSet<>();
    final Collection<File> jarFiles = new HashSet<>();

    for (final SchemaComposite schemaComposite : schemaComposites) {
      final SchemaModelComposite schemaModelComposite = (SchemaModelComposite)schemaComposite;
      final NamespaceURI namespaceURI = schemaModelComposite.getSchemaDocument().getSchemaReference().getNamespaceURI();
      if ((includes == null || includes.contains(namespaceURI)) && (excludes == null || !excludes.contains(namespaceURI))) {
        final String packageName = namespaceURI.getNamespaceBinding().getPackageName();
        final String packagePath = packageName.replace('.', '/');
        final Jar jar;
        if (isJar) {
          final File jarFile = new File(destDir, packageName + ".jar");
          if (jarFile.exists() && !jarFile.delete())
            throw new IOException("Unable to delete existing jar: " + jarFile.getAbsolutePath());

          jar = new Jar(jarFile);
          jarFiles.add(jarFile);

          if (!namespaceURIsAdded.contains(namespaceURI)) {
            namespaceURIsAdded.add(namespaceURI);

            final Collection<File> files = Files.listAll(new File(destDir, packagePath));
            if (files != null)
              for (final File file : files)
                if (!file.isDirectory() && (file.getName().endsWith(".java") || file.getName().endsWith(".class")))
                  jar.addEntry(Files.relativePath(destDir.getAbsoluteFile(), file.getAbsoluteFile()), java.nio.file.Files.readAllBytes(file.toPath()));
          }
        }
        else {
          jar = null;
        }

        if (!schemaModelComposite.getSchemaDocument().getSchemaReference().isInclude())
          addXSDs(schemaModelComposite.getSchemaDocument().getSchemaReference().getURL(), packagePath + '/' + packageName + ".xsd", jar, destDir, 0);

        if (jar != null)
          jar.close();
      }
    }

    return jarFiles;
  }

  private static void addXSDs(final URL url, final String filePath, final Jar jar, final File destDir, int includeCount) throws IOException, SAXException {
    final String baseDir = Paths.getCanonicalParent(filePath);
    String relativeRootPath = null;
    final Element element = DOMParsers.newDocumentBuilder().parse(url.openStream()).getDocumentElement();
    final NodeList children = element.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      final Node node = children.item(i);
      if (!XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(node.getNamespaceURI()))
        continue;

      if ("import".equals(node.getLocalName())) {
        final NamedNodeMap attributes = node.getAttributes();
        Node namespace = null;
        Node schemaLocation = null;
        for (int j = 0; j < attributes.getLength(); j++) {
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
        for (int j = 0; j < attributes.getLength(); j++) {
          final Node attribute = attributes.item(j);
          if ("schemaLocation".equals(attribute.getLocalName())) {
            final String schemaLocation = attribute.getNodeValue();
            final URL includeURL = URLs.isAbsolute(schemaLocation) ? new URL(schemaLocation) : Paths.isAbsolute(schemaLocation) ? URLs.makeCanonicalUrlFromPath(schemaLocation) : URLs.makeUrlFromPath(URLs.getCanonicalParent(url), schemaLocation);
            final String includePath = filePath.replace(".xsd", "-" + ++includeCount + ".xsd");
            attribute.setNodeValue(Paths.getName(includePath));
            addXSDs(includeURL, includePath, jar, destDir, includeCount);
          }
        }
      }
    }

    final byte[] bytes = DOMs.domToString(element, DOMStyle.INDENT).getBytes();
    if (jar != null)
      jar.addEntry(filePath, bytes);

    java.nio.file.Files.write(new File(destDir, filePath).toPath(), bytes);
  }

  private final Set<File> sourcePath;

  public BundleProcessor(final Set<File> sourcePath) {
    this.sourcePath = sourcePath;
  }

  @Override
  public Collection<Bundle> process(final GeneratorContext pipelineContext, final Collection<SchemaComposite> documents, final PipelineDirectory<GeneratorContext,SchemaComposite,Bundle> directory) {
    try {
      if (pipelineContext.getCompileDir() != null)
        BundleProcessor.compile(documents, pipelineContext.getCompileDir(), pipelineContext.getDestDir(), sourcePath);

      final Collection<Bundle> bundles = new ArrayList<>();
      final Collection<File> jarFiles = BundleProcessor.jar(pipelineContext.getDestDir(), pipelineContext.getPackage(), documents, pipelineContext.getIncludes(), pipelineContext.getExcludes());
      for (final File jarFile : jarFiles)
        bundles.add(new Bundle(jarFile));

      return bundles;
    }
    catch (final CompilationException | IOException | SAXException | URISyntaxException e) {
      throw new CompilerFailureException(e);
    }
  }
}