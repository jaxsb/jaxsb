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

package org.libx4j.xsb.compiler.schema.attribute;

import java.util.HashMap;
import java.util.Map;

public final class Block {
  private static final Map<String,Block> enums = new HashMap<String,Block>();

  public static final Block ALL = new Block("#all");
  public static final Block EXTENSION = new Block("extension");
  public static final Block RESTRICTION = new Block("restriction");
  public static final Block SUBSTITUTION = new Block("substitution");

  public static Block parseBlock(final String value) {
    return enums.get(value);
  }

  private final String value;

  private Block(final String value) {
    this.value = value;
    enums.put(value, this);
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Block))
      return false;

    return getValue().equals(((Block)obj).getValue());
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