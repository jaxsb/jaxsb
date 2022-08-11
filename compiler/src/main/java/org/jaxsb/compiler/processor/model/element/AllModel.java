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

import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.MultiplicableModel;
import org.jaxsb.compiler.schema.attribute.Occurs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class AllModel extends Model implements MultiplicableModel {
  private final ArrayList<MultiplicableModel> multiplicableModels = new ArrayList<>();

  private Occurs maxOccurs = Occurs.parseOccurs("1");
  private Occurs minOccurs = Occurs.parseOccurs("1");

  protected AllModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if ("maxOccurs".equals(attribute.getLocalName()))
        maxOccurs = Occurs.parseOccurs(attribute.getNodeValue());
      else if ("minOccurs".equals(attribute.getLocalName()))
        minOccurs = Occurs.parseOccurs(attribute.getNodeValue());
    }
  }

  @Override
  public void addMultiplicableModel(final MultiplicableModel multiplicableModel) {
    if (!this.equals(multiplicableModel))
      this.multiplicableModels.add(multiplicableModel);
  }

  @Override
  public ArrayList<MultiplicableModel> getMultiplicableModels() {
    return multiplicableModels;
  }

  @Override
  public Occurs getMaxOccurs() {
    return maxOccurs;
  }

  @Override
  public Occurs getMinOccurs() {
    return minOccurs;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public String toString() {
    return super.toString().replace(TO_STRING_DELIMITER, "maxOccurs=\"" + maxOccurs + "\" minOccurs=\"" + minOccurs + "\"");
  }
}