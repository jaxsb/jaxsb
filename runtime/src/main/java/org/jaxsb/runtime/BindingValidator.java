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

package org.jaxsb.runtime;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.libj.io.UnsynchronizedStringReader;
import org.libj.net.MemoryURLStreamHandler;
import org.openjax.xml.api.ValidationException;
import org.openjax.xml.dom.DOMs;
import org.openjax.xml.dom.Validator;
import org.openjax.xml.sax.CachedInputSource;
import org.openjax.xml.sax.XmlCatalog;
import org.openjax.xml.sax.XmlEntity;
import org.openjax.xml.sax.XmlPreview;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class BindingValidator extends Validator {
  private static BindingValidator validator;

  public static BindingValidator getSystemValidator() {
    return validator;
  }

  public static void setSystemValidator(final BindingValidator validator) {
    BindingValidator.validator = validator;
  }

  private final Map<String,URL> schemaReferences = new HashMap<>();
  private boolean validateOnMarshal;
  private boolean validateOnParse;

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

  private static final class BindingXmlCatalog extends XmlCatalog {
    /**
     * Creates a new {@link BindingXmlCatalog} with the specified {@link URL} and {@link CachedInputSource}.
     *
     * @param location The {@link URL}.
     * @param inputSource The {@link CachedInputSource}.
     * @throws NullPointerException If the specified {@link URL} or {@link CachedInputSource} is null.
     */
    private BindingXmlCatalog(final URL location, final CachedInputSource inputSource) {
      super(location, inputSource);
    }

    @Override
    public XmlEntity getEntity(final String uri) throws IOException {
      final URL url = BindingEntityResolver.schemaReferences.get(uri);
      return url == null ? null : new XmlEntity(url, new CachedInputSource(null, uri, null, url.openStream()));
    }
  }

  @Override
  protected void parse(final Element element) throws IOException, ValidationException {
    final String xml = DOMs.domToString(element);
    final URL url = MemoryURLStreamHandler.createURL(xml.getBytes());
    try (final CachedInputSource inputSource = new CachedInputSource(null, String.valueOf(System.identityHashCode(element)), null, new UnsynchronizedStringReader(xml))) {
      final XmlPreview preview = new XmlPreview(new BindingXmlCatalog(url, inputSource), false, false, null, null, BindingEntityResolver.schemaReferences, null) {
        @Override
        public boolean isSchema() {
          return false;
        }
      };
      org.openjax.xml.sax.Validator.validate(inputSource, preview, null);
    }
    catch (final IOException e) {
      throw e;
    }
    catch (final SAXException e) {
      throw new ValidationException("\n" + e.getMessage() + "\n" + xml, e);
    }
  }

  public void validateMarshal(final Element element) throws MarshalException {
    if (validateOnMarshal) {
      try {
        validate(element);
      }
      catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
      catch (final ValidationException e) {
        throw new MarshalException(e);
      }
    }
  }

  public void validateParse(final Element element) throws ValidationException {
    if (validateOnParse) {
      try {
        validate(element);
      }
      catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }
}