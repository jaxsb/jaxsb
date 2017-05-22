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

package org.safris.xsb.sample;

import org.safris.xsb.runtime.Binding;
import org.safris.xsb.sample.any.xe.any_trash;
import org.safris.xsb.sample.enums.xe.enums_color$;
import org.safris.xsb.sample.enums.xe.enums_coloredFruitBasket;
import org.safris.xsb.sample.simple.xe.$simple_fruitType;
import org.safris.xsb.sample.simple.xe.simple_fruit;
import org.safris.xsb.sample.simple.xe.simple_fruitBasket;
import org.safris.xsb.sample.xsitype.xe.$type_fleshyFruitType;
import org.safris.xsb.sample.xsitype.xe.$type_indehiscentDryFruitType;

public class AnySample {
  public static void main(final String[] args) {
    new AnySample().runSample();
  }

  public Binding runSample() {
    final simple_fruit strawberry = new simple_fruit();
    strawberry._name$(new simple_fruit._name$("strawberry"));
    strawberry._sweet$(new simple_fruit._sweet$(true));

    final simple_fruit jackfruit = new simple_fruit();
    jackfruit._name$(new simple_fruit._name$("jackfruit"));
    jackfruit._sweet$(new simple_fruit._sweet$(false));
    jackfruit._dry$(new simple_fruit._dry$(false));

    final simple_fruitBasket._fruits simple_fruits = new simple_fruitBasket._fruits();
    simple_fruits.simple_fruit(strawberry);
    simple_fruits.simple_fruit(jackfruit);

    final enums_coloredFruitBasket coloredBasket = new enums_coloredFruitBasket();
    coloredBasket.enums_color$(new enums_color$(enums_color$.blue));
    coloredBasket._fruits(simple_fruits);

    final $type_fleshyFruitType berry = new $type_fleshyFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return new simple_fruit();
      }
    };
    berry._name$(new $type_fleshyFruitType._name$($type_fleshyFruitType._name$.Berry));
    berry._pericarp$(new $type_fleshyFruitType._pericarp$($type_fleshyFruitType._pericarp$.soft));

    // Again, instantiate a nameless element.GenericBasket
    final $type_indehiscentDryFruitType grain = new $type_indehiscentDryFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return new simple_fruit();
      }
    };
    grain._name$(new $type_indehiscentDryFruitType._name$($type_indehiscentDryFruitType._name$.Grain));

    // Again, instantiate a nameless element.
    final $type_indehiscentDryFruitType nut = new $type_indehiscentDryFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return new simple_fruit();
      }
    };
    nut._name$(new $type_indehiscentDryFruitType._name$($type_indehiscentDryFruitType._name$.Nut));
    nut._dry$(new $type_indehiscentDryFruitType._dry$($type_indehiscentDryFruitType._dry$._5Ftrue));
//      nut.setDryAttr(new ITypesimple_fruitType.DryAttr(false));

    final any_trash trash = new any_trash();
    trash.addAny(coloredBasket);
    trash.addAny(berry);
    trash.addAny(grain);
    trash.addAny(nut);

    // Now verify the integrity of the code representing this XML structure.
    return trash;
  }
}