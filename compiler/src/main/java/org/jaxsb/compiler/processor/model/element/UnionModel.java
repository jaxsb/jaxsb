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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.model.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class UnionModel extends Model {
  private final HashSet<SimpleTypeModel<?>> memberTypes = new HashSet<>();
  private final HashSet<UnionModel> unions = new HashSet<>();

  protected UnionModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if ("memberTypes".equals(attribute.getLocalName()))
        parseMemberTypes(attribute.getNodeValue(), node);
    }
  }

  private void parseMemberTypes(final String memberTypes, final Node node) {
    final StringTokenizer tokenizer = new StringTokenizer(memberTypes);
    while (tokenizer.hasMoreTokens())
      this.memberTypes.add(SimpleTypeModel.Reference.parseSimpleType(UniqueQName.getInstance(parseQNameValue(tokenizer.nextToken(), node))));
  }

  public Collection<SimpleTypeModel<?>> getMemberTypes() {
    return memberTypes;
  }

  public void addUnion(final UnionModel unionModel) {
    unions.add(unionModel);
  }

  public Collection<SimpleTypeModel<?>> getNormalizedMemberTypes() {
    final ArrayList<SimpleTypeModel<?>> allMemberTypes = new ArrayList<>(getMemberTypes());
    if (unions.size() > 0)
      for (final UnionModel union : unions) // [S]
        allMemberTypes.addAll(union.getNormalizedMemberTypes());

    return allMemberTypes;
  }
}