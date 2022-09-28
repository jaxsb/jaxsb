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

import org.jaxsb.compiler.lang.LexerFailureException;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.AttributableModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.MultiplicableModel;
import org.jaxsb.compiler.processor.model.RestrictableModel;
import org.jaxsb.compiler.processor.model.element.AttributeModel;
import org.jaxsb.compiler.processor.model.element.ComplexTypeModel;
import org.jaxsb.compiler.processor.model.element.ElementModel;
import org.jaxsb.compiler.processor.model.element.ListModel;
import org.jaxsb.compiler.processor.model.element.RedefineModel;
import org.jaxsb.compiler.processor.model.element.RestrictionModel;
import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.jaxsb.compiler.processor.model.element.UnionModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;

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
    SimpleTypeModel<?> base;
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

    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
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

    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
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
    final ArrayList<AttributeModel> restrictionAttributes = findChildAttributes(model.getChildren());
    for (int i = 0, i$ = restrictionAttributes.size(); i < i$; ++i) { // [RA]
      final AttributeModel restrictionAttribute = restrictionAttributes.get(i);
      final RestrictionPair<AttributeModel> baseAttributePair = findBaseAttribute(restrictionAttribute.getName(), model.getBase());
      if (baseAttributePair == null)
        throw new LexerFailureException("we should have found an attribute we're restricting! what's going on?");

      restrictionAttribute.setRestriction(baseAttributePair.getModel());
      restrictionAttribute.setRestrictionOwner(baseAttributePair.getParent());
    }

    // find all elements declared in this restriction
    final ArrayList<ElementModel> restrictionElements = new ArrayList<>();
    findChildElements(restrictionElements, model.getChildren());
    for (int i = 0, i$ = restrictionElements.size(); i < i$; ++i) { // [RA]
      final ElementModel restrictionElement = restrictionElements.get(i);
      final RestrictionPair<ElementModel> baseElementPair = findBaseElement(restrictionElement.getName(), model.getBase());
      if (baseElementPair == null)
        throw new LexerFailureException("we should have found an element we're restricting! what's going on?");

      restrictionElement.setRestriction(baseElementPair.getModel());
      restrictionElement.setRestrictionOwner(baseElementPair.getParent());
    }
  }

  @Override
  protected void stage6(final RestrictionModel model) {
  }

  private static ArrayList<AttributeModel> findChildAttributes(final ArrayList<? extends Model> children) {
    final ArrayList<AttributeModel> attributes = new ArrayList<>();
    for (int i = 0, i$ = children.size(); i < i$; ++i) { // [RA]
      final Model model = children.get(i);
      if (model instanceof AttributeModel)
        attributes.add((AttributeModel)model);
    }

    return attributes;
  }

  private static RestrictionPair<AttributeModel> findBaseAttribute(final UniqueQName name, final SimpleTypeModel<?> typeModel) {
    if (name == null || typeModel == null || UniqueQName.XS.getNamespaceURI().equals(typeModel.getName().getNamespaceURI()))
      return null;

    // FIXME: Can I equate on just the localName of the QName???
    if (typeModel instanceof ComplexTypeModel) {
      final LinkedHashSet<AttributeModel> attributes = ((AttributableModel)typeModel).getAttributes();
      if (attributes.size() > 0)
        for (final AttributeModel attribute : attributes) // [S]
          if (name.getLocalPart().equals(attribute.getName().getLocalPart()))
            return new RestrictionPair<>(attribute, typeModel);
    }

    return findBaseAttribute(name, typeModel.getSuperType());
  }

  private static void findChildElements(final ArrayList<? super ElementModel> elements, final ArrayList<? extends Model> children) {
    for (int i = 0, i$ = children.size(); i < i$; ++i) { // [RA]
      final Model model = children.get(i);
      if (model instanceof ElementModel)
        elements.add((ElementModel)model);
      else if (model instanceof MultiplicableModel)
        findChildElements(elements, model.getChildren());
    }
  }

  private static RestrictionPair<ElementModel> findBaseElement(final UniqueQName name, final SimpleTypeModel<?> typeModel) {
    if (name == null || typeModel == null || UniqueQName.XS.getNamespaceURI().equals(typeModel.getName().getNamespaceURI()))
      return null;

    if (typeModel instanceof ComplexTypeModel) {
      final ArrayList<ElementModel> elements = new ArrayList<>();
      findChildElements(elements, typeModel.getChildren());
      final ArrayList<MultiplicableModel> multiplicableModels = ((ComplexTypeModel<?>)typeModel).getMultiplicableModels();
      for (int i = 0, i$ = multiplicableModels.size(); i < i$; ++i) // [RA]
        findChildElements(elements, ((Model)multiplicableModels.get(i)).getChildren());

      // FIXME: Can I equate on just the localName of the QName???
      for (int i = 0, i$ = elements.size(); i < i$; ++i) { // [RA]
        final ElementModel element = elements.get(i);
        if (name.getLocalPart().equals(element.getName().getLocalPart()))
          return new RestrictionPair<>(element, typeModel);
      }
    }

    return findBaseElement(name, typeModel.getSuperType());
  }

  private static final class RestrictionPair<T extends RestrictableModel<?>> {
    private final T model;
    private final SimpleTypeModel<?> parent;

    private RestrictionPair(final T model, final SimpleTypeModel<?> parent) {
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