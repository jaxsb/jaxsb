/* Copyright (c) 2008 Seva Safris
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

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import org.junit.Assert;
import org.junit.Test;
import org.safris.commons.xml.dom.DOMs;
import org.safris.xml.generator.compiler.runtime.Binding;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.safris.xml.schema.binding.test.unit.order.or_A;
import org.safris.xml.schema.binding.test.unit.order.or_B;
import org.safris.xml.schema.binding.test.unit.order.or_C;
import org.safris.xml.schema.binding.test.unit.order.or_root;
import org.xml.sax.InputSource;

public class IteratorTest extends AbstractTest {
  public static void main(String[] args) throws Exception {
    final IteratorTest orderTest = new IteratorTest();
    orderTest.testExample();
  }

  @Test
  public void testExample() throws Exception {
    or_A a = new or_A();
    a.add_innerA(new or_A._innerA("hello first time"));
    a.add_innerB(new or_A._innerB(1));
    a.add_innerC(new or_A._innerC((byte)'0'));

    or_B b = new or_B();
    b.add_innerA(new or_A._innerA("hi!!"));
    b.add_innerB(new or_A._innerB(2));
    b.add_innerC(new or_A._innerC((byte)'9'));
    b.add_A(a);
    b.add_innerE(new or_B._innerE(true));
    b.add_innerF(new or_B._innerF(5.3f));

    or_C c = new or_C();
    c.add_innerA(new or_A._innerA("yo, what's up!?"));
    or_A._innerB keepMe = new or_A._innerB(3);
    c.add_innerB(keepMe);
    or_A._innerB removeMe1 = new or_A._innerB(69);
    c.add_innerC(new or_A._innerC((byte)'4'));
    c.add_A(a);
    c.add_innerE(new or_B._innerE(false));
    c.add_innerF(new or_B._innerF(4f));
    or_A._innerB removeMe2 = new or_A._innerB(-69);
    c.add_B(b);
    or_C._innerA removeMe3 = new or_C._innerA("removeMe");
    c.add_innerA(removeMe3);
    or_C._innerA removeMe4 = new or_C._innerA("removeMe4");
    c.add_innerA(removeMe4);
    or_C._innerA removeMe5 = new or_C._innerA("removeMe5");
    c.add_innerA(removeMe5);

    or_C._innerG replaceMeToo = new or_C._innerG(1999d);
    c.add_innerG(replaceMeToo);

    // These lines assert that the listIterator.set() method works
    ListIterator listIterator = c.get_innerG().listIterator();
    while (listIterator.hasNext()) {
      if (listIterator.next() == replaceMeToo) {
        listIterator.set(new or_C._innerG(-99d));
        break;
      }
    }

    // These test the add() method on the ElementList
    c.get_innerA().add(new or_C._innerA("again"));
    c.get_innerB().add(removeMe1);
    c.get_innerB().add(removeMe2);

    // This tests the add(index) method on the ElementList
    or_C._innerF addBeforeMe = new or_C._innerF(56f);
    c.get_innerF().add(addBeforeMe);
    int index = c.get_innerF().indexOf(addBeforeMe);
    c.get_innerF().add(index, new or_C._innerF(54f));

    // This tests the set() method on the ElementList
    or_C._innerG replaceMe = new or_C._innerG(199d);
    c.add_innerG(replaceMe);
    index = c.get_innerG().indexOf(replaceMe);
    c.get_innerG().set(index, new or_C._innerG(99d));

    // These lines assert that the iterator.remove() method works
    Iterator iterator = c.get_innerA().iterator();
    while (iterator.hasNext()) {
      if (iterator.next() == removeMe4) {
        iterator.remove();
        break;
      }
    }

    // These lines assert that the listIterator.remove() method works
    listIterator = c.get_innerA().listIterator();
    while (listIterator.hasNext()) {
      if (listIterator.next() == removeMe5) {
        listIterator.remove();
        break;
      }
    }

    // These lines assert that the element removal code works
    assertFalse(c.get_innerA().remove(keepMe));
    assertTrue(c.get_innerA().remove(removeMe3));
    assertTrue(c.get_innerB().removeAll(Arrays.<or_A._innerB>asList(new or_A._innerB[]{removeMe1, removeMe2})));

    or_B remove_b = new or_B();
    remove_b.add_innerA(new or_B._innerA("should not be here"));

    or_root root = new or_root();
    root.addor_A(a);
    root.addor_B(b);
    root.addor_C(c);
    root.addor_B(remove_b);

    // These lines assert that the listIterator.add() method works
    listIterator = c.get_innerF().listIterator();
    while (listIterator.hasNext()) {
      if (listIterator.next() == addBeforeMe) {
        listIterator.add(new or_C._innerF(55f));
        break;
      }
    }

    // These lines assert that the elementIterator.remove() method works
    iterator = root.elementIterator();
    while (iterator.hasNext()) {
      if (iterator.next() == remove_b) {
        iterator.remove();
        break;
      }
    }

    assertTrue(verifyBinding(root));

    // This tests that our elementIterator() works
    final Iterator<Binding> elementIterator = root.elementIterator();
    assertTrue(elementIterator.hasNext());
    Binding child = elementIterator.next();
    assertTrue(child instanceof or_A);
    child = elementIterator.next();
    assertTrue(child instanceof or_B);
    child = elementIterator.next();
    assertTrue(child instanceof or_C);

    // Write the xml we've constructed in the code to a string
    String xmlFromObjects = DOMs.domToString(root.marshal());

    final File file = new File("src/test/resources/xml/unit/order.xml");
    if (!file.exists())
      throw new Error("File " + file.getAbsolutePath() + " does not exist.");

    if (!file.canRead())
      throw new Error("File " + file.getAbsolutePath() + " is not readable.");

    or_root root2 = (or_root)Bindings.parse(new InputSource(new FileInputStream(file)));
    assertTrue(verifyBinding(root2));

    // Write the xml we parsed from the file to a string
    String xmlFromFile = DOMs.domToString(root2.marshal());

    // The two xml strings should be equal
    Assert.assertEquals(xmlFromObjects, xmlFromFile);
  }
}