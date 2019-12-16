/* Copyright (c) 2017 JAX-SB
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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jaxsb.www.generator.html.xAA.Body;
import org.jaxsb.www.generator.html.xAA.Div;
import org.jaxsb.www.generator.html.xAA.Hr;
import org.jaxsb.www.generator.html.xAA.P;
import org.jaxsb.www.generator.html.xAA.Pre;
import org.junit.Test;
import org.libj.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratorIntegrationTest {
  private static final Logger logger = LoggerFactory.getLogger(GeneratorIntegrationTest.class);

  private static void assertElementCount(final String description, final List<Binding> expected, final int expectedLength, final BindingList<?> elements) {
    assertEquals(expectedLength, elements.size());
    final Iterator<? extends Binding> iterator = elements.getOwner().elementIterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      final Binding next = iterator.next();
      assertEquals("Index " + i, expected.get(i), next);
    }

    if (description != null) {
      logger.info(description + " " + Strings.repeat("=", 34 - description.length()));
      ((ElementCompositeList)((ElementCompositeList.ElementComponentList)elements).getCompositeList()).print(logger);
    }
  }

  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testElementLists() {
    final List<Binding> expected = new ArrayList<>();
    final Body body = new Body();

    final P p1 = new P();
    expected.add(p1);
    body.addHtmlP(p1);
    assertElementCount("Add " + System.identityHashCode(p1), expected, 1, body.getHtmlP());

    final P p2 = new P();
    expected.add(p2);
    body.addHtmlP(p2);
    assertElementCount("Add " + System.identityHashCode(p2), expected, 2, body.getHtmlP());

    expected.add(p2);
    ListIterator listIterator = body.elementListIterator(2);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2), expected, 3, body.getHtmlP());

    listIterator = body.elementListIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p1) + " at 0S", expected, 2, body.getHtmlP());

    expected.add(0, p2);
    listIterator = body.elementListIterator(0);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 0S", expected, 3, body.getHtmlP());

    final Div div1 = new Div();
    expected.add(2, div1);
    listIterator = body.elementListIterator(2);
    listIterator.add(div1);
    assertElementCount("Add " + System.identityHashCode(div1) + " at 2S", expected, 3, body.getHtmlP());
    assertElementCount(null, expected, 1, body.getHtmlDiv());

    listIterator = body.elementListIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 1S", expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 1, body.getHtmlDiv());

    listIterator = body.elementListIterator(1);
    expected.add(1, p2);
    listIterator.add(p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 1S", expected, 3, body.getHtmlP());
    assertElementCount(null, expected, 1, body.getHtmlDiv());

    final Div div2 = new Div();
    expected.add(1, div2);
    listIterator = body.elementListIterator();
    listIterator.next();
    listIterator.add(div2);
    assertElementCount("Add " + System.identityHashCode(div2) + " at 1S", expected, 3, body.getHtmlP());
    assertElementCount(null, expected, 2, body.getHtmlDiv());

    final Div div3 = new Div();
    expected.add(2, div3);
    listIterator = body.getHtmlDiv().listIterator();
    listIterator.next();
    listIterator.add(div3);
    assertElementCount("Add " + System.identityHashCode(div3) + " at 1E", expected, 3, body.getHtmlP());
    assertElementCount(null, expected, 3, body.getHtmlDiv());

    final Pre pre1 = new Pre();
    expected.set(3, pre1);
    listIterator = body.elementListIterator(3);
    listIterator.next();
    listIterator.set(pre1);
    assertElementCount("Set " + System.identityHashCode(pre1) + " at 3S", expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 3, body.getHtmlDiv());
    assertElementCount(null, expected, 1, body.getHtmlPre());

    final Hr hr1 = new Hr();
    expected.set(1, hr1);
    listIterator = body.elementListIterator(1);
    listIterator.next();
    listIterator.set(hr1);
    assertElementCount("Set " + System.identityHashCode(hr1) + " at 1S", expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 2, body.getHtmlDiv());
    assertElementCount(null, expected, 1, body.getHtmlPre());
    assertElementCount(null, expected, 1, body.getHtmlHr());

    listIterator = body.elementListIterator(1);
    expected.remove(1);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(hr1) + " at 1S", expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 2, body.getHtmlDiv());
    assertElementCount(null, expected, 1, body.getHtmlPre());
    assertElementCount(null, expected, 0, body.getHtmlHr());

    listIterator = body.elementListIterator(4);
    expected.remove(4);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 4S", expected, 1, body.getHtmlP());
    assertElementCount(null, expected, 2, body.getHtmlDiv());
    assertElementCount(null, expected, 1, body.getHtmlPre());
    assertElementCount(null, expected, 0, body.getHtmlHr());

    listIterator = body.getHtmlPre().listIterator();
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(pre1) + " at 0E", expected, 1, body.getHtmlP());
    assertElementCount(null, expected, 2, body.getHtmlDiv());
    assertElementCount(null, expected, 0, body.getHtmlPre());
    assertElementCount(null, expected, 0, body.getHtmlHr());

    listIterator = body.getHtmlDiv().listIterator(1);
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(div1) + " at 1E", expected, 1, body.getHtmlP());
    assertElementCount(null, expected, 1, body.getHtmlDiv());
    assertElementCount(null, expected, 0, body.getHtmlPre());
    assertElementCount(null, expected, 0, body.getHtmlHr());

    expected.add(0, p2);
    body.getHtmlP().add(0, p2);
    assertElementCount("Add " + System.identityHashCode(p2) + " at 0E", expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 1, body.getHtmlDiv());
    assertElementCount(null, expected, 0, body.getHtmlPre());
    assertElementCount(null, expected, 0, body.getHtmlHr());

    listIterator = body.getHtmlDiv().listIterator();
    expected.remove(2);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(div3) + " at 0E", expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 0, body.getHtmlDiv());
    assertElementCount(null, expected, 0, body.getHtmlPre());
    assertElementCount(null, expected, 0, body.getHtmlHr());

    listIterator = body.getHtmlP().listIterator();
    expected.remove(0);
    listIterator.next();
    listIterator.remove();
    assertElementCount("Remove " + System.identityHashCode(p2) + " at 0E", expected, 1, body.getHtmlP());
    assertElementCount(null, expected, 0, body.getHtmlDiv());
    assertElementCount(null, expected, 0, body.getHtmlPre());
    assertElementCount(null, expected, 0, body.getHtmlHr());

    expected.add(0, p1);
    body.getHtmlP().add(0, p1);
    assertElementCount("Add " + System.identityHashCode(p1) + " at 0E", expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 0, body.getHtmlDiv());
    assertElementCount(null, expected, 0, body.getHtmlPre());
    assertElementCount(null, expected, 0, body.getHtmlHr());

    final Hr hr2 = new Hr();
    expected.set(1, hr2);
    listIterator = body.elementListIterator(1);
    listIterator.next();
    listIterator.set(hr2);
    assertElementCount("Set " + System.identityHashCode(hr2) + " at 1S", expected, 1, body.getHtmlP());
    assertElementCount(null, expected, 0, body.getHtmlDiv());
    assertElementCount(null, expected, 0, body.getHtmlPre());
    assertElementCount(null, expected, 1, body.getHtmlHr());

    final P p3 = new P();
    expected.add(p3);
    body.addHtmlP(p3);
    assertElementCount("Add " + System.identityHashCode(p3), expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 0, body.getHtmlDiv());
    assertElementCount(null, expected, 0, body.getHtmlPre());
    assertElementCount(null, expected, 1, body.getHtmlHr());

    final Pre pre2 = new Pre();
    expected.add(1, pre2);
    listIterator = body.elementListIterator(1);
    listIterator.add(pre2);
    assertElementCount("Remove " + System.identityHashCode(pre2) + " at 1S", expected, 2, body.getHtmlP());
    assertElementCount(null, expected, 0, body.getHtmlDiv());
    assertElementCount(null, expected, 1, body.getHtmlPre());
    assertElementCount(null, expected, 1, body.getHtmlHr());
    assertNull(body.getHtmlH2());
  }
}