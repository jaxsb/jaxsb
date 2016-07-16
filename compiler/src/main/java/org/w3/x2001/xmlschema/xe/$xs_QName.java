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

import javax.xml.namespace.QName;

import org.safris.xsb.generator.compiler.runtime.MarshalException;
import org.safris.xsb.generator.compiler.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $xs_QName extends $xs_anySimpleType {
  public $xs_QName(final $xs_QName binding) {
    super(binding);
  }

  public $xs_QName(final QName value) {
    super(value);
  }

  protected $xs_QName() {
    super();
  }

  @Override
  public QName text() {
    return (QName)super.text();
  }

  public void text(final QName text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element element, final String value) throws ParseException {
    final QName temp = stringToQName(value);
    super.text(temp);
    if (element != null)
      super.text(new javax.xml.namespace.QName(element.getOwnerDocument().getDocumentElement().lookupNamespaceURI(temp.getPrefix()), temp.getLocalPart()));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    final QName value = (QName)super.text();
    if (value == null)
      return "";

    if (parent != null && value.getNamespaceURI() != null)
      return _$$getPrefix(parent, value) + ":" + value.getLocalPart();

    return value.getLocalPart();
  }

  @Override
  public $xs_QName clone() {
    return new $xs_QName(this) {
      @Override
      protected $xs_QName inherits() {
        return this;
      }
    };
  }
}