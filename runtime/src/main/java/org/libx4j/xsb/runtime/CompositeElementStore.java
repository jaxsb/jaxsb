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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.lib4j.util.IdentityArrayList;
import org.lib4j.util.ObservableList;

final class CompositeElementStore implements Serializable {
  private static final long serialVersionUID = 1140578580159732414L;

  @SuppressWarnings("unchecked")
  private class GeneralElementList<E extends Binding> extends ObservableList<E> implements Serializable {
    private static final long serialVersionUID = 4251508116129047104L;

    protected GeneralElementList(final CompositeElementStore directory, final int initialCapacity) {
      super(new IdentityArrayList<E>(initialCapacity));
    }

    protected GeneralElementList(final CompositeElementStore directory) {
      super(new IdentityArrayList<E>());
    }

    protected void addUnsafe(final E e) {
      super.source.add(e);
    }

    protected void addUnsafe(int index, final E e) {
      super.source.add(index, e);
    }

    protected E setUnsafe(int index, final E e) {
      return (E)super.source.set(index, e);
    }

    protected E removeUnsafe(int index) {
      return (E)super.source.remove(index);
    }

    @Override
    protected void beforeAdd(final int index, final E e) {
      final ElementAudit<? super E> audit = (ElementAudit<E>)parent._$$getElementAudit(e.getClass());
      if (audit == null)
        throw new IllegalArgumentException("Element " + e.name() + " of type " + e.typeName() + " is not allowed to appear in " + parent.name());

      audit.addElementUnsafe(e);
      elementAudits.add(index, audit);
    }

    @Override
    protected void beforeRemove(final int index) {
      final ElementAudit<?> audit = elementAudits.remove(index);
      final Binding element = elements.get(index);
      audit.removeUnsafe(element);
    }

    @Override
    protected void beforeSet(final int index, final E newElement) {
      final ElementAudit<?> audit = elementAudits.get(index);
      final Binding element = elements.get(index);
      final int auditIndex = audit.indexOf(element);
      if(element.getClass() == newElement.getClass()) {
        ((ElementAudit<E>)audit).setUnsafe(auditIndex, newElement);
      }
      else {
        final ElementAudit<? super E> newAudit = (ElementAudit<E>)parent._$$getElementAudit(newElement.getClass());
        if (newAudit == null)
          throw new IllegalArgumentException("Element " + newElement.name() + " of type " + newElement.typeName() + " is not allowed to appear in " + parent.name());

        audit.removeUnsafe(auditIndex);
        newAudit.addElementUnsafe(newElement);
        elementAudits.set(index, newAudit);
      }
    }
  }

  private final Binding parent;
  private final GeneralElementList<Binding> elements;
  private final List<ElementAudit<? extends Binding>> elementAudits;

  protected CompositeElementStore(final Binding parent) {
    this.parent = parent;
    this.elements = new GeneralElementList<Binding>(this);
    this.elementAudits = new ArrayList<ElementAudit<? extends Binding>>();
  }

  protected CompositeElementStore(final CompositeElementStore copy) {
    this.parent = copy.parent;
    this.elements = new GeneralElementList<Binding>(this);
    this.elementAudits = new ArrayList<ElementAudit<? extends Binding>>(copy.elementAudits);
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

  protected ElementAudit<? extends Binding> getElementAudit(final int index) {
    return elementAudits.get(index);
  }

  protected <B extends Binding>void add(final B element, final ElementAudit<B> elementAudit, final boolean addToAudit) {
    synchronized (elements) {
      elementAudits.add(elementAudit);
      // This complexity takes care of the situation when the _default value is replaced within the BindingProxy
      boolean addedToAudit = false;
      if (addToAudit)
        addedToAudit = elementAudit.addElementUnsafe(element);

      if (!addToAudit || addedToAudit)
        elements.addUnsafe(element);
    }
  }

  protected void addBefore(final Binding before, final Binding element, final ElementAudit<? extends Binding> elementAudit) {
    synchronized (elements) {
      final int index = elements.indexOf(before);
      elements.addUnsafe(index, element);
    }
  }

  protected void replace(final Binding original, final Binding element, final ElementAudit<? extends Binding> elementAudit) {
    synchronized (elements) {
      final int index = elements.indexOf(original);
      elementAudits.set(index, elementAudit);
      elements.setUnsafe(index, element);
    }
  }

  protected boolean remove(final Binding element) {
    synchronized (elements) {
      final int index = elements.indexOf(element);
      if (index < 0)
        return false;

      if (elements.removeUnsafe(index) != element)
        throw new BindingRuntimeException("Element identities do not match. Report this please.");

      // NOTE: The remove() method is initiated from the value list, which
      // NOTE: means that it is responsible for removing its own element
      // NOTE: and it is not the responsibility of this method to remove
      // NOTE: that element from the value list of the ElementAudit.
      elementAudits.remove(index);
    }

    return true;
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