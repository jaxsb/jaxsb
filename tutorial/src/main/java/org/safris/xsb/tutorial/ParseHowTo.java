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

package org.safris.xsb.tutorial;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.safris.commons.xml.binding.Date;
import org.safris.commons.xml.binding.Decimal;
import org.safris.xsb.runtime.Bindings;
import org.safris.xsb.tutorial.invoice.xe.$pv_itemType;
import org.safris.xsb.tutorial.invoice.xe.pv_invoice;
import org.xml.sax.InputSource;

public class ParseHowTo {
  public static void main(final String[] args) throws Exception {
    final File file = new File("src/main/resources/xml/invoice.xml");
    if (!file.exists())
      throw new Error("File " + file.getAbsolutePath() + " does not exist.");

    if (!file.canRead())
      throw new Error("File " + file.getAbsolutePath() + " is not readable.");

    final pv_invoice invoice = (pv_invoice)Bindings.parse(new InputSource(new FileInputStream(file)));

    final Integer number = invoice._number(0).text();
    System.out.print("This invoice # " + number + " ");

    final Date date = invoice._date(0).text();
    System.out.println("is established on " + date + " ");

    final String billingName = invoice._billingAddress(0)._name(0).text();
    System.out.print("from " + billingName + ", ");

    final String billingAddress = invoice._billingAddress(0)._address(0).text();
    System.out.print(billingAddress + ", ");

    final String billingCity = invoice._billingAddress(0)._city(0).text();
    System.out.print(billingCity + ", ");

    final Integer billingPostalCode = invoice._billingAddress(0)._postalCode(0).text();
    System.out.print(billingPostalCode + ", ");

    final String billingCountry = invoice._billingAddress(0)._country(0).text();
    System.out.println(billingCountry + ".");

    final String shippingName = invoice._shippingAddress(0)._name(0).text();
    System.out.print("Shipping address is: " + shippingName + ", ");

    final String shippingAddress = invoice._shippingAddress(0)._address(0).text();
    System.out.print(shippingAddress + ", ");

    final String shippingCity = invoice._shippingAddress(0)._city(0).text();
    System.out.print(shippingCity + ", ");

    final Integer shippingPostalCode = invoice._shippingAddress(0)._postalCode(0).text();
    System.out.print(shippingPostalCode + ", ");

    final String shippingCountry = invoice._shippingAddress(0)._country(0).text();
    System.out.println(shippingCountry + ".");

    System.out.println("The following items are included in this invoice:");
    for (final $pv_itemType item : (List<$pv_itemType>)invoice._billedItems(0)._item()) {
      final Integer quantity = item._quantity(0).text();
      System.out.print(quantity + " ");

      final String description = item._description(0).text();
      System.out.print(description + " ");

      final Integer code = item._code(0).text();
      System.out.print("(#" + code + ") ");

      final Decimal price = item._price(0).text();
      System.out.println("$" + price + " each.");
    }
  }
}