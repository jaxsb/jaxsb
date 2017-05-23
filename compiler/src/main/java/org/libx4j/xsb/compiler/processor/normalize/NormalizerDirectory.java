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

package org.libx4j.xsb.compiler.processor.normalize;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.lib4j.pipeline.PipelineDirectory;
import org.lib4j.pipeline.PipelineEntity;
import org.lib4j.pipeline.PipelineProcessor;
import org.libx4j.xsb.compiler.lang.LexerFailureException;
import org.libx4j.xsb.compiler.processor.GeneratorContext;
import org.libx4j.xsb.compiler.processor.model.Model;
import org.libx4j.xsb.compiler.processor.model.element.AllModel;
import org.libx4j.xsb.compiler.processor.model.element.AnnotationModel;
import org.libx4j.xsb.compiler.processor.model.element.AnyAttributeModel;
import org.libx4j.xsb.compiler.processor.model.element.AnyModel;
import org.libx4j.xsb.compiler.processor.model.element.AppinfoModel;
import org.libx4j.xsb.compiler.processor.model.element.AttributeGroupModel;
import org.libx4j.xsb.compiler.processor.model.element.AttributeModel;
import org.libx4j.xsb.compiler.processor.model.element.ChoiceModel;
import org.libx4j.xsb.compiler.processor.model.element.ComplexContentModel;
import org.libx4j.xsb.compiler.processor.model.element.ComplexTypeModel;
import org.libx4j.xsb.compiler.processor.model.element.DocumentationModel;
import org.libx4j.xsb.compiler.processor.model.element.ElementModel;
import org.libx4j.xsb.compiler.processor.model.element.EnumerationModel;
import org.libx4j.xsb.compiler.processor.model.element.ExtensionModel;
import org.libx4j.xsb.compiler.processor.model.element.FieldModel;
import org.libx4j.xsb.compiler.processor.model.element.FractionDigitsModel;
import org.libx4j.xsb.compiler.processor.model.element.GroupModel;
import org.libx4j.xsb.compiler.processor.model.element.HasFacetModel;
import org.libx4j.xsb.compiler.processor.model.element.HasPropertyModel;
import org.libx4j.xsb.compiler.processor.model.element.ImportModel;
import org.libx4j.xsb.compiler.processor.model.element.IncludeModel;
import org.libx4j.xsb.compiler.processor.model.element.KeyModel;
import org.libx4j.xsb.compiler.processor.model.element.KeyrefModel;
import org.libx4j.xsb.compiler.processor.model.element.LengthModel;
import org.libx4j.xsb.compiler.processor.model.element.ListModel;
import org.libx4j.xsb.compiler.processor.model.element.MaxExclusiveModel;
import org.libx4j.xsb.compiler.processor.model.element.MaxInclusiveModel;
import org.libx4j.xsb.compiler.processor.model.element.MaxLengthModel;
import org.libx4j.xsb.compiler.processor.model.element.MinExclusiveModel;
import org.libx4j.xsb.compiler.processor.model.element.MinInclusiveModel;
import org.libx4j.xsb.compiler.processor.model.element.MinLengthModel;
import org.libx4j.xsb.compiler.processor.model.element.NotationModel;
import org.libx4j.xsb.compiler.processor.model.element.PatternModel;
import org.libx4j.xsb.compiler.processor.model.element.RedefineModel;
import org.libx4j.xsb.compiler.processor.model.element.RestrictionModel;
import org.libx4j.xsb.compiler.processor.model.element.SchemaModel;
import org.libx4j.xsb.compiler.processor.model.element.SelectorModel;
import org.libx4j.xsb.compiler.processor.model.element.SequenceModel;
import org.libx4j.xsb.compiler.processor.model.element.SimpleContentModel;
import org.libx4j.xsb.compiler.processor.model.element.SimpleTypeModel;
import org.libx4j.xsb.compiler.processor.model.element.UnionModel;
import org.libx4j.xsb.compiler.processor.model.element.UniqueModel;
import org.libx4j.xsb.compiler.processor.model.element.UnknownModel;
import org.libx4j.xsb.compiler.processor.model.element.WhiteSpaceModel;
import org.libx4j.xsb.compiler.processor.normalize.element.AllNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.AnnotationNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.AnyAttributeNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.AnyNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.AppinfoNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.AttributeGroupNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.AttributeNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.ChoiceNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.ComplexContentNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.ComplexTypeNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.DocumentationNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.ElementNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.EnumerationNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.ExtensionNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.FieldNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.FractionDigitsNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.GroupNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.HasFacetNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.HasPropertyNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.ImportNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.IncludeNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.KeyNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.KeyrefNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.LengthNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.ListNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.MaxExclusiveNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.MaxInclusiveNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.MaxLengthNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.MinExclusiveNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.MinInclusiveNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.MinLengthNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.NotationNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.PatternNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.RedefineNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.RestrictionNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.SchemaNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.SelectorNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.SequenceNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.SimpleContentNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.SimpleTypeNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.UnionNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.UniqueNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.UnknownNormalizer;
import org.libx4j.xsb.compiler.processor.normalize.element.WhiteSpaceNormalizer;

