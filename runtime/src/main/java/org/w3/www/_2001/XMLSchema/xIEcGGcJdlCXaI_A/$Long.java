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

import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $Long extends $Integer {
  private static final long serialVersionUID = 3110685220531748900L;

  public $Long(final $Long binding) {
    super(binding);
  }

  public $Long(final Long value) {
    super(value);
  }

  protected $Long(final Number value) {
    super(value);
  }

  protected $Long() {
    super();
  }

  @Override
  public Number text() {
    return super.text();
  }

  public void text(final Long text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(Long.parseLong(String.valueOf(value)));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $Long clone() {
    return new $Long(this) {
      private static final long serialVersionUID = -6394607848954244651L;

      @Override
      protected $Long inherits() {
        return this;
      }
    };
  }
}