/* Copyright (c) 2008 lib4j
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

package org.libx4j.xsb.compiler.processor.reference;

import java.net.URL;

import org.lib4j.xml.NamespaceURI;
import org.libx4j.xsb.compiler.lang.UniqueQName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class SchemaNamespaceHandler extends DefaultHandler {
  private final URL schemaUrl;

  public SchemaNamespaceHandler(final URL schemaUrl) {
    this.schemaUrl = schemaUrl;
  }

  @Override
  public void startElement(final String uri, final String localName, String qName, final Attributes attributes) throws SAXException {
    if (!UniqueQName.XS.getNamespaceURI().toString().equals(uri) || !"schema".equals(localName))
      return;

    final int index = attributes.getIndex("targetNamespace");
    final NamespaceURI namespaceURI = NamespaceURI.getInstance(index != -1 ? attributes.getValue(index) : "");

    String prefix = null;
    for (int i = 0; i < attributes.getLength(); i++) {
      final String name = attributes.getQName(i);
      if (name.startsWith("xmlns:") && namespaceURI.toString().equals(attributes.getValue(i)))
        prefix = name.substring(6);
    }

    if (prefix == null)
      prefix = "";

    throw new SAXException(schemaUrl.hashCode() + "\"" + namespaceURI + "\"" + prefix);
  }
}