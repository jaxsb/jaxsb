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

import org.jaxsb.compiler.processor.model.element.DocumentationModel;
import org.jaxsb.generator.processor.plan.Plan;

public final class DocumentationPlan extends Plan<DocumentationModel> {
  private final String text;
  private String preparedDocumentation;

  public static String getMetaDocumentation() {
    return " * @author Source generated with: <u>org.jaxsb</u>\n";
  }

  public DocumentationPlan(final DocumentationModel model, final Plan<?> parent) {
    super(model, parent);
    this.text = model.getText();
  }

  public String getDocumentation() {
    if (preparedDocumentation != null)
      return preparedDocumentation;

    if (text.trim().length() == 0)
      return preparedDocumentation = "\t/**\n" + getMetaDocumentation() + " */\n";

    String text = this.text;
    text = text.replace("\t", " ");
    text = text.replace("\n\n", "^^^^");
    text = text.replace("\n", " ");
    text = text.replace("^^^^", "\n\n");
    String fixedSpaces;
    do {
      fixedSpaces = text;
      text = fixedSpaces.replace("  ", " ");
    }
    while (!text.equals(fixedSpaces));

    do {
      fixedSpaces = text;
      text = fixedSpaces.replace("\n\n", "\n \n *");
    }
    while (!text.equals(fixedSpaces));

    text = text.trim();
    final StringBuilder formatted = new StringBuilder("\t/**\n");
    int start = 0;
    int end = 0;

    while ((end = text.indexOf(' ', end + 74)) != -1) {
      final int index = text.indexOf('\n', start);
      if (index != -1 && index < end)
        end = index;

      formatted.append("\t * ").append(text, start, end).append('\n');
      start = end + 1;
    }

    preparedDocumentation = formatted + "\t * " + text.substring(start).replace(" * ", "") + "\n\n *\n" + getMetaDocumentation() + "\t */\n";
    preparedDocumentation = preparedDocumentation.replaceAll("\n[ \t]*\n", "\n");
    do {
      fixedSpaces = preparedDocumentation;
      preparedDocumentation = fixedSpaces.replace("* *", "*");
    }
    while (!preparedDocumentation.equals(fixedSpaces));

    do {
      fixedSpaces = preparedDocumentation;
      preparedDocumentation = fixedSpaces.replace(" *\n *\n", " *\n");
    }
    while (!preparedDocumentation.equals(fixedSpaces));

    return preparedDocumentation;
  }
}