/* Copyright (c) 2006 Seva Safris
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

import javax.xml.namespace.QName;

import org.w3c.dom.Element;

public final class ElementAudit<T extends Binding> {
  private final Binding parent;
  private final BindingList<T> _default;
  private final QName name;
  private final QName typeName;
  private final boolean qualified;
  private final boolean nillable;
  private final int minOccurs;
  private final int maxOccurs;
  private SpecificElementList<T> value = null;

  public ElementAudit(final Binding parent, final T _default, QName name, final QName typeName, boolean qualified, final boolean nillable, int minOccurs, final int maxOccurs) {
    this.parent = parent;
    this._default = _default != null ? SpecificElementList.singleton(this, _default) : null;
    this.name = name;
    this.typeName = typeName;
    this.qualified = qualified;
    this.nillable = nillable;
    this.minOccurs = minOccurs;
    this.maxOccurs = maxOccurs;
  }

  public ElementAudit(final Binding parent, final ElementAudit<T> copy) {
    this.parent = parent;
    this._default = copy._default;
    this.name = copy.name;
    this.typeName = copy.typeName;
    this.qualified = copy.qualified;
    this.nillable = copy.nillable;
    this.minOccurs = copy.minOccurs;
    this.maxOccurs = copy.maxOccurs;
    this.value = copy.value.clone(this);
  }

  protected Binding getParent() {
    return parent;
  }

  public boolean isQualified() {
    return qualified;
  }

  public BindingList<T> getDefault() {
    return _default;
  }

  public QName getName() {
    return name;
  }

  public QName getTypeName() {
    return typeName;
  }

  public boolean isNillable() {
    return nillable;
  }

  public int getMinOccurs() {
    return minOccurs;
  }

  public int getMaxOccurs() {
    return maxOccurs;
  }

  public boolean addElement(final T element) {
    if (parent.isNull())
      throw new BindingRuntimeException("NULL Object is immutable.");

    if (this.value == null)
      this.value = new SpecificElementList<T>(this, 2);

    return this.value.add(element, false);
  }

  public BindingList<T> getElements() {
    return value != null ? value : getDefault();
  }

  protected void reset() {
    value = null;
  }

  private static void marshalNil(final Element element, final Element parent) {
    // NOTE: This makes the assumption that the xmlns:xsi will be present if
    // NOTE: xsi:nil is present, saving us a hasAttributeNS() call.
    if (!element.hasAttributeNS(Binding.XSI_NIL.getNamespaceURI(), Binding.XSI_NIL.getLocalPart())) {
      element.setAttributeNS(Binding.XSI_NIL.getNamespaceURI(), Binding.XSI_NIL.getPrefix() + ":" + Binding.XSI_NIL.getLocalPart(), "true");
      if (!parent.getOwnerDocument().getDocumentElement().hasAttributeNS(Binding.XMLNS.getNamespaceURI(), Binding.XSI_NIL.getPrefix()))
        parent.getOwnerDocument().getDocumentElement().setAttributeNS(Binding.XMLNS.getNamespaceURI(), Binding.XMLNS.getLocalPart() + ":" + Binding.XSI_NIL.getPrefix(), Binding.XSI_NIL.getNamespaceURI());
    }
  }

  protected void marshal(final Element parent, final T element) throws MarshalException {
    if (value == null)
      return;

    QName name = getName();
    if (name == null)
      name = element.name();

    final Element node = element.marshal(parent, name, getTypeName());
    if (!element._$$hasElements() && isNillable())
      marshalNil(node, parent);

    element._$$marshalElements(node);
    if (!isQualified())
      node.setPrefix(null);

    parent.appendChild(node);
  }

  public ElementAudit<T> clone(final Binding parent) {
    return new ElementAudit<T>(parent, this);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ElementAudit))
      return false;

    return value != null ? value.equals(((ElementAudit<?>)obj).value) : ((ElementAudit<?>)obj).value == null;
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