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

package org.safris.xsb.lexer.processor.model.element;

import javax.xml.namespace.QName;

import org.safris.xsb.lexer.processor.model.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class EnumerationModel extends Model {
  private QName value = null;

  protected EnumerationModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      if ("value".equals(attribute.getLocalName()))
        value = parseQNameValue(attribute.getNodeValue(), node);
    }
  }

  public EnumerationModel(final QName value) {
    super(null, null);
    this.value = value;
  }

  public final QName getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof EnumerationModel))
      return false;

    final EnumerationModel that = (EnumerationModel)obj;
    return (getValue() == null && that.getValue() == null) || (getValue() != null && getValue().equals(that.getValue()));
  }

  @Override
  public int hashCode() {
    return getValue().hashCode();
  }
}