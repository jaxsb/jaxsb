/* Copyright (c) 2006 lib4j
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

package org.safris.xsb.runtime;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;

public interface ComplexType extends BindingType {
  public Iterator<? extends $xs_anySimpleType> attributeIterator();
  public Iterator<? extends Binding> elementIterator();
  public BindingList<? extends Binding> fetchChild(final QName name);
}