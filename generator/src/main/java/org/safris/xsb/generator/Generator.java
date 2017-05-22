/* Copyright (c) 2006 lib4j
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

package org.safris.xsb.generator;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.safris.commons.io.Files;
import org.safris.commons.lang.Paths;
import org.safris.commons.net.URLs;
import org.safris.commons.pipeline.Pipeline;
import org.safris.xsb.compiler.processor.GeneratorContext;
import org.safris.xsb.compiler.processor.composite.SchemaComposite;
import org.safris.xsb.compiler.processor.composite.SchemaCompositeDirectory;
import org.safris.xsb.compiler.processor.document.SchemaDocument;
import org.safris.xsb.compiler.processor.model.Model;
import org.safris.xsb.compiler.processor.model.ModelDirectory;
import org.safris.xsb.compiler.processor.normalize.Normalizer;
import org.safris.xsb.compiler.processor.normalize.NormalizerDirectory;
import org.safris.xsb.compiler.processor.reference.SchemaReference;
import org.safris.xsb.compiler.processor.reference.SchemaReferenceDirectory;
import org.safris.xsb.generator.processor.bundle.Bundle;
import org.safris.xsb.generator.processor.bundle.BundleDirectory;
import org.safris.xsb.generator.processor.plan.Plan;
import org.safris.xsb.generator.processor.plan.PlanDirectory;
import org.safris.xsb.generator.processor.timestamp.TimestampDirectory;
import org.safris.xsb.generator.processor.write.Writer;
import org.safris.xsb.generator.processor.write.WriterDirectory;
import org.safris.xsb.generator.schema.SchemaDocumentDirectory;

public final class Generator extends AbstractGenerator {
  private static void trapPrintUsage() {
    System.err.println("Usage: Generator [OPTIONS] <-d DEST_DIR> <SCHEMA_XSD>");
    System.err.println("");
    System.err.println("Mandatory arguments:");
    System.err.println("  -d <destDir>    Specify the destination directory.");
    System.err.println("");
    System.err.println("Optional arguments:");
    System.err.println("  --overwrite     Overwrite all existing generated classes.");
    System.err.println("  --compile       Compile generated source.");
    System.err.println("  --package       Package generated jars into a jar.");
    System.exit(1);
  }

  public static void main(final String[] args) throws MalformedURLException {
    if (args.length == 0 || args[0] == null || args[0].length() == 0)
      trapPrintUsage();

    boolean overwrite = false;
    boolean compile = false;
    boolean pack = false;
    File destDir = null;
    final Collection<SchemaReference> schemas = new HashSet<SchemaReference>();
    for (int i = 0; i < args.length; i++) {
      if ("--overwrite".equals(args[i]))
        overwrite = true;
      else if ("--compile".equals(args[i]))
        compile = true;
      else if ("--package".equals(args[i]))
        pack = true;
      else if ("-d".equals(args[i]) && i < args.length)
        destDir = new File(args[++i]).getAbsoluteFile();
      else
        schemas.add(new SchemaReference(Paths.isAbsolute(args[i]) ? URLs.makeUrlFromPath(args[i]) : new File(Files.getCwd(), args[i]).toURI().toURL(), false));
    }

    if (destDir == null)
      destDir = Files.getCwd();

    final GeneratorContext generatorContext = new GeneratorContext(destDir, overwrite, compile, pack, null, null);
    final Generator generator = new Generator(generatorContext, schemas, null);
    generator.generate();
  }

  private final GeneratorContext generatorContext;
  private final Collection<SchemaReference> schemas;
  private final Set<File> sourcePath;

  public Generator(final GeneratorContext generatorContext, final Collection<SchemaReference> schemas, final Set<File> sourcePath) {
    this.generatorContext = generatorContext;
    this.schemas = schemas;
    this.sourcePath = sourcePath;
  }

  public GeneratorContext getGeneratorContext() {
    return generatorContext;
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
    pipeline.<SchemaComposite,Bundle>addProcessor(schemaComposites, bundles, new BundleDirectory(sourcePath));

    // timestamp the generated files and directories
    pipeline.<Bundle,Bundle>addProcessor(bundles, null, new TimestampDirectory());

    // start the pipeline
    pipeline.begin();

    return bundles;
  }
}