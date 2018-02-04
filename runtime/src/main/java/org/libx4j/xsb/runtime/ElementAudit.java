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

package org.libx4j.xsb.runtime;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;

@SuppressWarnings("unchecked")
public final class ElementAudit<B extends Binding> implements Serializable {
  private static void marshalNil(final Element element, final Element parent) {
    // NOTE: This makes the assumption that the xmlns:xsi will be present if
    // NOTE: xsi:nil is present, saving us a hasAttributeNS() call.
    if (!element.hasAttributeNS(Binding.XSI_NIL.getNamespaceURI(), Binding.XSI_NIL.getLocalPart())) {
      element.setAttributeNS(Binding.XSI_NIL.getNamespaceURI(), Binding.XSI_NIL.getPrefix() + ":" + Binding.XSI_NIL.getLocalPart(), "true");
      if (!parent.getOwnerDocument().getDocumentElement().hasAttributeNS(Binding.XMLNS.getNamespaceURI(), Binding.XSI_NIL.getPrefix()))
        parent.getOwnerDocument().getDocumentElement().setAttributeNS(Binding.XMLNS.getNamespaceURI(), Binding.XMLNS.getLocalPart() + ":" + Binding.XSI_NIL.getPrefix(), Binding.XSI_NIL.getNamespaceURI());
    }
  }

  private static final long serialVersionUID = -8175752291591734863L;

  private final Class<B> type;
  private final Binding parent;
  private final B _default;
  private final BindingProxy<B> defaultProxy;
  private final QName name;
  private final QName typeName;
  private final boolean qualified;
  private final boolean nillable;
  private final int minOccurs;
  private final int maxOccurs;
  private ElementSuperList.ElementSubList<B> elements;

  public ElementAudit(final Class<? extends Binding> type, final Binding parent, final B _default, QName name, final QName typeName, boolean qualified, final boolean nillable, int minOccurs, final int maxOccurs) {
    this.type = (Class<B>)type;
    this.parent = parent;
    if (_default != null) {
      this._default = _default;
      this.defaultProxy = new BindingProxy<B>(null);
      parent._$$addElement(this, (B)this.defaultProxy);
    }
    else {
      this._default = null;
      this.defaultProxy = null;
    }

    this.name = name;
    this.typeName = typeName;
    this.qualified = qualified;
    this.nillable = nillable;
    this.minOccurs = minOccurs;
    this.maxOccurs = maxOccurs;
    parent._$$registerElementAudit(this);
  }

  public ElementAudit(final Binding parent, final ElementAudit<B> copy) {
    this.parent = parent;
    this.type = copy.type;
    this._default = copy._default;
    this.defaultProxy = copy.defaultProxy;
    this.name = copy.name;
    this.typeName = copy.typeName;
    this.qualified = copy.qualified;
    this.nillable = copy.nillable;
    this.minOccurs = copy.minOccurs;
    this.maxOccurs = copy.maxOccurs;
  }

  public Class<B> getType() {
    return this.type;
  }

  public Binding getParent() {
    return this.parent;
  }

  public boolean isQualified() {
    return qualified;
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

  public BindingList<B> getElements() {
    return elements;
  }

  public B getElement() {
    return elements == null || elements.size() == 0 ? null : elements.get(0);
  }

  public int size() {
    return elements == null ? 0 : elements.size();
  }

  protected int indexOf(final Binding element) {
    return elements.indexOf(element);
  }

  protected void setElements(final ElementSuperList.ElementSubList<B> elements) {
    this.elements = elements;
  }

  public boolean addElement(final B element) {
    if (elements == null)
      elements = parent.getCreateElementDirectory().newPartition(this);

    if (maxOccurs > 1 || elements.size() == 0)
      elements.add(element);
    else
      elements.set(0, element);

    return true;
  }

  protected void marshal(final Element parent, final B element) throws MarshalException {
    if (elements == null)
      return;

    QName name = getName();
    if (name == null)
      name = element.name();

    QName type = getTypeName();
    if (type == null)
      type = element.type();

    final Element node = element.marshal(parent, name, type);
    if (!element._$$hasElements() && isNillable())
      marshalNil(node, parent);

    element._$$marshalElements(node);
    if (!isQualified())
      node.setPrefix(null);

    parent.appendChild(node);
  }

  public ElementAudit<B> clone(final ElementSuperList elementList) {
    final ElementAudit<B> clone = new ElementAudit<B>(elementList.getParent(), this);
    clone.elements = elementList.getPartition(type);
    clone.elements.setAudit(this);
    return clone;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ElementAudit))
      return false;

    return elements != null ? elements.equals(((ElementAudit<?>)obj).elements) : ((ElementAudit<?>)obj).elements == null;
  }

  @Override
  public int hashCode() {
    return elements != null ? elements.hashCode() : 0;
  }

  @Override
  public String toString() {
    return elements != null ? elements.toString() : super.toString();
  }
}