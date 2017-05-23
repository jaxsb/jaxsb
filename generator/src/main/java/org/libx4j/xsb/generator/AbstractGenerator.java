/* Copyright (c) 2006 lib4j
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

package org.libx4j.xsb.generator;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.lib4j.net.URLs;
import org.libx4j.xsb.compiler.processor.document.SchemaDocument;
import org.libx4j.xsb.compiler.processor.reference.SchemaReference;
import org.libx4j.xsb.runtime.BindingError;
import org.libx4j.xsb.runtime.CompilerFailureException;
import org.safris.commons.xml.dom.DOMParsers;
import org.w3c.dom.Document;

public abstract class AbstractGenerator {
  private static final Map<String,SchemaDocument> parsedDocuments = new HashMap<String,SchemaDocument>();

  public static SchemaDocument parse(final SchemaReference schemaReference) {
    URL url = null;
    SchemaDocument parsedDocument = null;
    Document document = null;
    try {
      url = URLs.canonicalizeURL(schemaReference.getURL());
      final DocumentBuilder documentBuilder = DOMParsers.newDocumentBuilder();
      document = documentBuilder.parse(url.toURI().toString());
    }
    catch (final FileNotFoundException e) {
      throw new BindingError(e.getMessage());
    }
    catch (final Exception e) {
      throw new CompilerFailureException(e);
    }

    parsedDocument = new SchemaDocument(schemaReference, document);
    parsedDocuments.put(schemaReference.getNamespaceURI() + url.toString(), parsedDocument);
    return parsedDocument;
  }
}