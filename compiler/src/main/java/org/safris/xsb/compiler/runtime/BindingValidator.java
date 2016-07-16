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

package org.safris.xsb.compiler.runtime;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.xml.dom.DOMs;
import org.safris.commons.xml.sax.SAXFeature;
import org.safris.commons.xml.sax.SAXParser;
import org.safris.commons.xml.sax.SAXParsers;
import org.safris.commons.xml.sax.SAXProperty;
import org.safris.commons.xml.validator.ValidationException;
import org.safris.commons.xml.validator.Validator;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public final class BindingValidator extends Validator {
  private final Map<String,URL> schemaReferences = new HashMap<String,URL>();

  @Override
  protected URL lookupSchemaLocation(final String namespaceURI) {
    return schemaReferences.get(namespaceURI);
  }

  @Override
  protected URL getSchemaLocation(final String namespaceURI) {
    return BindingEntityResolver.lookupSchemaLocation(namespaceURI);
  }

  @Override
  protected void parse(final Element element) throws IOException, ValidationException {
    final SAXParser saxParser;
    try {
      saxParser = SAXParsers.createParser();

      saxParser.setFeature(SAXFeature.CONTINUE_AFTER_FATAL_ERROR, true);
      saxParser.setFeature(SAXFeature.DYNAMIC_VALIDATION, true);
      saxParser.setFeature(SAXFeature.NAMESPACE_PREFIXES, true);
      saxParser.setFeature(SAXFeature.NAMESPACES, true);
      saxParser.setFeature(SAXFeature.SCHEMA_VALIDATION, true);
      saxParser.setFeature(SAXFeature.VALIDATION, true);

      saxParser.setProptery(SAXProperty.SCHEMA_LOCATION, "http://www.w3.org/2001/XMLSchema http://www.w3.org/2001/XMLSchema.xsd");
      saxParser.setProptery(SAXProperty.ENTITY_RESOLVER, new BindingEntityResolver());

      saxParser.setErrorHandler(BindingErrorHandler.getInstance());
    }
    catch (final Exception e) {
      throw new ValidationException(e);
    }

    final String output = DOMs.domToString(element);
    try {
      saxParser.parse(new InputSource(new StringReader(output)));
    }
    catch (final IOException e) {
      throw e;
    }
    catch (final Exception e) {
      throw new ValidationException("\n" + e.getMessage() + "\n" + output, e);
    }
  }
}