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

package org.jaxsb.generator.processor.plan;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.model.Model;
import org.libj.io.FileUtil;
import org.libj.net.URLs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PlanProcessor implements PipelineProcessor<GeneratorContext,Model,Plan<?>> {
  private static final Logger logger = LoggerFactory.getLogger(PlanProcessor.class);

  @Override
  public Collection<Plan<?>> process(final GeneratorContext pipelineContext, final Collection<? extends Model> documents, final PipelineDirectory<GeneratorContext,? super Model,Plan<?>> directory) {
    final Plan<?> root = new Plan<Model>(null, null) {};
    final ArrayList<Plan<?>> plans = new ArrayList<>();
    for (final Model model : documents) { // [C]
      if (model.getChildren() == null || model.getChildren().size() == 0)
        continue;

      final URL url = model.getSchema().getURL();
      final String display = (URLs.isLocal(url) ? FileUtil.getCwd().toPath().relativize(new File(url.getFile()).getAbsoluteFile().toPath()) : url).toString();
      logger.info("Parsing {" + model.getTargetNamespace() + "} from " + display);

      final ArrayList<Model> children = model.getChildren();
      for (int i = 0, i$ = children.size(); i < i$; ++i) // [RA]
        disclose(children.get(i), root, plans, directory);
    }

    return plans;
  }

  protected static void disclose(final Model model, final Plan<?> parent, final Collection<? super Plan<?>> plans, final PipelineDirectory<GeneratorContext,? super Model,? super Plan<?>> directory) {
    final Plan<?> plan = (Plan<?>)directory.getEntity(model, parent);
    plans.add(plan);
  }
}