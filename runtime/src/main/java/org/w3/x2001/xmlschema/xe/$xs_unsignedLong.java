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

import java.math.BigInteger;

import org.safris.xsb.runtime.MarshalException;
import org.safris.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $xs_unsignedLong extends $xs_nonNegativeInteger {
  public $xs_unsignedLong(final $xs_unsignedLong binding) {
    super(binding);
  }

  public $xs_unsignedLong(final BigInteger value) {
    super(value);
  }

  protected $xs_unsignedLong(final Number value) {
    super(value);
  }

  protected $xs_unsignedLong() {
    super();
  }

  @Override
  public Number text() {
    return super.text();
  }

  @Override
  public void text(final BigInteger text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(new BigInteger(value));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $xs_unsignedLong clone() {
    return new $xs_unsignedLong(this) {
      @Override
      protected $xs_unsignedLong inherits() {
        return this;
      }
    };
  }
}