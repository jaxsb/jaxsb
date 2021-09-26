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

package org.jaxsb.runtime;

public class CompilerFailureException extends BindingRuntimeException {
  public CompilerFailureException() {
    super();
  }

  public CompilerFailureException(final String message) {
    super(message);
  }

  public CompilerFailureException(final Throwable cause) {
    super(cause);
  }

  public CompilerFailureException(final String message, final Throwable cause) {
    super(message, cause);
  }
}