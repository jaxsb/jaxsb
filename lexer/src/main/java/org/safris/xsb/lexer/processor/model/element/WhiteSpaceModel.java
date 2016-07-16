/* Copyright (c) 2008 Seva Safris
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

package org.safris.xsb.generator.lexer.processor.model.element;

import org.safris.xsb.generator.lexer.processor.model.Model;
import org.safris.xsb.generator.lexer.processor.model.NamedModel;
import org.safris.xsb.generator.lexer.schema.attribute.Value;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class WhiteSpaceModel extends NamedModel {
  private Boolean fixed = null;
  private Value value = null;

  protected WhiteSpaceModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      if ("fixed".equals(attribute.getLocalName()))
        fixed = Boolean.parseBoolean(attribute.getNodeValue());
      else if ("value".equals(attribute.getLocalName()))
        value = Value.parseValue(attribute.getNodeValue());
    }
  }

  public final Boolean getFixed() {
    return fixed;
  }

  public final Value getValue() {
    return value;
  }
}