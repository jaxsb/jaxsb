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

package org.jaxsb.generator;

import java.net.MalformedURLException;

import org.junit.Test;

public class GeneratorTest {
  @Test
  public void testHtml() throws MalformedURLException {
    Generator.main(new String[] {"--overwrite", "--compile", "target/test-classes", "-d", "target/generated-test-sources/jaxsb", "src/test/resources/html.xsd"});
  }

  @Test
  public void testName() throws MalformedURLException {
    Generator.main(new String[] {"--overwrite", "--compile", "target/test-classes", "-d", "target/generated-test-sources/jaxsb", "src/test/resources/name1.xsd"});
  }

  @Test
  public void testAttribute() throws MalformedURLException {
    Generator.main(new String[] {"--overwrite", "--compile", "target/test-classes", "-d", "target/generated-test-sources/jaxsb", "src/test/resources/attribute.xsd"});
  }

  @Test
  public void testAnyAttribute() throws MalformedURLException {
    Generator.main(new String[] {"--overwrite", "--compile", "target/test-classes", "-d", "target/generated-test-sources/jaxsb", "src/test/resources/anyAttribute.xsd"});
  }
}