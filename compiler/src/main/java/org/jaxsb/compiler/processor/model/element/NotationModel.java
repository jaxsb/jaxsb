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

import java.util.HashMap;
import java.util.Map;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Referenceable;
import org.jaxsb.compiler.processor.model.AliasModel;
import org.jaxsb.compiler.processor.model.Model;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NotationModel extends AliasModel {
  private String _public;
  private String system;

  protected NotationModel(final Node node, final Model parent) {
    super(node, parent);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = attributes.item(i);
      if ("public".equals(attribute.getLocalName()))
        _public = attribute.getNodeValue();
      else if ("system".equals(attribute.getLocalName()))
        system = attribute.getNodeValue();
    }
  }

  public final String getSystem() {
    return system;
  }

  public final String getPublic() {
    return _public;
  }

  public static final class Reference extends NotationModel implements Referenceable {
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