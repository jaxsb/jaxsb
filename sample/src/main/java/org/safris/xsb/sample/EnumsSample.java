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

package org.safris.xsb.sample;

import org.safris.xml.toolkit.sample.binding.enums.xe.enums_color$;
import org.safris.xml.toolkit.sample.binding.enums.xe.enums_coloredFruitBasket;
import org.safris.xml.toolkit.sample.binding.simple.xe.simple_fruit;
import org.safris.xsb.generator.compiler.runtime.Binding;

public class EnumsSample {
  public static void main(final String[] args) {
    new EnumsSample().runSample();
  }

  public Binding runSample() {
    final simple_fruit strawberry = new simple_fruit();
    strawberry._name$(new simple_fruit._name$("strawberry"));
    strawberry._sweet$(new simple_fruit._sweet$(true));

    final simple_fruit jackfruit = new simple_fruit();
    jackfruit._name$(new simple_fruit._name$("jackfruit"));
    jackfruit._sweet$(new simple_fruit._sweet$(false));
    jackfruit._dry$(new simple_fruit._dry$(false));

    final enums_coloredFruitBasket._fruits simple_fruits = new enums_coloredFruitBasket._fruits();
    simple_fruits.simple_fruit(strawberry);
    simple_fruits.simple_fruit(jackfruit);

    final enums_coloredFruitBasket coloredBasket = new enums_coloredFruitBasket();
    coloredBasket.enums_color$(new enums_color$(enums_color$.blue));
    coloredBasket._fruits(simple_fruits);

    // Now verify the integrity of the code representing this XML structure.
    return coloredBasket;
  }
}