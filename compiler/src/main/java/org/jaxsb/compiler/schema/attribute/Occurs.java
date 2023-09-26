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

public final class Occurs {
  public static final Occurs UNBOUNDED = new Occurs(Integer.MAX_VALUE);

  public static Occurs parseOccurs(final String value) {
    return "unbounded".equals(value) ? UNBOUNDED : new Occurs(Integer.parseInt(value));
  }

  private final int value;

  private Occurs(final int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Occurs))
      return false;

    return value == ((Occurs)obj).value;
  }

  @Override
  public int hashCode() {
    return 31 * getClass().getName().hashCode() + Integer.hashCode(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}