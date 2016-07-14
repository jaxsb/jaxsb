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

import aol_liberty_config.aol_IdentityProvider;
import aol_liberty_config.aol_ServiceProvider;
import java.util.Arrays;
import liberty_iff_2003_08.lib_AffiliationID;
import liberty_iff_2003_08.lib_ProviderID;
import liberty_metadata_2003_08.$md_entityDescriptorType;
import liberty_metadata_2003_08.$md_keyInfoType;
import liberty_metadata_2003_08.md_EntityDescriptor;
import liberty_metadata_2003_08.md_cacheDuration$;
import liberty_metadata_2003_08.md_providerID$;
import org.junit.Ignore;
import org.safris.commons.xml.binding.Duration;
import org.w3.x2000.x09.xmldsig.ds_X509Data;

@Ignore("Make this a real test!")
public class ServiceProviderMetadata extends Metadata {
  private static final String DEFAULT_HOST = "aol-3";
  private static final String DEFAULT_DOMAIN = "liberty-iop.biz";
  private static String host = DEFAULT_HOST;
  private static String domain = DEFAULT_DOMAIN;

  public static void main(String[] args) {
    if (args.length == 2) {
      host = args[0];
      domain = args[1];
    }

    System.out.print(getEntityDescriptor());
    System.out.print(getServiceProvider());
  }

