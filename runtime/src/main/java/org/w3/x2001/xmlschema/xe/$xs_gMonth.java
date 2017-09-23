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

import org.lib4j.xml.binding.Month;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $xs_gMonth extends $xs_anySimpleType {
  private static final long serialVersionUID = -9051270336948297692L;

  public $xs_gMonth(final $xs_gMonth binding) {
    super(binding);
  }

  public $xs_gMonth(final Month value) {
    super(value);
  }

  protected $xs_gMonth() {
    super();
  }

  @Override
  public Month text() {
    return (Month)super.text();
  }

  public void text(final Month text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(Month.parseMonth(value));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $xs_gMonth clone() {
    return new $xs_gMonth(this) {
      private static final long serialVersionUID = 2153662309105407332L;

      @Override
      protected $xs_gMonth inherits() {
        return this;
      }
    };
  }
}