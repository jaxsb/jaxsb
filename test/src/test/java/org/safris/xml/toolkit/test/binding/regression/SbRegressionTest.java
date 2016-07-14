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

import liberty_sb_2003_08.sb_Consent;
import liberty_sb_2003_08.sb_Correlation;
import liberty_sb_2003_08.sb_UsageDirective;
import org.junit.Ignore;
import org.xmlsoap.schemas.soap.envelope.tns_actor$;
import org.xmlsoap.schemas.soap.envelope.tns_mustUnderstand$;

@Ignore("Make this a real test!")
public class SbRegressionTest extends RegressionTest {
  private static final String namespaceURI = "urn:liberty:sb:2003-08";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new SbRegressionTest();

  public static void main(String[] args) {
    getCorrelation();
    getConsent();
    getUsageDirective();
  }

  public static sb_Correlation getCorrelation() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    sb_Correlation binding = new sb_Correlation();
    binding.set_messageID$(new sb_Correlation._messageID$(getRandomString()));
    binding.set_refToMessageID$(new sb_Correlation._refToMessageID$(getRandomString()));
    binding.set_timestamp$(new sb_Correlation._timestamp$(getRandomDateTime()));
    binding.set_id$(new sb_Correlation._id$(getRandomString()));
    binding.settns_mustUnderstand$(new tns_mustUnderstand$(getRandomBoolean()));
    binding.settns_actor$(new tns_actor$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static sb_Consent getConsent() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    sb_Consent binding = new sb_Consent();
    binding.set_uri$(new sb_Consent._uri$(getRandomString()));
    binding.set_timestamp$(new sb_Consent._timestamp$(getRandomDateTime()));
    binding.set_id$(new sb_Consent._id$(getRandomString()));
    binding.settns_mustUnderstand$(new tns_mustUnderstand$(getRandomBoolean()));
    binding.settns_actor$(new tns_actor$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static sb_UsageDirective getUsageDirective() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    sb_UsageDirective binding = new sb_UsageDirective();
    binding.set_id$(new sb_UsageDirective._id$(getRandomString()));
    binding.settns_mustUnderstand$(new tns_mustUnderstand$(getRandomBoolean()));
    binding.settns_actor$(new tns_actor$(getRandomString()));
    binding.set_ref$(new sb_UsageDirective._ref$(getRandomString()));
    do
      binding.addAny(instance.getAny());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }
}