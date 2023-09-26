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
import java.util.LinkedHashSet;
import java.util.List;

import org.jaxsb.compiler.lang.LexerFailureException;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.MultiplicableModel;
import org.jaxsb.compiler.processor.model.element.AttributeModel;
import org.jaxsb.compiler.processor.model.element.ComplexTypeModel;
import org.jaxsb.compiler.processor.model.element.ElementModel;
import org.jaxsb.compiler.processor.model.element.ExtensionModel;
import org.jaxsb.compiler.processor.model.element.RedefineModel;
import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;

public final class ExtensionNormalizer extends Normalizer<ExtensionModel> {
  private final ElementNormalizer elementNormalizer = (ElementNormalizer)getDirectory().lookup(ElementModel.class);
  private final SimpleTypeNormalizer simpleTypeNormalizer = (SimpleTypeNormalizer)getDirectory().lookup(SimpleTypeModel.class);
  private final ComplexTypeNormalizer complexTypeNormalizer = (ComplexTypeNormalizer)getDirectory().lookup(ComplexTypeModel.class);

  public ExtensionNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final ExtensionModel model) {
  }

  @Override
  protected void stage2(final ExtensionModel model) {
    // First de-reference the base
    SimpleTypeModel<?> base;
    final SimpleTypeModel<?> modelBase = model.getBase();
    if (modelBase instanceof SimpleTypeModel.Reference) {
      final UniqueQName name = modelBase.getName();
      base = simpleTypeNormalizer.parseSimpleType(name);
      if (base == null)
        base = complexTypeNormalizer.parseComplexType(modelBase.getName());

      if (base == null) {
        if (!UniqueQName.XS.getNamespaceURI().equals(modelBase.getName().getNamespaceURI()))
          throw new LexerFailureException("base == null for " + modelBase.getName());

        base = SimpleTypeModel.Undefined.parseSimpleType(modelBase.getName());
      }
    }
    else if (modelBase instanceof ComplexTypeModel.Reference) {
      final UniqueQName name = modelBase.getName();
      base = complexTypeNormalizer.parseComplexType(name);
      if (base == null) {
        if (!UniqueQName.XS.getNamespaceURI().equals(modelBase.getName().getNamespaceURI()))
          throw new LexerFailureException("base == null for " + modelBase.getName());

        base = ComplexTypeModel.Undefined.parseComplexType(modelBase.getName());
      }
    }
    else {
      throw new LexerFailureException(getClass().getName());
    }

    model.setBase(base);

    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
      if (parent instanceof Nameable && ((Nameable<?>)parent).getName() != null) {
        if (parent instanceof ElementModel) {
          // We do not want to dereference nested elements because there are name collisions
          ElementModel element = (ElementModel)parent;
          if (element.getParent() instanceof SchemaModel)
            element = elementNormalizer.parseElement(((Nameable<?>)parent).getName());

          if (element == null)
            throw new LexerFailureException("element is null");

          element.setSuperType(base);
        }
        else if (parent instanceof SimpleTypeModel) {
          SimpleTypeModel<?> type = simpleTypeNormalizer.parseSimpleType(((Nameable<?>)parent).getName());
          if (type == null)
            type = complexTypeNormalizer.parseComplexType(((Nameable<?>)parent).getName());

          if (type == null)
            throw new LexerFailureException("type == null for " + ((Nameable<?>)parent).getName());

          // NOTE: This occurs when we're doing a <redefine/>
          if (type == base)
            break;

          // Update the superType and restriction flag of the reference model
          type.setSuperType(base);

          // Update the superType and restriction flag of this model
          ((SimpleTypeModel<?>)parent).setSuperType(base);
        }
        else {
          throw new LexerFailureException(((Nameable<?>)parent).getName().toString());
        }

        break;
      }
    }
  }

  @Override
  protected void stage3(final ExtensionModel model) {
    if (model.getBase() == null || model.getBase().getName() == null)
      return;

    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
      if (parent instanceof SimpleTypeModel && model.getBase().getName().equals(((Nameable<?>)parent).getName()) && parent.getParent() instanceof RedefineModel) {
        model.getBase().setRedefine((SimpleTypeModel<?>)parent);

        if (parent instanceof SimpleTypeModel) {
          final SimpleTypeModel<?> redefine = (SimpleTypeModel<?>)parent;
          redefine.setSuperType(model.getBase().getSuperType());
        }
      }
    }
  }

  @Override
  protected void stage4(final ExtensionModel model) {
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void stage5(final ExtensionModel model) {
    if (model.getBase() == null || model.getBase().getName() == null)
      return;

    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
      if (parent instanceof SimpleTypeModel && model.getBase().getName().equals(((Nameable<?>)parent).getName()) && parent.getParent() instanceof RedefineModel) {
        if (parent instanceof ComplexTypeModel) {
          if (!(model.getBase() instanceof ComplexTypeModel))
            throw new LexerFailureException("complexType redefinition done by something other than a complexType");

          final ComplexTypeModel<?> redefine = (ComplexTypeModel<?>)parent;
          if (redefine.getAttributes().size() != 0) {
            final LinkedHashSet<AttributeModel> attributes = (LinkedHashSet<AttributeModel>)((ComplexTypeModel<?>)model.getBase()).getAttributes().clone();
            attributes.addAll(redefine.getAttributes());
            redefine.getAttributes().clear();
            redefine.getAttributes().addAll(attributes);
          }
          else {
            redefine.getAttributes().addAll(((ComplexTypeModel<?>)model.getBase()).getAttributes());
          }

          if (redefine.getMultiplicableModels().size() != 0) {
            final List<MultiplicableModel> multiplicableModels = (ArrayList<MultiplicableModel>)((ComplexTypeModel<?>)model.getBase()).getMultiplicableModels().clone();
            multiplicableModels.addAll(redefine.getMultiplicableModels());
            redefine.getMultiplicableModels().clear();
            redefine.getMultiplicableModels().addAll(multiplicableModels);
          }
          else {
            redefine.getMultiplicableModels().addAll(((ComplexTypeModel<?>)model.getBase()).getMultiplicableModels());
          }
        }

        break;
      }
    }
  }

  @Override
  protected void stage6(final ExtensionModel model) {
  }
}