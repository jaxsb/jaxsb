/* Copyright (c) 2008 OpenJAX
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

package org.openjax.xsb.generator.processor.plan;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.openjax.standard.io.FastFiles;
import org.openjax.standard.net.URLs;
import org.openjax.xsb.compiler.processor.GeneratorContext;
import org.openjax.xsb.compiler.processor.model.Model;
import org.openjax.xsb.helper.pipeline.PipelineDirectory;
import org.openjax.xsb.helper.pipeline.PipelineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PlanProcessor implements PipelineProcessor<GeneratorContext,Model,Plan<?>> {
  private static final Logger logger = LoggerFactory.getLogger(PlanProcessor.class);

  @Override
  public final Collection<Plan<?>> process(final GeneratorContext pipelineContext, final Collection<Model> documents, final PipelineDirectory<GeneratorContext,Model,Plan<?>> directory) {
    final Plan<?> root = new Plan<Model>(null, null) {};
    final Collection<Plan<?>> plans = new ArrayList<>();
    for (final Model model : documents) {
      if (model.getChildren() == null || model.getChildren().size() == 0)
        continue;

      final URL url = model.getSchema().getURL();
      final String display = URLs.isLocal(url) ? FastFiles.getCwd().toPath().relativize(new File(url.getFile()).getAbsoluteFile().toPath()).toString() : url.toExternalForm();
      logger.info("Parsing {" + model.getTargetNamespace() + "} from " + display);

      for (final Model child : model.getChildren())
        disclose(child, root, plans, directory);
    }

    return plans;
  }

  protected static void disclose(final Model model, final Plan<?> parent, Collection<Plan<?>> plans, final PipelineDirectory<GeneratorContext,Model,Plan<?>> directory) {
    final Plan<?> plan = (Plan<?>)directory.getEntity(model, parent);
    plans.add(plan);
  }
}