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

package org.w3.www._2001.XMLSchema.hF8l3SXA;

import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $AnyURI extends $AnySimpleType {
  private static final long serialVersionUID = 7515449882634738035L;

  public $AnyURI(final $AnyURI binding) {
    super(binding);
  }

  public $AnyURI(final String value) {
    super(value);
  }

  protected $AnyURI() {
    super();
  }

  @Override
  public String text() {
    return (String)super.text();
  }

  public void text(final String text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(value);
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $AnyURI clone() {
    return new $AnyURI(this) {
      private static final long serialVersionUID = 896348518452291347L;

      @Override
      protected $AnyURI inherits() {
        return this;
      }
    };
  }
}