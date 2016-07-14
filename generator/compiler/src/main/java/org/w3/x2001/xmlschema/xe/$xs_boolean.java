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

import java.util.HashMap;
import java.util.Map;

import org.safris.xml.generator.compiler.runtime.MarshalException;
import org.safris.xml.generator.compiler.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $xs_boolean extends $xs_anySimpleType {
  private static final Map<Boolean,String[]> valueMap = new HashMap<Boolean,String[]>();

  public static final Boolean parseBoolean(final String s) {
    if (s == null)
      return false;

    if (s.length() == 1)
      return "1".equals(s);

    return Boolean.parseBoolean(s);
  }

  static {
    valueMap.put(true, new String[] {"true", "1"});
    valueMap.put(false, new String[] {"false", "0"});
  }

  public $xs_boolean(final $xs_boolean binding) {
    super(binding);
  }

  public $xs_boolean(final Boolean value) {
    super(value);
  }

  protected $xs_boolean() {
    super();
  }

  @Override
  public Boolean text() {
    return (Boolean)super.text();
  }

  public void text(final Boolean text) {
    super.text(text);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    super.text(Boolean.valueOf("true".equals(value) || "1".equals(value)));
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    if (super.text() == null)
      return "";

    if (_$$getPattern() == null)
      return String.valueOf(super.text());

    for (final String pattern : _$$getPattern()) {
      String[] ret = valueMap.get(super.text());
      for (int i = 0; i < ret.length; i++) {
        if (ret[i].matches(pattern))
          return ret[i];
      }
    }

    throw new MarshalException("No valid return type. Schema error!!!");
  }

  @Override
  public $xs_boolean clone() {
    return new $xs_boolean(this) {
      @Override
      protected $xs_boolean inherits() {
        return this;
      }
    };
  }
}