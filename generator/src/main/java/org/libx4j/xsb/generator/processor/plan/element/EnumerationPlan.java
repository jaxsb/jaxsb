/* Copyright (c) 2008 lib4j
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

package org.libx4j.xsb.generator.processor.plan.element;

import java.util.Arrays;

import javax.xml.namespace.QName;

import org.lib4j.lang.Strings;
import org.libx4j.xsb.compiler.processor.model.element.EnumerationModel;
import org.libx4j.xsb.generator.processor.plan.Plan;
import org.libx4j.xsb.runtime.CompilerFailureException;

public final class EnumerationPlan extends Plan<EnumerationModel> {
  private static final char[] illegalChars = {' ', '!', '"', '#', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '.', '/', ':', ';', '<', '=', '>', '?', '@', '@', '[', '\\', ']', '^', '_', '{', '|', '|', '}', '~'};
  private static final String[] illegalWords = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"};

  private String declarationName = null;
  private final QName value;

  public static String getDeclarationName(final QName value) {
    String string = null;
    if (47 < value.getLocalPart().charAt(0) && value.getLocalPart().charAt(0) < 58)
      string = "_" + value.getLocalPart();
    else
      string = value.getLocalPart();

    if (value.getPrefix() != null && value.getPrefix().toString().length() != 0)
      string = value.getPrefix() + "_" + string;

    if (Arrays.binarySearch(illegalWords, string) >= 0)
      string = "_" + string;

    final StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      buffer.append(Arrays.binarySearch(illegalChars, ch) >= 0 ? "_" + Strings.toUTF8Literal(ch).substring(2, 4) : ch);
    }

    return buffer.toString();
  }

  public EnumerationPlan(final EnumerationModel model, final Plan<?> parent) {
    super(model, parent);
    this.value = model.getValue();
  }

  public QName getValue() {
    return value;
  }

  public String getDeclarationName() {
    if (declarationName != null)
      return declarationName;

    if (getModel().getValue().getLocalPart().length() == 0)
      throw new CompilerFailureException("The localPart of this enumeration cannot be length == 0");

    return declarationName = getDeclarationName(getModel().getValue());
  }
}