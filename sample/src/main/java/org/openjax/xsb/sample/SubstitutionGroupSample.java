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

import java.net.URL;
import java.util.List;

import org.openjax.xsb.runtime.Binding;
import org.openjax.xsb.runtime.Bindings;
import org.openjax.xsb.sample.substitutionGroup.xAA.$ProductType;
import org.openjax.xsb.sample.substitutionGroup.xAA.Hat;
import org.openjax.xsb.sample.substitutionGroup.xAA.Shirt;
import org.openjax.xsb.sample.substitutionGroup.xAA.StockList;
import org.openjax.xsb.sample.substitutionGroup.xAA.Umbrella;

public class SubstitutionGroupSample {
  public static void main(final String[] args) throws Exception {
    new SubstitutionGroupSample().runSample();
  }

  public Binding runSample() throws Exception {
    final URL url = getClass().getResource("/substitutionGroup.xml");
    final StockList stockList = (StockList)Bindings.parse(url.openStream());
    final List<$ProductType> products = stockList.getSgProduct();
    for (final $ProductType product : products) {
      if (product instanceof Shirt) {
        final Shirt shirt = (Shirt)product;
        System.out.println("There are " + shirt.getAmount().text() + " of '" + shirt.getName().text() + "' shirts colored " + shirt.getColor().text() + ", size " + shirt.getSize().text());
      }
      else if (product instanceof Hat) {
        final Hat hat = (Hat)product;
        System.out.println("There are " + hat.getAmount().text() + " of '" + hat.getName().text() + "' hats, size " + hat.getSize().text());
      }
      else if (product instanceof Umbrella) {
        final Umbrella umbrella = (Umbrella)product;
        System.out.println("There are " + umbrella.getAmount().text() + " of '" + umbrella.getName().text() + "' umbrellas");
      }
    }

    return stockList;
  }
}