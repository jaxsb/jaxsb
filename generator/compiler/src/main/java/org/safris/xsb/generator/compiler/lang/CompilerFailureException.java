/* Copyright (c) 2006 Seva Safris
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

package org.safris.xsb.generator.compiler.lang;

import org.safris.commons.xml.XMLError;

public final class CompilerError extends XMLError {
  private static final long serialVersionUID = -4619096950067417903L;

  public CompilerError() {
    super();
  }

  public CompilerError(final String message) {
    super(message);
  }

  public CompilerError(final Throwable cause) {
    super(cause);
  }

  public CompilerError(final String message, final Throwable cause) {
    super(message, cause);
  }
}