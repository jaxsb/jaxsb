/* Copyright (c) 2006 JAX-SB
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

package org.jaxsb.sample;

import org.jaxsb.www.sample.enums.xAA.Color$;
import org.jaxsb.www.sample.enums.xAA.ColoredFruitBasket;
import org.jaxsb.www.sample.simple.xAA.$FruitType;
import org.jaxsb.www.sample.simple.xAA.FruitBasket;
import org.jaxsb.www.sample.xsitype.xAA.$DehiscentDryFruitType;
import org.jaxsb.www.sample.xsitype.xAA.$FleshyFruitType;
import org.jaxsb.www.sample.xsitype.xAA.$IndehiscentDryFruitType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;

public class XsiTypeSample {
  public static void main(final String[] args) {
    new XsiTypeSample().runSample();
  }

  public $AnyType<?> runSample() {
    // Since there is no element declaration for the fleshyFruitType,
    // we need to instantiate a nameless element. Once this element is
    // put into the basket, the element will obtain the needed name.
    final $FleshyFruitType berry = new $FleshyFruitType() {
      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    berry.setName$(new $FleshyFruitType.Name$($FleshyFruitType.Name$.Berry));
    berry.setPericarp$(new $FleshyFruitType.Pericarp$($FleshyFruitType.Pericarp$.soft));

    // Again, instantiate a nameless element.
    final $FleshyFruitType drupe = new $FleshyFruitType() {
      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    drupe.setName$(new $FleshyFruitType.Name$($FleshyFruitType.Name$.Drupe));
    drupe.setPericarp$(new $FleshyFruitType.Pericarp$($FleshyFruitType.Pericarp$.fleshy));

    // Again, instantiate a nameless element.
    final $DehiscentDryFruitType legume = new $DehiscentDryFruitType() {
      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    legume.setName$(new $DehiscentDryFruitType.Name$($DehiscentDryFruitType.Name$.Legume));

    // Again, instantiate a nameless element.
    final $DehiscentDryFruitType follicle = new $DehiscentDryFruitType() {
      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    follicle.setName$(new $DehiscentDryFruitType.Name$($DehiscentDryFruitType.Name$.Follicle));

    // Again, instantiate a nameless element.GenericBasket
    final $IndehiscentDryFruitType grain = new $IndehiscentDryFruitType() {
      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    grain.setName$(new $IndehiscentDryFruitType.Name$($IndehiscentDryFruitType.Name$.Grain));

    // Again, instantiate a nameless element.
    final $IndehiscentDryFruitType nut = new $IndehiscentDryFruitType() {
      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    nut.setName$(new $IndehiscentDryFruitType.Name$($IndehiscentDryFruitType.Name$.Nut));
    nut.setDry$(new $IndehiscentDryFruitType.Dry$($IndehiscentDryFruitType.Dry$._5Ftrue));

    final FruitBasket.Fruits fruits = new FruitBasket.Fruits();
    fruits.addSimpleFruit(berry);
    fruits.addSimpleFruit(drupe);
    fruits.addSimpleFruit(legume);
    fruits.addSimpleFruit(follicle);
    fruits.addSimpleFruit(grain);
    fruits.addSimpleFruit(nut);

    final ColoredFruitBasket coloredBasket = new ColoredFruitBasket();
    coloredBasket.setEnumsColor$(new Color$(Color$.red));
    coloredBasket.setFruits(fruits);

    // Now verify the integrity of the code representing this XML structure.
    return coloredBasket;
  }
}