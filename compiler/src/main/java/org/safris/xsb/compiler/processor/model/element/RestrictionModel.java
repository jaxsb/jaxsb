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

package org.safris.xsb.compiler.processor.model.element;

import org.safris.xsb.compiler.lang.LexerFailureException;
import org.safris.xsb.compiler.lang.UniqueQName;
import org.safris.xsb.compiler.processor.model.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class RestrictionModel extends ElementModel {
  private SimpleTypeModel<?> base = null;

  protected RestrictionModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      if ("base".equals(attribute.getLocalName())) {
        final Node parentNode = node.getParentNode();
        if (parentNode.getLocalName().contains("complex"))
          base = ComplexTypeModel.Reference.parseComplexType(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node)));
        else if (parentNode.getLocalName().contains("simple"))
          base = SimpleTypeModel.Reference.parseSimpleType(UniqueQName.getInstance(parseQNameValue(attribute.getNodeValue(), node)));
        else
          throw new LexerFailureException("whoa, schema error?");
      }
    }
  }

  public final void setBase(final SimpleTypeModel<?> base) {
    this.base = base;
  }

  @SuppressWarnings("rawtypes")
  public final SimpleTypeModel getBase() {
    return base;
  }
}