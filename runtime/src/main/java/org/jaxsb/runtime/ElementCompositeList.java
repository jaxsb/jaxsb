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

import java.util.HashMap;

import javax.xml.namespace.QName;

import org.libj.util.CompositeList;
import org.slf4j.Logger;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;

@SuppressWarnings("rawtypes")
public class ElementCompositeList extends CompositeList<$AnyType,QName> {
  protected static final QName ANY = new QName("##any", "##any");

  protected class ElementComponentList extends ComponentList implements BindingList<$AnyType> {
    private ElementAudit<? extends $AnyType<?>> audit;

    public ElementComponentList(final ElementAudit<? extends $AnyType<?>> audit) {
      super(audit.getName() == null ? ANY : audit.getName());
      this.audit = audit;
    }

    protected void setAudit(final ElementAudit<? extends $AnyType<?>> audit) {
      this.audit = audit;
    }

    protected ElementAudit<? extends $AnyType<?>> getAudit() {
      return this.audit;
    }

    @Override
    protected void afterAdd(final int index, final $AnyType element, final RuntimeException e) {
      super.afterAdd(index, element, e);
      if (element.owner() != owner)
        element._$$setOwner(owner);
    }

    @Override
    protected CompositeList<$AnyType,QName> getCompositeList() {
      return super.getCompositeList();
    }

    @Override
    public $AnyType<?> getOwner() {
      return audit.getOwner();
    }

    @Override
    public ElementComponentList clone() {
      return (ElementComponentList)super.clone();
    }
  }

  protected HashMap<QName,ElementAudit<? extends $AnyType>> nameToAudit;
  private $AnySimpleType<?> owner;

  public ElementCompositeList(final $AnySimpleType<?> owner, final HashMap<QName,ElementAudit<?>> nameToAudit) {
    super(nameToAudit == null ? null : nameToAudit.keySet());
    this.nameToAudit = nameToAudit;
    this.owner = owner;
  }

  public $AnyType<?> getOwner() {
    return owner;
  }

  protected <T extends $AnyType<?>>ElementComponentList newComponentList(final ElementAudit<T> audit) {
    final ElementComponentList componentList = new ElementComponentList(audit);
    registerComponentList(audit.getName() == null ? ANY : audit.getName(), componentList);
    return componentList;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected ComponentList newComponentList(final QName name) {
    final ElementAudit<$AnyType<?>> audit = (ElementAudit<$AnyType<?>>)nameToAudit.get(name);
    final ElementComponentList componentList = new ElementComponentList(audit);
    audit.setElements(componentList);
    return componentList;
  }

  protected ComponentList getOrCreateComponentList(final QName name) {
    if (!containsComponentType(name))
      return null;

    ComponentList componentList = getComponentList(name);
    if (componentList == null)
      registerComponentList(name, componentList = newComponentList(name));

    return componentList;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected ComponentList getOrCreateComponentList(final $AnyType element) {
    Class<? extends $AnyType> type = element.getClass();
    do {
      final org.jaxsb.runtime.QName annotation = type.getAnnotation(org.jaxsb.runtime.QName.class);
      if (annotation == null)
        return null;

      final ComponentList componentList = getOrCreateComponentList(new QName(annotation.namespaceURI(), annotation.localPart()));
      if (componentList != null)
        return componentList;
    }
    while ((type = (Class<? extends $AnyType>)type.getSuperclass()) != null);
    return null;
  }

  @Override
  public ElementComponentList getComponentList(final int index) {
    return (ElementComponentList)super.getComponentList(index);
  }

  @Override
  protected void print(final Logger logger) {
    super.print(logger);
  }

  @Override
  protected $AnyType<?> clone(final $AnyType item) {
    return item.clone();
  }

  protected ElementCompositeList clone(final $AnySimpleType<?> owner) {
    final ElementCompositeList clone = clone();
    clone.owner = owner;
    clone.nameToAudit = new HashMap<>();
    for (final HashMap.Entry<QName,ElementAudit<?>> entry : nameToAudit.entrySet()) {
      final ElementAudit<?> copy = new ElementAudit<>(owner, entry.getValue(), (ElementCompositeList.ElementComponentList)clone.getOrCreateComponentList(entry.getValue().getName()));
      clone.nameToAudit.put(entry.getKey(), copy);
    }

    return clone;
  }

  @Override
  public ElementCompositeList clone() {
    return (ElementCompositeList)super.clone();
  }
}