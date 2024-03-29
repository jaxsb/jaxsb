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

package org.jaxsb.compiler.processor.normalize.element;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.AttributableModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.element.AttributeGroupModel;
import org.jaxsb.compiler.processor.model.element.AttributeModel;
import org.jaxsb.compiler.processor.model.element.ComplexTypeModel;
import org.jaxsb.compiler.processor.model.element.EnumerationModel;
import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;

public final class AttributeNormalizer extends Normalizer<AttributeModel> {
  private final Map<UniqueQName,AttributeModel> all = new HashMap<>();
  private final SimpleTypeNormalizer simpleTypeNormalizer = (SimpleTypeNormalizer)getDirectory().lookup(SimpleTypeModel.class);

  public AttributeNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  public AttributeModel parseAttribute(final UniqueQName name) {
    return all.get(name);
  }

  @Override
  protected void stage1(final AttributeModel model) {
    if (model.getName() == null || !(model.getParent() instanceof SchemaModel))
      return;

    if (parseAttribute(model.getName()) == null)
      all.put(model.getName(), model);
  }

  @Override
  protected void stage2(final AttributeModel model) {
    // First set the attributeFormDefault value
    Model schema = model.getParent();
    if (schema != null)
      while (!((schema = schema.getParent()) instanceof SchemaModel) && schema != null);

    if (schema != null)
      model.setFormDefault(((SchemaModel)schema).getAttributeFormDefault());

    if (model.getRef() instanceof AttributeModel.Reference) {
      final AttributeModel ref = parseAttribute(model.getRef().getName());
      model.setRef(ref);
    }

    if (model.getSuperType() instanceof SimpleTypeModel.Reference) {
      SimpleTypeModel<?> type = simpleTypeNormalizer.parseSimpleType(model.getSuperType().getName());
      if (type == null)
        type = SimpleTypeModel.Undefined.parseSimpleType(model.getSuperType().getName());

      model.setSuperType(type);
    }

    // Add the attribute to the attributeGroup
    if (model.getParent() instanceof AttributeGroupModel)
      ((AttributeGroupModel)model.getParent()).addAttribute(model);
  }

  @Override
  protected void stage3(final AttributeModel model) {
  }

  @Override
  protected void stage4(final AttributeModel model) {
    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
      if (parent instanceof AttributableModel && parent instanceof Nameable && ((Nameable<?>)parent).getName() != null) {
        ((AttributableModel)parent).addAttribute(model);
        break;
      }
    }

    if (model.getFixed() != null)
      model.addEnumeration(new EnumerationModel(model.getFixed()));
  }

  @Override
  protected void stage5(final AttributeModel model) {
  }

  @Override
  protected void stage6(final AttributeModel model) {
    if (model.getName() == null)
      return;

    if (model.getSuperType() == null) {
      final SimpleTypeModel<?> type = ComplexTypeModel.Undefined.parseComplexType(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anySimpleType"));
      model.setSuperType(type);
      model.setItemTypes(Collections.singletonList(type));
    }
  }
}