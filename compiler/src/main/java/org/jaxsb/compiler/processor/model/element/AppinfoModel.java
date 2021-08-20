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

import org.jaxsb.compiler.processor.model.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class AppinfoModel extends Model {
  private String source;

  protected AppinfoModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, len = attributes.getLength(); i < len; ++i) {
      final Node attribute = attributes.item(i);
      if ("source".equals(attribute.getLocalName()))
        source = attribute.getNodeValue();
    }
  }

  public String getSource() {
    return source;
  }

  @Override
  public String toString() {
    return super.toString().replace(TO_STRING_DELIMITER, "source=\"" + source + "\"");
  }
}