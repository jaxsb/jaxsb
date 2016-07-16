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

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.safris.xml.toolkit.sample.binding.substitutiongroup.xe.$sg_productType;
import org.safris.xml.toolkit.sample.binding.substitutiongroup.xe.sg_hat;
import org.safris.xml.toolkit.sample.binding.substitutiongroup.xe.sg_shirt;
import org.safris.xml.toolkit.sample.binding.substitutiongroup.xe.sg_stockList;
import org.safris.xml.toolkit.sample.binding.substitutiongroup.xe.sg_umbrella;
import org.safris.xsb.runtime.Binding;
import org.safris.xsb.runtime.Bindings;
import org.xml.sax.InputSource;

public class SubstitutionGroupSample {
  public static void main(final String[] args) throws Exception {
    new SubstitutionGroupSample().runSample();
  }

  public Binding runSample() throws Exception {
    final File file = new File("src/main/resources/xml/substitutionGroup.xml");
    if (!file.exists())
      throw new Error("File " + file.getAbsolutePath() + " does not exist.");

    if (!file.canRead())
      throw new Error("File " + file.getAbsolutePath() + " is not readable.");

    final sg_stockList stockList = (sg_stockList)Bindings.parse(new InputSource(new FileInputStream(file)));
    final List<$sg_productType> products = stockList.sg_product();
    for (final $sg_productType product : products) {
      if (product instanceof sg_shirt) {
        final sg_shirt shirt = (sg_shirt)product;
        System.out.println("There are " + shirt._amount(0).text() + " of '" + shirt._name(0).text() + "' shirts colored " + shirt._color(0).text() + ", size " + shirt._size(0).text());
      }
      else if (product instanceof sg_hat) {
        final sg_hat hat = (sg_hat)product;
        System.out.println("There are " + hat._amount(0).text() + " of '" + hat._name(0).text() + "' hats, size " + hat._size(0).text());
      }
      else if (product instanceof sg_umbrella) {
        final sg_umbrella umbrella = (sg_umbrella)product;
        System.out.println("There are " + umbrella._amount(0).text() + " of '" + umbrella._name(0).text() + "' umbrellas");
      }
    }

    return stockList;
  }
}