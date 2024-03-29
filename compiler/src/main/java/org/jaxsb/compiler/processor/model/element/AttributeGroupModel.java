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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Referenceable;
import org.jaxsb.compiler.processor.model.AttributableModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.NamedModel;
import org.jaxsb.compiler.processor.model.RedefinableModel;
import org.jaxsb.compiler.processor.model.ReferableModel;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AttributeGroupModel extends NamedModel implements AttributableModel, RedefinableModel<AttributeGroupModel>, ReferableModel<AttributeGroupModel> {
  private final LinkedHashSet<AttributeModel> attributes = new LinkedHashSet<>();
  private AttributeGroupModel ref;
  private AttributeGroupModel redefine;

  protected AttributeGroupModel(final Node node, final Model parent) {
    super(node, parent);
    if (node == null)
      return;

    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if ("ref".equals(attribute.getLocalName()))
        ref = AttributeGroupModel.Reference.parseAttributeGroup(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node)));
    }
  }

  @Override
  public final void setRedefine(final AttributeGroupModel redefine) {
    this.redefine = redefine;
  }

  @Override
  public final AttributeGroupModel getRedefine() {
    return redefine;
  }

  @Override
  public final void addAttribute(final AttributeModel attribute) {
    attributes.add(attribute);
  }

  public void addAllAttributes(final Collection<? extends AttributeModel> attributes) {
    this.attributes.addAll(attributes);
  }

  @Override
  public final LinkedHashSet<AttributeModel> getAttributes() {
    return attributes;
  }

  @Override
  public final void setRef(final AttributeGroupModel ref) {
    this.ref = ref;
  }

  @Override
  public final AttributeGroupModel getRef() {
    return ref;
  }

  @Override
  public boolean isQualified(final boolean nested) {
    return getRef() != null;
  }

  @Override
  public String toString() {
    return super.toString().replace(TO_STRING_DELIMITER, "ref=\"" + ref + "\"");
  }

  public static final class Reference extends AttributeGroupModel implements Referenceable {
    private static final Map<UniqueQName,Reference> all = new HashMap<>();

    protected Reference(final Model parent) {
      super(null, parent);
    }

    public static Reference parseAttributeGroup(final UniqueQName name) {
      Reference type = all.get(name);
      if (type != null)
        return type;

      type = new Reference(null);
      type.setName(name);
      Reference.all.put(name, type);
      return type;
    }
  }
}