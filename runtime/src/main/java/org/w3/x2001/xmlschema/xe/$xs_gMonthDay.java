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

import org.lib4j.xml.binding.MonthDay;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $xs_gMonthDay extends $xs_anySimpleType {
  private static final long serialVersionUID = 2892185404567465759L;

  public $xs_gMonthDay(final $xs_gMonthDay binding) {
    super(binding);
  }

  public $xs_gMonthDay(final MonthDay value) {
    super(value);
  }

  protected $xs_gMonthDay() {
    super();
  }

  @Override
  public MonthDay text() {
    return (MonthDay)super.text();
  }

  public void text(final MonthDay text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(MonthDay.parseMonthDay(value));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $xs_gMonthDay clone() {
    return new $xs_gMonthDay(this) {
      private static final long serialVersionUID = -4608081862190441411L;

      @Override
      protected $xs_gMonthDay inherits() {
        return this;
      }
    };
  }
}