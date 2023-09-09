/* Copyright (c) 2023 JAX-SB
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

import javax.xml.namespace.QName;

import org.jaxsb.runtime.Binding.PrefixToNamespace;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;

abstract class AbstractAttributeAudit {
  static void toString(final StringBuilder str, final PrefixToNamespace prefixToNamespace, final QName name, final boolean qualified, final Object text) {
    if (text == null)
      return;

    str.append(' ');
    if (qualified)
      str.append(prefixToNamespace.getPrefix(name)).append(':');

    str.append(name.getLocalPart());
    str.append("=\"");
    Binding.textToString(str, text, prefixToNamespace);
    str.append('"');
  }

  abstract Object getAttribute();
  abstract AbstractAttributeAudit clone($AnyType<?> owner);
  abstract void toString(StringBuilder str, PrefixToNamespace prefixToNamespace);
}