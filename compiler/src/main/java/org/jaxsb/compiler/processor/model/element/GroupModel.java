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
import java.util.HashMap;
import java.util.Map;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Referenceable;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.MultiplicableModel;
import org.jaxsb.compiler.processor.model.NamedModel;
import org.jaxsb.compiler.processor.model.RedefinableModel;
import org.jaxsb.compiler.processor.model.ReferableModel;
import org.jaxsb.compiler.schema.attribute.Occurs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class GroupModel extends NamedModel implements MultiplicableModel, RedefinableModel<GroupModel>, ReferableModel<GroupModel> {
  private final ArrayList<MultiplicableModel> multiplicableModels = new ArrayList<>();
  private Occurs maxOccurs = Occurs.parseOccurs("1");
  private Occurs minOccurs = Occurs.parseOccurs("1");
  private GroupModel ref;
  private GroupModel redefine;

  protected GroupModel(final Node node, final Model parent) {
    super(node, parent);
    if (node == null)
      return;

    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if ("maxOccurs".equals(attribute.getLocalName()))
        maxOccurs = Occurs.parseOccurs(attribute.getNodeValue());
      else if ("minOccurs".equals(attribute.getLocalName()))
        minOccurs = Occurs.parseOccurs(attribute.getNodeValue());
      else if ("ref".equals(attribute.getLocalName()))
        ref = GroupModel.Reference.parseGroup(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node)));
    }
  }

  @Override
  public final void setRedefine(final GroupModel redefine) {
    this.redefine = redefine;
  }

  @Override
  public final GroupModel getRedefine() {
    return redefine;
  }

  @Override
  public final void addMultiplicableModel(final MultiplicableModel multiplicableModel) {
    if (!this.equals(multiplicableModel))
      this.multiplicableModels.add(multiplicableModel);
  }

  @Override
  public final ArrayList<MultiplicableModel> getMultiplicableModels() {
    return multiplicableModels;
  }

  @Override
  public final Occurs getMaxOccurs() {
    return maxOccurs;
  }

  @Override
  public final Occurs getMinOccurs() {
    return minOccurs;
  }

  @Override
  public final void setRef(final GroupModel _ref) {
    this.ref = _ref;
  }

  @Override
  public final GroupModel getRef() {
    return ref;
  }

  @Override
  public boolean isQualified(final boolean nested) {
    return getRef() != null;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public String toString() {
    return super.toString().replace(TO_STRING_DELIMITER, "maxOccurs=\"" + maxOccurs + "\" minOccurs=\"" + minOccurs + "\" ref=\"" + ref + "\"");
  }

  public static final class Reference extends GroupModel implements Referenceable {
    private static final Map<UniqueQName,Reference> all = new HashMap<>();

    protected Reference(final Model parent) {
      super(null, parent);
    }

    public static Reference parseGroup(final UniqueQName name) {
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