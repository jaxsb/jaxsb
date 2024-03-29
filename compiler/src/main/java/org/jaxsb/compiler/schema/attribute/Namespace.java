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

public final class Namespace {
  private static final HashMap<String,Namespace> enums = new HashMap<>();

  public static final Namespace ANY = new Namespace("##any");
  public static final Namespace LOCAL = new Namespace("##local");
  public static final Namespace OTHER = new Namespace("##other");
  public static final Namespace TARGETNAMESPACE = new Namespace("##targetNamespace");

  public static Namespace parseNamespace(final String value) {
    final Namespace namespace = enums.get(value);
    if (namespace != null)
      return namespace;

    return new Namespace(value);
  }

  private final String value;

  public Namespace(final String value) {
    this.value = value;
    enums.put(value, this);
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Namespace))
      return false;

    return getValue().equals(((Namespace)obj).getValue());
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