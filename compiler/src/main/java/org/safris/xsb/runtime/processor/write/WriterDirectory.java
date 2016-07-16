/* Copyright (c) 2008 Seva Safris
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

package org.safris.xsb.runtime.processor.write;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.pipeline.PipelineDirectory;
import org.safris.commons.pipeline.PipelineEntity;
import org.safris.commons.pipeline.PipelineProcessor;
import org.safris.xsb.compiler.processor.GeneratorContext;
import org.safris.xsb.runtime.CompilerFailureException;
import org.safris.xsb.runtime.processor.plan.Plan;
import org.safris.xsb.runtime.processor.plan.element.AllPlan;
import org.safris.xsb.runtime.processor.plan.element.AnnotationPlan;
import org.safris.xsb.runtime.processor.plan.element.AnyAttributePlan;
import org.safris.xsb.runtime.processor.plan.element.AnyPlan;
import org.safris.xsb.runtime.processor.plan.element.AppinfoPlan;
import org.safris.xsb.runtime.processor.plan.element.AttributeGroupPlan;
import org.safris.xsb.runtime.processor.plan.element.AttributePlan;
import org.safris.xsb.runtime.processor.plan.element.ChoicePlan;
import org.safris.xsb.runtime.processor.plan.element.ComplexContentPlan;
import org.safris.xsb.runtime.processor.plan.element.ComplexTypePlan;
import org.safris.xsb.runtime.processor.plan.element.DocumentationPlan;
import org.safris.xsb.runtime.processor.plan.element.ElementPlan;
import org.safris.xsb.runtime.processor.plan.element.EnumerationPlan;
import org.safris.xsb.runtime.processor.plan.element.ExtensionPlan;
import org.safris.xsb.runtime.processor.plan.element.FieldPlan;
import org.safris.xsb.runtime.processor.plan.element.FractionDigitsPlan;
import org.safris.xsb.runtime.processor.plan.element.GroupPlan;
import org.safris.xsb.runtime.processor.plan.element.HasFacetPlan;
import org.safris.xsb.runtime.processor.plan.element.HasPropertyPlan;
import org.safris.xsb.runtime.processor.plan.element.ImportPlan;
import org.safris.xsb.runtime.processor.plan.element.IncludePlan;
import org.safris.xsb.runtime.processor.plan.element.KeyPlan;
import org.safris.xsb.runtime.processor.plan.element.KeyrefPlan;
import org.safris.xsb.runtime.processor.plan.element.LengthPlan;
import org.safris.xsb.runtime.processor.plan.element.ListPlan;
import org.safris.xsb.runtime.processor.plan.element.MaxExclusivePlan;
import org.safris.xsb.runtime.processor.plan.element.MaxInclusivePlan;
import org.safris.xsb.runtime.processor.plan.element.MaxLengthPlan;
import org.safris.xsb.runtime.processor.plan.element.MinExclusivePlan;
import org.safris.xsb.runtime.processor.plan.element.MinInclusivePlan;
import org.safris.xsb.runtime.processor.plan.element.MinLengthPlan;
import org.safris.xsb.runtime.processor.plan.element.NotationPlan;
import org.safris.xsb.runtime.processor.plan.element.PatternPlan;
import org.safris.xsb.runtime.processor.plan.element.RedefinePlan;
import org.safris.xsb.runtime.processor.plan.element.RestrictionPlan;
import org.safris.xsb.runtime.processor.plan.element.SchemaPlan;
import org.safris.xsb.runtime.processor.plan.element.SelectorPlan;
import org.safris.xsb.runtime.processor.plan.element.SequencePlan;
import org.safris.xsb.runtime.processor.plan.element.SimpleContentPlan;
import org.safris.xsb.runtime.processor.plan.element.SimpleTypePlan;
import org.safris.xsb.runtime.processor.plan.element.UnionPlan;
import org.safris.xsb.runtime.processor.plan.element.UniquePlan;
import org.safris.xsb.runtime.processor.plan.element.WhiteSpacePlan;
import org.safris.xsb.runtime.processor.write.element.AllWriter;
import org.safris.xsb.runtime.processor.write.element.AnnotationWriter;
import org.safris.xsb.runtime.processor.write.element.AnyAttributeWriter;
import org.safris.xsb.runtime.processor.write.element.AnyWriter;
import org.safris.xsb.runtime.processor.write.element.AppinfoWriter;
import org.safris.xsb.runtime.processor.write.element.AttributeGroupWriter;
import org.safris.xsb.runtime.processor.write.element.AttributeWriter;
import org.safris.xsb.runtime.processor.write.element.ChoiceWriter;
import org.safris.xsb.runtime.processor.write.element.ComplexContentWriter;
import org.safris.xsb.runtime.processor.write.element.ComplexTypeWriter;
import org.safris.xsb.runtime.processor.write.element.DocumentationWriter;
import org.safris.xsb.runtime.processor.write.element.ElementWriter;
import org.safris.xsb.runtime.processor.write.element.EnumerationWriter;
import org.safris.xsb.runtime.processor.write.element.ExtensionWriter;
import org.safris.xsb.runtime.processor.write.element.FieldWriter;
import org.safris.xsb.runtime.processor.write.element.FractionDigitsWriter;
import org.safris.xsb.runtime.processor.write.element.GroupWriter;
import org.safris.xsb.runtime.processor.write.element.HasFacetWriter;
import org.safris.xsb.runtime.processor.write.element.HasPropertyWriter;
import org.safris.xsb.runtime.processor.write.element.ImportWriter;
import org.safris.xsb.runtime.processor.write.element.IncludeWriter;
import org.safris.xsb.runtime.processor.write.element.KeyWriter;
import org.safris.xsb.runtime.processor.write.element.KeyrefWriter;
import org.safris.xsb.runtime.processor.write.element.LengthWriter;
import org.safris.xsb.runtime.processor.write.element.ListWriter;
import org.safris.xsb.runtime.processor.write.element.MaxExclusiveWriter;
import org.safris.xsb.runtime.processor.write.element.MaxInclusiveWriter;
import org.safris.xsb.runtime.processor.write.element.MaxLengthWriter;
import org.safris.xsb.runtime.processor.write.element.MinExclusiveWriter;
import org.safris.xsb.runtime.processor.write.element.MinInclusiveWriter;
import org.safris.xsb.runtime.processor.write.element.MinLengthWriter;
import org.safris.xsb.runtime.processor.write.element.NotationWriter;
import org.safris.xsb.runtime.processor.write.element.PatternWriter;
import org.safris.xsb.runtime.processor.write.element.RedefineWriter;
import org.safris.xsb.runtime.processor.write.element.RestrictionWriter;
import org.safris.xsb.runtime.processor.write.element.SchemaWriter;
import org.safris.xsb.runtime.processor.write.element.SelectorWriter;
import org.safris.xsb.runtime.processor.write.element.SequenceWriter;
import org.safris.xsb.runtime.processor.write.element.SimpleContentWriter;
import org.safris.xsb.runtime.processor.write.element.SimpleTypeWriter;
import org.safris.xsb.runtime.processor.write.element.UnionWriter;
import org.safris.xsb.runtime.processor.write.element.UniqueWriter;
import org.safris.xsb.runtime.processor.write.element.WhiteSpaceWriter;

@SuppressWarnings("rawtypes")
public final class WriterDirectory implements PipelineDirectory<GeneratorContext,Plan<?>,Writer<?>> {
  private final Map<Class<? extends Plan>,Class<? extends Writer>> classes = new HashMap<Class<? extends Plan>,Class<? extends Writer>>(39);
  private final Map<Class<? extends Plan>,Writer> instances = new HashMap<Class<? extends Plan>,Writer>(39);
  private final Collection<Class<? extends Plan>> keys;
  private final WriterProcessor processor = new WriterProcessor();

  public WriterDirectory() {
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
      instances.put(entity.getClass(), writerInstance = writerClass.newInstance());
      return writerInstance;
    }
    catch (final Exception e) {
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