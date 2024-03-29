/* Copyright (c) 2008 JAX-SB
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

import org.w3.www._2001.XMLSchema.yAA.$AnyType;

@SuppressWarnings("rawtypes")
public class BindingProxy<T extends $AnyType> extends $AnyType<Object> {
  private T binding;

  public BindingProxy(final T binding) {
    this.binding = binding;
  }

  public void setBinding(final T binding) {
    this.binding = binding;
  }

  public T getBinding() {
    return binding;
  }

  @Override
  protected boolean dirty() {
    return binding != null && binding.dirty();
  }

  @Override
  protected Binding inherits() {
    return binding == null ? null : binding.inherits();
  }

  @Override
  public QName name() {
    return binding.name();
  }

  @Override
  @SuppressWarnings("unchecked")
  public BindingProxy<T> clone() {
    final BindingProxy<T> clone = (BindingProxy<T>)super.clone();
    clone.binding = (T)binding.clone();
    return clone;
  }
}