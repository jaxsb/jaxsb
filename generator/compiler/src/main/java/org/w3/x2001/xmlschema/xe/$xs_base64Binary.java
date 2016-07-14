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

import org.safris.commons.xml.binding.Base64Binary;
import org.safris.xml.generator.compiler.runtime.MarshalException;
import org.safris.xml.generator.compiler.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $xs_base64Binary extends $xs_anySimpleType {
  public $xs_base64Binary(final $xs_base64Binary binding) {
    super(binding);
  }

  public $xs_base64Binary(final Base64Binary value) {
    super(value);
  }

  protected $xs_base64Binary() {
    super();
  }

  @Override
  public Base64Binary text() {
    return (Base64Binary)super.text();
  }

  public void text(final Base64Binary text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(Base64Binary.parseBase64Binary(String.valueOf(value)));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $xs_base64Binary clone() {
    return new $xs_base64Binary(this) {
      @Override
      protected $xs_base64Binary inherits() {
        return this;
      }
    };
  }
}