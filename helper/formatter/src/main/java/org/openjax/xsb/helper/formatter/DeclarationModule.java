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

public final class DeclarationModule extends FormatModule {
  @Override
  String format(final String formated, String token) {
    if (token.trim().lastIndexOf(";") == token.trim().length() - 1) {
      if (token.indexOf("package") == -1 && token.indexOf("import") == -1 && token.indexOf("public") == -1 && token.indexOf("protected") == -1 && token.indexOf("private") == -1) {
        for (int i = 0; i < getDepth(); i++)
          token = TAB + token;

        if (getLastModule() instanceof CloseBracketModule)
          token = "\n\n" + token;
        else if (getLastModule() instanceof StatementModule)
          token = "\n" + TAB + token + "\n";
        else
          token = "\n" + token;
      }
    }

    return token;
  }
}