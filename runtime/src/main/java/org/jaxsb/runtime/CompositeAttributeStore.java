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
import java.util.List;
import java.util.NoSuchElementException;

import org.jaxsb.runtime.Binding.PrefixToNamespace;
import org.libj.util.DelegateRandomAccessList;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class CompositeAttributeStore extends DelegateRandomAccessList<AbstractAttributeAudit,ArrayList<AbstractAttributeAudit>> {
  public CompositeAttributeStore() {
    super(new ArrayList<>());
  }

  @SuppressWarnings("rawtypes")
  private final class AttributeIterator implements Iterator<$AnySimpleType> {
    private final Iterator<AbstractAttributeAudit> iterator;
    private Object next;

    private AttributeIterator(final Iterator<AbstractAttributeAudit> iterator) {
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
    @SuppressWarnings("unchecked")
    public $AnySimpleType next() {
      if (next == null)
        throw new NoSuchElementException();

      final $AnySimpleType current;
      if (next instanceof $AnySimpleType) {
        current = ($AnySimpleType)next;
      }
      else if (next instanceof Iterator) {
        final Iterator<$AnySimpleType> iterator = (Iterator<$AnySimpleType>)next;
        current = iterator.next();
        if (iterator.hasNext())
          return current;
      }
      else if (next instanceof List) {
        next = ((List)next).iterator();
        return next();
      }
      else {
        throw new IllegalStateException("Unexpected attribute type: " + next.getClass().getName());
      }

      next = null;
      setNext();
      return current;
    }
  }

  @SuppressWarnings("rawtypes")
  public Iterator<$AnySimpleType> valueIterator() {
    return new AttributeIterator(iterator());
  }

  CompositeAttributeStore clone(final $AnySimpleType<?> owner) {
    final CompositeAttributeStore clone = new CompositeAttributeStore();
    for (int i = 0, i$ = this.size(); i < i$; ++i) // [RA]
      clone.add(get(i).clone(owner));

    return clone;
  }

  void toString(final StringBuilder str, final PrefixToNamespace prefixToNamespace) {
    for (int i = 0, i$ = this.size(); i < i$; ++i) // [RA]
      get(i).toString(str, prefixToNamespace);
  }
}