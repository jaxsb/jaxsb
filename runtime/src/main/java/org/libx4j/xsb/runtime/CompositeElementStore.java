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

import java.util.ArrayList;
import java.util.List;

final class CompositeElementStore {
  private final List<Binding> elements;
  private final List<ElementAudit<? extends Binding>> elementAudits;

  protected CompositeElementStore(final int initialCapacity) {
    this.elements = new GeneralElementList<Binding>(this, initialCapacity);
    this.elementAudits = new ArrayList<ElementAudit<? extends Binding>>(initialCapacity);
  }

  protected CompositeElementStore(final CompositeElementStore copy) {
    this.elements = new GeneralElementList<Binding>(this);
    this.elementAudits = new ArrayList<ElementAudit<? extends Binding>>(copy.elementAudits);
  }

  protected CompositeElementStore() {
    this.elements = new GeneralElementList<Binding>(this);
    this.elementAudits = new ArrayList<ElementAudit<? extends Binding>>();
  }

  protected int size() {
    return elements.size();
  }

  protected List<Binding> getElements() {
    return elements;
  }

  protected Binding getElement(final int index) {
    return elements.get(index);
  }

  protected ElementAudit<? extends Binding> getElementAudits(final int index) {
    return elementAudits.get(index);
  }

  protected <B extends Binding>boolean add(final B element, final ElementAudit<B> elementAudit, final boolean addToAudit) {
    synchronized (elements) {
      if (!elements.add(element))
        throw new BindingRuntimeException("Addition of element should have modified the elements list!");

      if (!elementAudits.add(elementAudit))
        throw new BindingRuntimeException("Addition of element should have modified the elementAudits list!");

      if (addToAudit && !elementAudit.addElement(element))
        throw new BindingRuntimeException("Addition of element should have modified the elementAudit list!");
    }

    return true;
  }

  protected void addBefore(final Binding before, final Binding element, final ElementAudit<? extends Binding> elementAudit) {
    synchronized (elements) {
      final int index = elements.indexOf(before);
      elements.add(index, element);
      elementAudits.add(index, elementAudit);
    }
  }

  protected void addAfter(final Binding after, final Binding element, final ElementAudit<? extends Binding> elementAudit) {
    synchronized (elements) {
      final int index = elements.indexOf(after);
      elements.add(index + 1, element);
      elementAudits.add(index, elementAudit);
    }
  }

  protected void replace(final Binding original, final Binding element, final ElementAudit<? extends Binding> elementAudit) {
    synchronized (elements) {
      final int index = elements.indexOf(original);
      elements.set(index, element);
      elementAudits.set(index, elementAudit);
    }
  }

  protected boolean remove(final Binding element) {
    synchronized (elements) {
      final int index = elements.indexOf(element);
      if (index < 0)
        return false;

      if (elements.remove(index) != element)
        throw new BindingRuntimeException("Element identities do not match. Report this please.");

      // NOTE: The remove() method is initiated from the value list, which
      // NOTE: means that it is responsible for removing its own element
      // NOTE: and it is not the responsibility of this method to remove
      // NOTE: that element from the value list of the ElementAudit.
      elementAudits.remove(index);
    }

    return true;
  }

  protected boolean remove(final int index, final Binding element) {
    synchronized (elements) {
      final ElementAudit<?> elementAudit = elementAudits.remove(index);
      if (elementAudit != null)
        return ((SpecificElementList<?>)elementAudit.getElements()).remove(element, false);
    }

    return false;
  }

  protected void clear() {
    synchronized (elements) {
      elements.clear();
      elementAudits.clear();
    }
  }

  @Override
  public CompositeElementStore clone() {
    return new CompositeElementStore(this);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof CompositeElementStore))
      return false;

    final CompositeElementStore that = (CompositeElementStore)obj;
    return elements.equals(that.elements) && elementAudits.equals(that.elementAudits);
  }

  @Override
  public int hashCode() {
    return elements.hashCode();
  }
}