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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

public class XIncludeSpecTest {
  private static final File destDir = new File("target/generated-test-sources/jaxsb");
  private static final String xIncludePath = "org/w3/www/_2001/XInclude/yAA.java";

  private static String removeGeneratedAnnotation(final byte[] bytes) {
    final StringBuilder code = new StringBuilder(new String(bytes).trim());
    final int start = code.indexOf("@javax.annotation.Generated(");
    final int end = code.indexOf("\")", start + 27) + 2;
    code.delete(start, end);
    return code.toString();
  }

  private static void assertEquals(final Path expected, final Path actual) throws IOException {
    final String expectedCode = removeGeneratedAnnotation(Files.readAllBytes(expected));
    final String actualCode = removeGeneratedAnnotation(Files.readAllBytes(actual));
    Assert.assertEquals(expectedCode, actualCode);
  }

  @Test
  public void test() throws IOException {
    final URL url = ClassLoader.getSystemClassLoader().getResource("XInclude.xsd");
    Generator.main(new String[] {"--overwrite", "--skip-xsd", "-d", destDir.getAbsolutePath(), url.toString()});

    final File testFile = new File(destDir, xIncludePath);
    final File controlFile = new File("../runtime/src/main/java/", xIncludePath);
    assertEquals(controlFile.toPath(), testFile.toPath());
  }
}