  public static $md_entityDescriptorType._SPDescriptor getSPDescriptor() {
    $md_entityDescriptorType._SPDescriptor._AssertionConsumerServiceURL assertionConsumerServiceURL = new $md_entityDescriptorType._SPDescriptor._AssertionConsumerServiceURL("https://" + host + "._" + domain + "/AssertionConsumer");
    assertionConsumerServiceURL.set_id$(new $md_entityDescriptorType._SPDescriptor._AssertionConsumerServiceURL._id$("_123"));
    assertionConsumerServiceURL.set_isDefault$(new $md_entityDescriptorType._SPDescriptor._AssertionConsumerServiceURL._isDefault$(new Boolean(true)));

    $md_entityDescriptorType._SPDescriptor spDescriptor = new $md_entityDescriptorType._SPDescriptor();
    spDescriptor.set_protocolSupportEnumeration$(new $md_entityDescriptorType._SPDescriptor._protocolSupportEnumeration$(Arrays.asList(new String[]{"urn:liberty:iff:2003-08"})));
    spDescriptor.add_AssertionConsumerServiceURL(assertionConsumerServiceURL);
    spDescriptor.add_FederationTerminationNotificationProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._FederationTerminationNotificationProtocolProfile("http://projectliberty._org/profiles/fedterm-idp-http"));
    spDescriptor.add_FederationTerminationNotificationProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._FederationTerminationNotificationProtocolProfile("http://projectliberty._org/profiles/fedterm-idp-soap"));
    spDescriptor.add_RegisterNameIdentifierProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._RegisterNameIdentifierProtocolProfile("http://projectliberty._org/profiles/rni-idp-http"));
    spDescriptor.add_RegisterNameIdentifierProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._RegisterNameIdentifierProtocolProfile("http://projectliberty._org/profiles/rni-idp-soap"));
    spDescriptor.add_SingleLogoutProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._SingleLogoutProtocolProfile("http://projectliberty._org/profiles/slo-idp-http"));
    spDescriptor.add_SingleLogoutProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._SingleLogoutProtocolProfile("http://projectliberty._org/profiles/slo-idp-soap"));
    spDescriptor.setmd_cacheDuration$(new md_cacheDuration$(new Duration(false, 0, 0, 7)));
    spDescriptor.add_AuthnRequestsSigned(new $md_entityDescriptorType._SPDescriptor._AuthnRequestsSigned(new Boolean(true)));
    spDescriptor.add_FederationTerminationServiceURL(new $md_entityDescriptorType._SPDescriptor._FederationTerminationServiceURL("https://" + host + "._" + domain + "/FederationTermination"));
    spDescriptor.add_FederationTerminationServiceReturnURL(new $md_entityDescriptorType._SPDescriptor._FederationTerminationServiceReturnURL("https://" + host + "._" + domain + "/FederationTerminationReturn"));
    spDescriptor.add_RegisterNameIdentifierServiceURL(new $md_entityDescriptorType._SPDescriptor._RegisterNameIdentifierServiceURL("https://" + host + "._" + domain + "/RegisterNameIdentifier"));
    spDescriptor.add_RegisterNameIdentifierServiceReturnURL(new $md_entityDescriptorType._SPDescriptor._RegisterNameIdentifierServiceReturnURL("https://" + host + "._" + domain + "/RegisterNameIdentifierReturn"));
    spDescriptor.add_SingleLogoutServiceURL(new $md_entityDescriptorType._SPDescriptor._SingleLogoutServiceURL("https://" + host + "._" + domain + "/SingleLogout"));
    spDescriptor.add_SingleLogoutServiceReturnURL(new $md_entityDescriptorType._SPDescriptor._SingleLogoutServiceReturnURL("https://" + host + "._" + domain + "/SingleLogoutReturn"));
    spDescriptor.setmd_providerID$(new md_providerID$("https://" + host + "._" + domain + "/metadata._xml"));
    spDescriptor.add_SoapEndpoint(new $md_entityDescriptorType._IDPDescriptor._SoapEndpoint("https://" + host + "._" + domain + "/services/SoapEndpoint"));

    ds_X509Data._X509Certificate x509Certificate = new ds_X509Data._X509Certificate(getKeyInfo(host));
    ds_X509Data x509Data = new ds_X509Data();
    x509Data.add_X509Certificate(x509Certificate);

    $md_keyInfoType keyInfo = new $md_keyInfoType() {
      protected $md_keyInfoType inherits() {
        return null;
      }
    };
    keyInfo.set_use$(new $md_keyInfoType._use$($md_keyInfoType._use$.SIGNING));
    keyInfo.addds_X509Data(x509Data);

    spDescriptor.add_KeyInfo(keyInfo);

    return spDescriptor;
  }

  public static md_EntityDescriptor getEntityDescriptor() {
    md_EntityDescriptor entityDescriptor = new md_EntityDescriptor();
    entityDescriptor.add_SPDescriptor(getSPDescriptor());
    return entityDescriptor;
  }

  public static aol_ServiceProvider getServiceProvider() {
    aol_ServiceProvider._GuidGenerator guidGenerator = new aol_ServiceProvider._GuidGenerator();
    guidGenerator.add_MacAddress(new aol_ServiceProvider._GuidGenerator._MacAddress("08:02:20:9f:ae:54"));

    aol_ServiceProvider._Session._Pool sessionPool = new aol_ServiceProvider._Session._Pool();
    sessionPool.add_Size(new aol_ServiceProvider._Session._Pool._Size(new Integer(10)));

    aol_ServiceProvider._Session._State sessionState = new aol_ServiceProvider._Session._State();
    sessionState.add_Timeout(new aol_ServiceProvider._Session._State._Timeout(new Integer(30000)));

    aol_ServiceProvider._Session session = new aol_ServiceProvider._Session();
    session.add_Pool(sessionPool);
    session.add_State(sessionState);

    aol_ServiceProvider._XmlSchema xmlSchema = new aol_ServiceProvider._XmlSchema();
    xmlSchema.add_SchemaLocation(new aol_ServiceProvider._XmlSchema._SchemaLocation("urn:aol:liberty:config /usr/safris/servers/docs/tacobell/schemas/aol-lib-config._xsd http://www._w3._org/2001/04/xmlenc# /usr/safris/servers/docs/tacobell/schemas/xenc-schema._xsd http://www._w3._org/2000/09/xmldsig# /usr/safris/servers/docs/tacobell/schemas/xmldsig-core-schema._xsd urn:oasis:names:tc:SAML:1._0:protocol /usr/safris/servers/docs/tacobell/schemas/cs-sstc-schema-protocol-01._xsd urn:oasis:names:tc:SAML:1._0:assertion /usr/safris/servers/docs/tacobell/schemas/cs-sstc-schema-assertion-01._xsd urn:liberty:ac:2003-08 /usr/safris/servers/docs/tacobell/schemas/lib-arch-authentication-context._xsd urn:liberty:iff:2003-08 lib-arch-protocols-schema._xsd urn:liberty:disco:2003-08 /usr/safris/servers/docs/tacobell/schemas/lib-arch-disco-svc._xsd urn:liberty:metadata:2003-08 /usr/safris/servers/docs/tacobell/schemas/lib-arch-metadata._xsd"));
    xmlSchema.add_ValidateMarshal(new aol_ServiceProvider._XmlSchema._ValidateMarshal(new Boolean(false)));
    xmlSchema.add_ValidateParse(new aol_ServiceProvider._XmlSchema._ValidateParse(new Boolean(true)));

    aol_ServiceProvider serviceProvider = new aol_ServiceProvider();
    serviceProvider.add_ApplicationServiceURL(new aol_ServiceProvider._ApplicationServiceURL("https://" + host + "._" + domain + "/Application"));
    serviceProvider.add_LoginServiceURL(new aol_ServiceProvider._LoginServiceURL("https://" + host + "._" + domain + "/Login"));
    serviceProvider.add_AdminServiceURL(new aol_ServiceProvider._AdminServiceURL("https://" + host + "._" + domain + "/Manager"));
    serviceProvider.addlib_ProviderID(new lib_ProviderID("https://" + host + "._" + domain + "/metadata._xml"));
    serviceProvider.addlib_AffiliationID(new lib_AffiliationID("https://" + host + "._" + domain + "/affiliation._xml"));
    serviceProvider.add_GuidGenerator(guidGenerator);
    serviceProvider.add_Session(session);
    serviceProvider.add_XmlSchema(xmlSchema);
    serviceProvider.add_AddSignature(new aol_ServiceProvider._AddSignature(new Boolean(true)));
    serviceProvider.add_VerifySignature(new aol_IdentityProvider._VerifySignature(new Boolean(true)));
    serviceProvider.add_SelectProtocolProfile(new aol_ServiceProvider._SelectProtocolProfile(new Boolean(true)));
    serviceProvider.add_SigAlgorithm(new aol_ServiceProvider._SigAlgorithm("rsa"));
    serviceProvider.add_CommonDomain(new aol_ServiceProvider._CommonDomain("._" + domain));
    serviceProvider.add_SingleSignOnProtocolProfile(new aol_ServiceProvider._SingleSignOnProtocolProfile("http://projectliberty._org/profiles/brws-art"));
    serviceProvider.add_NameIdentifierMappingServiceURL(new aol_ServiceProvider._NameIdentifierMappingServiceURL("https://" + host + "._" + domain + "/NameIdentifierMapping"));
    serviceProvider.add_DiscoveryLookupServiceURL(new aol_ServiceProvider._DiscoveryLookupServiceURL("https://" + host + "._" + domain + "/DiscoveryLookup"));

    return serviceProvider;
  }
}