public final class NormalizerDirectory implements PipelineDirectory<GeneratorContext,Model,Normalizer<?>> {
  private final Map<Class<? extends Model>,Class<? extends Normalizer<?>>> classes = new HashMap<Class<? extends Model>,Class<? extends Normalizer<?>>>(39);
  private final Map<Class<? extends Model>,Normalizer<?>> instances = new HashMap<Class<? extends Model>,Normalizer<?>>(39);
  private final Collection<Class<? extends Model>> keys;
  private final NormalizerProcessor processor = new NormalizerProcessor();

  public NormalizerDirectory() {
    classes.put(UnknownModel.class, UnknownNormalizer.class);
    classes.put(AllModel.class, AllNormalizer.class);
    classes.put(AnnotationModel.class, AnnotationNormalizer.class);
    classes.put(AnyAttributeModel.class, AnyAttributeNormalizer.class);
    classes.put(AnyModel.class, AnyNormalizer.class);
    classes.put(AppinfoModel.class, AppinfoNormalizer.class);
    classes.put(AttributeGroupModel.class, AttributeGroupNormalizer.class);
    classes.put(AttributeModel.class, AttributeNormalizer.class);
    classes.put(ChoiceModel.class, ChoiceNormalizer.class);
    classes.put(ComplexContentModel.class, ComplexContentNormalizer.class);
    classes.put(ComplexTypeModel.class, ComplexTypeNormalizer.class);
    classes.put(DocumentationModel.class, DocumentationNormalizer.class);
    classes.put(ElementModel.class, ElementNormalizer.class);
    classes.put(EnumerationModel.class, EnumerationNormalizer.class);
    classes.put(ExtensionModel.class, ExtensionNormalizer.class);
    classes.put(FieldModel.class, FieldNormalizer.class);
    classes.put(FractionDigitsModel.class, FractionDigitsNormalizer.class);
    classes.put(GroupModel.class, GroupNormalizer.class);
    classes.put(HasFacetModel.class, HasFacetNormalizer.class);
    classes.put(HasPropertyModel.class, HasPropertyNormalizer.class);
    classes.put(ImportModel.class, ImportNormalizer.class);
    classes.put(IncludeModel.class, IncludeNormalizer.class);
    classes.put(KeyModel.class, KeyNormalizer.class);
    classes.put(KeyrefModel.class, KeyrefNormalizer.class);
    classes.put(LengthModel.class, LengthNormalizer.class);
    classes.put(ListModel.class, ListNormalizer.class);
    classes.put(MaxExclusiveModel.class, MaxExclusiveNormalizer.class);
    classes.put(MaxInclusiveModel.class, MaxInclusiveNormalizer.class);
    classes.put(MaxLengthModel.class, MaxLengthNormalizer.class);
    classes.put(MinExclusiveModel.class, MinExclusiveNormalizer.class);
    classes.put(MinInclusiveModel.class, MinInclusiveNormalizer.class);
    classes.put(MinLengthModel.class, MinLengthNormalizer.class);
    classes.put(NotationModel.class, NotationNormalizer.class);
    classes.put(PatternModel.class, PatternNormalizer.class);
    classes.put(RedefineModel.class, RedefineNormalizer.class);
    classes.put(RestrictionModel.class, RestrictionNormalizer.class);
    classes.put(SchemaModel.class, SchemaNormalizer.class);
    classes.put(SelectorModel.class, SelectorNormalizer.class);
    classes.put(SequenceModel.class, SequenceNormalizer.class);
    classes.put(SimpleContentModel.class, SimpleContentNormalizer.class);
    classes.put(SimpleTypeModel.class, SimpleTypeNormalizer.class);
    classes.put(UnionModel.class, UnionNormalizer.class);
    classes.put(UniqueModel.class, UniqueNormalizer.class);
    classes.put(WhiteSpaceModel.class, WhiteSpaceNormalizer.class);
    keys = classes.keySet();
  }

  @Override
  public PipelineEntity getEntity(final Model entity, final Normalizer<?> parent) {
    return lookup(entity.getClass());
  }

  public PipelineEntity lookup(Class<? extends Model> clazz) {
    if (!keys.contains(clazz))
      throw new IllegalArgumentException("Unknown key: " + clazz.getSimpleName());

    Normalizer<?> normalizerInstance = instances.get(clazz);
    if (normalizerInstance != null)
      return normalizerInstance;

    final Class<? extends Normalizer<?>> normalizerClass = classes.get(clazz);
    try {
      final Constructor<? extends Normalizer<?>> constructor = normalizerClass.getConstructor(NormalizerDirectory.class);
      instances.put(clazz, normalizerInstance = constructor.newInstance(this));
      return normalizerInstance;
    }
    catch (final Exception e) {
      throw new LexerFailureException(e);
    }
  }

  @Override
  public PipelineProcessor<GeneratorContext,Model,Normalizer<?>> getProcessor() {
    return processor;
  }

  @Override
  public void clear() {
    instances.clear();
  }
}