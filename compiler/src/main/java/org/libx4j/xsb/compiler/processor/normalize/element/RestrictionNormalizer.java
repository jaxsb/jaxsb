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

package org.libx4j.xsb.compiler.processor.normalize.element;

import java.util.ArrayList;
import java.util.Collection;

import org.libx4j.xsb.compiler.lang.LexerFailureException;
import org.libx4j.xsb.compiler.lang.UniqueQName;
import org.libx4j.xsb.compiler.processor.Nameable;
import org.libx4j.xsb.compiler.processor.model.AttributableModel;
import org.libx4j.xsb.compiler.processor.model.Model;
import org.libx4j.xsb.compiler.processor.model.MultiplicableModel;
import org.libx4j.xsb.compiler.processor.model.RestrictableModel;
import org.libx4j.xsb.compiler.processor.model.element.AttributeModel;
import org.libx4j.xsb.compiler.processor.model.element.ComplexTypeModel;
import org.libx4j.xsb.compiler.processor.model.element.ElementModel;
import org.libx4j.xsb.compiler.processor.model.element.ListModel;
import org.libx4j.xsb.compiler.processor.model.element.RedefineModel;
import org.libx4j.xsb.compiler.processor.model.element.RestrictionModel;
import org.libx4j.xsb.compiler.processor.model.element.SchemaModel;
import org.libx4j.xsb.compiler.processor.model.element.SimpleTypeModel;
import org.libx4j.xsb.compiler.processor.model.element.UnionModel;
import org.libx4j.xsb.compiler.processor.normalize.Normalizer;
import org.libx4j.xsb.compiler.processor.normalize.NormalizerDirectory;

public final class RestrictionNormalizer extends Normalizer<RestrictionModel> {
  private final ElementNormalizer elementNormalizer = (ElementNormalizer)getDirectory().lookup(ElementModel.class);
  private final SimpleTypeNormalizer simpleTypeNormalizer = (SimpleTypeNormalizer)getDirectory().lookup(SimpleTypeModel.class);
  private final AttributeNormalizer attributeNormalizer = (AttributeNormalizer)getDirectory().lookup(AttributeModel.class);
  private final ComplexTypeNormalizer complexTypeNormalizer = (ComplexTypeNormalizer)getDirectory().lookup(ComplexTypeModel.class);

