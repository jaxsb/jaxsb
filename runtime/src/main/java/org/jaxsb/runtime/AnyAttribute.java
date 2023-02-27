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

package org.jaxsb.runtime;

import javax.xml.namespace.QName;

import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class AnyAttribute<T> extends $AnySimpleType<T> {
  private final QName name;

  public AnyAttribute(final QName name) {
    this.name = name;
  }

  @Override
  public QName name() {
    return name;
  }

  @Override
  protected Binding inherits() {
    return null;
  }

  @Override
  public AnyAttribute<T> clone() {
    return (AnyAttribute<T>)super.clone();
  }
}