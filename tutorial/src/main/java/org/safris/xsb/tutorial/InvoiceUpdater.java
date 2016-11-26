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

import org.safris.commons.xml.binding.Decimal;
import org.safris.commons.xml.dom.DOMStyle;
import org.safris.commons.xml.dom.DOMs;
import org.safris.xsb.runtime.Bindings;
import org.safris.xsb.tutorial.invoice.xe.pv_invoice;
import org.xml.sax.InputSource;

public class InvoiceUpdater {
  private static pv_invoice addItem(final File invoiceFile, final pv_invoice._billedItems._item item) throws Exception {
    final pv_invoice invoice = (pv_invoice)Bindings.parse(new InputSource(new FileInputStream(invoiceFile)));
    invoice._billedItems(0)._item(item);
    return invoice;
  }

  public static void main(final String[] args) throws Exception {
    if (args.length == 0)
      trapPrintUsage();

    final File file = new File(args[0]);

    final pv_invoice._billedItems._item item = new pv_invoice._billedItems._item();
    item._description(new pv_invoice._billedItems._item._description(args[1]));
    item._code(new pv_invoice._billedItems._item._code(Integer.parseInt(args[2])));
    item._quantity(new pv_invoice._billedItems._item._quantity(Integer.parseInt(args[3])));
    item._price(new pv_invoice._billedItems._item._price(new Decimal(Float.parseFloat(args[4]))));

    final pv_invoice invoice = addItem(file, item);
    DOMs.domToString(invoice.marshal(), DOMStyle.INDENT);
  }

  private static void trapPrintUsage() {
    System.err.println("Usage: InvoiceUpdater <invoice.xml>");
    System.exit(1);
  }
}