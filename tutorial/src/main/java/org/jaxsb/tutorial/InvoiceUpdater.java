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

package org.jaxsb.tutorial;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.jaxsb.runtime.Bindings;
import org.jaxsb.www.tutorial.invoice.xAA.Invoice;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class InvoiceUpdater {
  public static void main(final String[] args) throws Exception {
    if (args.length != 5) {
      System.err.println("Usage: InvoiceUpdater <invoice.xml> <description> <code> <quantity> <price>");
      System.exit(1);
    }

    final File file = new File(args[0]);

    final Invoice.BilledItems.Item item = new Invoice.BilledItems.Item();
    item.setDescription(new Invoice.BilledItems.Item.Description(args[1]));
    item.setCode(new Invoice.BilledItems.Item.Code(Long.parseLong(args[2])));
    item.setQuantity(new Invoice.BilledItems.Item.Quantity(Long.parseLong(args[3])));
    item.setPrice(new Invoice.BilledItems.Item.Price(new BigDecimal(args[4])));

    final Invoice invoice = addItem(file, item);
    System.out.println(invoice);
  }

  private static Invoice addItem(final File invoiceFile, final Invoice.BilledItems.Item item) throws IOException, SAXException {
    final Invoice invoice = (Invoice)Bindings.parse(new InputSource(new FileInputStream(invoiceFile)));
    invoice.getBilledItems().addItem(item);
    return invoice;
  }
}