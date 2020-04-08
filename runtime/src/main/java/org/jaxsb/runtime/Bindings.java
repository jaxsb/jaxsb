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

import org.openjax.xml.api.ValidationException;
import org.openjax.xml.dom.DOMs;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class Bindings {
  // FIXME: This is so inefficient!
  public static Binding clone(final Binding binding) {
    try {
      return Bindings.parse(new InputSource(new StringReader(DOMs.domToString(binding.marshal()))));
    }
    catch (final IOException | SAXException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Marshals a Binding instance to an Element object.
   *
   * @param binding Binding instance to marshal.
   * @return Element DOM object.
   * @throws MarshalException If the specified binding does not inherit from an
   *           element of attribute.
   */
  public static Element marshal(final Binding binding) throws MarshalException {
    if (binding.inherits() == null)
      throw new MarshalException("Binding must inherit from an instantiable element or attribute to be marshaled");

    return binding.marshal();
  }

  public static Binding parse(final String xml) throws IOException, SAXException {
    return parse(xml, null);
  }

  public static Binding parse(final String xml, final ErrorHandler errorHandler) throws IOException, SAXException {
    try (final ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes())) {
      return parse(new InputSource(in), Thread.currentThread().getContextClassLoader(), errorHandler);
    }
  }

  /**
   * Parse an Element object to a Binding instance.
   *
   * @param element Element object to parse.
   * @return Binding instance.
   * @throws ValidationException If a validation error has occurred.
   * @throws NullPointerException If {@code element} is null.
   */
  public static Binding parse(final Element element) throws ValidationException {
    return parse(element, Thread.currentThread().getContextClassLoader());
  }

  public static Binding parse(final Element element, final ClassLoader classLoader) throws ValidationException {
    final Binding binding = Binding.parseElement(element, null, classLoader);
    if (BindingValidator.getSystemValidator() != null)
      BindingValidator.getSystemValidator().validateParse(element);

    return binding;
  }

  /**
   * Parse an {@link URL} pointing to XML content into a {@link Binding}
   * instance.
   *
   * @param url {@link URL} pointing to XML content.
   * @return {@link Binding} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If a parse error has occurred.
   * @throws ValidationException If a validation error has occurred.
   * @throws NullPointerException If {@code url} is null.
   */
  public static Binding parse(final URL url) throws IOException, SAXException {
    return parse(url, Thread.currentThread().getContextClassLoader());
  }

  /**
   * Parse an {@link URL} pointing to XML content into a {@link Binding}
   * instance.
   *
   * @param url {@link URL} pointing to XML content.
   * @param errorHandler Specify the {@link ErrorHandler} to be used by the
   *          parser. Setting this to null will result in the underlying
   *          implementation using it's own default implementation and behavior.
   * @return {@link Binding} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If a parse error has occurred.
   * @throws ValidationException If a validation error has occurred.
   * @throws NullPointerException If {@code url} is null.
   */
  public static Binding parse(final URL url, final ErrorHandler errorHandler) throws IOException, SAXException {
    return parse(url, Thread.currentThread().getContextClassLoader(), errorHandler);
  }

  public static Binding parse(final URL url, final ClassLoader classLoader) throws IOException, SAXException {
    return parse(url, classLoader, null);
  }

  public static Binding parse(final URL url, final ClassLoader classLoader, final ErrorHandler errorHandler) throws IOException, SAXException {
    try (final InputStream in = url.openStream()) {
      final InputSource inputSource = new InputSource(url.toString());
      inputSource.setByteStream(in);
      return parse(inputSource, classLoader, errorHandler);
    }
  }

  /**
   * Parse an {@link InputSource} pointing to XML content into a {@link Binding}
   * instance.
   *
   * @param inputSource {@link InputSource} pointing to XML content.
   * @return {@link Binding} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If a parse error has occurred.
   * @throws ValidationException If a validation error has occurred.
   * @throws NullPointerException If {@code inputSource} is null.
   */
  public static Binding parse(final InputSource inputSource) throws IOException, SAXException {
    return parse(inputSource, (ErrorHandler)null);
  }

  /**
   * Parse an {@link InputSource} pointing to XML content into a {@link Binding}
   * instance.
   *
   * @param inputSource {@link InputSource} pointing to XML content.
   * @param errorHandler Specify the {@link ErrorHandler} to be used by the
   *          parser. Setting this to null will result in the underlying
   *          implementation using it's own default implementation and behavior.
   * @return {@link Binding} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If a parse error has occurred.
   * @throws ValidationException If a validation error has occurred.
   * @throws NullPointerException If {@code inputSource} is null.
   */
  public static Binding parse(final InputSource inputSource, final ErrorHandler errorHandler) throws IOException, SAXException {
    return parse(inputSource, Thread.currentThread().getContextClassLoader(), errorHandler);
  }

  public static Binding parse(final InputSource inputSource, final ClassLoader classLoader) throws IOException, SAXException {
    return parse(inputSource, classLoader, null);
  }

  public static Binding parse(final InputSource inputSource, final ClassLoader classLoader, final ErrorHandler errorHandler) throws IOException, SAXException {
    final DocumentBuilder builder = Binding.newDocumentBuilder();
    if (errorHandler != null)
      builder.setErrorHandler(errorHandler);

    final Element element = builder.parse(inputSource).getDocumentElement();
    if (BindingValidator.getSystemValidator() != null)
      BindingValidator.getSystemValidator().validateParse(element);

    return Binding.parseElement(element, null, classLoader);
  }

  public static javax.xml.namespace.QName getTypeName(final Binding binding) {
    final QName name = binding.getClass().getAnnotation(QName.class);
    return name != null ? new javax.xml.namespace.QName(name.namespaceURI(), name.localPart(), name.prefix()) : null;
  }

  public static String getXPath(final Binding binding, final Function<? super Binding,String> function) {
    final StringBuilder builder = new StringBuilder();
    Binding owner = binding;
    do
      builder.insert(0, (owner instanceof Attribute ? "/@" : "/") + function.apply(owner));
    while ((owner = owner.owner()) != null);
    return builder.toString();
  }
}