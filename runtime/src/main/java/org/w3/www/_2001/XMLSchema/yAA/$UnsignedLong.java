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

package org.w3.www._2001.XMLSchema.yAA;

import java.math.BigInteger;

import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $UnsignedLong extends $NonNegativeInteger {
  private static final long serialVersionUID = 2576671958539402360L;

  public $UnsignedLong(final $UnsignedLong binding) {
    super(binding);
  }

  public $UnsignedLong(final BigInteger value) {
    super(value);
  }

  protected $UnsignedLong(final Number value) {
    super(value);
  }

  protected $UnsignedLong() {
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
  public $UnsignedLong clone() {
    return new $UnsignedLong(this) {
      private static final long serialVersionUID = -8017442916115215530L;

      @Override
      protected $UnsignedLong inherits() {
        return this;
      }
    };
  }
}