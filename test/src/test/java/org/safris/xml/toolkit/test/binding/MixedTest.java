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
import org.safris.commons.lang.Strings;
import org.safris.commons.xml.dom.DOMStyle;
import org.safris.commons.xml.dom.DOMs;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.safris.xml.schema.binding.test.unit.mixed._root;

public class MixedTest {
  public static void main(String[] args) throws Exception {
    new MixedTest().testMixed();
  }

  @Test
  public void testMixed() throws Exception {
    _root._zero zero = new _root._zero();

    _root._oneEx oneEx = new _root._oneEx(Strings.getRandomAlphaString(8));
    oneEx.setText(Strings.getRandomAlphaString(8));

    _root._oneRe oneRe = new _root._oneRe();            // FIXME: This should NOT be mixed

    _root._two two = new _root._two("mixed");
    two.setText("mixed");

    _root._three three = new _root._three(Strings.getRandomAlphaString(8));
    three.setText(Strings.getRandomAlphaString(8));

    _root._four four = new _root._four(Strings.getRandomAlphaString(8));
    four.setText(Strings.getRandomAlphaString(8));

    _root._five five = new _root._five();

    _root._six six = new _root._six(Strings.getRandomAlphaString(8));
    six.setText(Strings.getRandomAlphaString(8));

    _root._seven seven = new _root._seven();

    _root._eight eight = new _root._eight("mixed");
    eight.setText("mixed");

    _root._ten ten = new _root._ten();

    _root._elevenEx elevenEx = new _root._elevenEx(Strings.getRandomAlphaString(8));
    elevenEx.setText(Strings.getRandomAlphaString(8));

    _root._elevenRe elevenRe = new _root._elevenRe();   // FIXME: This should NOT be mixed

    _root._twelve twelve = new _root._twelve("mixed");
    twelve.setText("mixed");

    _root._thirteen thirteen = new _root._thirteen(Strings.getRandomAlphaString(8));
    thirteen.setText(Strings.getRandomAlphaString(8));

    _root._fourteen fourteen = new _root._fourteen(Strings.getRandomAlphaString(8));
    fourteen.setText(Strings.getRandomAlphaString(8));

    _root._fifteen fifteen = new _root._fifteen();

    _root._sixteen sixteen = new _root._sixteen(Strings.getRandomAlphaString(8));
    sixteen.setText(Strings.getRandomAlphaString(8));

    _root._seventeen seventeen = new _root._seventeen();

    _root._eighteen eighteen = new _root._eighteen("mixed");
    eighteen.setText("mixed");

    _root._twenty twenty = new _root._twenty();

    _root._twentyOne twentyOne = new _root._twentyOne();

    _root._twentyTwo twentyTwo = new _root._twentyTwo("mixed");
    twentyTwo.setText("mixed");

    _root._twentyThree twentyThree = new _root._twentyThree(Strings.getRandomAlphaString(8));
    twentyThree.setText(Strings.getRandomAlphaString(8));

    _root._twentyFour twentyFour = new _root._twentyFour(Strings.getRandomAlphaString(8));
    twentyFour.setText(Strings.getRandomAlphaString(8));

    _root._twentyFive twentyFive = new _root._twentyFive();

    _root._twentySix twentySix = new _root._twentySix(Strings.getRandomAlphaString(8));
    twentySix.setText(Strings.getRandomAlphaString(8));

    _root._twentySeven twentySeven = new _root._twentySeven();

    _root._twentyEight twentyEight = new _root._twentyEight("mixed");
    twentyEight.setText("mixed");

    _root._thirty thirty = new _root._thirty();

    _root._thirtyOne thirtyOne = new _root._thirtyOne();

    _root._thirtyTwo thirtyTwo = new _root._thirtyTwo("mixed");
    thirtyTwo.setText("mixed");

    _root._thirtyThree thirtyThree = new _root._thirtyThree(Strings.getRandomAlphaString(8));
    thirtyThree.setText(Strings.getRandomAlphaString(8));

    _root._thirtyFour thirtyFour = new _root._thirtyFour(Strings.getRandomAlphaString(8));
    thirtyFour.setText(Strings.getRandomAlphaString(8));

    _root._thirtyFive thirtyFive = new _root._thirtyFive();

    _root._thirtySix thirtySix = new _root._thirtySix(Strings.getRandomAlphaString(8));
    thirtySix.setText(Strings.getRandomAlphaString(8));

    _root._thirtySeven thirtySeven = new _root._thirtySeven();

    _root._thirtyEight thirtyEight = new _root._thirtyEight("mixed");
    thirtyEight.setText("mixed");

    _root root = new _root();
    root.add_zero(zero);
    root.add_oneEx(oneEx);
    root.add_oneRe(oneRe);
    root.add_two(two);
    root.add_three(three);
    root.add_four(four);
    root.add_five(five);
    root.add_six(six);
    root.add_seven(seven);
    root.add_eight(eight);
    root.add_ten(ten);
    root.add_elevenEx(elevenEx);
    root.add_elevenRe(elevenRe);
    root.add_twelve(twelve);
    root.add_thirteen(thirteen);
    root.add_fourteen(fourteen);
    root.add_fifteen(fifteen);
    root.add_sixteen(sixteen);
    root.add_seventeen(seventeen);
    root.add_eighteen(eighteen);
    root.add_twenty(twenty);
    root.add_twentyOne(twentyOne);
    root.add_twentyTwo(twentyTwo);
    root.add_twentyThree(twentyThree);
    root.add_twentyFour(twentyFour);
    root.add_twentyFive(twentyFive);
    root.add_twentySix(twentySix);
    root.add_twentySeven(twentySeven);
    root.add_twentyEight(twentyEight);
    root.add_thirty(thirty);
    root.add_thirtyOne(thirtyOne);
    root.add_thirtyTwo(thirtyTwo);
    root.add_thirtyThree(thirtyThree);
    root.add_thirtyFour(thirtyFour);
    root.add_thirtyFive(thirtyFive);
    root.add_thirtySix(thirtySix);
    root.add_thirtySeven(thirtySeven);
    root.add_thirtyEight(thirtyEight);

    System.out.println(DOMs.domToString(Bindings.marshal(root), DOMStyle.INDENT));
  }
}