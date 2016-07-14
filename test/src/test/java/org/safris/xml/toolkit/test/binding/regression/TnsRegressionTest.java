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

package org.safris.xml.toolkit.test.binding.regression;

import org.junit.Ignore;
import org.xmlsoap.schemas.soap.envelope.tns_Body;
import org.xmlsoap.schemas.soap.envelope.tns_Envelope;
import org.xmlsoap.schemas.soap.envelope.tns_Fault;
import org.xmlsoap.schemas.soap.envelope.tns_Header;

@Ignore("Make this a real test!")
public class TnsRegressionTest extends RegressionTest {
  private static final String namespaceURI = "http://schemas.xmlsoap.org/soap/envelope/";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new DscRegressionTest();

  public static void main(String[] args) {
    getEnvelope();
    getFault();
  }

  public static tns_Envelope getEnvelope() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    tns_Envelope binding = new tns_Envelope();
    while (Math.random() < ADD_SEED)
      binding.addtns_Header(getHeader());
    binding.addtns_Body(getBody());
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
//          while(Math.random() < ADD_SEED)
//              binding.addAnyATTR(instance.getAny$ibute(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static tns_Header getHeader() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    tns_Header binding = new tns_Header();
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
//          do
//              binding.addAnyATTR(instance.getAny$ibute(getRandomString()));
//          while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static tns_Body getBody() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    tns_Body binding = new tns_Body();
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
//      do
//          binding.addAnyATTR(instance.getAny$ibute(getRandomString()));
//      while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static tns_Fault getFault() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    tns_Fault binding = new tns_Fault();
    binding.add_faultcode(new tns_Fault._faultcode(getRandomQName()));
    binding.add_faultstring(new tns_Fault._faultstring(getRandomString()));
    binding.add_faultactor(new tns_Fault._faultactor(getRandomString()));
    tns_Fault._detail detail = new tns_Fault._detail();
    while (Math.random() < ADD_SEED)
      detail.addAny(instance.getAny());
//      while(Math.random() < ADD_SEED)
//          detail.addAnyATTR(instance.getAny$ibute(getRandomString()));
    binding.add_detail(detail);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }
}