/* Copyright (c) 2006 OpenJAX
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

package org.openjax.xsb.helper.formatter;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public final class SourceFormat {
  public static SourceFormat getDefaultFormat() {
    return new SourceFormat();
  }

  private List<FormatModule> modules = new LinkedList<>();

  private SourceFormat() {
    addModule(new PackageModule());
    addModule(new DocumentationModule());
    addModule(new ImportModule());
    addModule(new AnnotationModule());
    addModule(new MethodModule());
    addModule(new FieldModule());
    addModule(new ClassModule());
    addModule(new OpenBracketModule());
    addModule(new CloseBracketModule());
    addModule(new DeclarationModule());
    addModule(new StatementModule());
  }

  public void addModule(final FormatModule module) {
    modules.add(module);
  }

  public String format(final String unformated) {
    if (unformated == null)
      return "";

    FormatModule.restetDepth();

    String formated = "";
    final StringTokenizer stringTokenizer = new StringTokenizer(unformated, "\t\n\r\f");
    while (stringTokenizer.hasMoreTokens())
      formated = modules(formated, stringTokenizer.nextToken());

    return formated;
  }

  private String modules(String formated, String token) {
    String formatedToken = token;
    for (int i = 0; i < modules.size(); i++) {
      FormatModule module = modules.get(i);
      token = module.format(formated, token);

      /*if (FormatModule.getLastModule() instanceof OpenBracketModule && !formated.endsWith("\n") && !token.startsWith("\n"))
       {
       for(int j = 0; j < module.getDepth(); j++)
       {
       token = TAB + token;
       }
       token = "\n" + token;
       }*/

      if (!formatedToken.equals(token)) {
        FormatModule.setLastModule(module);
        break;
      }

      formatedToken = token;
    }

    return formated += token;
  }
}