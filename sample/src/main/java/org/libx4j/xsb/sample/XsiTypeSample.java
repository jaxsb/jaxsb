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

package org.libx4j.xsb.sample;

import org.libx4j.xsb.runtime.Binding;
import org.libx4j.xsb.sample.enums.xe.enums_color$;
import org.libx4j.xsb.sample.enums.xe.enums_coloredFruitBasket;
import org.libx4j.xsb.sample.simple.xe.$simple_fruitType;
import org.libx4j.xsb.sample.simple.xe.simple_fruitBasket;
import org.libx4j.xsb.sample.xsitype.xe.$type_dehiscentDryFruitType;
import org.libx4j.xsb.sample.xsitype.xe.$type_fleshyFruitType;
import org.libx4j.xsb.sample.xsitype.xe.$type_indehiscentDryFruitType;

public class XsiTypeSample {
  public static void main(final String[] args) {
    new XsiTypeSample().runSample();
  }

  public Binding runSample() {
    // Since there is no element declaration for the fleshyFruitType,
    // we need to instantiate a nameless element. Once this element is
    // put into the basket, the element will obtain the needed name.
    final $type_fleshyFruitType berry = new $type_fleshyFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return null;
      }
    };
    berry._name$(new $type_fleshyFruitType._name$($type_fleshyFruitType._name$.Berry));
    berry._pericarp$(new $type_fleshyFruitType._pericarp$($type_fleshyFruitType._pericarp$.soft));

    // Again, instantiate a nameless element.
    final $type_fleshyFruitType drupe = new $type_fleshyFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return null;
      }
    };
    drupe._name$(new $type_fleshyFruitType._name$($type_fleshyFruitType._name$.Drupe));
    drupe._pericarp$(new $type_fleshyFruitType._pericarp$($type_fleshyFruitType._pericarp$.fleshy));

    // Again, instantiate a nameless element.
    final $type_dehiscentDryFruitType legume = new $type_dehiscentDryFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return null;
      }
    };
    legume._name$(new $type_dehiscentDryFruitType._name$($type_dehiscentDryFruitType._name$.Legume));

    // Again, instantiate a nameless element.
    final $type_dehiscentDryFruitType follicle = new $type_dehiscentDryFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return null;
      }
    };
    follicle._name$(new $type_dehiscentDryFruitType._name$($type_dehiscentDryFruitType._name$.Follicle));

    // Again, instantiate a nameless element.GenericBasket
    final $type_indehiscentDryFruitType grain = new $type_indehiscentDryFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return null;
      }
    };
    grain._name$(new $type_indehiscentDryFruitType._name$($type_indehiscentDryFruitType._name$.Grain));

    // Again, instantiate a nameless element.
    final $type_indehiscentDryFruitType nut = new $type_indehiscentDryFruitType() {
      @Override
      protected $simple_fruitType inherits() {
        return null;
      }
    };
    nut._name$(new $type_indehiscentDryFruitType._name$($type_indehiscentDryFruitType._name$.Nut));
    nut._dry$(new $type_indehiscentDryFruitType._dry$($type_indehiscentDryFruitType._dry$._5Ftrue));
//      nut.addDry$(new ITypesimple_fruitType.Dry$(false));

    final simple_fruitBasket._fruits fruits = new simple_fruitBasket._fruits();
    fruits.simple_fruit(berry);
    fruits.simple_fruit(drupe);
    fruits.simple_fruit(legume);
    fruits.simple_fruit(follicle);
    fruits.simple_fruit(grain);
    fruits.simple_fruit(nut);

    final enums_coloredFruitBasket coloredBasket = new enums_coloredFruitBasket();
    coloredBasket.enums_color$(new enums_color$(enums_color$.red));
    coloredBasket._fruits(fruits);

    // Now verify the integrity of the code representing this XML structure.
    return coloredBasket;
  }
}