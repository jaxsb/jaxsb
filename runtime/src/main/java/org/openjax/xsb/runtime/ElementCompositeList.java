/* Copyright (c) 2017 OpenJAX
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

package org.openjax.xsb.runtime;

import java.io.Serializable;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.fastjax.util.MergedList;
import org.slf4j.Logger;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class ElementMergedList extends MergedList<Binding,QName> implements Serializable {
  private static final long serialVersionUID = -6869927461811501469L;

  protected static final QName ANY = new QName("##any", "##any");

  protected class ElementPartList extends PartList<Binding> implements BindingList<Binding> {
    private static final long serialVersionUID = -9155837408220305718L;
    private ElementAudit<? extends Binding> audit;

    public ElementPartList(final ElementAudit<? extends Binding> audit) {
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
    protected MergedList<Binding,QName> getMergedList() {
      return super.getMergedList();
    }

    @Override
    public Binding getOwner() {
      return audit.getOwner();
    }

    @Override
    public ElementPartList clone() {
      return (ElementPartList)super.clone();
    }
  }

  protected HashMap<QName,ElementAudit<? extends Binding>> nameToAudit;
  private $AnySimpleType owner;

  public ElementMergedList(final $AnySimpleType owner, final HashMap<QName,ElementAudit<?>> nameToAudit) {
    super(nameToAudit == null ? null : nameToAudit.keySet());
    this.nameToAudit = nameToAudit;
    this.owner = owner;
  }

  public Binding getOwner() {
    return owner;
  }

  protected <T extends Binding>ElementPartList newPartList(final ElementAudit<T> audit) {
    final ElementPartList partList = new ElementPartList(audit);
    typeToPartList.put(audit.getName() == null ? ANY : audit.getName(), partList);
    return partList;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected PartList<Binding> newPartList(final QName name) {
    final ElementAudit<Binding> audit = (ElementAudit<Binding>)nameToAudit.get(name);
    final ElementPartList partList = new ElementPartList(audit);
    audit.setElements(partList);
    return partList;
  }

  @SuppressWarnings("unchecked")
  protected PartList<Binding> getPartList(final QName name) {
    if (!typeToPartList.containsKey(name))
      return null;

    PartList<Binding> partialList = (PartList<Binding>)typeToPartList.get(name);
    if (partialList == null)
      typeToPartList.put(name, partialList = newPartList(name));

    return partialList;
  }

  protected PartList<Binding> getPartList(final QName name, final Class<? extends Binding> type) {
    final PartList<Binding> partialList = getPartList(name);
    return partialList != null ? partialList : getPartList(type);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected PartList<Binding> getPartList(Class<? extends Binding> type) {
    do {
      final org.openjax.xsb.runtime.QName annotation = type.getAnnotation(org.openjax.xsb.runtime.QName.class);
      if (annotation == null)
        return null;

      final PartList<Binding> partialList = getPartList(new QName(annotation.namespaceURI(), annotation.localPart()));
      if (partialList != null)
        return partialList;
    }
    while ((type = (Class<? extends Binding>)type.getSuperclass()) != null);
    return null;
  }

  public ElementPartList getPartList(final int index) {
    return (ElementPartList)partLists.get(index);
  }

  @Override
  protected void print(final Logger logger) {
    super.print(logger);
  }

  @Override
  protected Binding clone(final Binding item) {
    return item.clone();
  }

  protected ElementMergedList clone(final $AnySimpleType owner) {
    final ElementMergedList clone = (ElementMergedList)super.clone();
    clone.owner = owner;
    clone.nameToAudit = new HashMap<QName,ElementAudit<?>>();
    for (final HashMap.Entry<QName,ElementAudit<?>> entry : nameToAudit.entrySet()) {
      final ElementAudit<?> copy = new ElementAudit<>(owner, entry.getValue(), (ElementMergedList.ElementPartList)clone.getPartList(entry.getValue().getName()));
      clone.nameToAudit.put(entry.getKey(), copy);
    }

    return clone;
  }
}