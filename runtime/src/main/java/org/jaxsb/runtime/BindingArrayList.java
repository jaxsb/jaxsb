/* Copyright (c) 2023 JAX-SB
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
import java.util.Collection;

public class BindingArrayList<E> extends ArrayList<E> implements BindingList<E> {
  private final Binding owner;

  /**
   * Constructs an empty {@link BindingArrayList} with the specified initial capacity.
   *
   * @param initialCapacity The initial capacity of the list.
   * @param owner The owner {@link Binding}.
   * @throws IllegalArgumentException If the specified initial capacity is negative.
   */
  public BindingArrayList(final int initialCapacity, final Binding owner) {
    super(initialCapacity);
    this.owner = owner;
  }

  /**
   * Constructs a {@link BindingArrayList} containing the elements of the specified collection, in the order they are returned by the
   * collection's iterator.
   *
   * @param c The collection whose elements are to be placed into this list.
   * @param owner The owner {@link Binding}.
   * @throws NullPointerException If the specified collection is null.
   */
  public BindingArrayList(final Collection<? extends E> c, final Binding owner) {
    super(c);
    this.owner = owner;
  }

  /**
   * Constructs an empty {@link BindingArrayList} with an initial capacity of ten.
   *
   * @param owner The owner {@link Binding}.
   */
  public BindingArrayList(final Binding owner) {
    this.owner = owner;
  }

  @Override
  public Binding getOwner() {
    return owner;
  }

  @Override
  @SuppressWarnings("unchecked")
  public BindingArrayList<E> clone() {
    return (BindingArrayList<E>)super.clone();
  }
}