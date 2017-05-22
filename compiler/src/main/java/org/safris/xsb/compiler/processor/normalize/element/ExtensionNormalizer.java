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

package org.safris.xsb.compiler.processor.normalize.element;

import java.util.LinkedHashSet;

import org.safris.xsb.compiler.lang.LexerFailureException;
import org.safris.xsb.compiler.lang.UniqueQName;
import org.safris.xsb.compiler.processor.Nameable;
import org.safris.xsb.compiler.processor.model.Model;
import org.safris.xsb.compiler.processor.model.MultiplicableModel;
import org.safris.xsb.compiler.processor.model.element.AttributeModel;
import org.safris.xsb.compiler.processor.model.element.ComplexTypeModel;
import org.safris.xsb.compiler.processor.model.element.ElementModel;
import org.safris.xsb.compiler.processor.model.element.ExtensionModel;
import org.safris.xsb.compiler.processor.model.element.RedefineModel;
import org.safris.xsb.compiler.processor.model.element.SchemaModel;
import org.safris.xsb.compiler.processor.model.element.SimpleTypeModel;
import org.safris.xsb.compiler.processor.normalize.Normalizer;
import org.safris.xsb.compiler.processor.normalize.NormalizerDirectory;

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
    SimpleTypeModel<?> base = null;
    if (model.getBase() instanceof SimpleTypeModel.Reference) {
      base = simpleTypeNormalizer.parseSimpleType(model.getBase().getName());
      if (base == null)
        base = complexTypeNormalizer.parseComplexType(model.getBase().getName());

      if (base == null) {
        if (!UniqueQName.XS.getNamespaceURI().equals(model.getBase().getName().getNamespaceURI()))
          throw new LexerFailureException("base == null for " + model.getBase().getName());

        base = SimpleTypeModel.Undefined.parseSimpleType(model.getBase().getName());
      }
    }
    else if (model.getBase() instanceof ComplexTypeModel.Reference) {
      base = complexTypeNormalizer.parseComplexType(model.getBase().getName());
      if (base == null) {
        if (!UniqueQName.XS.getNamespaceURI().equals(model.getBase().getName().getNamespaceURI()))
          throw new LexerFailureException("base == null for " + model.getBase().getName());

        base = ComplexTypeModel.Undefined.parseComplexType(model.getBase().getName());
      }
    }
    else {
      throw new LexerFailureException(getClass().getName());
    }

    model.setBase(base);

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof Nameable && ((Nameable<?>)parent).getName() != null) {
        if (parent instanceof ElementModel) {
          // We do not want to dereference nested elements because there are name collisions
          ElementModel element = (ElementModel)parent;
          if (element.getParent() instanceof SchemaModel)
            element = elementNormalizer.parseElement(((Nameable<?>)parent).getName());

          if (element == null)
            throw new LexerFailureException("element == null");

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

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof SimpleTypeModel && model.getBase().getName().equals(((Nameable<?>)parent).getName()) && parent.getParent() instanceof RedefineModel) {
        model.getBase().setRedefine((SimpleTypeModel<?>)parent);

        if (parent instanceof SimpleTypeModel) {
          SimpleTypeModel<?> redefine = (SimpleTypeModel<?>)parent;
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

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof SimpleTypeModel && model.getBase().getName().equals(((Nameable<?>)parent).getName()) && parent.getParent() instanceof RedefineModel) {
        if (parent instanceof ComplexTypeModel) {
          if (!(model.getBase() instanceof ComplexTypeModel))
            throw new LexerFailureException("complexType redefinition done by something other than a complexType");

          ComplexTypeModel<?> redefine = (ComplexTypeModel<?>)parent;
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
            final LinkedHashSet<MultiplicableModel> multiplicableModels = (LinkedHashSet<MultiplicableModel>)((ComplexTypeModel<?>)model.getBase()).getMultiplicableModels().clone();
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