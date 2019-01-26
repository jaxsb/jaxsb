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

package org.openjax.xsb.generator.processor.write;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openjax.xsb.compiler.processor.GeneratorContext;
import org.openjax.xsb.generator.processor.plan.Plan;
import org.openjax.xsb.generator.processor.plan.element.AllPlan;
import org.openjax.xsb.generator.processor.plan.element.AnnotationPlan;
import org.openjax.xsb.generator.processor.plan.element.AnyAttributePlan;
import org.openjax.xsb.generator.processor.plan.element.AnyPlan;
import org.openjax.xsb.generator.processor.plan.element.AppinfoPlan;
import org.openjax.xsb.generator.processor.plan.element.AttributeGroupPlan;
import org.openjax.xsb.generator.processor.plan.element.AttributePlan;
import org.openjax.xsb.generator.processor.plan.element.ChoicePlan;
import org.openjax.xsb.generator.processor.plan.element.ComplexContentPlan;
import org.openjax.xsb.generator.processor.plan.element.ComplexTypePlan;
import org.openjax.xsb.generator.processor.plan.element.DocumentationPlan;
import org.openjax.xsb.generator.processor.plan.element.ElementPlan;
import org.openjax.xsb.generator.processor.plan.element.EnumerationPlan;
import org.openjax.xsb.generator.processor.plan.element.ExtensionPlan;
import org.openjax.xsb.generator.processor.plan.element.FieldPlan;
import org.openjax.xsb.generator.processor.plan.element.FractionDigitsPlan;
import org.openjax.xsb.generator.processor.plan.element.GroupPlan;
import org.openjax.xsb.generator.processor.plan.element.HasFacetPlan;
import org.openjax.xsb.generator.processor.plan.element.HasPropertyPlan;
import org.openjax.xsb.generator.processor.plan.element.ImportPlan;
import org.openjax.xsb.generator.processor.plan.element.IncludePlan;
import org.openjax.xsb.generator.processor.plan.element.KeyPlan;
import org.openjax.xsb.generator.processor.plan.element.KeyrefPlan;
import org.openjax.xsb.generator.processor.plan.element.LengthPlan;
import org.openjax.xsb.generator.processor.plan.element.ListPlan;
import org.openjax.xsb.generator.processor.plan.element.MaxExclusivePlan;
import org.openjax.xsb.generator.processor.plan.element.MaxInclusivePlan;
import org.openjax.xsb.generator.processor.plan.element.MaxLengthPlan;
import org.openjax.xsb.generator.processor.plan.element.MinExclusivePlan;
import org.openjax.xsb.generator.processor.plan.element.MinInclusivePlan;
import org.openjax.xsb.generator.processor.plan.element.MinLengthPlan;
import org.openjax.xsb.generator.processor.plan.element.NotationPlan;
import org.openjax.xsb.generator.processor.plan.element.PatternPlan;
import org.openjax.xsb.generator.processor.plan.element.RedefinePlan;
import org.openjax.xsb.generator.processor.plan.element.RestrictionPlan;
import org.openjax.xsb.generator.processor.plan.element.SchemaPlan;
import org.openjax.xsb.generator.processor.plan.element.SelectorPlan;
import org.openjax.xsb.generator.processor.plan.element.SequencePlan;
import org.openjax.xsb.generator.processor.plan.element.SimpleContentPlan;
import org.openjax.xsb.generator.processor.plan.element.SimpleTypePlan;
import org.openjax.xsb.generator.processor.plan.element.UnionPlan;
import org.openjax.xsb.generator.processor.plan.element.UniquePlan;
import org.openjax.xsb.generator.processor.plan.element.WhiteSpacePlan;
import org.openjax.xsb.generator.processor.write.element.AllWriter;
import org.openjax.xsb.generator.processor.write.element.AnnotationWriter;
import org.openjax.xsb.generator.processor.write.element.AnyAttributeWriter;
import org.openjax.xsb.generator.processor.write.element.AnyWriter;
import org.openjax.xsb.generator.processor.write.element.AppinfoWriter;
import org.openjax.xsb.generator.processor.write.element.AttributeGroupWriter;
import org.openjax.xsb.generator.processor.write.element.AttributeWriter;
import org.openjax.xsb.generator.processor.write.element.ChoiceWriter;
import org.openjax.xsb.generator.processor.write.element.ComplexContentWriter;
import org.openjax.xsb.generator.processor.write.element.ComplexTypeWriter;
import org.openjax.xsb.generator.processor.write.element.DocumentationWriter;
import org.openjax.xsb.generator.processor.write.element.ElementWriter;
import org.openjax.xsb.generator.processor.write.element.EnumerationWriter;
import org.openjax.xsb.generator.processor.write.element.ExtensionWriter;
import org.openjax.xsb.generator.processor.write.element.FieldWriter;
import org.openjax.xsb.generator.processor.write.element.FractionDigitsWriter;
import org.openjax.xsb.generator.processor.write.element.GroupWriter;
import org.openjax.xsb.generator.processor.write.element.HasFacetWriter;
import org.openjax.xsb.generator.processor.write.element.HasPropertyWriter;
import org.openjax.xsb.generator.processor.write.element.ImportWriter;
import org.openjax.xsb.generator.processor.write.element.IncludeWriter;
import org.openjax.xsb.generator.processor.write.element.KeyWriter;
import org.openjax.xsb.generator.processor.write.element.KeyrefWriter;
import org.openjax.xsb.generator.processor.write.element.LengthWriter;
import org.openjax.xsb.generator.processor.write.element.ListWriter;
import org.openjax.xsb.generator.processor.write.element.MaxExclusiveWriter;
import org.openjax.xsb.generator.processor.write.element.MaxInclusiveWriter;
import org.openjax.xsb.generator.processor.write.element.MaxLengthWriter;
import org.openjax.xsb.generator.processor.write.element.MinExclusiveWriter;
import org.openjax.xsb.generator.processor.write.element.MinInclusiveWriter;
import org.openjax.xsb.generator.processor.write.element.MinLengthWriter;
import org.openjax.xsb.generator.processor.write.element.NotationWriter;
import org.openjax.xsb.generator.processor.write.element.PatternWriter;
import org.openjax.xsb.generator.processor.write.element.RedefineWriter;
import org.openjax.xsb.generator.processor.write.element.RestrictionWriter;
import org.openjax.xsb.generator.processor.write.element.SchemaWriter;
import org.openjax.xsb.generator.processor.write.element.SelectorWriter;
import org.openjax.xsb.generator.processor.write.element.SequenceWriter;
import org.openjax.xsb.generator.processor.write.element.SimpleContentWriter;
import org.openjax.xsb.generator.processor.write.element.SimpleTypeWriter;
import org.openjax.xsb.generator.processor.write.element.UnionWriter;
import org.openjax.xsb.generator.processor.write.element.UniqueWriter;
import org.openjax.xsb.generator.processor.write.element.WhiteSpaceWriter;
import org.openjax.xsb.compiler.pipeline.PipelineDirectory;
import org.openjax.xsb.compiler.pipeline.PipelineEntity;
import org.openjax.xsb.compiler.pipeline.PipelineProcessor;
import org.openjax.xsb.runtime.CompilerFailureException;

