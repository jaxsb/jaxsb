/* Copyright (c) 2006 JAX-SB
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

package org.jaxsb.runtime;

import java.util.Objects;

import javax.xml.namespace.QName;

import org.jaxsb.runtime.Binding.PrefixToNamespace;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.w3c.dom.Element;

@SuppressWarnings("rawtypes")
public class AttributeAudit<B extends $AnySimpleType> extends AbstractAttributeAudit {
  private final $AnyType<?> owner;
  private final B _default;
  private final QName name;
  private final boolean qualified;
  private final boolean required;
  private B value;

  public AttributeAudit(final $AnyType<?> owner, final B _default, final QName name, final boolean qualified, final boolean required) {
    this.owner = owner;
    this._default = _default;
    if (_default != null)
      Binding.markDefault(_default);

    this.name = name;
    this.qualified = qualified;
    this.required = required;
  }

  private AttributeAudit(final $AnyType<?> owner, final AttributeAudit<B> copy) {
    this.owner = owner;
    this._default = copy._default == null ? null : (B)copy._default.clone();
    this.name = copy.name;
    this.qualified = copy.qualified;
    this.required = copy.required;
    this.value = copy.value == null ? null : (B)copy.value.clone();
  }

  public B getDefault() {
    return _default;
  }

  public QName getName() {
    return name;
  }

  public boolean isQualified() {
    return qualified;
  }

  public boolean isRequired() {
    return required;
  }

  public boolean setAttribute(final B value) {
    this.value = value;
    owner.setDirty();
    return true;
  }

  @Override
  public B getAttribute() {
    return value != null ? value : getDefault();
  }

  public void marshal(final Element parent) throws MarshalException {
    if (value == null || value.equals(getDefault()))
      return;

    QName name = getName();
    if (name == null)
      name = ((Binding)value).name(); // FIXME: Why is this being done?

    final String marshalName = isQualified() ? Binding._$$getPrefix(parent, name) + ":" + name.getLocalPart() :  name.getLocalPart();
    parent.setAttributeNodeNS(((Binding)value).marshalAttr(marshalName, parent));
  }

  @Override
  public AttributeAudit<B> clone(final $AnyType<?> owner) {
    return new AttributeAudit<>(owner, this);
  }

  @Override
  public boolean equals(final Object obj) {
    return obj == this || (obj instanceof AttributeAudit) ? Objects.equals(value, ((AttributeAudit<?>)obj).value) : Objects.equals(obj, value);
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    if (value != null)
      hashCode = 31 * hashCode + value.hashCode();

    return hashCode;
  }

  @Override
  public String toString() {
    return value != null ? value.toString() : super.toString();
  }

  @Override
  void toString(final StringBuilder str, final PrefixToNamespace prefixToNamespace) {
    if (value == null)
      return;

    QName name = getName();
    if (name == null)
      name = ((Binding)value).name(); // FIXME: Why is this being done?

    AbstractAttributeAudit.toString(str, prefixToNamespace, name, isQualified(), value.text());
  }
}