  public RestrictionNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final RestrictionModel model) {
  }

  @Override
  protected void stage2(final RestrictionModel model) {
    if (model.getBase() == null)
      return;

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
    else
      throw new LexerFailureException(getClass().getName());

    model.setBase(base);

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof SimpleTypeModel) {
        if (parent instanceof ListModel) {
          ((ListModel)parent).setItemType(base);
          break;
        }

        if (parent instanceof UnionModel) {
          // NOTE: We do not pass the SimpleTypeModel.Undefined
          // NOTE: version of this element because it will be modified
          // NOTE: later to add enumerations, which would end up
          // NOTE: modifying ALL such SimpleTypeModel.Undefined
          // NOTE: references.
//                  ((NamedModel)model.getParent()).setName(base.getName());
          ((UnionModel)parent).getMemberTypes().add((SimpleTypeModel<?>)model.getParent());
          break;
        }
      }

      if (parent instanceof Nameable && ((Nameable<?>)parent).getName() != null) {
        if (parent instanceof ElementModel) {
          // We do not want to dereference nested elements because there are name collisions
          ElementModel element = (ElementModel)parent;
          if (element.getParent() instanceof SchemaModel)
            element = elementNormalizer.parseElement(((Nameable<?>)parent).getName());

          if (element == null)
            throw new LexerFailureException("element == null");

          element.setSuperType(base);
          element.setRestriction(true);
        }
        else if (parent instanceof AttributeModel) {
          // We do not want to dereference nested attributes because there are name collisions
          AttributeModel attribute = (AttributeModel)parent;
          if (attribute.getParent() instanceof SchemaModel)
            attribute = attributeNormalizer.parseAttribute(((Nameable<?>)parent).getName());

          if (attribute == null)
            throw new LexerFailureException("attribute == null");

          attribute.setSuperType(base);
          attribute.setRestriction(true);
        }
        else if (parent instanceof SimpleTypeModel) {
          SimpleTypeModel<?> type = simpleTypeNormalizer.parseSimpleType(((Nameable<?>)parent).getName());
          if (type == null)
            type = complexTypeNormalizer.parseComplexType(((Nameable<?>)parent).getName());

          if (type == null)
            throw new LexerFailureException("type == null for " + ((Nameable<?>)parent).getName());

          if (type instanceof ComplexTypeModel && type.getSuperType() != null)
            break;

          // Update the superType and restriction flag of the reference model
          type.setSuperType(base);
          type.setRestriction(true);

          // Update the superType and restriction flag of this model
          ((SimpleTypeModel<?>)parent).setSuperType(base);
          ((SimpleTypeModel<?>)parent).setRestriction(true);
        }
        else {
          throw new LexerFailureException(((Nameable<?>)parent).getName().toString());
        }

        break;
      }
    }
  }

  @Override
  protected void stage3(final RestrictionModel model) {
    if (model.getBase() == null || model.getBase().getName() == null)
      return;

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof SimpleTypeModel && model.getBase().getName().equals(((Nameable<?>)parent).getName()) && parent.getParent() instanceof RedefineModel) {
        model.getBase().setRedefine((SimpleTypeModel<?>)parent);

        if (parent instanceof SimpleTypeModel) {
          final SimpleTypeModel<?> redefine = (SimpleTypeModel<?>)parent;
          redefine.setSuperType(model.getBase().getSuperType());
        }

        break;
      }
    }
  }

  @Override
  protected void stage4(final RestrictionModel model) {
  }

  @Override
  protected void stage5(final RestrictionModel model) {
    if (model.getBase() == null || UniqueQName.XS.getNamespaceURI().equals(model.getBase().getName().getNamespaceURI()))
      return;

    if (!(model.getBase() instanceof ComplexTypeModel))
      return;

    // handle all attributes
    final Collection<AttributeModel> restrictionAttributes = findChildAttributes(model.getChildren());
    for (final AttributeModel restrictionAttribute : restrictionAttributes) {
      final RestrictionPair<AttributeModel> baseAttributePair = findBaseAttribute(restrictionAttribute.getName(), model.getBase());
      if (baseAttributePair == null)
        throw new LexerFailureException("we should have found an attribute we're restricting! what's goin on?");

      restrictionAttribute.setRestriction(baseAttributePair.getModel());
      restrictionAttribute.setRestrictionOwner(baseAttributePair.getParent());
    }

    // find all elements declared in this restriction
    final Collection<ElementModel> restrictionElements = new ArrayList<ElementModel>();
    findChildElements(restrictionElements, model.getChildren());
    for (final ElementModel restrictionElement : restrictionElements) {
      final RestrictionPair<ElementModel> baseElementPair = findBaseElement(restrictionElement.getName(), model.getBase());
      if (baseElementPair == null)
        throw new LexerFailureException("we should have found an element we're restricting! what's goin on?");

      restrictionElement.setRestriction(baseElementPair.getModel());
      restrictionElement.setRestrictionOwner(baseElementPair.getParent());
    }
  }

  @Override
  protected void stage6(final RestrictionModel model) {
  }

  private static Collection<AttributeModel> findChildAttributes(final Collection<Model> children) {
    Collection<AttributeModel> attributes = new ArrayList<AttributeModel>();
    for (final Model model : children)
      if (model instanceof AttributeModel)
        attributes.add((AttributeModel)model);

    return attributes;
  }

  private static RestrictionPair<AttributeModel> findBaseAttribute(final UniqueQName name, final SimpleTypeModel<?> typeModel) {
    if (name == null || typeModel == null || UniqueQName.XS.getNamespaceURI().equals(typeModel.getName().getNamespaceURI()))
      return null;

    // FIXME: Can I equate on just the localPart of the QName???
    if (typeModel instanceof ComplexTypeModel)
      for (final AttributeModel attribute : ((AttributableModel)typeModel).getAttributes())
        if (name.getLocalPart().equals(attribute.getName().getLocalPart()))
          return new RestrictionPair<AttributeModel>(attribute, typeModel);

    return findBaseAttribute(name, typeModel.getSuperType());
  }

  private static void findChildElements(final Collection<ElementModel> elements, final Collection<Model> children) {
    for (final Model model : children) {
      if (model instanceof ElementModel) {
        elements.add((ElementModel)model);
        continue;
      }

      if (model instanceof MultiplicableModel)
        findChildElements(elements, model.getChildren());
    }
  }

  private static RestrictionPair<ElementModel> findBaseElement(final UniqueQName name, final SimpleTypeModel<?> typeModel) {
    if (name == null || typeModel == null || UniqueQName.XS.getNamespaceURI().equals(typeModel.getName().getNamespaceURI()))
      return null;

    if (typeModel instanceof ComplexTypeModel) {
      final Collection<ElementModel> elements = new ArrayList<ElementModel>();
      findChildElements(elements, typeModel.getChildren());
      for (final MultiplicableModel multiplicableModel : ((ComplexTypeModel<?>)typeModel).getMultiplicableModels())
        findChildElements(elements, ((Model)multiplicableModel).getChildren());

      // FIXME: Can I equate on just the localPart of the QName???
      for (final ElementModel element : elements)
        if (name.getLocalPart().equals(element.getName().getLocalPart()))
          return new RestrictionPair<ElementModel>(element, typeModel);
    }

    return findBaseElement(name, typeModel.getSuperType());
  }

  private static final class RestrictionPair<T extends RestrictableModel<?>> {
    private final T model;
    private final SimpleTypeModel<?> parent;

    public RestrictionPair(final T model, final SimpleTypeModel<?> parent) {
      this.model = model;
      this.parent = parent;
    }

    public T getModel() {
      return model;
    }

    public SimpleTypeModel<?> getParent() {
      return parent;
    }
  }
}