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

import org.lib4j.util.IdentityArrayList;
import org.lib4j.util.ObservableList;
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

  public final class SpecificElementList extends ObservableList<B> implements BindingList<B>, Cloneable {
    private static final long serialVersionUID = -6390976298261014375L;

    public SpecificElementList() {
      super(new IdentityArrayList<B>());
    }

    private SpecificElementList(final SpecificElementList copy) {
      super(new IdentityArrayList<B>(copy));
    }

    protected void addUnsafe(final int index, final B e) {
      super.source.add(index, e);
    }

    protected void addUnsafe(final B e) {
      super.source.add(e);
    }

    protected B setUnsafe(int index, final B e) {
      return (B)super.source.set(index, e);
    }

    protected B removeUnsafe(int index) {
      return (B)super.source.remove(index);
    }

    @Override
    protected void beforeRemove(final int index) {
      final B element = get(index);
      parent._$$removeElement(element);
    }

    @Override
    protected void beforeSet(final int index, final B newElement) {
      final B element = get(index);
      parent._$$replaceElement(element, ElementAudit.this, newElement);
    }

    @Override
    protected void beforeAdd(final int index, final B e) {
      if (index == size()) {
        parent._$$addElementNoAudit(ElementAudit.this, e);
      }
      else {
        final B element = get(index);
        parent._$$addElementBefore(element, ElementAudit.this, e);
      }
    }

    @Override
    public SpecificElementList clone() {
      return new SpecificElementList(this);
    }

    @Override
    public Binding getParent() {
      return parent;
    }
  }

  private final Class<? extends Binding> type;
  private final Binding parent;
  private final B _default;
  private final BindingProxy<B> defaultProxy;
  private final QName name;
  private final QName typeName;
  private final boolean qualified;
  private final boolean nillable;
  private final int minOccurs;
  private final int maxOccurs;
  private SpecificElementList value;

  public ElementAudit(final Class<? extends Binding> type, final Binding parent, final B _default, QName name, final QName typeName, boolean qualified, final boolean nillable, int minOccurs, final int maxOccurs) {
    this.type = type;
    this.parent = parent;
    if (_default != null) {
      parent._$$addElementNoAudit(this, (B)(this.defaultProxy = new BindingProxy<B>(this._default = _default)));
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
    this.value = copy.value.clone();
  }

  public Class<? extends Binding> getType() {
    return this.type;
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

  public boolean addElement(final B element) {
    return addElement(element, false);
  }

  public boolean addElementUnsafe(final B element) {
    return addElement(element, true);
  }

  private boolean addElement(final B element, final boolean unsafe) {
    if (parent.isNull())
      throw new BindingRuntimeException("NULL Object is immutable.");

    if (this.value == null)
      this.value = new SpecificElementList();

    if (defaultProxy != null && defaultProxy.getBinding() == _default) {
      defaultProxy.setBinding(element);
      return false;
    }

    if (unsafe)
      this.value.addUnsafe(element);
    else
      this.value.add(element);

    return true;
  }

  public BindingList<B> getElements() {
    return value;
  }

  protected int indexOf(final Binding element) {
    return value.indexOf(element);
  }

  protected boolean removeUnsafe(final Binding element) {
    final int index = this.value.indexOf(element);
    return index > -1 && this.value.removeUnsafe(index) != null;
  }

  protected B removeUnsafe(final int index) {
    return this.value.removeUnsafe(index);
  }

  protected B setUnsafe(final int index, final B element) {
    return value.setUnsafe(index, element);
  }

  protected void marshal(final Element parent, final B element) throws MarshalException {
    if (value == null)
      return;

    QName name = getName();
    if (name == null)
      name = element.name();

    QName typeName = getTypeName();
    if (typeName == null)
      typeName = element.typeName();

    final Element node = element.marshal(parent, name, typeName);
    if (!element._$$hasElements() && isNillable())
      marshalNil(node, parent);

    element._$$marshalElements(node);
    if (!isQualified())
      node.setPrefix(null);

    parent.appendChild(node);
  }

  public ElementAudit<B> clone(final Binding parent) {
    return new ElementAudit<B>(parent, this);
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