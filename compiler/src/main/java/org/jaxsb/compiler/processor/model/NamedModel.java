/* Copyright (c) 2008 JAX-SB
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

package org.jaxsb.compiler.processor.model;

import java.util.ArrayList;
import java.util.Objects;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.element.RestrictionModel;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class NamedModel extends Model implements Nameable<Model> {
  private UniqueQName name;

  protected NamedModel(final Node node, final Model parent) {
    super(node, parent);
    if (node == null)
      return;

    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if ("name".equals(attribute.getLocalName()))
        name = UniqueQName.getInstance(getTargetNamespace(), attribute.getNodeValue());
    }
  }

  protected final void setName(final UniqueQName name) {
    this.name = name;
  }

  @Override
  public UniqueQName getName() {
    return name;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(getClass().isInstance(obj)))
      return false;

    final NamedModel that = (NamedModel)obj;
    return Objects.equals(name, that.name);
  }

  // FIXME: This is dirty!!
  public static UniqueQName getNameOfRestrictionBase(final NamedModel model) {
    if (model == null)
      return null;

    final ArrayList<Model> children = model.getChildren();
    for (int i = 0, i$ = children.size(); i < i$; ++i) { // [RA]
      final Model child = children.get(i);
      if (child instanceof RestrictionModel)
        return ((RestrictionModel)child).getBase().getName();
    }

    return null;
  }

  @Override
  public int hashCode() {
    UniqueQName name = this.name;
    if (name == null)
      name = getNameOfRestrictionBase(this);

    return 3 * (name != null ? name.hashCode() : -1);
  }

  @Override
  public String toString() {
    UniqueQName name = this.name;
    if (name == null)
      name = getNameOfRestrictionBase(this);

    if (name == null)
      return super.toString();

    return super.toString() + name;
  }
}