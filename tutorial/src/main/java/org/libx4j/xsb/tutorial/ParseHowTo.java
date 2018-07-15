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

package org.libx4j.xsb.tutorial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import org.lib4j.xml.datatype.Date;
import org.libx4j.xsb.runtime.Bindings;
import org.libx4j.xsb.tutorial.invoice.xAA.$ItemType;
import org.libx4j.xsb.tutorial.invoice.xAA.Invoice;
import org.xml.sax.InputSource;

public class ParseHowTo {
  public static void main(final String[] args) throws Exception {
    final File file = new File("src/main/resources/xml/invoice.xml");
    if (!file.exists())
      throw new FileNotFoundException("File " + file.getAbsolutePath() + " does not exist.");

    if (!file.canRead())
      throw new IllegalStateException("File " + file.getAbsolutePath() + " is not readable.");

    final Invoice invoice = (Invoice)Bindings.parse(new InputSource(new FileInputStream(file)));

    final Integer number = invoice.getNumber().text();
    System.out.print("This invoice # " + number + " ");

    final Date date = invoice.getDate().text();
    System.out.println("is established on " + date + " ");

    final String billingName = invoice.getBillingAddress().getName().text();
    System.out.print("from " + billingName + ", ");

    final String billingAddress = invoice.getBillingAddress().getAddress().text();
    System.out.print(billingAddress + ", ");

    final String billingCity = invoice.getBillingAddress().getCity().text();
    System.out.print(billingCity + ", ");

    final Integer billingPostalCode = invoice.getBillingAddress().getPostalCode().text();
    System.out.print(billingPostalCode + ", ");

    final String billingCountry = invoice.getBillingAddress().getCountry().text();
    System.out.println(billingCountry + ".");

    final String shippingName = invoice.getShippingAddress().getName().text();
    System.out.print("Shipping address is: " + shippingName + ", ");

    final String shippingAddress = invoice.getShippingAddress().getAddress().text();
    System.out.print(shippingAddress + ", ");

    final String shippingCity = invoice.getShippingAddress().getCity().text();
    System.out.print(shippingCity + ", ");

    final Integer shippingPostalCode = invoice.getShippingAddress().getPostalCode().text();
    System.out.print(shippingPostalCode + ", ");

    final String shippingCountry = invoice.getShippingAddress().getCountry().text();
    System.out.println(shippingCountry + ".");

    System.out.println("The following items are included in this invoice:");
    for (final $ItemType item : (List<$ItemType>)invoice.getBilledItems().getItem()) {
      final Integer quantity = item.getQuantity().text();
      System.out.print(quantity + " ");

      final String description = item.getDescription().text();
      System.out.print(description + " ");

      final Integer code = item.getCode().text();
      System.out.print("(#" + code + ") ");

      final BigDecimal price = item.getPrice().text();
      System.out.println("$" + price + " each.");
    }
  }
}