/* Copyright (c) 2006 JAX-SB
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

package org.jaxsb.generator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.jaxsb.compiler.processor.document.SchemaDocument;
import org.jaxsb.compiler.processor.reference.SchemaReference;
import org.jaxsb.runtime.CompilerFailureException;
import org.libj.net.URLConnections;
import org.openjax.xml.dom.DOMParsers;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class AbstractGenerator {
  private static final Map<String,SchemaDocument> parsedDocuments = new HashMap<>();

  public static SchemaDocument parse(final SchemaReference schemaReference) throws IOException {
    try {
      final URL url = schemaReference.getURL();
      final DocumentBuilder documentBuilder = DOMParsers.newDocumentBuilder();
      final Document document;
      try (final InputStream in = URLConnections.checkFollowRedirect(url.openConnection()).getInputStream()) {
        document = documentBuilder.parse(in);
      }

      final SchemaDocument parsedDocument = new SchemaDocument(schemaReference, document);
      // FIXME: String thrashing...
      parsedDocuments.put(schemaReference.getNamespaceURI().toString() + url, parsedDocument);
      return parsedDocument;
    }
    catch (final SAXException e) {
      throw new CompilerFailureException(e);
    }
  }
}