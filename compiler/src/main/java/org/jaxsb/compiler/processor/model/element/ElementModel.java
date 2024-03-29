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

package org.jaxsb.compiler.processor.model.element;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Formable;
import org.jaxsb.compiler.processor.Referenceable;
import org.jaxsb.compiler.processor.model.AliasModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.MultiplicableModel;
import org.jaxsb.compiler.processor.model.ReferableModel;
import org.jaxsb.compiler.processor.model.RestrictableModel;
import org.jaxsb.compiler.schema.attribute.Form;
import org.jaxsb.compiler.schema.attribute.Occurs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ElementModel extends ComplexTypeModel<SimpleTypeModel<?>> implements Formable<Model>, MultiplicableModel, ReferableModel<ElementModel>, RestrictableModel<ElementModel> {
  private Boolean _abstract = false;
  private QName _default;
  private QName fixed;
  private Occurs maxOccurs = Occurs.parseOccurs("1");
  private Occurs minOccurs = Occurs.parseOccurs("1");
  private Boolean nillable = false;
  private ElementModel ref;
  private UniqueQName substitutionGroup;
  private Form formDefault;
  private AliasModel restrictionOwner;
  private ElementModel restriction;

  protected ElementModel(final Node node, final Model parent) {
    super(node, parent);
    if (node == null)
      return;

    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if (attribute.getNodeValue() == null)
        continue;

      if ("abstract".equals(attribute.getLocalName()))
        _abstract = Boolean.parseBoolean(attribute.getNodeValue());
      else if ("default".equals(attribute.getLocalName()))
        _default = parseQNameValue(attribute.getNodeValue(), node);
      else if ("fixed".equals(attribute.getLocalName()))
        fixed = parseQNameValue(attribute.getNodeValue(), node);
      else if ("maxOccurs".equals(attribute.getLocalName()))
        maxOccurs = Occurs.parseOccurs(attribute.getNodeValue());
      else if ("minOccurs".equals(attribute.getLocalName()))
        minOccurs = Occurs.parseOccurs(attribute.getNodeValue());
      else if ("nillable".equals(attribute.getLocalName()))
        nillable = Boolean.parseBoolean(attribute.getNodeValue());
      else if ("ref".equals(attribute.getLocalName()))
        setRef(ElementModel.Reference.parseElement(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node))));
      else if ("substitutionGroup".equals(attribute.getLocalName()))
        substitutionGroup = UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node));
      else if ("type".equals(attribute.getLocalName()))
        setSuperType(ComplexTypeModel.Reference.parseComplexType(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node))));
    }
  }

  @Override
  public final Boolean getAbstract() {
    return _abstract;
  }

  @Override
  public final void setRestriction(final ElementModel restriction) {
    this.restriction = restriction;
  }

  @Override
  public final ElementModel getRestriction() {
    return restriction;
  }

  @Override
  public final AliasModel getRestrictionOwner() {
    return restrictionOwner;
  }

  @Override
  public final void setRestrictionOwner(final AliasModel restrictionOwner) {
    this.restrictionOwner = restrictionOwner;
  }

  @Override
  public final ElementModel getRef() {
    return ref;
  }

  @Override
  public final void setRef(final ElementModel ref) {
    this.ref = ref;
  }

  @Override
  public final UniqueQName getName() {
    return ref != null ? ref.getName() : super.getName();
  }

  @Override
  public final SimpleTypeModel<?> getSuperType() {
    return ref != null ? ref.getSuperType() : super.getSuperType();
  }

  public final void setFormDefault(final Form formDefault) {
    this.formDefault = formDefault;
  }

  @Override
  public final Form getFormDefault() {
    return formDefault != null ? formDefault : getSchema().getElementFormDefault();
  }

  public final QName getDefault() {
    return _default;
  }

  public final QName getFixed() {
    return fixed;
  }

  @Override
  public final Occurs getMaxOccurs() {
    return maxOccurs;
  }

  @Override
  public final Occurs getMinOccurs() {
    return minOccurs;
  }

  public final Boolean getNillable() {
    return nillable;
  }

  public final UniqueQName getSubstitutionGroup() {
    return substitutionGroup;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof ElementModel))
      return false;

    final ElementModel that = (ElementModel)obj;
    final ElementModel a = getRef() != null ? getRef() : this;
    final ElementModel b = that.getRef() != null ? that.getRef() : that;
    return a.getName().equals(b.getName());
  }

  @Override
  public boolean isQualified(final boolean nested) {
    return !nested && (getRef() != null || getSchema().getElementFormDefault() == Form.QUALIFIED);
  }

  @Override
  public int hashCode() {
    if (getRef() != null && getRef().getName() != null)
      return getRef().getName().toString().hashCode();

    if (getName() != null)
      return getName().toString().hashCode();

    return super.hashCode();
  }

  @Override
  public String toString() {
    return UniqueQName.XS.getNamespaceURI() + " " + getName();
  }

  public static final class Reference extends ElementModel implements Referenceable {
    private static final Map<UniqueQName,Reference> all = new HashMap<>();

    protected Reference(final Model parent) {
      super(null, parent);
    }

    public static Reference parseElement(final UniqueQName name) {
      Reference type = all.get(name);
      if (type != null)
        return type;

      type = new Reference(null);
      type.setName(name);
      all.put(name, type);
      return type;
    }
  }
}