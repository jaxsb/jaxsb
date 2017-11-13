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

import org.lib4j.xml.binding.Day;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $GDay extends $AnySimpleType {
  private static final long serialVersionUID = -7306738302337538690L;

  public $GDay(final $GDay binding) {
    super(binding);
  }

  public $GDay(final Day value) {
    super(value);
  }

  protected $GDay() {
    super();
  }

  @Override
  public Day text() {
    return (Day)super.text();
  }

  public void text(final Day text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(Day.parse(value));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $GDay clone() {
    return new $GDay(this) {
      private static final long serialVersionUID = 6038331742814204927L;

      @Override
      protected $GDay inherits() {
        return this;
      }
    };
  }
}