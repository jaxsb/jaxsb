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

package org.jaxsb.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;

import org.libj.lang.Assertions;
import org.openjax.xml.api.ValidationException;
import org.openjax.xml.dom.Documents;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class Bindings {
  // FIXME: This is so inefficient!
  public static $AnyType<?> clone(final $AnyType<?> anyType) {
    try {
      return Bindings.parse(new InputSource(new StringReader(anyType.toString())));
    }
    catch (final IOException | SAXException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Marshals an {@link $AnyType} instance to an Element object.
   *
   * @param anyType {@link $AnyType} instance to marshal.
   * @return THe {@link Element} DOM object.
   * @throws MarshalException If the specified binding does not inherit from an
   *           element of attribute.
   */
  public static Element marshal(final $AnyType<?> anyType) throws MarshalException {
    if (anyType.inherits() == null)
      throw new MarshalException("$AnyType<?> must inherit from an instantiable element or attribute to be marshaled");

    return anyType.marshal();
  }

  /**
   * Parse an Element object to an {@link $AnyType} instance.
   *
   * @param element {@link Element} object to parse.
   * @return The {@link $AnyType} instance.
   * @throws ValidationException If a validation error has occurred.
   * @throws IllegalArgumentException If {@code element} is null.
   */
  public static $AnyType<?> parse(final Element element) throws ValidationException {
    return parse(Assertions.assertNotNull(element), Thread.currentThread().getContextClassLoader());
  }

  public static $AnyType<?> parse(final Element element, final ClassLoader classLoader) throws ValidationException {
    final $AnyType<?> binding = Binding.parseElement(element, null, classLoader);
    if (BindingValidator.getSystemValidator() != null)
      BindingValidator.getSystemValidator().validateParse(element);

    return binding;
  }

  public static $AnyType<?> parse(final String xml) throws IOException, SAXException {
    return parse(xml, null, null);
  }

  public static $AnyType<?> parse(final String xml, final String defaultNamespace) throws IOException, SAXException {
    return parse(xml, defaultNamespace, null);
  }

  public static $AnyType<?> parse(final String xml, final ErrorHandler errorHandler) throws IOException, SAXException {
    return parse(xml, null, errorHandler);
  }

  public static $AnyType<?> parse(final String xml, final String defaultNamespace, final ErrorHandler errorHandler) throws IOException, SAXException {
    try (final ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes())) {
      return parse(new InputSource(in), defaultNamespace, Thread.currentThread().getContextClassLoader(), errorHandler);
    }
  }

  /**
   * Parse an {@link URL} pointing to XML content into an {@link $AnyType}
   * instance.
   *
   * @param url {@link URL} pointing to XML content.
   * @return The {@link $AnyType} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If a parse error has occurred.
   * @throws ValidationException If a validation error has occurred.
   * @throws IllegalArgumentException If {@code url} is null.
   */
  public static $AnyType<?> parse(final URL url) throws IOException, SAXException {
    return parse(Assertions.assertNotNull(url), null, Thread.currentThread().getContextClassLoader());
  }

  public static $AnyType<?> parse(final URL url, final String defaultNamespace) throws IOException, SAXException {
    return parse(url, defaultNamespace, Thread.currentThread().getContextClassLoader());
  }

  /**
   * Parse an {@link URL} pointing to XML content into an {@link $AnyType}
   * instance.
   *
   * @param url {@link URL} pointing to XML content.
   * @param errorHandler Specify the {@link ErrorHandler} to be used by the
   *          parser. Setting this to null will result in the underlying
   *          implementation using it's own default implementation and behavior.
   * @return The {@link $AnyType} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If a parse error has occurred.
   * @throws ValidationException If a validation error has occurred.
   * @throws IllegalArgumentException If {@code url} is null.
   */
  public static $AnyType<?> parse(final URL url, final ErrorHandler errorHandler) throws IOException, SAXException {
    return parse(Assertions.assertNotNull(url), null, Thread.currentThread().getContextClassLoader(), errorHandler);
  }

  public static $AnyType<?> parse(final URL url, final String defaultNamespace, final ErrorHandler errorHandler) throws IOException, SAXException {
    return parse(url, defaultNamespace, Thread.currentThread().getContextClassLoader(), errorHandler);
  }

  public static $AnyType<?> parse(final URL url, final ClassLoader classLoader) throws IOException, SAXException {
    return parse(url, null, classLoader, null);
  }

  public static $AnyType<?> parse(final URL url, final String defaultNamespace, final ClassLoader classLoader) throws IOException, SAXException {
    return parse(url, defaultNamespace, classLoader, null);
  }

  public static $AnyType<?> parse(final URL url, final String defaultNamespace, final ClassLoader classLoader, final ErrorHandler errorHandler) throws IOException, SAXException {
    try (final InputStream in = url.openStream()) {
      final InputSource inputSource = new InputSource(url.toString());
      inputSource.setByteStream(in);
      return parse(inputSource, defaultNamespace, classLoader, errorHandler);
    }
  }

  public static $AnyType<?> parse(final URL url, final ClassLoader classLoader, final ErrorHandler errorHandler) throws IOException, SAXException {
    return parse(url, null, classLoader, errorHandler);
  }

  /**
   * Parse an {@link InputSource} pointing to XML content into an
   * {@link $AnyType} instance.
   *
   * @param inputSource {@link InputSource} pointing to XML content.
   * @return The {@link $AnyType} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If a parse error has occurred.
   * @throws ValidationException If a validation error has occurred.
   * @throws IllegalArgumentException If {@code inputSource} is null.
   */
  public static $AnyType<?> parse(final InputSource inputSource) throws IOException, SAXException {
    return parse(Assertions.assertNotNull(inputSource), null, (ErrorHandler)null);
  }

  public static $AnyType<?> parse(final InputSource inputSource, final String defaultNamespace) throws IOException, SAXException {
    return parse(inputSource, defaultNamespace, (ErrorHandler)null);
  }

  /**
   * Parse an {@link InputSource} pointing to XML content into an
   * {@link $AnyType} instance.
   *
   * @param inputSource {@link InputSource} pointing to XML content.
   * @param errorHandler Specify the {@link ErrorHandler} to be used by the
   *          parser. Setting this to null will result in the underlying
   *          implementation using it's own default implementation and behavior.
   * @return The {@link $AnyType} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If a parse error has occurred.
   * @throws ValidationException If a validation error has occurred.
   * @throws IllegalArgumentException If {@code inputSource} is null.
   */
  public static $AnyType<?> parse(final InputSource inputSource, final ErrorHandler errorHandler) throws IOException, SAXException {
    return parse(Assertions.assertNotNull(inputSource), null, Thread.currentThread().getContextClassLoader(), errorHandler);
  }

  public static $AnyType<?> parse(final InputSource inputSource, final String defaultNamespace, final ErrorHandler errorHandler) throws IOException, SAXException {
    return parse(inputSource, defaultNamespace, Thread.currentThread().getContextClassLoader(), errorHandler);
  }

  public static $AnyType<?> parse(final InputSource inputSource, final ClassLoader classLoader) throws IOException, SAXException {
    return parse(inputSource, null, classLoader, null);
  }

  public static $AnyType<?> parse(final InputSource inputSource, final String defaultNamespace, final ClassLoader classLoader) throws IOException, SAXException {
    return parse(inputSource, defaultNamespace, classLoader, null);
  }

  public static $AnyType<?> parse(final InputSource inputSource, final String defaultNamespace, final ClassLoader classLoader, final ErrorHandler errorHandler) throws IOException, SAXException {
    final DocumentBuilder builder = Binding.newDocumentBuilder();
    if (errorHandler != null)
      builder.setErrorHandler(errorHandler);

    final Document document = builder.parse(inputSource);
    if (defaultNamespace != null && document.isDefaultNamespace(null))
      Documents.setNamespaceURI(document, defaultNamespace, false);

    final Element element = document.getDocumentElement();

    if (BindingValidator.getSystemValidator() != null)
      BindingValidator.getSystemValidator().validateParse(element);

    return Binding.parseElement(element, null, classLoader);
  }

  public static javax.xml.namespace.QName getTypeName(final $AnyType<?> anyType) {
    final QName name = anyType.getClass().getAnnotation(QName.class);
    return name != null ? new javax.xml.namespace.QName(name.namespaceURI(), name.localPart(), name.prefix()) : null;
  }

  public static String getXPath(final $AnySimpleType<?> binding, final Function<? super $AnySimpleType<?>,String> function) {
    final StringBuilder builder = new StringBuilder();
    $AnySimpleType<?> owner = binding;
    do
      builder.insert(0, (owner instanceof Attribute ? "/@" : "/") + function.apply(owner));
    while ((owner = owner.owner()) != null);
    return builder.toString();
  }
}