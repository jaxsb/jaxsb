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

import liberty_disco_2003_08.$isf_ServiceInstanceType;
import liberty_disco_2003_08.isf_AuthenticateSessionContext;
import liberty_disco_2003_08.isf_AuthorizeRequester;
import liberty_disco_2003_08.isf_EncryptedResourceID;
import liberty_disco_2003_08.isf_Extension;
import liberty_disco_2003_08.isf_Options;
import liberty_disco_2003_08.isf_ResourceID;
import liberty_disco_2003_08.isf_ResourceOffering;
import liberty_disco_2003_08.isf_ServiceType;
import liberty_disco_2003_08.isf_Status;
import org.junit.Ignore;

@Ignore("Make this a real test!")
public class DscRegressionTest extends RegressionTest {
  private static final String namespaceURI = "urn:liberty:disco:2003-08";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new DscRegressionTest();

  public static void main(String[] args) {
    getExtension();
    getAuthorizeRequester();
    getAuthenticateSessionContext();
  }

  public static isf_Status getStatus() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_Status binding = new isf_Status();
    if (Math.random() < RECURSION_SEED)
      binding.addisf_Status(getStatus());
    binding.set_code$(new isf_Status._code$(getRandomQName()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static isf_Extension getExtension() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_Extension binding = new isf_Extension();
    do
      binding.addAny(instance.getAny());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static isf_ServiceType getServiceType() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_ServiceType binding = new isf_ServiceType(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static isf_Options getOptions() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_Options binding = new isf_Options();
    while (Math.random() < ADD_SEED)
      binding.add_Option(new isf_Options._Option(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static isf_ResourceOffering getResourceOffering() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_ResourceOffering binding = new isf_ResourceOffering();
    double random = Math.random();
    if (random < 1 / 2)
      binding.addisf_ResourceID(getResourceID());
    else
      binding.addisf_EncryptedResourceID(getEncryptedResourceID());
    binding.set_entryID$(new isf_ResourceOffering._entryID$(getRandomString()));
    binding.add_ServiceInstance(getServiceInstance());
    if (Math.random() < RECURSION_SEED)
      binding.addisf_Options(getOptions());
    if (Math.random() < RECURSION_SEED)
      binding.add_Abstract(new isf_ResourceOffering._Abstract(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static isf_ResourceID getResourceID() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_ResourceID binding = new isf_ResourceID();
    binding.set_id$(new isf_ResourceID._id$(getRandomString()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static isf_EncryptedResourceID getEncryptedResourceID() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_EncryptedResourceID binding = new isf_EncryptedResourceID();
    binding.addxenc_EncryptedData(XencRegressionTest.getEncryptedData());
    binding.addxenc_EncryptedKey(XencRegressionTest.getEncryptedKey());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static final $isf_ServiceInstanceType getServiceInstance() {
    $isf_ServiceInstanceType binding = new $isf_ServiceInstanceType() {
      protected $isf_ServiceInstanceType inherits() {
        return null;
      }
    };
    $isf_ServiceInstanceType._Description description = new $isf_ServiceInstanceType._Description();

    do {
      do
        description.add_CredentialRef(new $isf_ServiceInstanceType._Description._CredentialRef(getRandomString()));
      while(Math.random() < ADD_SEED);
      do
        description.add_SecurityMechID(new $isf_ServiceInstanceType._Description._SecurityMechID(getRandomString()));
      while(Math.random() < ADD_SEED);
      description.add_Endpoint(new $isf_ServiceInstanceType._Description._Endpoint(getRandomString()));
      description.add_ServiceNameRef(new $isf_ServiceInstanceType._Description._ServiceNameRef(getRandomQName()));
      description.add_SoapAction(new $isf_ServiceInstanceType._Description._SoapAction(getRandomString()));
      description.add_WsdlURI(new $isf_ServiceInstanceType._Description._WsdlURI(getRandomString()));
      binding.add_Description(description);
    }
    while(Math.random() < ADD_SEED);
    binding.add_ProviderID(new $isf_ServiceInstanceType._ProviderID(LibRegressionTest.getProviderID().getText()));
    binding.addisf_ServiceType(getServiceType());
    return binding;
  }

  public static isf_AuthorizeRequester getAuthorizeRequester() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_AuthorizeRequester binding = new isf_AuthorizeRequester();
    while (Math.random() < ADD_SEED)
      binding.set_descriptionIDRefs$(new isf_AuthorizeRequester._descriptionIDRefs$(getRandomStrings()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static isf_AuthenticateSessionContext getAuthenticateSessionContext() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    isf_AuthenticateSessionContext binding = new isf_AuthenticateSessionContext();
    while (Math.random() < ADD_SEED)
      binding.set_descriptionIDRefs$(new isf_AuthenticateSessionContext._descriptionIDRefs$(getRandomStrings()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }
}