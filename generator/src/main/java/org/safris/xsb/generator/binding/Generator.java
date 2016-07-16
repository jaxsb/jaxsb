/* Copyright (c) 2006 Seva Safris
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

package org.safris.xsb.generator.binding;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;

import org.safris.commons.io.Files;
import org.safris.commons.lang.Paths;
import org.safris.commons.net.URLs;
import org.safris.commons.pipeline.Pipeline;
import org.safris.commons.xml.dom.DOMParsers;
import org.safris.maven.common.Resolver;
import org.safris.xsb.compiler.lang.CompilerFailureException;
import org.safris.xsb.compiler.processor.plan.Plan;
import org.safris.xsb.compiler.processor.plan.PlanDirectory;
import org.safris.xsb.compiler.processor.write.Writer;
import org.safris.xsb.compiler.processor.write.WriterDirectory;
import org.safris.xsb.compiler.runtime.BindingError;
import org.safris.xsb.generator.processor.bundle.Bundle;
import org.safris.xsb.generator.processor.bundle.BundleDirectory;
import org.safris.xsb.generator.processor.timestamp.TimestampDirectory;
import org.safris.xsb.generator.schema.SchemaDocumentDirectory;
import org.safris.xsb.lexer.processor.GeneratorContext;
import org.safris.xsb.lexer.processor.composite.SchemaComposite;
import org.safris.xsb.lexer.processor.composite.SchemaCompositeDirectory;
import org.safris.xsb.lexer.processor.document.SchemaDocument;
import org.safris.xsb.lexer.processor.model.Model;
import org.safris.xsb.lexer.processor.model.ModelDirectory;
import org.safris.xsb.lexer.processor.normalize.Normalizer;
import org.safris.xsb.lexer.processor.normalize.NormalizerDirectory;
import org.safris.xsb.lexer.processor.reference.SchemaReference;
import org.safris.xsb.lexer.processor.reference.SchemaReferenceDirectory;
import org.w3.x2001.xmlschema.xe.$xs_boolean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class Generator extends AbstractGenerator {
  private static void trapPrintUsage() {
    System.err.println("Usage: Generator [OPTIONS] <-d DEST_DIR> <SCHEMA_XSD>");
    System.err.println("");
    System.err.println("Mandatory arguments:");
    System.err.println("  -d <destDir>    Specify the destination directory.");
    System.err.println("");
    System.err.println("Optional arguments:");
    System.err.println("  --explodeJars   Explode generated jars into the destination directory.");
    System.err.println("  --overwrite     Overwrite all existing generated classes.");
    System.exit(1);
  }

  public static void main(final String[] args) {
    if (args.length == 0 || args[0] == null || args[0].length() == 0)
      trapPrintUsage();

    boolean explodeJars = false;
    boolean overwrite = false;
    File destDir = null;
    final Collection<SchemaReference> schemas = new HashSet<SchemaReference>();
    for (int i = 0; i < args.length; i++) {
      if ("--explodeJars".equals(args[i]))
        explodeJars = true;
      else if ("--overwrite".equals(args[i]))
        overwrite = true;
      else if ("-d".equals(args[i]) && i < args.length)
        destDir = new File(args[++i]).getAbsoluteFile();
      else
        schemas.add(new SchemaReference(args[i], false));
    }

    if (destDir == null)
      destDir = Files.getCwd();

    final GeneratorContext generatorContext = new GeneratorContext(System.currentTimeMillis(), destDir, explodeJars, overwrite);
    final Generator generator = new Generator(generatorContext, schemas);
    generator.generate();
  }

  private static final String MANIFEST_ERROR = "There is an error in your binding xml. Please consult manifest.xsd for proper usage.";
  private final GeneratorContext generatorContext;
  private final Collection<SchemaReference> schemas;

  public Generator(final GeneratorContext generatorContext, final Collection<SchemaReference> schemas) {
    this.generatorContext = generatorContext;
    this.schemas = schemas;
  }

  public Generator(final File basedir, final Element bindingsElement, long lastModified, final Resolver<String> resolver) {
    this.schemas = new HashSet<SchemaReference>();
    this.generatorContext = parseConfig(basedir, bindingsElement, lastModified, resolver);
  }

  public GeneratorContext getGeneratorContext() {
    return generatorContext;
  }

  public Collection<SchemaReference> getSchemas() {
    return schemas;
  }

  public GeneratorContext parseConfig(final File basedir, final Element bindingsElement, long lastModified, final Resolver<String> resolver) {
    if (!"manifest".equals(bindingsElement.getNodeName()))
      throw new IllegalArgumentException("Invalid manifest element!");

    File destDir = null;
    boolean explodeJars = false;
    boolean overwrite = false;

    URL hrefURL = null;
    final NodeList list = bindingsElement.getChildNodes();
    for (int i = 0; i < list.getLength(); i++) {
      String schemaReference = null;
      final Node child = list.item(i);
      if ("schemas".equals(child.getNodeName())) {
        NodeList schemaNodes = child.getChildNodes();
        for (int j = 0; j < schemaNodes.getLength(); j++) {
          Node schemaNode = schemaNodes.item(j);
          if (!"schema".equals(schemaNode.getLocalName()))
            continue;

          NodeList text = schemaNode.getChildNodes();
          for (int k = 0; k < text.getLength(); k++) {
            final Node node = text.item(k);
            if (node.getNodeType() != Node.TEXT_NODE)
              continue;

            schemaReference = resolver.resolve(node.getNodeValue());
            break;
          }

          if (schemaReference.length() != 0 && !Paths.isAbsolute(schemaReference))
            schemaReference = basedir.getAbsolutePath() + File.separator + schemaReference;

          schemas.add(new SchemaReference(resolver.resolve(schemaReference), false));
        }
      }
      else if (destDir == null && "destdir".equals(child.getLocalName())) {
        final NodeList text = child.getChildNodes();
        for (int j = 0; j < text.getLength(); j++) {
          final Node node = text.item(j);
          if (node.getNodeType() != Node.TEXT_NODE)
            continue;

          destDir = new File(resolver.resolve(node.getNodeValue()));
          final NamedNodeMap attributes = child.getAttributes();
          if (attributes != null) {
            for (int k = 0; k < attributes.getLength(); k++) {
              final Node attribute = attributes.item(k);
              if ("explodeJars".equals(attribute.getLocalName()))
                explodeJars = $xs_boolean.parseBoolean(attribute.getNodeValue());
              else if ("overwrite".equals(attribute.getLocalName()))
                overwrite = $xs_boolean.parseBoolean(attribute.getNodeValue());
            }
          }

          break;
        }
      }
      else if (hrefURL == null && "link".equals(child.getLocalName())) {
        final NamedNodeMap attributes = child.getAttributes();
        Node hrefNode = null;
        if (attributes == null || (hrefNode = attributes.getNamedItemNS("http://www.w3.org/1999/xlink", "href")) == null || hrefNode.getNodeValue().length() == 0)
          throw new BindingError(MANIFEST_ERROR);

        final String href = resolver.resolve(hrefNode.getNodeValue());
        try {
          if (basedir != null)
            hrefURL = URLs.makeUrlFromPath(basedir.getAbsolutePath(), href);
          else
            hrefURL = URLs.makeUrlFromPath(href);
        }
        catch (final MalformedURLException e) {
          throw new CompilerFailureException(e);
        }
      }
      else if (child.getNodeType() == Node.ELEMENT_NODE)
        throw new BindingError(MANIFEST_ERROR);
    }

    if (hrefURL != null) {
      if (destDir != null || schemas.size() != 0)
        throw new BindingError(MANIFEST_ERROR);

      long modified = 0;
      final Document document;
      try {
        final DocumentBuilder documentBuilder = DOMParsers.newDocumentBuilder();
        final URLConnection connection = hrefURL.openConnection();
        modified = connection.getLastModified();
        document = documentBuilder.parse(connection.getInputStream());
      }
      catch (final Exception e) {
        throw new CompilerFailureException(e);
      }

      return parseConfig(basedir, document.getDocumentElement(), modified, resolver);
    }

    return new GeneratorContext(lastModified, destDir, explodeJars, overwrite);
  }

  public Collection<Bundle> generate() {
    final Pipeline<GeneratorContext> pipeline = new Pipeline<GeneratorContext>(generatorContext);

    // select the schemas to be generated and exit if no schemas need work
    final Collection<SchemaReference> schemaReferences = new ArrayList<SchemaReference>();
    pipeline.<SchemaReference,SchemaReference>addProcessor(schemas, schemaReferences, new SchemaReferenceDirectory());

    // prepare the schemas to be worked on and build the dependency graph
    final Collection<SchemaDocument> schemaDocuments = new ArrayList<SchemaDocument>();
    pipeline.<SchemaReference,SchemaDocument>addProcessor(schemaReferences, schemaDocuments, new SchemaDocumentDirectory());

    // bridge the dependency structure within the framework
    final Collection<SchemaComposite> schemaComposites = new ArrayList<SchemaComposite>();
    pipeline.<SchemaDocument,SchemaComposite>addProcessor(schemaDocuments, schemaComposites, new SchemaCompositeDirectory());

    // model the schema elements using Model objects
    final Collection<Model> models = new ArrayList<Model>();
    pipeline.<SchemaComposite,Model>addProcessor(schemaComposites, models, new ModelDirectory());

    // normalize the models
    pipeline.<Model,Normalizer<?>>addProcessor(models, null, new NormalizerDirectory());

    // plan the schema elements using Plan objects
    final Collection<Plan<?>> plans = new ArrayList<Plan<?>>();
    pipeline.<Model,Plan<?>>addProcessor(models, plans, new PlanDirectory());

    // write the plans to files
    pipeline.<Plan<?>,Writer<?>>addProcessor(plans, null, new WriterDirectory());

    // compile and jar the bindings
    final Collection<Bundle> bundles = new ArrayList<Bundle>();
    pipeline.<SchemaComposite,Bundle>addProcessor(schemaComposites, bundles, new BundleDirectory());

    // timestamp the generated files and directories
    pipeline.<Bundle,Bundle>addProcessor(bundles, null, new TimestampDirectory());

    // start the pipeline
    pipeline.begin();

    return bundles;
  }
}