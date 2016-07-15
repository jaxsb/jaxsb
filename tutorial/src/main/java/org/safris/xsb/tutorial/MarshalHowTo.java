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

package org.safris.xml.tutorial.binding;

import org.safris.commons.xml.binding.Date;
import org.safris.commons.xml.binding.Decimal;
import org.safris.commons.xml.dom.DOMStyle;
import org.safris.commons.xml.dom.DOMs;
import org.safris.xml.tutorial.binding.invoice.xe.pv_invoice;

public class MarshalHowTo {
  public static void main(final String[] args) throws Exception {
    final pv_invoice invoice = new pv_invoice();
    invoice._date(new pv_invoice._date(new Date(2003, 1, 7)));

    invoice._number(new pv_invoice._number(1458));

    final pv_invoice._billingAddress billingAddress = new pv_invoice._billingAddress();
    billingAddress._name(new pv_invoice._billingAddress._name("Ian Barking"));
    billingAddress._address(new pv_invoice._billingAddress._address("123 Kennel Street"));
    billingAddress._city(new pv_invoice._billingAddress._city("Dachshund City"));
    billingAddress._postalCode(new pv_invoice._billingAddress._postalCode(98765));
    billingAddress._country(new pv_invoice._billingAddress._country("US"));

    invoice._billingAddress(billingAddress);

    final pv_invoice._shippingAddress shippingAddress = new pv_invoice._shippingAddress();
    shippingAddress._name(new pv_invoice._billingAddress._name("Retail Dept."));
    shippingAddress._address(new pv_invoice._billingAddress._address("888 Dogbowl Street"));
    shippingAddress._city(new pv_invoice._billingAddress._city("Pet City"));
    shippingAddress._postalCode(new pv_invoice._billingAddress._postalCode(98765));
    shippingAddress._country(new pv_invoice._billingAddress._country("US"));

    invoice._shippingAddress(shippingAddress);

    final pv_invoice._billedItems billedItems = new pv_invoice._billedItems();

    pv_invoice._billedItems._item item = new pv_invoice._billedItems._item();
    item._description(new pv_invoice._billedItems._item._description("Studded Collar"));
    item._code(new pv_invoice._billedItems._item._code(45342));
    item._quantity(new pv_invoice._billedItems._item._quantity(10));
    item._price(new pv_invoice._billedItems._item._price(new Decimal(11.95)));

    billedItems._item(item);

    item = new pv_invoice._billedItems._item();
    item._description(new pv_invoice._billedItems._item._description("K9 Pet Coat"));
    item._code(new pv_invoice._billedItems._item._code(25233));
    item._quantity(new pv_invoice._billedItems._item._quantity(5));
    item._price(new pv_invoice._billedItems._item._price(new Decimal(25.01)));

    billedItems._item(item);

    invoice._billedItems(billedItems);

    System.out.println(DOMs.domToString(invoice.marshal(), DOMStyle.INDENT));
  }
}