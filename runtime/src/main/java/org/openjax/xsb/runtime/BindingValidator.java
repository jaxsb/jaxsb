/* Copyright (c) 2008 OpenJAX
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

package org.openjax.xsb.runtime;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.fastjax.xml.ValidationException;
import org.fastjax.xml.dom.DOMs;
import org.fastjax.xml.dom.Validator;
import org.fastjax.xml.sax.SchemaLocation;
import org.fastjax.xml.sax.XMLCatalog;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class BindingValidator extends Validator {
  private static BindingValidator validator = null;

  public static BindingValidator getSystemValidator() {
    return validator;
  }

  public static void setSystemValidator(final BindingValidator validator) {
    BindingValidator.validator = validator;
  }

  private final Map<String,URL> schemaReferences = new HashMap<>();
  private boolean validateOnMarshal = false;
  private boolean validateOnParse = false;

  public void setValidateOnMarshal(final boolean validateOnMarshal) {
    this.validateOnMarshal = validateOnMarshal;
  }

  public boolean isValidateOnMarshal() {
    return validateOnMarshal;
  }

  public void setValidateOnParse(final boolean validateOnParse) {
    this.validateOnParse = validateOnParse;
  }

  public boolean isValidateOnParse() {
    return validateOnParse;
  }

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
    final String output = DOMs.domToString(element);
    try {
      org.fastjax.xml.sax.Validator.validate(new StreamSource(new StringReader(output)), new XMLCatalog() {
        @Override
        public SchemaLocation getSchemaLocation(final String namespaceURI) {
          return new SchemaLocation(namespaceURI) {
            @Override
            public Map<String,URL> getDirectory() {
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

  public void validateMarshal(final Element element) {
    if (validateOnMarshal) {
      try {
        validate(element);
      }
      catch (final ValidationException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void validateParse(final Element element) throws ValidationException {
    if (validateOnParse)
      validate(element);
  }
}