/* Copyright (c) 2008 Seva Safris
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
import org.safris.xsb.compiler.processor.model.ReferableModel;
import org.safris.xsb.compiler.processor.model.RestrictableModel;
import org.safris.xsb.compiler.schema.attribute.Form;
import org.safris.xsb.compiler.schema.attribute.Use;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AttributeModel extends SimpleTypeModel<SimpleTypeModel<?>> implements Formable<Model>, ReferableModel<AttributeModel>, RestrictableModel<AttributeModel> {
  private QName _default = null;
  private QName fixed = null;
  private Form form = null;
  private Use use = Use.OPTIONAL;
  private Form formDefault = null;
  private AttributeModel ref = null;
  private AliasModel restrictionOwner = null;
  private AttributeModel restriction = null;

  public AttributeModel(final Node node, final Model parent) {
    super(node, parent);
    if (node == null)
      return;

    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      if ("default".equals(attribute.getLocalName()))
        _default = parseQNameValue(attribute.getNodeValue(), node);
      else if ("fixed".equals(attribute.getLocalName()))
        fixed = parseQNameValue(attribute.getNodeValue(), node);
      else if ("form".equals(attribute.getLocalName()))
        form = Form.parseForm(attribute.getNodeValue());
      else if ("ref".equals(attribute.getLocalName()))
        setRef(AttributeModel.Reference.parseAttribute(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node))));
      else if ("type".equals(attribute.getLocalName()))
        setSuperType(SimpleTypeModel.Reference.parseSimpleType(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node))));
      else if ("use".equals(attribute.getLocalName()))
        use = Use.parseUse(attribute.getNodeValue());
    }
  }

  @Override
  public final void setRestriction(final AttributeModel restriction) {
    this.restriction = restriction;
  }

  @Override
  public final AttributeModel getRestriction() {
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
  public final AttributeModel getRef() {
    return ref;
  }

  @Override
  public final void setRef(final AttributeModel ref) {
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

  public final QName getDefault() {
    return _default;
  }

  public final QName getFixed() {
    return fixed;
  }

  public final Form getForm() {
    return form;
  }

  public final Use getUse() {
    return use;
  }

  public final void setFormDefault(final Form formDefault) {
    this.formDefault = formDefault;
  }

  @Override
  public final Form getFormDefault() {
    return formDefault;
  }

  @Override
  public boolean equals(final Object obj) {
    final boolean equals = super.equals(obj);
    if (!equals)
      return false;

    final AttributeModel that = (AttributeModel)obj;
    return (getRef() == null && that.getRef() == null) || (getRef() != null && getRef().equals(that.getRef()));
  }

  @Override
  public int hashCode() {
    return (getClass().getName() + toString()).hashCode();
  }

  @Override
  public String toString() {
    if (getName() == null) {
      if (getRef() == null)
        return null;

      return getRef().toString();
    }

    return getName().toString();
  }

  public static final class Reference extends AttributeModel implements Referenceable {
    private static final Map<UniqueQName,Reference> all = new HashMap<UniqueQName,Reference>();

    protected Reference(final Model parent) {
      super(null, parent);
    }

    public static Reference parseAttribute(final UniqueQName name) {
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