/* Copyright (c) 2006 lib4j
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

package org.safris.xsb.runtime;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;
import org.w3c.dom.Element;

public final class AttributeAudit<T extends $xs_anySimpleType> {
  private final $xs_anySimpleType parent;
  private final T _default;
  private final QName name;
  private final boolean qualified;
  private final boolean required;
  private T value = null;

  public AttributeAudit(final $xs_anySimpleType parent, final T _default, final QName name, final boolean qualified, final boolean required) {
    this.parent = parent;
    this._default = _default;
    this.name = name;
    this.qualified = qualified;
    this.required = required;
  }

  public AttributeAudit(final AttributeAudit<T> copy) {
    this.parent = copy.parent;
    this._default = copy._default;
    this.name = copy.name;
    this.qualified = copy.qualified;
    this.required = copy.required;
  }

  public T getDefault() {
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

  public boolean setAttribute(final T value) {
    if (parent.isNull())
      throw new BindingRuntimeException("NULL Object is immutable.");

    this.value = value;
    return true;
  }

  public T getAttribute() {
    return value != null ? value : getDefault();
  }

  public void marshal(final Element parent) throws MarshalException {
    final Object value = getAttribute();
    if (value == null || value.equals(getDefault()))
        return;

    if (value instanceof Collection) {
      String name = null;
      if (getName() != null)
        name = isQualified() ? Binding._$$getPrefix(parent, getName()) + ":" + getName().getLocalPart() : getName().getLocalPart();

      for (final Object object : (Collection<?>)value) {
        Binding binding = (Binding)object;
        if (name == null) {
          final QName actualName = Binding.name(binding);
          name = isQualified() ? Binding._$$getPrefix(parent, getName()) + ":" + getName().getLocalPart() : actualName.getLocalPart();
        }

        parent.setAttributeNodeNS(binding.marshalAttr(name, parent));
      }
    }
    else {
      QName name = getName();
      if (name == null)
        name = ((Binding)value).name();

      final String marshalName = isQualified() ? Binding._$$getPrefix(parent, name) + ":" + name.getLocalPart() :  name.getLocalPart();
      parent.setAttributeNodeNS(((Binding)value).marshalAttr(marshalName, parent));
    }
  }

  @Override
  public AttributeAudit<T> clone() {
    return new AttributeAudit<T>(this);
  }

  @Override
  public boolean equals(final Object obj) {
    return obj != null ? obj.equals(value) : value == null;
  }

  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }

  @Override
  public String toString() {
    return value != null ? value.toString() : super.toString();
  }
}