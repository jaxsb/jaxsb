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

package org.w3.www._2001.XMLSchema.xIEcGGcJdlCXaI_A;

import javax.xml.namespace.QName;

import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $qName extends $AnySimpleType {
  private static final long serialVersionUID = -6159227210752729335L;

  public $qName(final $qName binding) {
    super(binding);
  }

  public $qName(final QName value) {
    super(value);
  }

  protected $qName() {
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
  public $qName clone() {
    return new $qName(this) {
      private static final long serialVersionUID = 5679781638989841841L;

      @Override
      protected $qName inherits() {
        return this;
      }
    };
  }
}