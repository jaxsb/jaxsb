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

package org.jaxsb.sample;

import static org.junit.jupiter.api.Assertions.*;

import org.jaxsb.runtime.BindingValidator;
import org.jaxsb.runtime.Bindings;
import org.libj.io.UnsynchronizedStringReader;
import org.openjax.xml.dom.DOMStyle;
import org.openjax.xml.dom.DOMs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public abstract class SampleTest {
  private static final Logger logger = LoggerFactory.getLogger(SampleTest.class);

  static {
    final BindingValidator validator = new BindingValidator();
    BindingValidator.setSystemValidator(validator);
  }

  protected static boolean verifyBinding(final $AnyType<?> anyType) throws Exception {
    boolean success = true;
    final Element element = Bindings.marshal(anyType);
    final String xml = DOMs.domToString(element, DOMStyle.INDENT);
    if (logger.isInfoEnabled()) { logger.info(xml + "\n"); }
    final $AnyType<?> reparsed = Bindings.parse(new InputSource(new UnsynchronizedStringReader(xml)));
    String message = "SUCCESS";
    String not = "---";
    if (!anyType.equals(reparsed)) {
      success = false;
      message = "FAILURE";
      not = "NOT";
    }

    String log = "java -> xml -> java           Object.equals() [" + message + "]";
    log += "\n ^-" + not + "-equal----^\n";

    BindingValidator.getSystemValidator().validate(element);

    log += "\n        xml              Validator.validate() [" + message + "]";
    log += "\n         ^\n";

    message = "SUCCESS";
    not = "---";
    final String xml2 = DOMs.domToString(Bindings.marshal(reparsed), DOMStyle.INDENT);
    if (!xml.equals(xml2)) {
      if (logger.isInfoEnabled()) { logger.info(xml2); }
      success = false;
      message = "FAILURE";
      not = "NOT";
    }

    log += "\njava -> xml -> java -> xml    String.equals() [" + message + "]";
    log += "\n         ^-" + not + "-equal----^\n";
    if (logger.isInfoEnabled()) { logger.info(log); }

    return success;
  }

  public abstract void testSample() throws Exception;
}