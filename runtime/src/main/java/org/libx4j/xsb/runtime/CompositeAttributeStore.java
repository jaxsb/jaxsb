/* Copyright (c) 2017 lib4j
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

package org.libx4j.xsb.runtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class CompositeAttributeStore implements Serializable {
  private static final long serialVersionUID = 5214136979828034837L;

  private final ArrayList<AttributeAudit<?>> audits;

  public CompositeAttributeStore() {
    this.audits = new ArrayList<AttributeAudit<?>>();
  }

  private CompositeAttributeStore(final ArrayList<AttributeAudit<?>> audits) {
    this.audits = audits;
  }

  private class AttributeIterator implements Iterator<$AnySimpleType> {
    private final Iterator<AttributeAudit<?>> iterator;
    private $AnySimpleType next;

    public AttributeIterator(final Iterator<AttributeAudit<?>> iterator) {
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
      final $AnySimpleType current = next;
      next = null;
      setNext();
      return current;
    }
  }

  public void add(final AttributeAudit<?> audit) {
    this.audits.add(audit);
  }

  public Iterator<? extends $AnySimpleType> iterator() {
    return new AttributeIterator(audits.iterator());
  }

  public CompositeAttributeStore clone(final $AnySimpleType owner) {
    final ArrayList<AttributeAudit<?>> clone = new ArrayList<AttributeAudit<?>>();
    for (final AttributeAudit<?> audit : audits)
      clone.add(audit.clone(owner));

    return new CompositeAttributeStore(clone);
  }
}