@SuppressWarnings("rawtypes")
public final class WriterDirectory implements PipelineDirectory<GeneratorContext,Plan<?>,Writer<?>> {
  private final Map<Class<? extends Plan>,Class<? extends Writer>> classes = new HashMap<>(39);
  private final Map<Class<? extends Plan>,Writer> instances = new HashMap<>(39);
  private final Collection<Class<? extends Plan>> keys;
  private final WriterProcessor processor;

  public WriterDirectory() {
    processor = new WriterProcessor();
    classes.put(AllPlan.class, AllWriter.class);
    classes.put(AnnotationPlan.class, AnnotationWriter.class);
    classes.put(AnyAttributePlan.class, AnyAttributeWriter.class);
    classes.put(AnyPlan.class, AnyWriter.class);
    classes.put(AppinfoPlan.class, AppinfoWriter.class);
    classes.put(AttributeGroupPlan.class, AttributeGroupWriter.class);
    classes.put(AttributePlan.class, AttributeWriter.class);
    classes.put(ChoicePlan.class, ChoiceWriter.class);
    classes.put(ComplexContentPlan.class, ComplexContentWriter.class);
    classes.put(ComplexTypePlan.class, ComplexTypeWriter.class);
    classes.put(DocumentationPlan.class, DocumentationWriter.class);
    classes.put(ElementPlan.class, ElementWriter.class);
    classes.put(EnumerationPlan.class, EnumerationWriter.class);
    classes.put(ExtensionPlan.class, ExtensionWriter.class);
    classes.put(FieldPlan.class, FieldWriter.class);
    classes.put(FractionDigitsPlan.class, FractionDigitsWriter.class);
    classes.put(GroupPlan.class, GroupWriter.class);
    classes.put(HasFacetPlan.class, HasFacetWriter.class);
    classes.put(HasPropertyPlan.class, HasPropertyWriter.class);
    classes.put(ImportPlan.class, ImportWriter.class);
    classes.put(IncludePlan.class, IncludeWriter.class);
    classes.put(KeyPlan.class, KeyWriter.class);
    classes.put(KeyrefPlan.class, KeyrefWriter.class);
    classes.put(LengthPlan.class, LengthWriter.class);
    classes.put(ListPlan.class, ListWriter.class);
    classes.put(MaxExclusivePlan.class, MaxExclusiveWriter.class);
    classes.put(MaxInclusivePlan.class, MaxInclusiveWriter.class);
    classes.put(MaxLengthPlan.class, MaxLengthWriter.class);
    classes.put(MinExclusivePlan.class, MinExclusiveWriter.class);
    classes.put(MinInclusivePlan.class, MinInclusiveWriter.class);
    classes.put(MinLengthPlan.class, MinLengthWriter.class);
    classes.put(NotationPlan.class, NotationWriter.class);
    classes.put(PatternPlan.class, PatternWriter.class);
    classes.put(RedefinePlan.class, RedefineWriter.class);
    classes.put(RestrictionPlan.class, RestrictionWriter.class);
    classes.put(SchemaPlan.class, SchemaWriter.class);
    classes.put(SelectorPlan.class, SelectorWriter.class);
    classes.put(SequencePlan.class, SequenceWriter.class);
    classes.put(SimpleContentPlan.class, SimpleContentWriter.class);
    classes.put(SimpleTypePlan.class, SimpleTypeWriter.class);
    classes.put(UnionPlan.class, UnionWriter.class);
    classes.put(UniquePlan.class, UniqueWriter.class);
    classes.put(WhiteSpacePlan.class, WhiteSpaceWriter.class);
    keys = classes.keySet();
  }

  @Override
  public PipelineEntity getEntity(final Plan<?> entity, final Writer<?> parent) {
    if (!keys.contains(entity.getClass()))
      throw new IllegalArgumentException("Unknown key: " + entity.getClass().getSimpleName());

    Writer writerInstance = instances.get(entity.getClass());
    if (writerInstance != null)
      return writerInstance;

    final Class<? extends Writer> writerClass = classes.get(entity.getClass());
    try {
      instances.put(entity.getClass(), writerInstance = writerClass.getDeclaredConstructor().newInstance());
      return writerInstance;
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
      throw new CompilerFailureException(e);
    }
  }

  @Override
  public PipelineProcessor<GeneratorContext,Plan<?>,Writer<?>> getProcessor() {
    return processor;
  }

  @Override
  public void clear() {
    instances.clear();
  }
}