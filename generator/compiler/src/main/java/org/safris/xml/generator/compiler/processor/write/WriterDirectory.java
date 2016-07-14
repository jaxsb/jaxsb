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

package org.safris.xml.generator.compiler.processor.write;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.pipeline.PipelineDirectory;
import org.safris.commons.pipeline.PipelineEntity;
import org.safris.commons.pipeline.PipelineProcessor;
import org.safris.xml.generator.compiler.lang.CompilerError;
import org.safris.xml.generator.compiler.processor.plan.Plan;
import org.safris.xml.generator.compiler.processor.plan.element.AllPlan;
import org.safris.xml.generator.compiler.processor.plan.element.AnnotationPlan;
import org.safris.xml.generator.compiler.processor.plan.element.AnyAttributePlan;
import org.safris.xml.generator.compiler.processor.plan.element.AnyPlan;
import org.safris.xml.generator.compiler.processor.plan.element.AppinfoPlan;
import org.safris.xml.generator.compiler.processor.plan.element.AttributeGroupPlan;
import org.safris.xml.generator.compiler.processor.plan.element.AttributePlan;
import org.safris.xml.generator.compiler.processor.plan.element.ChoicePlan;
import org.safris.xml.generator.compiler.processor.plan.element.ComplexContentPlan;
import org.safris.xml.generator.compiler.processor.plan.element.ComplexTypePlan;
import org.safris.xml.generator.compiler.processor.plan.element.DocumentationPlan;
import org.safris.xml.generator.compiler.processor.plan.element.ElementPlan;
import org.safris.xml.generator.compiler.processor.plan.element.EnumerationPlan;
import org.safris.xml.generator.compiler.processor.plan.element.ExtensionPlan;
import org.safris.xml.generator.compiler.processor.plan.element.FieldPlan;
import org.safris.xml.generator.compiler.processor.plan.element.FractionDigitsPlan;
import org.safris.xml.generator.compiler.processor.plan.element.GroupPlan;
import org.safris.xml.generator.compiler.processor.plan.element.HasFacetPlan;
import org.safris.xml.generator.compiler.processor.plan.element.HasPropertyPlan;
import org.safris.xml.generator.compiler.processor.plan.element.ImportPlan;
import org.safris.xml.generator.compiler.processor.plan.element.IncludePlan;
import org.safris.xml.generator.compiler.processor.plan.element.KeyPlan;
import org.safris.xml.generator.compiler.processor.plan.element.KeyrefPlan;
import org.safris.xml.generator.compiler.processor.plan.element.LengthPlan;
import org.safris.xml.generator.compiler.processor.plan.element.ListPlan;
import org.safris.xml.generator.compiler.processor.plan.element.MaxExclusivePlan;
import org.safris.xml.generator.compiler.processor.plan.element.MaxInclusivePlan;
import org.safris.xml.generator.compiler.processor.plan.element.MaxLengthPlan;
import org.safris.xml.generator.compiler.processor.plan.element.MinExclusivePlan;
import org.safris.xml.generator.compiler.processor.plan.element.MinInclusivePlan;
import org.safris.xml.generator.compiler.processor.plan.element.MinLengthPlan;
import org.safris.xml.generator.compiler.processor.plan.element.NotationPlan;
import org.safris.xml.generator.compiler.processor.plan.element.PatternPlan;
import org.safris.xml.generator.compiler.processor.plan.element.RedefinePlan;
import org.safris.xml.generator.compiler.processor.plan.element.RestrictionPlan;
import org.safris.xml.generator.compiler.processor.plan.element.SchemaPlan;
import org.safris.xml.generator.compiler.processor.plan.element.SelectorPlan;
import org.safris.xml.generator.compiler.processor.plan.element.SequencePlan;
import org.safris.xml.generator.compiler.processor.plan.element.SimpleContentPlan;
import org.safris.xml.generator.compiler.processor.plan.element.SimpleTypePlan;
import org.safris.xml.generator.compiler.processor.plan.element.UnionPlan;
import org.safris.xml.generator.compiler.processor.plan.element.UniquePlan;
import org.safris.xml.generator.compiler.processor.plan.element.WhiteSpacePlan;
import org.safris.xml.generator.compiler.processor.write.element.AllWriter;
import org.safris.xml.generator.compiler.processor.write.element.AnnotationWriter;
import org.safris.xml.generator.compiler.processor.write.element.AnyAttributeWriter;
import org.safris.xml.generator.compiler.processor.write.element.AnyWriter;
import org.safris.xml.generator.compiler.processor.write.element.AppinfoWriter;
import org.safris.xml.generator.compiler.processor.write.element.AttributeGroupWriter;
import org.safris.xml.generator.compiler.processor.write.element.AttributeWriter;
import org.safris.xml.generator.compiler.processor.write.element.ChoiceWriter;
import org.safris.xml.generator.compiler.processor.write.element.ComplexContentWriter;
import org.safris.xml.generator.compiler.processor.write.element.ComplexTypeWriter;
import org.safris.xml.generator.compiler.processor.write.element.DocumentationWriter;
import org.safris.xml.generator.compiler.processor.write.element.ElementWriter;
import org.safris.xml.generator.compiler.processor.write.element.EnumerationWriter;
import org.safris.xml.generator.compiler.processor.write.element.ExtensionWriter;
import org.safris.xml.generator.compiler.processor.write.element.FieldWriter;
import org.safris.xml.generator.compiler.processor.write.element.FractionDigitsWriter;
import org.safris.xml.generator.compiler.processor.write.element.GroupWriter;
import org.safris.xml.generator.compiler.processor.write.element.HasFacetWriter;
import org.safris.xml.generator.compiler.processor.write.element.HasPropertyWriter;
import org.safris.xml.generator.compiler.processor.write.element.ImportWriter;
import org.safris.xml.generator.compiler.processor.write.element.IncludeWriter;
import org.safris.xml.generator.compiler.processor.write.element.KeyWriter;
import org.safris.xml.generator.compiler.processor.write.element.KeyrefWriter;
import org.safris.xml.generator.compiler.processor.write.element.LengthWriter;
import org.safris.xml.generator.compiler.processor.write.element.ListWriter;
import org.safris.xml.generator.compiler.processor.write.element.MaxExclusiveWriter;
import org.safris.xml.generator.compiler.processor.write.element.MaxInclusiveWriter;
import org.safris.xml.generator.compiler.processor.write.element.MaxLengthWriter;
import org.safris.xml.generator.compiler.processor.write.element.MinExclusiveWriter;
import org.safris.xml.generator.compiler.processor.write.element.MinInclusiveWriter;
import org.safris.xml.generator.compiler.processor.write.element.MinLengthWriter;
import org.safris.xml.generator.compiler.processor.write.element.NotationWriter;
import org.safris.xml.generator.compiler.processor.write.element.PatternWriter;
import org.safris.xml.generator.compiler.processor.write.element.RedefineWriter;
import org.safris.xml.generator.compiler.processor.write.element.RestrictionWriter;
import org.safris.xml.generator.compiler.processor.write.element.SchemaWriter;
import org.safris.xml.generator.compiler.processor.write.element.SelectorWriter;
import org.safris.xml.generator.compiler.processor.write.element.SequenceWriter;
import org.safris.xml.generator.compiler.processor.write.element.SimpleContentWriter;
import org.safris.xml.generator.compiler.processor.write.element.SimpleTypeWriter;
import org.safris.xml.generator.compiler.processor.write.element.UnionWriter;
import org.safris.xml.generator.compiler.processor.write.element.UniqueWriter;
import org.safris.xml.generator.compiler.processor.write.element.WhiteSpaceWriter;
import org.safris.xml.generator.lexer.processor.GeneratorContext;

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
      throw new CompilerError(e);
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