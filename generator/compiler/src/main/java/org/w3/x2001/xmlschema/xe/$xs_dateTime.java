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

import org.safris.commons.xml.binding.DateTime;
import org.safris.xsb.generator.compiler.runtime.MarshalException;
import org.safris.xsb.generator.compiler.runtime.ParseException;
import org.w3c.dom.Element;

/**
 * This final class represents the Java binding of the dateTime instance of time.
 *
 * @see <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">Definition</a>
 */
public abstract class $xs_dateTime extends $xs_anySimpleType {
  public $xs_dateTime(final $xs_dateTime binding) {
    super(binding);
  }

  public $xs_dateTime(final DateTime value) {
    super(value);
  }

  /**
   * Allocates a <code>Date</code> object and initializes it so that it
   * represents the time at which it was allocated. Milliseconds are
   * <b>NOT</b> significant figures and are not represented.
   *
   * @see java.lang.System#currentTimeMillis()
   */
  protected $xs_dateTime() {
    super();
  }

  @Override
  public DateTime text() {
    return (DateTime)super.text();
  }

  public void text(final DateTime text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(DateTime.parseDateTime(value));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $xs_dateTime clone() {
    return new $xs_dateTime(this) {
      @Override
      protected $xs_dateTime inherits() {
        return this;
      }
    };
  }
}