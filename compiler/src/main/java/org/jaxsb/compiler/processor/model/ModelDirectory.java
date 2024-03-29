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

package org.jaxsb.compiler.processor.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jaxsb.compiler.lang.LexerFailureException;
import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.composite.SchemaComposite;
import org.jaxsb.compiler.processor.composite.SchemaNodeComposite;
import org.jaxsb.compiler.processor.model.element.AllModel;
import org.jaxsb.compiler.processor.model.element.AnnotationModel;
import org.jaxsb.compiler.processor.model.element.AnyAttributeModel;
import org.jaxsb.compiler.processor.model.element.AnyModel;
import org.jaxsb.compiler.processor.model.element.AppinfoModel;
import org.jaxsb.compiler.processor.model.element.AttributeGroupModel;
import org.jaxsb.compiler.processor.model.element.AttributeModel;
import org.jaxsb.compiler.processor.model.element.ChoiceModel;
import org.jaxsb.compiler.processor.model.element.ComplexContentModel;
import org.jaxsb.compiler.processor.model.element.ComplexTypeModel;
import org.jaxsb.compiler.processor.model.element.DocumentationModel;
import org.jaxsb.compiler.processor.model.element.ElementModel;
import org.jaxsb.compiler.processor.model.element.EnumerationModel;
import org.jaxsb.compiler.processor.model.element.ExtensionModel;
import org.jaxsb.compiler.processor.model.element.FieldModel;
import org.jaxsb.compiler.processor.model.element.FractionDigitsModel;
import org.jaxsb.compiler.processor.model.element.GroupModel;
import org.jaxsb.compiler.processor.model.element.HasFacetModel;
import org.jaxsb.compiler.processor.model.element.HasPropertyModel;
import org.jaxsb.compiler.processor.model.element.ImportModel;
import org.jaxsb.compiler.processor.model.element.IncludeModel;
import org.jaxsb.compiler.processor.model.element.KeyModel;
import org.jaxsb.compiler.processor.model.element.KeyrefModel;
import org.jaxsb.compiler.processor.model.element.LengthModel;
import org.jaxsb.compiler.processor.model.element.ListModel;
import org.jaxsb.compiler.processor.model.element.MaxExclusiveModel;
import org.jaxsb.compiler.processor.model.element.MaxInclusiveModel;
import org.jaxsb.compiler.processor.model.element.MaxLengthModel;
import org.jaxsb.compiler.processor.model.element.MinExclusiveModel;
import org.jaxsb.compiler.processor.model.element.MinInclusiveModel;
import org.jaxsb.compiler.processor.model.element.MinLengthModel;
import org.jaxsb.compiler.processor.model.element.NotationModel;
import org.jaxsb.compiler.processor.model.element.PatternModel;
import org.jaxsb.compiler.processor.model.element.RedefineModel;
import org.jaxsb.compiler.processor.model.element.RestrictionModel;
import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.jaxsb.compiler.processor.model.element.SelectorModel;
import org.jaxsb.compiler.processor.model.element.SequenceModel;
import org.jaxsb.compiler.processor.model.element.SimpleContentModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.jaxsb.compiler.processor.model.element.UnionModel;
import org.jaxsb.compiler.processor.model.element.UniqueModel;
import org.jaxsb.compiler.processor.model.element.UnknownModel;
import org.jaxsb.compiler.processor.model.element.WhiteSpaceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

public final class ModelDirectory implements PipelineDirectory<GeneratorContext,SchemaComposite,Model> {
  private static final Logger logger = LoggerFactory.getLogger(ModelDirectory.class);

  private final Map<String,Class<? extends Model>> classes = new HashMap<>(39);
  private final Collection<String> keys;
  private final ModelProcessor processor = new ModelProcessor();

  public ModelDirectory() {
    classes.put(null, UnknownModel.class);

    classes.put("all", AllModel.class);
    classes.put("annotation", AnnotationModel.class);
    classes.put("anyAttribute", AnyAttributeModel.class);
    classes.put("any", AnyModel.class);
    classes.put("appinfo", AppinfoModel.class);
    classes.put("attributeGroup", AttributeGroupModel.class);
    classes.put("attribute", AttributeModel.class);
    classes.put("choice", ChoiceModel.class);
    classes.put("complexContent", ComplexContentModel.class);
    classes.put("complexType", ComplexTypeModel.class);
    classes.put("documentation", DocumentationModel.class);
    classes.put("element", ElementModel.class);
    classes.put("enumeration", EnumerationModel.class);
    classes.put("extension", ExtensionModel.class);
    classes.put("field", FieldModel.class);
    classes.put("fractionDigits", FractionDigitsModel.class);
    classes.put("group", GroupModel.class);
    classes.put("hasFacet", HasFacetModel.class);
    classes.put("hasProperty", HasPropertyModel.class);
    classes.put("import", ImportModel.class);
    classes.put("include", IncludeModel.class);
    classes.put("key", KeyModel.class);
    classes.put("keyref", KeyrefModel.class);
    classes.put("length", LengthModel.class);
    classes.put("list", ListModel.class);
    classes.put("maxExclusive", MaxExclusiveModel.class);
    classes.put("maxInclusive", MaxInclusiveModel.class);
    classes.put("maxLength", MaxLengthModel.class);
    classes.put("minExclusive", MinExclusiveModel.class);
    classes.put("minInclusive", MinInclusiveModel.class);
    classes.put("minLength", MinLengthModel.class);
    classes.put("notation", NotationModel.class);
    classes.put("pattern", PatternModel.class);
    classes.put("redefine", RedefineModel.class);
    classes.put("restriction", RestrictionModel.class);
    classes.put("schema", SchemaModel.class);
    classes.put("selector", SelectorModel.class);
    classes.put("sequence", SequenceModel.class);
    classes.put("simpleContent", SimpleContentModel.class);
    classes.put("simpleType", SimpleTypeModel.class);
    classes.put("union", UnionModel.class);
    classes.put("unique", UniqueModel.class);
    classes.put("whiteSpace", WhiteSpaceModel.class);
    keys = classes.keySet();
  }

  @Override
  public PipelineEntity getEntity(final SchemaComposite entity, final Model parent) {
    if (!(entity instanceof SchemaNodeComposite))
      return null;

    final SchemaNodeComposite schemaNodeComposite = (SchemaNodeComposite)entity;
    String elementName = schemaNodeComposite.getNode().getLocalName();
    if (elementName == null)
      throw new IllegalArgumentException("Node key without local name");

    if (!keys.contains(elementName)) {
      if (logger.isDebugEnabled()) { logger.debug("Unknown schema element <" + (schemaNodeComposite.getNode().getPrefix() != null ? schemaNodeComposite.getNode().getPrefix() + ":" : "") + elementName + ">"); }
      elementName = null;
    }

    final Class<? extends Model> modelClass = classes.get(elementName);
    try {
      final Constructor<? extends Model> constructor = modelClass.getDeclaredConstructor(Node.class, Model.class);
      constructor.setAccessible(true);
      return constructor.newInstance(schemaNodeComposite.getNode(), parent);
    }
    catch (final IllegalAccessException | InstantiationException | NoSuchMethodException e) {
      throw new LexerFailureException(e);
    }
    catch (final InvocationTargetException e) {
      final Throwable cause = e.getCause();
      if (cause instanceof RuntimeException)
        throw (RuntimeException)cause;

      throw new LexerFailureException(cause);
    }
  }

  @Override
  public PipelineProcessor<GeneratorContext,SchemaComposite,Model> getProcessor() {
    return processor;
  }

  @Override
  public void clear() {
  }
}