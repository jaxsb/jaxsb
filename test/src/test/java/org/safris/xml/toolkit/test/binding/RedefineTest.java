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

import org.junit.Test;
import org.safris.xml.schema.binding.test.unit.everything.$ev_complexTypie;
import org.safris.xml.schema.binding.test.unit.everything.$ev_complexTypie_original;
import org.safris.xml.schema.binding.test.unit.everything.$ev_simpleTypie;
import org.safris.xml.schema.binding.test.unit.everything.$ev_simpleTypie_original;

public class RedefineTest {
  public static void main(String[] args) {
    new RedefineTest().testComplexTypie();
    new RedefineTest().testSimpleTypie();
  }

  @Test
  public void testComplexTypie() {
    $ev_complexTypie_original original = new $ev_complexTypie_original() {
      protected $ev_complexTypie_original inherits() {
        return null;
      }
    };

    original.setattr_test_attribute_nested1$(null);
    original.setattr_test_attribute_type$(null);
    original.addty_test_type_NCName(null);
    original.addty_test_type_QName(null);
    original.addAny(null);                  // in <redefine/> of groupie
    original.addAny$(null);                 // in <redefine/> of attrGroupie

    $ev_complexTypie redefined = new $ev_complexTypie() {
      protected $ev_complexTypie inherits() {
        return null;
      }
    };

    redefined.setattr_test_attribute_nested1$(null);
    redefined.setattr_test_attribute_type$(null);
    redefined.addty_test_type_NCName(null);
    redefined.addty_test_type_QName(null);
    redefined.addAny(null);                 // in <redefine/> of groupie
    redefined.addAny$(null);                // in <redefine/> of attrGroupie
    redefined.set_redefineTest$(null);      // in <redefine/> of complexTypie
  }

  @Test
  public void testSimpleTypie() {
    $ev_simpleTypie_original original = new $ev_simpleTypie_original() {
      protected $ev_simpleTypie_original inherits() {
        return null;
      }
    };

    original.setText($ev_simpleTypie_original._1);
    original.setText($ev_simpleTypie_original._2);
    original.setText($ev_simpleTypie_original._3);

    $ev_simpleTypie redefined = new $ev_simpleTypie() {
      protected $ev_simpleTypie inherits() {
        return null;
      }
    };

    redefined.setText($ev_simpleTypie._1);
//      redefined.setText($ev_simpleTypie._2);  // removed in <redefine/> of simpleTypie
    redefined.setText($ev_simpleTypie._3);
  }
}