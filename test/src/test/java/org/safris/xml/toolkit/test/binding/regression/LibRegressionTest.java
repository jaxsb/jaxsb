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

import liberty_iff_2003_08.$lib_AuthenticationStatementType;
import liberty_iff_2003_08.lib_AffiliationID;
import liberty_iff_2003_08.lib_Assertion;
import liberty_iff_2003_08.lib_AuthnContext;
import liberty_iff_2003_08.lib_AuthnRequest;
import liberty_iff_2003_08.lib_AuthnRequestEnvelope;
import liberty_iff_2003_08.lib_AuthnResponse;
import liberty_iff_2003_08.lib_AuthnResponseEnvelope;
import liberty_iff_2003_08.lib_Extension;
import liberty_iff_2003_08.lib_FederationTerminationNotification;
import liberty_iff_2003_08.lib_GetComplete;
import liberty_iff_2003_08.lib_IDPEntries;
import liberty_iff_2003_08.lib_IDPEntry;
import liberty_iff_2003_08.lib_IDPList;
import liberty_iff_2003_08.lib_IDPProvidedNameIdentifier;
import liberty_iff_2003_08.lib_LogoutRequest;
import liberty_iff_2003_08.lib_LogoutResponse;
import liberty_iff_2003_08.lib_NameIDPolicy;
import liberty_iff_2003_08.lib_OldProvidedNameIdentifier;
import liberty_iff_2003_08.lib_ProtocolProfile;
import liberty_iff_2003_08.lib_ProviderID;
import liberty_iff_2003_08.lib_RegisterNameIdentifierRequest;
import liberty_iff_2003_08.lib_RegisterNameIdentifierResponse;
import liberty_iff_2003_08.lib_RelayState;
import liberty_iff_2003_08.lib_SPProvidedNameIdentifier;
import liberty_iff_2003_08.lib_consent$;
import org.junit.Ignore;

@Ignore("Make this a real test!")
public class LibRegressionTest extends RegressionTest {
  private static final String namespaceURI = "urn:liberty:iff:2003-08";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new LibRegressionTest();

  public static void main(String[] args) {
    getAuthnRequest();
    getAuthnRequestEnvelope();
    getAuthnResponse();
    getAuthnResponseEnvelope();
    getRegisterNameIdentifierRequest();
    getRegisterNameIdentifierResponse();
    getFederationTerminationNotification();
    getLogoutRequest();
    getLogoutResponse();
  }

