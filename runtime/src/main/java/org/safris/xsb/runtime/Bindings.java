/* Copyright (c) 2006 Seva Safris
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

package org.safris.xsb.runtime;

import java.io.StringReader;

import org.safris.commons.xml.XMLException;
import org.safris.commons.xml.dom.DOMs;
import org.safris.commons.xml.dom.Validator;
import org.safris.commons.xml.validate.ValidationException;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public abstract class Bindings {
  public static Binding clone(final Binding binding) throws XMLException {
    return Bindings.parse(new InputSource(new StringReader(DOMs.domToString(binding.marshal()))));
  }

  /**
   * Marshals a Binding instance to an Element object.
   *
   * @param binding Binding instance to marshal.
   * @return Element DOM object.
   */
  public static Element marshal(final Binding binding) throws MarshalException, ValidationException {
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
  public static Binding parse(final Element element) throws ParseException, ValidationException {
    final Binding binding = Binding.parseElement(element, null);
    if (Validator.getSystemValidator() != null)
      Validator.getSystemValidator().validateParse(element);

    return binding;
  }

  /**
   * Parse an InputSource pointing to xml into a Binding instance.
   *
   * @param inputSource InputSource pointing to xml.
   * @return Binding instance.
   */
  public static Binding parse(final InputSource inputSource) throws ParseException, ValidationException {
    final Element element;
    try {
      element = Binding.newDocumentBuilder().parse(inputSource).getDocumentElement();
    }
    catch (final Exception e) {
      throw new ParseException(e);
    }

    if (Validator.getSystemValidator() != null)
      Validator.getSystemValidator().validateParse(element);

    return Binding.parseElement(element, null);
  }

  public static javax.xml.namespace.QName getTypeName(final Binding binding) {
    final QName name = binding.getClass().getAnnotation(QName.class);
    return name != null ? new javax.xml.namespace.QName(name.namespaceURI(), name.localPart(), name.prefix()) : null;
  }
}