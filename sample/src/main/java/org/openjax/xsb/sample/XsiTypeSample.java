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
import org.openjax.xsb.sample.enums.xAA.Color$;
import org.openjax.xsb.sample.enums.xAA.ColoredFruitBasket;
import org.openjax.xsb.sample.simple.xAA.$FruitType;
import org.openjax.xsb.sample.simple.xAA.FruitBasket;
import org.openjax.xsb.sample.xsitype.xAA.$DehiscentDryFruitType;
import org.openjax.xsb.sample.xsitype.xAA.$FleshyFruitType;
import org.openjax.xsb.sample.xsitype.xAA.$IndehiscentDryFruitType;

public class XsiTypeSample {
  public static void main(final String[] args) {
    new XsiTypeSample().runSample();
  }

  public Binding runSample() {
    // Since there is no element declaration for the fleshyFruitType,
    // we need to instantiate a nameless element. Once this element is
    // put into the basket, the element will obtain the needed name.
    final $FleshyFruitType berry = new $FleshyFruitType() {
      private static final long serialVersionUID = 3429848525130169057L;

      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    berry.setName$(new $FleshyFruitType.Name$($FleshyFruitType.Name$.Berry));
    berry.setPericarp$(new $FleshyFruitType.Pericarp$($FleshyFruitType.Pericarp$.soft));

    // Again, instantiate a nameless element.
    final $FleshyFruitType drupe = new $FleshyFruitType() {
      private static final long serialVersionUID = -2818980037530493310L;

      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    drupe.setName$(new $FleshyFruitType.Name$($FleshyFruitType.Name$.Drupe));
    drupe.setPericarp$(new $FleshyFruitType.Pericarp$($FleshyFruitType.Pericarp$.fleshy));

    // Again, instantiate a nameless element.
    final $DehiscentDryFruitType legume = new $DehiscentDryFruitType() {
      private static final long serialVersionUID = 7187893314583339315L;

      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    legume.setName$(new $DehiscentDryFruitType.Name$($DehiscentDryFruitType.Name$.Legume));

    // Again, instantiate a nameless element.
    final $DehiscentDryFruitType follicle = new $DehiscentDryFruitType() {
      private static final long serialVersionUID = -3745696718412448230L;

      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    follicle.setName$(new $DehiscentDryFruitType.Name$($DehiscentDryFruitType.Name$.Follicle));

    // Again, instantiate a nameless element.GenericBasket
    final $IndehiscentDryFruitType grain = new $IndehiscentDryFruitType() {
      private static final long serialVersionUID = 4305262292756850924L;

      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    grain.setName$(new $IndehiscentDryFruitType.Name$($IndehiscentDryFruitType.Name$.Grain));

    // Again, instantiate a nameless element.
    final $IndehiscentDryFruitType nut = new $IndehiscentDryFruitType() {
      private static final long serialVersionUID = 5866670471244912648L;

      @Override
      protected $FruitType inherits() {
        return null;
      }
    };
    nut.setName$(new $IndehiscentDryFruitType.Name$($IndehiscentDryFruitType.Name$.Nut));
    nut.setDry$(new $IndehiscentDryFruitType.Dry$($IndehiscentDryFruitType.Dry$._5Ftrue));
//      nut.addDry$(new ITypesimple_fruitType.Dry$(false));

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