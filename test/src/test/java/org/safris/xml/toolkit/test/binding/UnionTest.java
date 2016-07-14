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

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.safris.commons.xml.dom.DOMStyle;
import org.safris.commons.xml.dom.DOMs;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.safris.xml.schema.binding.test.unit.union.un_four;
import org.safris.xml.schema.binding.test.unit.union.un_root;
import org.safris.xml.schema.binding.test.unit.union.un_six;
import org.safris.xml.schema.binding.test.unit.union.un_two;

public class UnionTest {
  @Test
  public void testUnion() throws Exception {
    un_root root = new un_root();

    un_root._one one = new un_root._one();
    one.setText(new Integer(9));
    root.add_one(one);

    un_two two = new un_two();
    List twos = new ArrayList();
    twos.add(new Integer(1));
    twos.add("hi");
    two.setText(twos);
    root.addun_two(two);

    un_root._three three = new un_root._three(5);
    root.add_three(three);

    un_four four = new un_four("text");
    root.addun_four(four);

    un_root._five five = new un_root._five(314);
    root.add_five(five);

    un_six six = new un_six("yeah");
    root.addun_six(six);

    un_root._seven seven = new un_root._seven(777);
    root.add_seven(seven);

    System.out.println(DOMs.domToString(Bindings.marshal(root), DOMStyle.INDENT));
  }
}