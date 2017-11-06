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

import org.lib4j.lang.Numbers;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $Float extends $AnySimpleType {
  private static final long serialVersionUID = 8396002187294788098L;

  public $Float(final $Float binding) {
    super(binding);
  }

  public $Float(final Float value) {
    super(value);
  }

  protected $Float() {
    super();
  }

  @Override
  public Float text() {
    return (Float)super.text();
  }

  public void text(final Float text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(Float.parseFloat(value));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? Numbers.roundInsignificant(super.text().toString()) : "";
  }

  @Override
  public $Float clone() {
    return new $Float(this) {
      private static final long serialVersionUID = -4803378714254264243L;

      @Override
      protected $Float inherits() {
        return this;
      }
    };
  }
}