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

package org.jaxsb.compiler.processor.model.element;

import java.util.Collection;
import java.util.Collections;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.model.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class ListModel extends SimpleTypeModel<SimpleTypeModel<?>> {
  private SimpleTypeModel<?> itemType;
  private UnionModel unionType;

  protected ListModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if ("itemType".equals(attribute.getLocalName()))
        setItemType(SimpleTypeModel.Reference.parseSimpleType(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node))));
    }
  }

  public void setItemType(final SimpleTypeModel<?> itemType) {
    this.itemType = itemType;
  }

  public void setItemType(final UnionModel unionType) {
    this.unionType = unionType;
  }

  public Collection<SimpleTypeModel<?>> getItemType() {
    if (unionType != null)
      return unionType.getNormalizedMemberTypes();

    if (itemType != null)
      return Collections.singletonList(itemType);

    return null;
  }
}