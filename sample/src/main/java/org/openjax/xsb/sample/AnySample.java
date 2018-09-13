/* Copyright (c) 2006 OpenJAX
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

package org.openjax.xsb.sample;

import org.openjax.xsb.runtime.Binding;
import org.libx4j.xsb.sample.any.xAA.Trash;
import org.libx4j.xsb.sample.enums.xAA.Color$;
import org.libx4j.xsb.sample.enums.xAA.ColoredFruitBasket;
import org.libx4j.xsb.sample.simple.xAA.$FruitType;
import org.libx4j.xsb.sample.simple.xAA.Fruit;
import org.libx4j.xsb.sample.simple.xAA.FruitBasket;
import org.libx4j.xsb.sample.xsitype.xAA.$FleshyFruitType;
import org.libx4j.xsb.sample.xsitype.xAA.$IndehiscentDryFruitType;

public class AnySample {
  public static void main(final String[] args) {
    new AnySample().runSample();
  }

  public Binding runSample() {
    final Fruit strawberry = new Fruit();
    strawberry.setName$(new Fruit.Name$("strawberry"));
    strawberry.setSweet$(new Fruit.Sweet$(true));

    final Fruit jackfruit = new Fruit();
    jackfruit.setName$(new Fruit.Name$("jackfruit"));
    jackfruit.setSweet$(new Fruit.Sweet$(false));
    jackfruit.setDry$(new Fruit.Dry$(false));

    final FruitBasket.Fruits fruits = new FruitBasket.Fruits();
    fruits.addSimpleFruit(strawberry);
    fruits.addSimpleFruit(jackfruit);

    final ColoredFruitBasket coloredBasket = new ColoredFruitBasket();
    coloredBasket.setEnumsColor$(new Color$(Color$.blue));
    coloredBasket.setFruits(fruits);

    final $FleshyFruitType berry = new $FleshyFruitType() {
      private static final long serialVersionUID = 2218327322530017590L;

      @Override
      protected $FruitType inherits() {
        return new Fruit();
      }
    };
    berry.setName$(new $FleshyFruitType.Name$($FleshyFruitType.Name$.Berry));
    berry.setPericarp$(new $FleshyFruitType.Pericarp$($FleshyFruitType.Pericarp$.soft));

    // Again, instantiate a nameless element.GenericBasket
    final $IndehiscentDryFruitType grain = new $IndehiscentDryFruitType() {
      private static final long serialVersionUID = 867582743279198067L;

      @Override
      protected $FruitType inherits() {
        return new Fruit();
      }
    };
    grain.setName$(new $IndehiscentDryFruitType.Name$($IndehiscentDryFruitType.Name$.Grain));

    // Again, instantiate a nameless element.
    final $IndehiscentDryFruitType nut = new $IndehiscentDryFruitType() {
      private static final long serialVersionUID = -1491146052315929931L;

      @Override
      protected $FruitType inherits() {
        return new Fruit();
      }
    };
    nut.setName$(new $IndehiscentDryFruitType.Name$($IndehiscentDryFruitType.Name$.Nut));
    nut.setDry$(new $IndehiscentDryFruitType.Dry$($IndehiscentDryFruitType.Dry$._5Ftrue));

    final Trash trash = new Trash();
    trash.addAny(coloredBasket);
    trash.addAny(berry);
    trash.addAny(grain);
    trash.addAny(nut);

    // Now verify the integrity of the code representing this XML structure.
    return trash;
  }
}