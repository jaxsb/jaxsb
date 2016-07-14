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

package org.safris.xml.toolkit.test.binding;

import com.safris.schema.test.$te_complexD;
import com.safris.schema.test.te_elemD;
import java.io.StringReader;
import org.junit.Assert;
import org.junit.Test;
import org.safris.commons.lang.Strings;
import org.safris.commons.xml.dom.DOMStyle;
import org.safris.commons.xml.dom.DOMs;
import org.safris.xml.generator.compiler.runtime.Binding;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.safris.xml.toolkit.test.binding.regression.Metadata;
import org.xml.sax.InputSource;

public class TypeTest extends Metadata {
  private static final String DEFAULT_HOST = "aol-3";
  private static final String DEFAULT_dOMAIN = "liberty-iop.biz";
  private static String host = DEFAULT_HOST;
  private static String domain = DEFAULT_dOMAIN;

  @Test
  public void testType() throws Exception {
    $te_complexD xsiType = new $te_complexD() {
      protected $te_complexD inherits() {
        return null;
      }
    };
    xsiType.set_a_attr1$(new $te_complexD._a_attr1$(Strings.getRandomString(8)));
    xsiType.set_a_attr2$(new $te_complexD._a_attr2$(Strings.getRandomString(8)));
    xsiType.set_c_attr1$(new $te_complexD._c_attr1$(Strings.getRandomString(8)));
    xsiType.set_c_attr2$(new $te_complexD._c_attr2$(Strings.getRandomString(8)));
    xsiType.set_d_attr1$(new $te_complexD._d_attr1$(Strings.getRandomString(8)));
    xsiType.set_d_attr2$(new $te_complexD._d_attr2$(Strings.getRandomString(8)));

    te_elemD elemD = new te_elemD();
    elemD.addte_elemC(xsiType);

    String marshalled = DOMs.domToString(elemD.marshal(), DOMStyle.INDENT);
    Binding binding = Bindings.parse(new InputSource(new StringReader(marshalled)));
    String remarshalled = DOMs.domToString(Bindings.marshal(binding), DOMStyle.INDENT);
    Assert.assertEquals(marshalled, remarshalled);
  }
}