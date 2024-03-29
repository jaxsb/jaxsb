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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jaxsb.compiler.lang.LexerFailureException;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.model.ElementableModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.element.ComplexTypeModel;
import org.jaxsb.compiler.processor.model.element.ElementModel;
import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;

public final class ElementNormalizer extends Normalizer<ElementModel> {
  private final Map<UniqueQName,ElementModel> all = new HashMap<>();
  private final SimpleTypeNormalizer simpleTypeNormalizer = (SimpleTypeNormalizer)getDirectory().lookup(SimpleTypeModel.class);
  private final ComplexTypeNormalizer complexTypeNormalizer = (ComplexTypeNormalizer)getDirectory().lookup(ComplexTypeModel.class);

  public ElementNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  public ElementModel parseElement(final UniqueQName name) {
    return all.get(name);
  }

  @Override
  protected void stage1(final ElementModel model) {
    if (model.getName() == null || !(model.getParent() instanceof SchemaModel))
      return;

    if (parseElement(model.getName()) == null)
      all.put(model.getName(), model);
  }

  @Override
  protected void stage2(final ElementModel model) {
    // First set the elementFormDefault
    Model schema = model.getParent();
    if (schema != null)
      while (!((schema = schema.getParent()) instanceof SchemaModel) && schema != null);

    if (schema != null)
      model.setFormDefault(((SchemaModel)schema).getElementFormDefault());

    if (model.getRef() instanceof ElementModel.Reference) {
      final ElementModel ref = parseElement(model.getRef().getName());
      if (ref == null)
        throw new LexerFailureException("ref == null for " + model.getRef().getName());

      model.setRef(ref);
    }
    else if (model.getName() != null) {
      if (model.getSuperType() instanceof ComplexTypeModel.Reference) {
        SimpleTypeModel<?> type = complexTypeNormalizer.parseComplexType(model.getSuperType().getName());
        if (type == null)
          type = simpleTypeNormalizer.parseSimpleType(model.getSuperType().getName());

        if (type == null) {
          if (!UniqueQName.XS.getNamespaceURI().equals(model.getSuperType().getName().getNamespaceURI()))
            throw new LexerFailureException("type == null for " + model.getSuperType().getName());

          type = SimpleTypeModel.Undefined.parseSimpleType(model.getSuperType().getName());
        }

        model.setSuperType(type);
      }
    }
    else {
      throw new LexerFailureException("element type not handled");
    }
  }

  @Override
  protected void stage3(final ElementModel model) {
  }

  @Override
  protected void stage4(final ElementModel model) {
    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof ElementableModel) {
        ((ElementableModel)parent).addMultiplicableModel(model);
        break;
      }
    }

    parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof ElementModel) {
        ((ElementModel)parent).setExtension(true);
        break;
      }
    }
  }

  @Override
  protected void stage5(final ElementModel model) {
  }

  @Override
  protected void stage6(final ElementModel model) {
    if (model.getName() == null || model.getRef() != null || model.getSuperType() != null)
      return;

    if (model.getSubstitutionGroup() != null) {
      model.setSuperType(parseElement(model.getSubstitutionGroup()));
      return;
    }

    boolean def = false;
    final ArrayList<Model> children = model.getChildren();
    for (int i = 0, i$ = children.size(); i < i$; ++i) { // [RA]
      final Model child = children.get(i);
      if (child instanceof SimpleTypeModel) {
        def = true;
        break;
      }
    }

    if (def) {
      model.setSuperType(ComplexTypeModel.Undefined.parseComplexType(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anySimpleType")));
    }
    else {
      final SimpleTypeModel<?> type = ComplexTypeModel.Undefined.parseComplexType(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anyType"));
      model.setSuperType(type);
      model.setItemTypes(Collections.singletonList(type));
    }
  }
}