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
import java.util.Arrays;
import javax.xml.namespace.QName;
import liberty_iff_2003_08.lib_ProviderID;
import liberty_metadata_2003_08.$md_entityDescriptorType;
import liberty_metadata_2003_08.$md_keyInfoType;
import liberty_metadata_2003_08.md_EntityDescriptor;
import liberty_metadata_2003_08.md_cacheDuration$;
import liberty_metadata_2003_08.md_providerID$;
import org.junit.Ignore;
import org.safris.commons.xml.binding.Duration;
import org.safris.xml.toolkit.test.binding.regression.Metadata;
import org.w3.x2000.x09.xmldsig.ds_X509Data;

@Ignore("Make this a real test!")
public class IdentityProviderMetadata extends Metadata {
  private static String DEFAULT_HOST = "aol-1";
  private static String DEFAULT_DOMAIN = "liberty-iop.biz";
  private static String host = DEFAULT_HOST;
  private static String domain = DEFAULT_DOMAIN;

  public static void main(String[] args) {
    if (args.length == 2) {
      host = args[0];
      domain = args[1];
    }

    System.out.print(getEntityDescriptor());
    System.out.print(getIdentityProvider());
  }

  public static $md_entityDescriptorType._IDPDescriptor getIDPDescriptor() {
    $md_entityDescriptorType._IDPDescriptor._NameIdentifierMappingBinding nameIdentifierMappingBinding = new $md_entityDescriptorType._IDPDescriptor._NameIdentifierMappingBinding();
    nameIdentifierMappingBinding.set_AuthorityKind$(new $md_entityDescriptorType._IDPDescriptor._NameIdentifierMappingBinding._AuthorityKind$(new QName("urn:oasis:names:tc:Saml:1.0:protocol", "$ibuteQuery")));
    nameIdentifierMappingBinding.set_Location$(new $md_entityDescriptorType._IDPDescriptor._NameIdentifierMappingBinding._Location$(("https://" + host + "." + domain + "/services/SoapEndpoint")));
    nameIdentifierMappingBinding.set_Binding$(new $md_entityDescriptorType._IDPDescriptor._NameIdentifierMappingBinding._Binding$("urn:oasis:names:tc:Saml:1.0:assertion"));

    $md_entityDescriptorType._IDPDescriptor idpDescriptor = new $md_entityDescriptorType._IDPDescriptor();
    idpDescriptor.set_protocolSupportEnumeration$(new $md_entityDescriptorType._IDPDescriptor._protocolSupportEnumeration$(Arrays.asList(new String[]{"urn:liberty:iff:2003-08"})));
    idpDescriptor.add_SingleSignOnProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._SingleSignOnProtocolProfile("http://projectliberty.org/profiles/brws-art"));
    idpDescriptor.add_SingleSignOnProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._SingleSignOnProtocolProfile("http://projectliberty.org/profiles/brws-post"));
    idpDescriptor.add_SingleSignOnServiceURL(new $md_entityDescriptorType._IDPDescriptor._SingleSignOnServiceURL("https://" + host + "." + domain + "/SingleSignOn"));
    idpDescriptor.setmd_cacheDuration$(new md_cacheDuration$(new Duration(false, 0, 0, 7)));
    idpDescriptor.add_FederationTerminationNotificationProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._FederationTerminationNotificationProtocolProfile("http://projectliberty.org/profiles/fedterm-sp-http"));
    idpDescriptor.add_FederationTerminationNotificationProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._FederationTerminationNotificationProtocolProfile("http://projectliberty.org/profiles/fedterm-sp-soap"));
    idpDescriptor.add_RegisterNameIdentifierProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._RegisterNameIdentifierProtocolProfile("http://projectliberty.org/profiles/rni-sp-http"));
    idpDescriptor.add_RegisterNameIdentifierProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._RegisterNameIdentifierProtocolProfile("http://projectliberty.org/profiles/rni-sp-soap"));
    idpDescriptor.add_SingleLogoutProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._SingleLogoutProtocolProfile("http://projectliberty.org/profiles/slo-sp-http"));
    idpDescriptor.add_SingleLogoutProtocolProfile(new $md_entityDescriptorType._IDPDescriptor._SingleLogoutProtocolProfile("http://projectliberty.org/profiles/slo-sp-soap"));
    idpDescriptor.add_NameIdentifierMappingBinding(nameIdentifierMappingBinding);
    idpDescriptor.add_FederationTerminationServiceURL(new $md_entityDescriptorType._SPDescriptor._FederationTerminationServiceURL("https://" + host + "." + domain + "/FederationTermination"));
    idpDescriptor.add_FederationTerminationServiceReturnURL(new $md_entityDescriptorType._SPDescriptor._FederationTerminationServiceReturnURL("https://" + host + "." + domain + "/FederationTerminationReturn"));
    idpDescriptor.add_RegisterNameIdentifierServiceURL(new $md_entityDescriptorType._SPDescriptor._RegisterNameIdentifierServiceURL("https://" + host + "." + domain + "/RegisterNameIdentifier"));
    idpDescriptor.add_RegisterNameIdentifierServiceReturnURL(new $md_entityDescriptorType._SPDescriptor._RegisterNameIdentifierServiceReturnURL("https://" + host + "." + domain + "/RegisterNameIdentifierReturn"));
    idpDescriptor.add_SingleLogoutServiceURL(new $md_entityDescriptorType._SPDescriptor._SingleLogoutServiceURL("https://" + host + "." + domain + "/SingleLogout"));
    idpDescriptor.add_SingleLogoutServiceReturnURL(new $md_entityDescriptorType._SPDescriptor._SingleLogoutServiceReturnURL("https://" + host + "." + domain + "/SingleLogoutReturn"));
    idpDescriptor.setmd_providerID$(new md_providerID$("https://" + host + "." + domain + "/metadata.xml"));
    idpDescriptor.add_SoapEndpoint(new $md_entityDescriptorType._IDPDescriptor._SoapEndpoint("https://" + host + "." + domain + "/services/SoapEndpoint"));

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

    idpDescriptor.add_KeyInfo(keyInfo);
    return idpDescriptor;
  }

  public static md_EntityDescriptor getEntityDescriptor() {
    final md_EntityDescriptor entityDescriptor = new md_EntityDescriptor();
    entityDescriptor.add_IDPDescriptor(getIDPDescriptor());
    return entityDescriptor;
  }

  public static aol_IdentityProvider getIdentityProvider() {
    aol_IdentityProvider._GuidGenerator guidGenerator = new aol_IdentityProvider._GuidGenerator();
    guidGenerator.add_MacAddress(new aol_IdentityProvider._GuidGenerator._MacAddress("10:11:11:2f:ee:24"));

    aol_IdentityProvider._Jdbc jdbc = new aol_IdentityProvider._Jdbc();
    jdbc.add_ConfigPath(new aol_IdentityProvider._Jdbc._ConfigPath("/usr/local/tomcat4/" + host + "/server/webapps/liberty/WEB-$NF/etc/connection-pool.xml"));

    aol_IdentityProvider._Session._Pool sessionPool = new aol_IdentityProvider._Session._Pool();
    sessionPool.add_Size(new aol_IdentityProvider._Session._Pool._Size(new Integer(10)));

    aol_IdentityProvider._Session._State sessionState = new aol_IdentityProvider._Session._State();
    sessionState.add_Timeout(new aol_IdentityProvider._Session._State._Timeout(new Integer(30000)));

    aol_IdentityProvider._Session session = new aol_IdentityProvider._Session();
    session.add_Pool(sessionPool);
    session.add_State(sessionState);

    aol_IdentityProvider._XmlSchema xmlSchema = new aol_IdentityProvider._XmlSchema();
    xmlSchema.add_SchemaLocation(new aol_IdentityProvider._XmlSchema._SchemaLocation("urn:aol:liberty:config /usr/safris/servers/docs/foodidp/schemas/aol-lib-config.xsd http://www.w3.org/2001/04/xmlenc# /usr/safris/servers/docs/foodidp/schemas/xenc-schema.xsd http://www.w3.org/2000/09/xmldsig# /usr/safris/servers/docs/foodidp/schemas/xmldsig-core-schema.xsd urn:oasis:names:tc:Saml:1.0:protocol /usr/safris/servers/docs/foodidp/schemas/cs-sstc-schema-protocol-01.xsd urn:oasis:names:tc:Saml:1.0:assertion /usr/safris/servers/docs/foodidp/schemas/cs-sstc-schema-assertion-01.xsd urn:liberty:ac:2003-08 /usr/safris/servers/docs/foodidp/schemas/lib-arch-authentication-context.xsd urn:liberty:iff:2003-08 lib-arch-protocols-schema.xsd urn:liberty:disco:2003-08 /usr/safris/servers/docs/foodidp/schemas/lib-arch-disco-svc.xsd urn:liberty:metadata:2003-08 /usr/safris/servers/docs/foodidp/schemas/lib-arch-metadata.xsd"));
    xmlSchema.add_ValidateMarshal(new aol_IdentityProvider._XmlSchema._ValidateMarshal(new Boolean(false)));
    xmlSchema.add_ValidateParse(new aol_IdentityProvider._XmlSchema._ValidateParse(new Boolean(true)));

    aol_IdentityProvider identityProvider = new aol_IdentityProvider();
    identityProvider.addlib_ProviderID(new lib_ProviderID("https://" + host + "." + domain + "/metadata.xml"));
    identityProvider.add_ApplicationServiceURL(new aol_IdentityProvider._ApplicationServiceURL("https://" + host + "." + domain + "/Application"));
    identityProvider.add_LoginServiceURL(new aol_IdentityProvider._LoginServiceURL("https://" + host + "." + domain + "/Login"));
    identityProvider.add_AdminServiceURL(new aol_IdentityProvider._AdminServiceURL("https://" + host + "." + domain + "/Manager"));
    identityProvider.add_GuidGenerator(guidGenerator);
    identityProvider.add_Jdbc(jdbc);
    identityProvider.add_Session(session);
    identityProvider.add_XmlSchema(xmlSchema);
    identityProvider.add_AddSignature(new aol_IdentityProvider._AddSignature(new Boolean(true)));
    identityProvider.add_VerifySignature(new aol_IdentityProvider._VerifySignature(new Boolean(true)));
    identityProvider.add_SelectProtocolProfile(new aol_IdentityProvider._SelectProtocolProfile(new Boolean(true)));
    identityProvider.add_SigAlgorithm(new aol_IdentityProvider._SigAlgorithm("rsa"));
    identityProvider.add_CommonDomain(new aol_IdentityProvider._CommonDomain("." + domain));
    identityProvider.add_RegistrationServiceURL(new aol_IdentityProvider._RegistrationServiceURL("https://" + host + "." + domain + "/Registration"));
    identityProvider.add_DiscoveryProviderID(new aol_IdentityProvider._DiscoveryProviderID("https://aol-2." + domain + "/metadata.xml"));

    return identityProvider;
  }
}
