/* Copyright (c) 2008 lib4j
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

import java.util.Iterator;

import org.lib4j.util.IdentityArrayList;

final class GeneralElementList<E extends Binding> extends IdentityArrayList<E> {
  private static final long serialVersionUID = 4251508116129047104L;

  private final CompositeElementStore directory;

  protected GeneralElementList(final CompositeElementStore directory, final int initialCapacity) {
    super(initialCapacity);
    this.directory = directory;
  }

  protected GeneralElementList(final CompositeElementStore directory) {
    this.directory = directory;
  }

  @Override
  public Iterator<E> iterator() {
    return new ElementIterator();
  }

  private final class ElementIterator implements Iterator<E> {
    private final Iterator<E> iterator = GeneralElementList.super.iterator();
    private int cursor = 0;
    private int lastRet = -1;

    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }

    @Override
    public E next() {
      final E next = iterator.next();
      lastRet = cursor++;
      return next;
    }

    @Override
    public void remove() {
      if (lastRet == -1)
        throw new IllegalStateException();

      final E removeMe = GeneralElementList.this.get(lastRet);
      directory.remove(lastRet, removeMe);
      GeneralElementList.this.remove(lastRet);

      if (lastRet < cursor)
        cursor--;

      lastRet = -1;
    }
  }
}