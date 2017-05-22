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

package org.safris.xsb.compiler.processor.model.element;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.safris.xsb.compiler.lang.UniqueQName;
import org.safris.xsb.compiler.processor.Formable;
import org.safris.xsb.compiler.processor.Referenceable;
import org.safris.xsb.compiler.processor.model.AliasModel;
import org.safris.xsb.compiler.processor.model.Model;
import org.safris.xsb.compiler.processor.model.MultiplicableModel;
import org.safris.xsb.compiler.processor.model.ReferableModel;
import org.safris.xsb.compiler.processor.model.RestrictableModel;
import org.safris.xsb.compiler.schema.attribute.Form;
import org.safris.xsb.compiler.schema.attribute.Occurs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ElementModel extends ComplexTypeModel<SimpleTypeModel<?>> implements Formable<Model>, MultiplicableModel, ReferableModel<ElementModel>, RestrictableModel<ElementModel> {
  private Boolean _abstract = false;
  private QName _default = null;
  private QName fixed = null;
  private Occurs maxOccurs = Occurs.parseOccurs("1");
  private Occurs minOccurs = Occurs.parseOccurs("1");
  private Boolean nillable = false;
  private ElementModel ref = null;
  private UniqueQName substitutionGroup = null;
  private Form formDefault = null;
  private AliasModel restrictionOwner = null;
  private ElementModel restriction = null;

  protected ElementModel(final Node node, final Model parent) {
    super(node, parent);
    if (node == null)
      return;

    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
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
    return formDefault;
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

    return getRef() == null ? getName().equals(((ElementModel)obj).getName()) : (((ElementModel)obj).getRef() != null ? getRef().getName().equals(((ElementModel)obj).getRef().getName()) : false);
  }

  @Override
  public boolean isQualified(final boolean nested) {
    return !nested && (getRef() != null || getSchema().getElementFormDefault() == Form.QUALIFIED);
  }

  @Override
  public int hashCode() {
    if (getRef() != null && getRef().getName() != null)
      return ("ref" + getRef().getName().toString()).hashCode();

    if (getName() != null)
      return ("elem" + getName().toString()).hashCode();

    return super.hashCode();
  }

  @Override
  public String toString() {
    return UniqueQName.XS.getNamespaceURI() + " " + getName();
  }

  public static final class Reference extends ElementModel implements Referenceable {
    private static final Map<UniqueQName,Reference> all = new HashMap<UniqueQName,Reference>();

    protected Reference(final Model parent) {
      super(null, parent);
    }

    public static Reference parseElement(final UniqueQName name) {
      Reference type = all.get(name);
      if (type != null)
        return type;

      type = new Reference(null);
      type.setName(name);
      Reference.all.put(name, type);
      return type;
    }
  }
}