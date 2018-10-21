/* Copyright (c) 2006 OpenJAX
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

package org.openjax.xsb.generator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.fastjax.net.URLs;
import org.fastjax.xml.dom.DOMParsers;
import org.openjax.xsb.compiler.processor.document.SchemaDocument;
import org.openjax.xsb.compiler.processor.reference.SchemaReference;
import org.openjax.xsb.runtime.CompilerFailureException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class AbstractGenerator {
  private static final Map<String,SchemaDocument> parsedDocuments = new HashMap<>();

  public static SchemaDocument parse(final SchemaReference schemaReference) throws IOException {
    try {
      final URL url = URLs.canonicalize(schemaReference.getURL());
      final DocumentBuilder documentBuilder = DOMParsers.newDocumentBuilder();
      final Document document = documentBuilder.parse(url.toURI().toString());
      final SchemaDocument parsedDocument = new SchemaDocument(schemaReference, document);
      parsedDocuments.put(schemaReference.getNamespaceURI() + url.toString(), parsedDocument);
      return parsedDocument;
    }
    catch (final SAXException | URISyntaxException e) {
      throw new CompilerFailureException(e);
    }
  }
}