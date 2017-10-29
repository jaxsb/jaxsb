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
import java.util.IdentityHashMap;
import java.util.Map;

import org.lib4j.util.PartitionedList;

public class ElementSuperList extends PartitionedList<Binding> implements Serializable {
  private static final long serialVersionUID = -6869927461811501469L;

  protected class ElementSubList<B extends Binding> extends PartitionedList<Binding>.PartitionList<B> implements BindingList<B>, Serializable {
    private static final long serialVersionUID = -9155837408220305718L;
    private ElementAudit<B> audit;

    public ElementSubList(final ElementAudit<B> audit) {
      super(audit.getType());
      this.audit = audit;
    }

    protected void setAudit(final ElementAudit<B> audit) {
      this.audit = audit;
    }

    protected ElementAudit<B> getAudit() {
      return this.audit;
    }

    @Override
    protected PartitionedList<Binding> getSuperList() {
      return super.getSuperList();
    }

    @Override
    public Binding getParent() {
      return audit.getParent();
    }

    @Override
    public ElementSubList<B> clone() {
      return (ElementSubList<B>)super.clone();
    }
  }

  private IdentityHashMap<Class<? extends Binding>,ElementAudit<?>> typeToAudit;

  public ElementSuperList(final IdentityHashMap<Class<? extends Binding>,ElementAudit<?>> typeToAudit) {
    super(typeToAudit.keySet());
    this.typeToAudit = typeToAudit;
  }

  public Binding getParent() {
    return null;
  }

  protected <T extends Binding>ElementSubList<T> newPartition(final ElementAudit<T> audit) {
    final ElementSubList<T> subList = new ElementSubList<T>(audit);
    typeToSubList.put(audit.getType(), subList);
    return subList;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected <T extends Binding>PartitionedList<Binding>.PartitionList<T> newPartition(final Class<T> type) {
    final ElementAudit<T> audit = (ElementAudit<T>)typeToAudit.get(type);
    final ElementSubList<T> subList = new ElementSubList<T>(audit);
    audit.setElements(subList);
    return subList;
  }

  @Override
  public <T extends Binding>ElementSubList<T> getPartition(final Class<T> type) {
    return (ElementSubList<T>)super.getPartition(type);
  }

  public ElementSubList<? extends Binding> getPartition(final int index) {
    return (ElementSubList<?>)subLists.get(index);
  }

  @Override
  protected void print() {
    super.print();
  }

  @Override
  public ElementSuperList clone() {
    final ElementSuperList clone = (ElementSuperList)super.clone();
    clone.typeToAudit = new IdentityHashMap<Class<? extends Binding>,ElementAudit<?>>();
    for (final Map.Entry<Class<? extends Binding>,ElementAudit<?>> entry : typeToAudit.entrySet())
      clone.typeToAudit.put(entry.getKey(), entry.getValue().clone(this));

    return clone;
  }
}