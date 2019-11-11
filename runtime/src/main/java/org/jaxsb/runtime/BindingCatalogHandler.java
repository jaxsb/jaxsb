/* Copyright (c) 2019 JAX-SB
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

package org.jaxsb.runtime;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.openjax.xml.sax.SchemaLocation;
import org.openjax.xml.sax.Validator;
import org.openjax.xml.sax.XMLCatalog;
import org.openjax.xml.sax.XMLCatalogHandler;
import org.xml.sax.SAXException;

public class BindingCatalogHandler extends XMLCatalogHandler {
  public BindingCatalogHandler(final String systemId, final Map<String,URL> schemaLocations) throws IOException {
    super(systemId, null, new XMLCatalog() {
      @Override
      public SchemaLocation getSchemaLocation(final String namespaceURI) {
        return new SchemaLocation(namespaceURI) {
          @Override
          public Map<String,URL> getDirectory() {
            return schemaLocations;
          }
        };
      }
    });
  }

  public void validate(final String xml) throws IOException, SAXException {
    try (final StringReader reader = new StringReader(xml)) {
      Validator.validate(new StreamSource(reader), this, null);
    }
  }

  @Override
  public boolean isSchema() {
    return false;
  }
}