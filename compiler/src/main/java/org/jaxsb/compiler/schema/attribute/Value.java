/* Copyright (c) 2006 JAX-SB
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

package org.jaxsb.compiler.schema.attribute;

import java.util.HashMap;

public final class Value {
  private static final HashMap<String,Value> enums = new HashMap<>();

  public static final Value COLLAPSE = new Value("collapse");
  public static final Value PRESERVE = new Value("preserve");
  public static final Value REPLACE = new Value("replace");

  public static Value parseValue(final String value) {
    return enums.get(value);
  }

  private final String value;

  private Value(final String value) {
    this.value = value;
    enums.put(value, this);
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Value))
      return false;

    return getValue().equals(((Value)obj).getValue());
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