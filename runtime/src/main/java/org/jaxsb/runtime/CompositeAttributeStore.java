/* Copyright (c) 2017 JAX-SB
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jaxsb.runtime.Binding.PrefixToNamespace;
import org.libj.util.DelegateList;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class CompositeAttributeStore extends DelegateList<AttributeAudit<?>,ArrayList<AttributeAudit<?>>> {
  public CompositeAttributeStore() {
    super(new ArrayList<>());
  }

  @SuppressWarnings("rawtypes")
  private final class AttributeIterator implements Iterator<$AnySimpleType> {
    private final Iterator<AttributeAudit<?>> iterator;
    private $AnySimpleType next;

    private AttributeIterator(final Iterator<AttributeAudit<?>> iterator) {
      this.iterator = iterator;
      setNext();
    }

    private void setNext() {
      while (next == null && iterator.hasNext())
        next = iterator.next().getAttribute();
    }

    @Override
    public boolean hasNext() {
      return next != null;
    }

    @Override
    public $AnySimpleType next() {
      if (next == null)
        throw new NoSuchElementException();

      final $AnySimpleType current = next;
      next = null;
      setNext();
      return current;
    }
  }

  @SuppressWarnings("rawtypes")
  public Iterator<$AnySimpleType> valueIterator() {
    return new AttributeIterator(iterator());
  }

  public CompositeAttributeStore clone(final $AnySimpleType<?> owner) {
    final CompositeAttributeStore clone = new CompositeAttributeStore();
    for (int i = 0, i$ = this.size(); i < i$; ++i) // [RA]
      clone.add(get(i).clone(owner));

    return clone;
  }

  public void toString(final StringBuilder str, final PrefixToNamespace prefixToNamespace) {
    for (int i = 0, i$ = this.size(); i < i$; ++i) // [RA]
      get(i).toString(str,prefixToNamespace);
  }
}