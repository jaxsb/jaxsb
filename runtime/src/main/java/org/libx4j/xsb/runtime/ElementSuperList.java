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
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.lib4j.util.PartitionedList;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class ElementSuperList extends PartitionedList<Binding,QName> implements Serializable {
  private static final long serialVersionUID = -6869927461811501469L;

  protected static final QName ANY = new QName("##any", "##any");

  protected class ElementSubList extends PartitionedList<Binding,QName>.PartitionList<Binding> implements BindingList<Binding> {
    private static final long serialVersionUID = -9155837408220305718L;
    private ElementAudit<? extends Binding> audit;

    public ElementSubList(final ElementAudit<? extends Binding> audit) {
      super(audit.getName() == null ? ANY : audit.getName());
      this.audit = audit;
    }

    protected void setAudit(final ElementAudit<? extends Binding> audit) {
      this.audit = audit;
    }

    protected ElementAudit<? extends Binding> getAudit() {
      return this.audit;
    }

    @Override
    protected void afterAdd(final int index, final Binding e, final RuntimeException re) {
      super.afterAdd(index, e, re);
      if (e.owner() != owner)
        e._$$setOwner(owner);
    }

    @Override
    protected PartitionedList<Binding,QName> getSuperList() {
      return super.getSuperList();
    }

    @Override
    public Binding getOwner() {
      return audit.getOwner();
    }

    @Override
    public ElementSubList clone() {
      return (ElementSubList)super.clone();
    }
  }

  protected HashMap<QName,ElementAudit<? extends Binding>> nameToAudit;
  private $AnySimpleType owner;

  public ElementSuperList(final $AnySimpleType owner, final HashMap<QName,ElementAudit<?>> nameToAudit) {
    super(nameToAudit == null ? null : nameToAudit.keySet());
    this.nameToAudit = nameToAudit;
    this.owner = owner;
  }

  public Binding getOwner() {
    return owner;
  }

  protected <T extends Binding>ElementSubList newPartition(final ElementAudit<T> audit) {
    final ElementSubList subList = new ElementSubList(audit);
    typeToSubList.put(audit.getName() == null ? ANY : audit.getName(), subList);
    return subList;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected PartitionList<Binding> newPartition(final QName name) {
    final ElementAudit<Binding> audit = (ElementAudit<Binding>)nameToAudit.get(name);
    final ElementSubList subList = new ElementSubList(audit);
    audit.setElements(subList);
    return subList;
  }

  @SuppressWarnings("unchecked")
  protected PartitionedList<Binding,QName>.PartitionList<Binding> getPartition(final QName name) {
    if (!typeToSubList.containsKey(name))
      return null;

    PartitionList<Binding> partitionList = (PartitionedList<Binding,QName>.PartitionList<Binding>)typeToSubList.get(name);
    if (partitionList == null)
      typeToSubList.put(name, partitionList = newPartition(name));

    return partitionList;
  }

  protected PartitionedList<Binding,QName>.PartitionList<Binding> getPartition(final QName name, final Class<? extends Binding> type) {
    final PartitionedList<Binding,QName>.PartitionList<Binding> partition = getPartition(name);
    return partition != null ? partition : getPartition(type);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected PartitionedList<Binding,QName>.PartitionList<Binding> getPartition(Class<? extends Binding> type) {
    do {
      final org.libx4j.xsb.runtime.QName annotation = type.getAnnotation(org.libx4j.xsb.runtime.QName.class);
      if (annotation == null)
        return null;

      final PartitionedList<Binding,QName>.PartitionList<Binding> partition = getPartition(new QName(annotation.namespaceURI(), annotation.localPart()));
      if (partition != null)
        return partition;
    }
    while ((type = (Class<? extends Binding>)type.getSuperclass()) != null);
    return null;
  }

  public ElementSubList getPartition(final int index) {
    return (ElementSubList)subLists.get(index);
  }

  @Override
  protected void print() {
    super.print();
  }

  @Override
  protected Binding clone(final Binding item) {
    return item.clone();
  }

  protected ElementSuperList clone(final $AnySimpleType owner) {
    final ElementSuperList clone = (ElementSuperList)super.clone();
    clone.owner = owner;
    clone.nameToAudit = new HashMap<QName,ElementAudit<?>>();
    for (final HashMap.Entry<QName,ElementAudit<?>> entry : nameToAudit.entrySet()) {
      final ElementAudit<?> copy = new ElementAudit<>(owner, entry.getValue(), (ElementSuperList.ElementSubList)clone.getPartition(entry.getValue().getName()));
      clone.nameToAudit.put(entry.getKey(), copy);
    }

    return clone;
  }
}