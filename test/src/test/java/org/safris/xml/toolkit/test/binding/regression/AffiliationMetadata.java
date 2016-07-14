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

import liberty_metadata_2003_08.$md_affiliationDescriptorType;
import liberty_metadata_2003_08.$md_entityDescriptorType;
import org.junit.Ignore;

@Ignore("Make this a real test!")
public class AffiliationMetadata extends Metadata {
  private static String DEFAULT_HOST = "aol-4";
  private static String DEFAULT_DOMAIN = "liberty-iop.biz";
  private static String host = DEFAULT_HOST;
  private static String domain = DEFAULT_DOMAIN;

  public static void main(String[] args) {
    if (args.length == 2) {
      host = args[0];
      domain = args[1];
    }

    System.out.println(getAffiliationDescriptor());
  }

  public static $md_affiliationDescriptorType getAffiliationDescriptor() {
    final $md_affiliationDescriptorType affiliationDescriptor = new $md_entityDescriptorType._AffiliationDescriptor();
    affiliationDescriptor.add_AffiliateMember(new $md_affiliationDescriptorType._AffiliateMember("https://aol-3." + domain + "/metadata.xml"));
    affiliationDescriptor.add_AffiliateMember(new $md_affiliationDescriptorType._AffiliateMember("https://aol-4." + domain + "/metadata.xml"));
    affiliationDescriptor.set_affiliationID$(new $md_affiliationDescriptorType._affiliationID$("https://aol-3." + domain + "/affiliation.xml"));
    affiliationDescriptor.set_affiliationOwnerID$(new $md_affiliationDescriptorType._affiliationOwnerID$("https://aol-3." + domain + "/metadata.xml"));
    return affiliationDescriptor;
  }
}
