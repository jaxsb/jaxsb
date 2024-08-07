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

import static org.junit.Assert.*;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.junit.Test;
import org.openjax.xml.api.ValidationException;
import org.openjax.xml.dom.DOMParsers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BindingValidatorTest {
  /**
   * This test verifies that the correct implementation of the SAXParser is used within the validator. A SAXParser implementation
   * other than the default will cause a ClassCastException stating that {@link BindingEntityResolver} cannot be cast to
   * {@link XMLEntityResolver}.
   *
   * @exception Exception If any {@link Exception} is thrown.
   */
  @Test
  public void testSAXParser() throws Exception {
    System.setProperty("org.xml.sax.driver", SAXParser.class.getName());
    final Document document = DOMParsers.newDocumentBuilder().parse(ClassLoader.getSystemClassLoader().getResourceAsStream("empty.xml"));
    if (document == null)
      fail("document is null");

    final Element element = document.getDocumentElement();
    try {
      new BindingValidator().parse(element);
    }
    catch (final ValidationException e) {
      if (e.getMessage().startsWith(BindingEntityResolver.class.getName() + " cannot be cast to " + XMLEntityResolver.class.getName()))
        fail(e.getMessage());
      else if (e.getCause() == null || e.getCause().getMessage() == null || !e.getCause().getMessage().startsWith("cvc-elt.1.a: Cannot find the declaration of element 'empty'."))
        throw e;
    }
  }
}