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

import org.w3.www._2001.XMLSchema.yAA.$qName;

public abstract class NotationType extends $qName {
  public static NotationType parse(final javax.xml.namespace.QName name) {
    return _$$getNotation(name);
  }

  @Override
  protected Binding inherits() {
    return this;
  }

  protected abstract String getName();
  protected abstract String getPublic();
  protected abstract String getSystem();

  @Override
  protected int toString(final StringBuilder str, final PrefixToNamespace prefixToNamespace, final int indent, final int depth, final boolean qualified, final boolean nillable) {
    final javax.xml.namespace.QName name = name();
    str.append(prefixToNamespace.getPrefix(name)).append(':').append(name.getLocalPart());
    return -1;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public NotationType clone() {
    return (NotationType)super.clone();
  }
}