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

import java.util.HashMap;
import java.util.Map;

import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $Boolean extends $AnySimpleType {
  private static final long serialVersionUID = 5805207783730082952L;
  private static final Map<Boolean,String[]> valueMap = new HashMap<>();

  public static final Boolean parse(final String s) {
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

  public $Boolean(final $Boolean binding) {
    super(binding);
  }

  public $Boolean(final Boolean value) {
    super(value);
  }

  protected $Boolean() {
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
  public $Boolean clone() {
    return ($Boolean)super.clone();
  }
}