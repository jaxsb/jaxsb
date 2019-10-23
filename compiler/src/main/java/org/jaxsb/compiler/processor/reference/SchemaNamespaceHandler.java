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

package org.jaxsb.compiler.processor.reference;

import org.libj.util.BiMap;
import org.libj.util.HashBiMap;
import org.jaxsb.compiler.lang.NamespaceURI;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class SchemaNamespaceHandler extends DefaultHandler {
  private final BiMap<String,String> xmlns = new HashBiMap<>();

  @Override
  public void startElement(final String uri, String localName, final String qName, final Attributes attributes) throws SAXException {
    if (xmlns.isEmpty()) {
      for (int i = 0; i < attributes.getLength(); i++) {
        final String name = attributes.getQName(i);
        if (name.startsWith("xmlns")) {
          final int colon = name.indexOf(':');
          xmlns.put(colon == -1 ? "" : name.substring(colon + 1), attributes.getValue(i));
        }
      }
    }

    final int colon = qName.indexOf(':');
    final String prefix;
    if (colon != -1) {
      prefix = qName.substring(0, colon);
      localName = qName.substring(colon + 1);
    }
    else {
      prefix = "";
      localName = qName;
    }

    if (!"schema".equals(localName))
      return;

    if (!NamespaceURI.XS.getNamespaceURI().equals(this.xmlns.get(prefix)))
      return;

    final int index = attributes.getIndex("targetNamespace");
    final String namespaceURI = index != -1 ? attributes.getValue(index) : "";
    final String nsPrefix = xmlns.reverse().get(namespaceURI);
    throw new ReferenceSAXException(namespaceURI, nsPrefix != null ? nsPrefix : "");
  }
}