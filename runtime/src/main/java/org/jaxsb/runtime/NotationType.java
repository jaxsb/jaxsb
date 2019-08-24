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

public abstract class NotationType extends Binding {
  private static final long serialVersionUID = -7634751765517029331L;

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
  public String toString() {
    return getName();
  }
}