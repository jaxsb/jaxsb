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

package org.jaxsb.generator.processor.plan.element;

import java.util.Arrays;

import javax.xml.namespace.QName;

import org.jaxsb.compiler.processor.model.element.EnumerationModel;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.runtime.CompilerFailureException;
import org.libj.lang.Strings;

public final class EnumerationPlan extends Plan<EnumerationModel> {
  private static final char[] illegalChars = {' ', '!', '"', '#', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '.', '/', ':', ';', '<', '=', '>', '?', '@', '@', '[', '\\', ']', '^', '_', '{', '|', '|', '}', '~'};
  private static final String[] illegalWords = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"};

  private String declarationName;
  private final QName value;

  public static String getDeclarationName(final QName value) {
    char ch = value.getLocalPart().charAt(0);
    final StringBuilder builder = new StringBuilder();
    if (47 < ch && ch < 58)
      builder.append('_');

    builder.append(value.getLocalPart());
    if (value.getPrefix().length() != 0)
      builder.insert(0, value.getPrefix() + "_");

    if (Arrays.binarySearch(illegalWords, builder.toString()) >= 0)
      builder.insert(0, "_");

    final String string = builder.toString();
    builder.setLength(0);
    for (int i = 0, i$ = string.length(); i < i$; ++i) { // [N]
      ch = string.charAt(i);
      builder.append(Arrays.binarySearch(illegalChars, ch) >= 0 ? "_" + Strings.toUTF8Literal(ch).substring(2, 4).toUpperCase() : ch);
    }

    return builder.toString();
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