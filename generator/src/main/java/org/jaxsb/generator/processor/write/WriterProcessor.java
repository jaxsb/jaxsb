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

package org.jaxsb.generator.processor.write;

import java.io.StringWriter;
import java.util.Collection;

import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.generator.processor.plan.AliasPlan;
import org.jaxsb.generator.processor.plan.NestablePlan;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.runtime.CompilerFailureException;

public final class WriterProcessor implements PipelineProcessor<GeneratorContext,Plan<?>,Writer<?>> {
  private final Writer<?> root = new Writer<Plan<?>>() {
    @Override
    protected void appendRegistration(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendDeclaration(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendGetMethod(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendSetMethod(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendMarshal(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendParse(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendCopy(final StringWriter writer, final Plan<?> plan, final Plan<?> parent, final String variable) {
    }

    @Override
    protected void appendEquals(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendHashCode(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendClone(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }

    @Override
    protected void appendClass(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    }
  };

  @Override
  public Collection<Writer<?>> process(final GeneratorContext pipelineContext, final Collection<? extends Plan<?>> documents, final PipelineDirectory<GeneratorContext,? super Plan<?>,Writer<?>> directory) {
    Writer.directory = directory;
    tailRecurse(pipelineContext, documents, directory);
    return null;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void tailRecurse(final GeneratorContext pipelineContext, final Collection<? extends Plan<?>> plans, final PipelineDirectory<GeneratorContext,? super Plan<?>,Writer<?>> directory) {
    if (plans == null || plans.size() == 0)
      return;

    for (final Plan<?> plan : plans)
      if (plan != null)
        tailRecurse(pipelineContext, disclose(pipelineContext, plan, directory), directory);

    for (final Plan<?> plan : plans)
      if (plan != null)
        ((Writer)root).closeFile(plan, pipelineContext.getDestDir());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected Collection<Plan<?>> disclose(final GeneratorContext pipelineContext, final Plan<?> plan, final PipelineDirectory<GeneratorContext,? super Plan<?>,Writer<?>> directory) {
    if (!(plan instanceof AliasPlan) || (plan instanceof NestablePlan && ((NestablePlan)plan).isNested()))
      return null;

    if (((Nameable<?>)plan).getName().getNamespaceURI() == null)
      throw new CompilerFailureException("Cannot generate classes for schema with no targetNamespace");

    if ((pipelineContext.getIncludes() == null || pipelineContext.getIncludes().contains(plan.getModel().getTargetNamespace())) && (pipelineContext.getExcludes() == null || !pipelineContext.getExcludes().contains(plan.getModel().getTargetNamespace())))
      ((Writer)root).writeFile(((Writer<?>)directory.getEntity(plan, null)), plan, pipelineContext.getDestDir());

    return null;
  }
}