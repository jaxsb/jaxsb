/* Copyright (c) 2019 JAX-SB
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

import java.util.ArrayList;
import java.util.Objects;

import javax.xml.namespace.QName;

import org.jaxsb.runtime.Binding.PrefixToNamespace;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.w3c.dom.Element;

@SuppressWarnings("rawtypes")
public final class AnyAttributeAudit<T extends $AnySimpleType> extends AbstractAttributeAudit {
  private final boolean qualified;
  private final boolean required;
  private ArrayList<T> value;

  public AnyAttributeAudit(final boolean qualified, final boolean required) {
    this.qualified = qualified;
    this.required = required;
  }

  @SuppressWarnings("unchecked")
  private AnyAttributeAudit(final AnyAttributeAudit<T> copy) {
    this.qualified = copy.qualified;
    this.required = copy.required;
    this.value = copy.value == null ? null : (ArrayList<T>)copy.value.clone();
  }

  public boolean isQualified() {
    return qualified;
  }

  public boolean isRequired() {
    return required;
  }

  public boolean setAttribute(final ArrayList<T> value) {
    this.value = value;
    return true;
  }

  @Override
  public ArrayList<T> getAttribute() {
    return value;
  }

  public void marshal(final Element parent) throws MarshalException {
    if (value == null)
      return;

    for (int i = 0, i$ = value.size(); i < i$; ++i) { // [RA]
      final Binding binding = value.get(i);
      final QName qName = Binding.name(binding);
      final String name;
      final String prefix = qName.getPrefix();
      if (prefix.length() > 0) {
        name = prefix + ":" + qName.getLocalPart();
        parent.setAttributeNS(AbstractBinding.XMLNS.getNamespaceURI(), AbstractBinding.XMLNS.getLocalPart() + ":" + prefix, qName.getNamespaceURI());
      }
      else {
        name = qName.getLocalPart();
      }

      parent.setAttributeNodeNS(binding.marshalAttr(name, parent));
    }
  }

  @Override
  public AnyAttributeAudit<T> clone(final $AnyType<?> owner) {
    return new AnyAttributeAudit<>(this);
  }

  @Override
  public boolean equals(final Object obj) {
    return obj == this || (obj instanceof AnyAttributeAudit) ? Objects.equals(value, ((AnyAttributeAudit<?>)obj).value) : Objects.equals(obj, value);
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

    for (int i = 0, i$ = value.size(); i < i$; ++i) { // [RA]
      final Binding binding = value.get(i);
      final QName qName = Binding.name(binding);
      AbstractAttributeAudit.toString(str, prefixToNamespace, qName, isQualified(), binding.text().toString());
    }
  }
}