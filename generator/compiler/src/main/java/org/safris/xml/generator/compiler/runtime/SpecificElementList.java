/* Copyright (c) 2008 Seva Safris
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

package org.safris.xml.generator.compiler.runtime;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import org.safris.commons.util.IdentityArrayList;

final class SpecificElementList<E extends Binding> extends IdentityArrayList<E> implements BindingList<E> {
  public static <E extends Binding>SpecificElementList<E> singleton(final ElementAudit<E> elementAudit, final E binding) {
    final SpecificElementList<E> list = new SpecificElementList<E>(elementAudit, 1);
    list.add(binding, false);
    return list;
  }

  private static final long serialVersionUID = 4802873640158426470L;

  private final ElementAudit<E> elementAudit;

  protected SpecificElementList(final ElementAudit<E> elementAudit, final int initialCapacity) {
    super(initialCapacity);
    this.elementAudit = elementAudit;
  }

  protected SpecificElementList(final ElementAudit<E> elementAudit, Collection<? extends E> c) {
    super(c);
    this.elementAudit = elementAudit;
  }

  protected SpecificElementList(final ElementAudit<E> elementAudit) {
    super();
    this.elementAudit = elementAudit;
  }

  @Override
  public Binding getParent() {
    return elementAudit.getParent();
  }

  protected boolean add(final E o, final boolean addWithAudit) {
    return addWithAudit ? elementAudit.getParent()._$$addElementNoAudit(elementAudit, o) && super.add(o) : super.add(o);
  }

  @Override
  public boolean add(final E o) {
    final E after = get(size() - 1);
    elementAudit.getParent()._$$addElementAfter(after, elementAudit, o);
    return super.add(o);
  }

  @Override
  public E set(final int index, final E element) {
    final E original = get(index);
    elementAudit.getParent()._$$replaceElement(original, elementAudit, element);
    return super.set(index, element);
  }

  @Override
  public void add(final int index, final E element) {
    final E before = get(index);
    elementAudit.getParent()._$$addElementBefore(before, elementAudit, element);
    super.add(index, element);
  }

  @Override
  public E remove(final int index) {
    final E element = get(index);
    remove(element);
    return element;
  }

  protected boolean remove(final Object o, final boolean removeFromAudit) {
    if (!(o instanceof Binding) || !contains(o))
      return false;

    final boolean listModified = super.remove(o);
    if (!removeFromAudit)
      return listModified;

    final boolean auditModified = elementAudit.getParent()._$$removeElement((Binding)o);
    if (auditModified == listModified)
      return auditModified;

    throw new BindingRuntimeException("Both lists should have been modified, or none at all.");
  }

  @Override
  public boolean remove(final Object o) {
    final boolean retVal = remove(o, true);
    if (size() == 0)
      elementAudit.reset();

    return retVal;
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    final boolean retVal = super.removeAll(c);
    if (size() == 0)
      elementAudit.reset();

    return retVal;
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    final boolean retVal = super.retainAll(c);
    if (size() == 0)
      elementAudit.reset();

    return retVal;
  }

  @Override
  public void clear() {
    super.clear();
    elementAudit.reset();
  }

  @Override
  public Iterator<E> iterator() {
    return new ElementIterator();
  }

  @Override
  public ListIterator<E> listIterator() {
    return listIterator(0);
  }

  @Override
  public ListIterator<E> listIterator(final int index) {
    if (index < 0 || index > size())
      throw new IndexOutOfBoundsException("Index: " + index);

    return new ElementListIterator(index);
  }

  public SpecificElementList<E> clone(final ElementAudit<E> audit) {
    return new SpecificElementList<E>(audit, this);
  }

  private final class ElementIterator implements Iterator<E> {
    private final Iterator<E> iterator = SpecificElementList.super.iterator();
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

      final E removeMe = SpecificElementList.this.get(lastRet);
      if (!SpecificElementList.this.remove(removeMe))
        throw new IllegalStateException("remove() method should have removed an element here");

      if (lastRet < cursor)
        cursor--;
      lastRet = -1;
    }
  }

  private final class ElementListIterator implements ListIterator<E> {
    private final ListIterator<E> listIterator;

    protected ElementListIterator(final int index) {
      listIterator = SpecificElementList.super.listIterator(index);
    }

    @Override
    public boolean hasNext() {
      return listIterator.hasNext();
    }

    @Override
    public E next() {
      return listIterator.next();
    }

    @Override
    public boolean hasPrevious() {
      return listIterator.hasPrevious();
    }

    @Override
    public E previous() {
      return listIterator.previous();
    }

    @Override
    public int nextIndex() {
      return listIterator.nextIndex();
    }

    @Override
    public int previousIndex() {
      return listIterator.previousIndex();
    }

    @Override
    public void remove() {
      final int index = nextIndex() - 1;
      SpecificElementList.this.remove(index);
    }

    @Override
    public void set(final E o) {
      final int index = nextIndex() - 1;
      SpecificElementList.this.set(index, o);
    }

    @Override
    public void add(final E o) {
      final int index = nextIndex() - 1;
      SpecificElementList.this.add(index, o);
    }
  }
}