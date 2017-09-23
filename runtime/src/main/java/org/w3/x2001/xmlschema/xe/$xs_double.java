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

package org.w3.x2001.xmlschema.xe;

import org.lib4j.lang.Numbers;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $xs_double extends $xs_anySimpleType {
  private static final long serialVersionUID = 5696304973581941043L;

  public $xs_double(final $xs_double binding) {
    super(binding);
  }

  public $xs_double(final Double value) {
    super(value);
  }

  protected $xs_double() {
    super();
  }

  @Override
  public Double text() {
    return (Double)super.text();
  }

  public void text(final Double text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(Double.parseDouble(value));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? Numbers.roundInsignificant(super.text().toString()) : "";
  }

  @Override
  public $xs_double clone() {
    return new $xs_double(this) {
      private static final long serialVersionUID = -186742707176755984L;

      @Override
      protected $xs_double inherits() {
        return this;
      }
    };
  }
}