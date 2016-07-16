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

package org.w3.x2001.xmlschema.xe;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.safris.commons.xml.validator.ValidationException;
import org.safris.xsb.generator.compiler.runtime.Binding;
import org.safris.xsb.generator.compiler.runtime.BindingRuntimeException;
import org.safris.xsb.generator.compiler.runtime.MarshalException;
import org.safris.xsb.generator.compiler.runtime.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public abstract class $xs_anySimpleType extends Binding {
  private Object text = null;

  public $xs_anySimpleType(final $xs_anySimpleType copy) {
    super(copy);
    this.text = copy.text;
  }

  public $xs_anySimpleType(final Object text) {
    super();
    if (text instanceof $xs_anySimpleType && (($xs_anySimpleType)text)._$$hasElements())
      merge(($xs_anySimpleType)text);
    else
      this.text = text;
  }

  protected $xs_anySimpleType() {
    super();
  }

  public void text(final Object text) {
    if (isNull())
      throw new BindingRuntimeException("NULL Object is immutable.");

    this.text = text;
  }

  @Override
  public Object text() {
    return text;
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    this.text = value;
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    if (text() == null)
      return "";

    if (text() instanceof Collection)
      throw new Error("Why is this a Collection? The collection logic should be in the appropriate subclass.");

    return text().toString();
  }

  private transient Element parent = null;

  @Override
  protected Element marshal(final Element parent, final QName name, final QName typeName) throws MarshalException {
    this.parent = parent;
    final Element element = super.marshal(parent, name, typeName);
    if (text != null)
      element.appendChild(parent.getOwnerDocument().createTextNode(String.valueOf(_$$encode(parent))));

    return element;
  }

  @Override
  protected Attr marshalAttr(final String name, final Element parent) throws MarshalException {
    this.parent = parent;
    final Attr attr = parent.getOwnerDocument().createAttribute(name);
    attr.setNodeValue(String.valueOf(_$$encode(parent)));
    return attr;
  }

  @Override
  protected void parseText(final Text text) throws ParseException, ValidationException {
    // Ignore all attributes that have a xsi prefix because these are
    // controlled implicitly by the framework
    if (XSI_NIL.getPrefix().equals(text.getPrefix()))
      return;

    String value = "";
    if (text.getNodeValue() != null)
      value += text.getNodeValue().trim();

    if (value.length() != 0)
      _$$decode((Element)text.getParentNode(), value);
  }

  @Override
  public QName name() {
    return name(inherits());
  }

  @Override
  protected QName typeName() {
    return null;
  }

  @Override
  public $xs_anySimpleType clone() {
    return new $xs_anySimpleType(this) {
      @Override
      protected $xs_anySimpleType inherits() {
        return this;
      }
    };
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof $xs_anySimpleType))
      return false;

    final $xs_anySimpleType that = ($xs_anySimpleType)obj;
    try {
      final String thisEncoded = _$$encode(parent);
      final String thatEncoded = that._$$encode(parent);
      return thisEncoded != null ? thisEncoded.equals(thatEncoded) : thatEncoded == null;
    }
    catch (final MarshalException e) {
      return false;
    }
  }

  @Override
  public int hashCode() {
    final String value;
    try {
      value = _$$encode(parent);
    }
    catch (final MarshalException e) {
      return super.hashCode();
    }

    if (value == null)
      return super.hashCode();

    return value.hashCode();
  }

  @Override
  public String toString() {
    try {
      return String.valueOf(_$$encode(parent));
    }
    catch (final MarshalException e) {
      return super.toString();
    }
  }
}