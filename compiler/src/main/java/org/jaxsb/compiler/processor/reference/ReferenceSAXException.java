/* Copyright (c) 2018 JAX-SB
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

package org.jaxsb.compiler.processor.reference;

import java.util.Objects;

import org.xml.sax.SAXException;

public class ReferenceSAXException extends SAXException {
  private static final long serialVersionUID = -1383168276382541459L;

  private final String namespaceURI;
  private final String prefix;

  public ReferenceSAXException(final String namespaceURI, final String prefix) {
    this.namespaceURI = Objects.requireNonNull(namespaceURI);
    this.prefix = Objects.requireNonNull(prefix);
  }

  public String getNamespaceURI() {
    return this.namespaceURI;
  }

  public String getPrefix() {
    return this.prefix;
  }
}