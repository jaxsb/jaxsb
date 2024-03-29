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

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jaxsb.runtime.Bindings;
import org.jaxsb.www.sample.substitutionGroup.xAA.$ProductType;
import org.jaxsb.www.sample.substitutionGroup.xAA.Hat;
import org.jaxsb.www.sample.substitutionGroup.xAA.Shirt;
import org.jaxsb.www.sample.substitutionGroup.xAA.StockList;
import org.jaxsb.www.sample.substitutionGroup.xAA.Umbrella;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.xml.sax.SAXException;

public class SubstitutionGroupSample {
  public static void main(final String[] args) throws Exception {
    new SubstitutionGroupSample().runSample();
  }

  public $AnyType<?> runSample() throws IOException, SAXException {
    final URL url = getClass().getResource("/substitutionGroup.xml");
    final StockList stockList = (StockList)Bindings.parse(url);
    final List<$ProductType> products = stockList.getSgProduct();
    for (final $ProductType product : products) { // [L]
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