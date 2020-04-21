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

package org.jaxsb.generator;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jaxsb.runtime.Binding;
import org.jaxsb.runtime.Bindings;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class ParseMarshalITest {
  private static final Logger logger = LoggerFactory.getLogger(ParseMarshalITest.class);

  @Test
  public void testAttribute() throws IOException, SAXException {
    final Binding binding = Bindings.parse(ClassLoader.getSystemClassLoader().getResource("attribute.xml"));
    final String xml = binding.toString();
    logger.info(xml);

    final Binding b2 = Bindings.parse(xml);
    assertEquals(binding, b2);
  }

  @Test
  public void testAnyAttribute() throws IOException, SAXException {
    final Binding binding = Bindings.parse(ClassLoader.getSystemClassLoader().getResource("anyAttribute.xml"));
    final String xml = binding.toString();
    logger.info(xml);

    final Binding b2 = Bindings.parse(xml);
    assertEquals(binding, b2);
  }
}