  public static lib_ProviderID getProviderID() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_ProviderID binding = new lib_ProviderID();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_AffiliationID getAffiliationID() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_AffiliationID binding = new lib_AffiliationID();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_AuthnRequest getAuthnRequest() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_AuthnRequest binding = new lib_AuthnRequest();
    binding.set_IssueInstant$(new lib_AuthnRequest._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new lib_AuthnRequest._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new lib_AuthnRequest._MinorVersion$(getRandomInteger()));
    while (Math.random() < ADD_SEED)
      binding.addsamlp_RespondWith(SamlpRegressionTest.getRespondWith());
    binding.set_RequestID$(new lib_AuthnRequest._RequestID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());

    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());
    binding.addlib_ProviderID(getProviderID());
    binding.addlib_AffiliationID(getAffiliationID());
    binding.addlib_NameIDPolicy(getNameIDPolicy());
    binding.add_ForceAuthn(new lib_AuthnRequest._ForceAuthn(getRandomBoolean()));
    binding.add_IsPassive(new lib_AuthnRequest._IsPassive(getRandomBoolean()));
    binding.addlib_ProtocolProfile(getProtocolProfile());
    binding.add_AssertionConsumerServiceID(new lib_AuthnRequest._AssertionConsumerServiceID(getRandomString()));

    lib_AuthnContext._AuthnContextComparison authnContextComparison = null;
    double random = Math.random();
    if (random < 1 / 3)
      authnContextComparison = new lib_AuthnContext._AuthnContextComparison(lib_AuthnContext._AuthnContextComparison.BETTER);
    else if (random < 2 / 3)
      authnContextComparison = new lib_AuthnContext._AuthnContextComparison(lib_AuthnContext._AuthnContextComparison.EXACT);
    else
      authnContextComparison = new lib_AuthnContext._AuthnContextComparison(lib_AuthnContext._AuthnContextComparison.MINIMUM);

    lib_AuthnContext authnContext = new lib_AuthnContext();
    authnContext.add_AuthnContextComparison(authnContextComparison);

    if (Math.random() < CHOICE_SEED)
      do
        authnContext.add_AuthnContextClassRef(new lib_AuthnContext._AuthnContextClassRef(getRandomString()));
      while(Math.random() < ADD_SEED);
    else
      do
        authnContext.add_AuthnContextStatementRef(new lib_AuthnContext._AuthnContextStatementRef(getRandomString()));
      while(Math.random() < ADD_SEED);

    binding.addlib_AuthnContext(authnContext);
    binding.addlib_RelayState(getRelayState());
    binding.setlib_consent$(new lib_consent$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_NameIDPolicy getNameIDPolicy() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_NameIDPolicy binding;
    double random = Math.random();
    if (random < 1 / 4)
      binding = new lib_NameIDPolicy(lib_NameIDPolicy.FEDERATED);
    else if (random < 1 / 2)
      binding = new lib_NameIDPolicy(lib_NameIDPolicy.NONE);
    else if (random < 3 / 4)
      binding = new lib_NameIDPolicy(lib_NameIDPolicy.ONETIME);
    else
      binding = new lib_NameIDPolicy(lib_NameIDPolicy.ANY);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_RelayState getRelayState() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_RelayState binding = new lib_RelayState();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_ProtocolProfile getProtocolProfile() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_ProtocolProfile binding = new lib_ProtocolProfile();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_AuthnContext getAuthnContext() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_AuthnContext binding = new lib_AuthnContext();
    if (Math.random() < CHOICE_SEED)
      do
        binding.add_AuthnContextClassRef(new lib_AuthnContext._AuthnContextClassRef(getRandomString()));
      while(Math.random() < ADD_SEED);
    else
      do
        binding.add_AuthnContextStatementRef(new lib_AuthnContext._AuthnContextStatementRef(getRandomString()));
      while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_AuthnResponse getAuthnResponse() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_AuthnResponse binding = new lib_AuthnResponse();
    while (Math.random() < ADD_SEED)
      binding.addsaml_Assertion(getAssertion());
    binding.set_InResponseTo$(new lib_AuthnResponse._InResponseTo$(getRandomString()));
    binding.set_IssueInstant$(new lib_AuthnResponse._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new lib_AuthnResponse._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new lib_AuthnResponse._MinorVersion$(getRandomInteger()));
    binding.set_Recipient$(new lib_AuthnResponse._Recipient$(getRandomString()));
    binding.set_ResponseID$(new lib_AuthnResponse._ResponseID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());
    binding.addsamlp_Status(SamlpRegressionTest.getStatus());

    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());
    binding.addlib_ProviderID(getProviderID());
    binding.addlib_RelayState(getRelayState());
    binding.setlib_consent$(new lib_consent$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_Assertion getAssertion() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_Assertion binding = new lib_Assertion();
    do
      binding.addsaml_AttributeStatement(SamlRegressionTest.getAttributeStatement());
    while(Math.random() < ADD_SEED);
    while (Math.random() < ADD_SEED)
      binding.addsaml_AuthenticationStatement(getAuthenticationStatement());
    while (Math.random() < ADD_SEED)
      binding.addsaml_AuthorizationDecisionStatement(SamlRegressionTest.getAuthorizationDecisionStatement());
    while (Math.random() < ADD_SEED)
      binding.addsaml_Statement(SamlRegressionTest.getStatement());
    while (Math.random() < ADD_SEED)
      binding.addsaml_SubjectStatement(SamlRegressionTest.getSubjectStatement());
    if (Math.random() < ADD_SEED)
      binding.addsaml_Advice(SamlRegressionTest.getAdvice());
    binding.set_AssertionID$(new lib_Assertion._AssertionID$(getRandomString()));
    if (Math.random() < ADD_SEED)
      binding.addsaml_Conditions(SamlRegressionTest.getConditions());
    binding.set_IssueInstant$(new lib_Assertion._IssueInstant$(getRandomDateTime()));
    binding.set_Issuer$(new lib_Assertion._Issuer$(getRandomString()));
    binding.set_MajorVersion$(new lib_Assertion._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new lib_Assertion._MinorVersion$(getRandomInteger()));
    if (Math.random() < ADD_SEED)
      binding.addds_Signature(DsRegressionTest.getSignature());

    binding.set_InResponseTo$(new lib_Assertion._InResponseTo$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_AuthnRequestEnvelope getAuthnRequestEnvelope() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_AuthnRequestEnvelope binding = new lib_AuthnRequestEnvelope();
    binding.addlib_AuthnRequest(getAuthnRequest());
    binding.addlib_ProviderID(getProviderID());
    binding.add_ProviderName(new lib_AuthnRequestEnvelope._ProviderName(getRandomString()));
    binding.add_AssertionConsumerServiceURL(new lib_AuthnRequestEnvelope._AssertionConsumerServiceURL(getRandomString()));
    binding.addlib_IDPList(getIDPList());
    binding.add_IsPassive(new lib_AuthnRequestEnvelope._IsPassive(getRandomBoolean()));

    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_IDPList getIDPList() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_IDPList binding = new lib_IDPList();
    binding.addlib_IDPEntries(getIDPEntries());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public final static lib_IDPEntry getIDPEntry() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_IDPEntry binding = new lib_IDPEntry();
    binding.addlib_ProviderID(getProviderID());
    binding.add_ProviderName(new lib_IDPEntry._ProviderName(getRandomString()));
    binding.add_Loc(new lib_IDPEntry._Loc(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_IDPEntries getIDPEntries() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_IDPEntries binding = new lib_IDPEntries();
    do
      binding.addlib_IDPEntry(getIDPEntry());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_GetComplete getGetComplete() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_GetComplete binding = new lib_GetComplete();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_AuthnResponseEnvelope getAuthnResponseEnvelope() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_AuthnResponseEnvelope binding = new lib_AuthnResponseEnvelope();
    binding.addlib_AuthnResponse(getAuthnResponse());
    binding.add_AssertionConsumerServiceURL(new lib_AuthnResponseEnvelope._AssertionConsumerServiceURL(getRandomString()));
    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_RegisterNameIdentifierRequest getRegisterNameIdentifierRequest() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_RegisterNameIdentifierRequest binding = new lib_RegisterNameIdentifierRequest();
    while (Math.random() < ADD_SEED)
      binding.addsamlp_RespondWith(SamlpRegressionTest.getRespondWith());
    binding.set_IssueInstant$(new lib_RegisterNameIdentifierRequest._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new lib_RegisterNameIdentifierRequest._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new lib_RegisterNameIdentifierRequest._MinorVersion$(getRandomInteger()));
    binding.set_RequestID$(new lib_RegisterNameIdentifierRequest._RequestID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());

    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());
    binding.addlib_ProviderID(getProviderID());
    binding.addlib_IDPProvidedNameIdentifier(getIDPProvidedNameIdentifier());
    binding.addlib_SPProvidedNameIdentifier(getSPProvidedNameIdentifier());
    binding.addlib_OldProvidedNameIdentifier(getOldProvidedNameIdentifier());
    binding.addlib_RelayState(getRelayState());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_IDPProvidedNameIdentifier getIDPProvidedNameIdentifier() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_IDPProvidedNameIdentifier binding = new lib_IDPProvidedNameIdentifier();
    binding.set_Format$(new lib_IDPProvidedNameIdentifier._Format$(getRandomString()));
    binding.set_NameQualifier$(new lib_IDPProvidedNameIdentifier._NameQualifier$(getRandomString()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_SPProvidedNameIdentifier getSPProvidedNameIdentifier() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_SPProvidedNameIdentifier binding = new lib_SPProvidedNameIdentifier();
    binding.set_Format$(new lib_SPProvidedNameIdentifier._Format$(getRandomString()));
    binding.set_NameQualifier$(new lib_SPProvidedNameIdentifier._NameQualifier$(getRandomString()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_OldProvidedNameIdentifier getOldProvidedNameIdentifier() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_OldProvidedNameIdentifier binding = new lib_OldProvidedNameIdentifier();
    binding.set_Format$(new lib_OldProvidedNameIdentifier._Format$(getRandomString()));
    binding.set_NameQualifier$(new lib_OldProvidedNameIdentifier._NameQualifier$(getRandomString()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_RegisterNameIdentifierResponse getRegisterNameIdentifierResponse() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_RegisterNameIdentifierResponse binding = new lib_RegisterNameIdentifierResponse();
    binding.set_InResponseTo$(new lib_RegisterNameIdentifierResponse._InResponseTo$(getRandomString()));
    binding.set_IssueInstant$(new lib_RegisterNameIdentifierResponse._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new lib_RegisterNameIdentifierResponse._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new lib_RegisterNameIdentifierResponse._MinorVersion$(getRandomInteger()));
    binding.set_Recipient$(new lib_RegisterNameIdentifierResponse._Recipient$(getRandomString()));
    binding.set_ResponseID$(new lib_RegisterNameIdentifierResponse._ResponseID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());

    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());
    binding.addlib_ProviderID(getProviderID());
    binding.addsamlp_Status(SamlpRegressionTest.getStatus());
    binding.addlib_RelayState(getRelayState());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_FederationTerminationNotification getFederationTerminationNotification() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_FederationTerminationNotification binding = new lib_FederationTerminationNotification();
    while (Math.random() < ADD_SEED)
      binding.addsamlp_RespondWith(SamlpRegressionTest.getRespondWith());
    binding.set_IssueInstant$(new lib_FederationTerminationNotification._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new lib_FederationTerminationNotification._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new lib_FederationTerminationNotification._MinorVersion$(getRandomInteger()));
    binding.set_RequestID$(new lib_FederationTerminationNotification._RequestID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());

    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());
    binding.addlib_ProviderID(getProviderID());
    binding.addsaml_NameIdentifier(getIDPProvidedNameIdentifier());
    binding.setlib_consent$(new lib_consent$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_LogoutRequest getLogoutRequest() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_LogoutRequest binding = new lib_LogoutRequest();
    while (Math.random() < ADD_SEED)
      binding.addsamlp_RespondWith(SamlpRegressionTest.getRespondWith());
    binding.set_IssueInstant$(new lib_LogoutRequest._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new lib_LogoutRequest._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new lib_LogoutRequest._MinorVersion$(getRandomInteger()));
    binding.set_RequestID$(new lib_LogoutRequest._RequestID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());

    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());
    binding.addlib_ProviderID(getProviderID());
    binding.addsaml_NameIdentifier(getSPProvidedNameIdentifier());
    binding.add_SessionIndex(new lib_LogoutRequest._SessionIndex(getRandomString()));
    binding.addlib_RelayState(getRelayState());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static lib_LogoutResponse getLogoutResponse() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_LogoutResponse binding = new lib_LogoutResponse();
    while (Math.random() < ADD_SEED)
      binding.addlib_Extension(getExtension());
    binding.addlib_ProviderID(getProviderID());
    binding.addsamlp_Status(SamlpRegressionTest.getStatus());
    binding.addlib_RelayState(getRelayState());

    binding.set_InResponseTo$(new lib_LogoutResponse._InResponseTo$(getRandomString()));
    binding.set_IssueInstant$(new lib_LogoutResponse._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new lib_LogoutResponse._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new lib_LogoutResponse._MinorVersion$(getRandomInteger()));
    binding.set_Recipient$(new lib_LogoutResponse._Recipient$(getRandomString()));
    binding.set_ResponseID$(new lib_LogoutResponse._ResponseID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  // NOTE: There is not element that encompases this type directly.
  public static final $lib_AuthenticationStatementType getAuthenticationStatement() {
    $lib_AuthenticationStatementType binding = new $lib_AuthenticationStatementType() {
      protected $lib_AuthenticationStatementType inherits() {
        return null;
      }
    };
    binding.set_AuthenticationInstant$(new $lib_AuthenticationStatementType._AuthenticationInstant$(getRandomDateTime()));
    binding.set_AuthenticationMethod$(new $lib_AuthenticationStatementType._AuthenticationMethod$(getRandomString()));
    while (Math.random() < ADD_SEED)
      binding.addsaml_AuthorityBinding(SamlRegressionTest.getAuthorityBinding());
    binding.addsaml_Subject(SamlRegressionTest.getSubject());
    binding.addsaml_SubjectLocality(SamlRegressionTest.getSubjectLocality());

    $lib_AuthenticationStatementType._AuthnContext authnContext = new $lib_AuthenticationStatementType._AuthnContext();
//      authnContext.addlib_AuthnContextClassRef(new lib_AuthenticationStatementType.AuthnContext.AuthnContextClassRef(getRandomString()));
    if (Math.random() < CHOICE_SEED)
      authnContext.addac_AuthenticationContextStatement(AcRegressionTest.getAuthenticationContextStatement());
//      else
    //          authnContext.addlib_AuthnContextStatementRef(new lib_AuthenticationStatementType.AuthnContext.AuthnContextStatementRef(getRandomString()));
    binding.add_AuthnContext(authnContext);

    return binding;
  }

  public static lib_Extension getExtension() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    lib_Extension binding = new lib_Extension();
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