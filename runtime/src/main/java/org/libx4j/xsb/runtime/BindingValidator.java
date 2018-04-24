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

package org.libx4j.xsb.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.lib4j.net.CachedURL;
import org.lib4j.xml.dom.DOMs;
import org.lib4j.xml.dom.Validator;
import org.lib4j.xml.sax.SchemaLocation;
import org.lib4j.xml.sax.XMLCatalog;
import org.lib4j.xml.validate.ValidationException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class BindingValidator extends Validator {
  private final Map<String,URL> schemaReferences = new HashMap<String,URL>();

  @Override
  protected URL lookupSchemaLocation(final String namespaceURI) {
    return schemaReferences.get(namespaceURI);
  }

  @Override
  protected CachedURL getSchemaLocation(final String namespaceURI) {
    return BindingEntityResolver.lookupSchemaLocation(namespaceURI);
  }

  @Override
  protected void parse(final Element element) throws IOException, ValidationException {
    final String output = DOMs.domToString(element);
    try {
      org.lib4j.xml.sax.Validator.validate(new StreamSource(new ByteArrayInputStream(output.getBytes())), new XMLCatalog() {
        @Override
        public SchemaLocation getSchemaLocation(final String namespaceURI) {
          return new SchemaLocation(namespaceURI) {
            @Override
            public Map<String,CachedURL> getDirectory() {
              return BindingEntityResolver.schemaReferences;
            }
          };
        }
      }, false, null);
    }
    catch (final IOException e) {
      throw e;
    }
    catch (final SAXException e) {
      throw new ValidationException("\n" + e.getMessage() + "\n" + output, e);
    }
  }
}