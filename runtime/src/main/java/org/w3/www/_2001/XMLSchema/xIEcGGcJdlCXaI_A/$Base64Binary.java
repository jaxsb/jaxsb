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

import org.lib4j.xml.binding.Base64Binary;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $Base64Binary extends $AnySimpleType {
  private static final long serialVersionUID = -5862425707789169319L;

  public $Base64Binary(final $Base64Binary binding) {
    super(binding);
  }

  public $Base64Binary(final Base64Binary value) {
    super(value);
  }

  protected $Base64Binary() {
    super();
  }

  @Override
  public Base64Binary text() {
    return (Base64Binary)super.text();
  }

  public void text(final Base64Binary text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(Base64Binary.parse(String.valueOf(value)));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $Base64Binary clone() {
    return new $Base64Binary(this) {
      private static final long serialVersionUID = 130881242257625800L;

      @Override
      protected $Base64Binary inherits() {
        return this;
      }
    };
  }
}