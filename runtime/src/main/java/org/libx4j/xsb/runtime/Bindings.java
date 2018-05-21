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

package org.libx4j.xsb.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.function.Function;

import org.lib4j.xml.dom.DOMs;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class Bindings {
  public static Binding clone(final Binding binding) throws IOException {
    return Bindings.parse(new InputSource(new StringReader(DOMs.domToString(binding.marshal()))));
  }

  /**
   * Marshals a Binding instance to an Element object.
   *
   * @param binding Binding instance to marshal.
   * @return Element DOM object.
   */
  public static Element marshal(final Binding binding) throws MarshalException {
    if (binding.inherits() == null)
      throw new MarshalException("Binding must inherit from an instantiable element or attribute to be marshaled!");

    return binding.marshal();
  }

  /**
   * Parse an Element object to a Binding instance.
   *
   * @param element Element object to parse.
   * @return Binding instance.
   */
  public static Binding parse(final Element element) throws ParseException {
    return parse(element, Thread.currentThread().getContextClassLoader());
  }

  public static Binding parse(final Element element, final ClassLoader classLoader) throws ParseException {
    final Binding binding = Binding.parseElement(element, null, classLoader);
    if (BindingValidator.getSystemValidator() != null)
      BindingValidator.getSystemValidator().validateParse(element);

    return binding;
  }

  /**
   * Parse an URL pointing to xml into a Binding instance.
   *
   * @param url URL pointing to xml.
   * @return Binding instance.
   */
  public static Binding parse(final URL url) throws IOException, ParseException {
    return parse(url, Thread.currentThread().getContextClassLoader());
  }

  public static Binding parse(final URL url, final ClassLoader classLoader) throws IOException, ParseException {
    try (final InputStream in = url.openStream()) {
      return parse(new InputSource(in), classLoader);
    }
  }

  public static Binding parse(final InputStream in) throws IOException, ParseException {
    return parse(in, Thread.currentThread().getContextClassLoader());
  }

  public static Binding parse(final InputStream in, final ClassLoader classLoader) throws IOException, ParseException {
    if (in == null)
      throw new NullPointerException("in == null");

    return parse(new InputSource(in), classLoader);
  }

  /**
   * Parse an InputSource pointing to xml into a Binding instance.
   *
   * @param inputSource InputSource pointing to xml.
   * @return Binding instance.
   */
  public static Binding parse(final InputSource inputSource) throws IOException, ParseException {
    return parse(inputSource, Thread.currentThread().getContextClassLoader());
  }

  public static Binding parse(final InputSource inputSource, final ClassLoader classLoader) throws IOException, ParseException {
    final Element element;
    try {
      element = Binding.newDocumentBuilder().parse(inputSource).getDocumentElement();
    }
    catch (final SAXException e) {
      throw new ParseException(e);
    }

    if (BindingValidator.getSystemValidator() != null)
      BindingValidator.getSystemValidator().validateParse(element);

    return Binding.parseElement(element, null, classLoader);
  }

  public static javax.xml.namespace.QName getTypeName(final Binding binding) {
    final QName name = binding.getClass().getAnnotation(QName.class);
    return name != null ? new javax.xml.namespace.QName(name.namespaceURI(), name.localPart(), name.prefix()) : null;
  }

  public static String getXPath(final Binding binding, final Function<Binding,String> function) {
    final StringBuilder builder = new StringBuilder();
    Binding owner = binding;
    do
      builder.insert(0, "/" + function.apply(owner));
    while ((owner = owner.owner()) != null);
    return builder.toString();
  }
}