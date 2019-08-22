/* Copyright (c) 2006 JAX-SB
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

package org.jaxsb.generator;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jaxsb.compiler.pipeline.Pipeline;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.composite.SchemaComposite;
import org.jaxsb.compiler.processor.composite.SchemaCompositeDirectory;
import org.jaxsb.compiler.processor.document.SchemaDocument;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.ModelDirectory;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;
import org.jaxsb.compiler.processor.reference.SchemaReference;
import org.jaxsb.compiler.processor.reference.SchemaReferenceDirectory;
import org.jaxsb.generator.processor.bundle.Bundle;
import org.jaxsb.generator.processor.bundle.BundleDirectory;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.generator.processor.plan.PlanDirectory;
import org.jaxsb.generator.processor.timestamp.TimestampDirectory;
import org.jaxsb.generator.processor.write.WriterDirectory;
import org.jaxsb.generator.schema.SchemaDocumentDirectory;
import org.libj.io.FileUtil;
import org.libj.net.URLs;
import org.libj.util.Paths;

public final class Generator extends AbstractGenerator {
  private static void trapPrintUsage() {
    System.err.println("Usage: Generator [OPTIONS] <-d DEST_DIR> <SCHEMA_XSD>");
    System.err.println();
    System.err.println("Mandatory arguments:");
    System.err.println("  -d <destDir>    Specify the destination directory.");
    System.err.println();
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
    File compile = null;
    boolean pack = false;
    File destDir = null;
    final Collection<SchemaReference> schemas = new HashSet<>();
    for (int i = 0; i < args.length; ++i) {
      if ("--overwrite".equals(args[i]))
        overwrite = true;
      else if ("--compile".equals(args[i]))
        compile = new File(args[++i]).getAbsoluteFile();
      else if ("--package".equals(args[i]))
        pack = true;
      else if ("-d".equals(args[i]) && i < args.length)
        destDir = new File(args[++i]).getAbsoluteFile();
      else
        schemas.add(new SchemaReference(Paths.isAbsolute(args[i]) ? URLs.toCanonicalURL(args[i]) : new File(FileUtil.getCwd(), args[i]).toURI().toURL(), false));
    }

    generate(new GeneratorContext(destDir == null ? FileUtil.getCwd() : destDir, overwrite, compile, pack, null, null), schemas, null);
  }

  public static Collection<Bundle> generate(final GeneratorContext generatorContext, final Collection<SchemaReference> schemas, final Set<File> sourcePath) {
    final Pipeline<GeneratorContext> pipeline = new Pipeline<>(generatorContext);

    // select the schemas to be generated and exit if no schemas need work
    final Collection<SchemaReference> schemaReferences = new ArrayList<>();
    pipeline.addProcessor(schemas, schemaReferences, new SchemaReferenceDirectory());

    // prepare the schemas to be worked on and build the dependency graph
    final Collection<SchemaDocument> schemaDocuments = new ArrayList<>();
    pipeline.addProcessor(schemaReferences, schemaDocuments, new SchemaDocumentDirectory());

    // bridge the dependency structure within the framework
    final Collection<SchemaComposite> schemaComposites = new ArrayList<>();
    pipeline.addProcessor(schemaDocuments, schemaComposites, new SchemaCompositeDirectory());

    // model the schema elements using Model objects
    final Collection<Model> models = new ArrayList<>();
    pipeline.addProcessor(schemaComposites, models, new ModelDirectory());

    // normalize the models
    pipeline.addProcessor(models, null, new NormalizerDirectory());

    // plan the schema elements using Plan objects
    final Collection<Plan<?>> plans = new ArrayList<>();
    pipeline.addProcessor(models, plans, new PlanDirectory());

    // write the plans to files
    pipeline.addProcessor(plans, null, new WriterDirectory());

    // compile and jar the bindings
    final Collection<Bundle> bundles = new ArrayList<>();
    pipeline.addProcessor(schemaComposites, bundles, new BundleDirectory(sourcePath));

    // timestamp the generated files and directories
    pipeline.addProcessor(bundles, null, new TimestampDirectory());

    // start the pipeline
    pipeline.begin();

    return bundles;
  }

  private Generator() {
  }
}