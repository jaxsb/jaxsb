/* Copyright (c) 2017 JAX-SB
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

package org.jaxsb;

import java.io.IOException;

import org.jaxsb.runtime.Bindings;
import org.jaxsb.www._do._abstract.n$a_m_e_1.xJ8jmj5CVYwlGMJSA.Class;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class NameTest {
  private static final Logger logger = LoggerFactory.getLogger(NameTest.class);

  @Test
  public void test() throws IOException, SAXException {
    final Class name = (Class)Bindings.parse(ClassLoader.getSystemClassLoader().getResource("name.xml"));
    if (logger.isInfoEnabled()) logger.info(name.toString());
  }
}