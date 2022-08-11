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

import java.util.LinkedHashSet;

import org.jaxsb.compiler.processor.model.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class RedefineModel extends Model {
  private final LinkedHashSet<SimpleTypeModel<?>> simpleTypeModels = new LinkedHashSet<>();
  private final LinkedHashSet<ComplexTypeModel<?>> complexTypeModels = new LinkedHashSet<>();
  private final LinkedHashSet<GroupModel> groupModels = new LinkedHashSet<>();
  private final LinkedHashSet<AttributeGroupModel> attributeGroupModels = new LinkedHashSet<>();
  private String schemaLocation;

  protected RedefineModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if ("schemaLocation".equals(attribute.getLocalName()))
        schemaLocation = attribute.getNodeValue();
    }
  }

  public String getSchemaLocation() {
    return schemaLocation;
  }

  public LinkedHashSet<SimpleTypeModel<?>> getSimpleTypeModels() {
    return simpleTypeModels;
  }

  public LinkedHashSet<ComplexTypeModel<?>> getComplexTypeModels() {
    return complexTypeModels;
  }

  public LinkedHashSet<GroupModel> getGroupModels() {
    return groupModels;
  }

  public LinkedHashSet<AttributeGroupModel> getAttributeGroupModels() {
    return attributeGroupModels;
  }
}