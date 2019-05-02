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

import org.jaxsb.www.sample.simple.xAA.Fruit;
import org.jaxsb.www.sample.simple.xAA.FruitBasket;
import org.jaxsb.runtime.Binding;

public class SimpleSample {
  public static void main(final String[] args) {
    new SimpleSample().runSample();
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

    final FruitBasket genericBasket = new FruitBasket();
    genericBasket.setFruits(fruits);

    // Now verify the integrity of the code representing this XML structure.
    return genericBasket;
  }
}