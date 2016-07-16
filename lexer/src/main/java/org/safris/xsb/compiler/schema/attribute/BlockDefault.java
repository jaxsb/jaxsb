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

package org.safris.xsb.lexer.schema.attribute;

import java.util.HashMap;
import java.util.Map;

public final class BlockDefault {
  private static final Map<String,BlockDefault> enums = new HashMap<String,BlockDefault>();

  public static final BlockDefault ALL = new BlockDefault("#all");
  public static final BlockDefault EXTENSION = new BlockDefault("extension");
  public static final BlockDefault RESTRICTION = new BlockDefault("restriction");
  public static final BlockDefault SUBSTITUTION = new BlockDefault("substitution");

  public static BlockDefault parseBlockDefault(final String value) {
    return enums.get(value);
  }

  private final String value;

  private BlockDefault(final String value) {
    this.value = value;
    enums.put(value, this);
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof BlockDefault))
      return false;

    return getValue().equals(((BlockDefault)obj).getValue());
  }

  @Override
  public int hashCode() {
    return (getClass().getName() + toString()).hashCode();
  }

  @Override
  public String toString() {
    return getValue();
  }
}