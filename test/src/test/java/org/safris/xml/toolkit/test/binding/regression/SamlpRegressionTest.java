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

import _0_protocol.oasis_names_tc_saml_1.$samlp_QueryAbstractType;
import _0_protocol.oasis_names_tc_saml_1.$samlp_SubjectQueryAbstractType;
import _0_protocol.oasis_names_tc_saml_1.samlp_AssertionArtifact;
import _0_protocol.oasis_names_tc_saml_1.samlp_AttributeQuery;
import _0_protocol.oasis_names_tc_saml_1.samlp_AuthenticationQuery;
import _0_protocol.oasis_names_tc_saml_1.samlp_AuthorizationDecisionQuery;
import _0_protocol.oasis_names_tc_saml_1.samlp_Request;
import _0_protocol.oasis_names_tc_saml_1.samlp_RespondWith;
import _0_protocol.oasis_names_tc_saml_1.samlp_Response;
import _0_protocol.oasis_names_tc_saml_1.samlp_Status;
import _0_protocol.oasis_names_tc_saml_1.samlp_StatusCode;
import _0_protocol.oasis_names_tc_saml_1.samlp_StatusDetail;
import _0_protocol.oasis_names_tc_saml_1.samlp_StatusMessage;
import org.junit.Ignore;

@Ignore("Make this a real test!")
public class SamlpRegressionTest extends RegressionTest {
  private static final String namespaceURI = "urn:oasis:names:tc:Saml:1.0:protocol";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new SamlpRegressionTest();

  public static void main(String[] args) {
    getRequest();
    getResponse();
  }

  public static samlp_RespondWith getRespondWith() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_RespondWith binding = new samlp_RespondWith();
    binding.setText(getRandomQName());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_Request getRequest() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_Request binding = new samlp_Request();
    double random = Math.random();
    do
     binding.addsamlp_RespondWith(getRespondWith());
    while(Math.random() < ADD_SEED);
    binding.set_IssueInstant$(new samlp_Request._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new samlp_Request._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new samlp_Request._MinorVersion$(getRandomInteger()));
    binding.set_RequestID$(new samlp_Request._RequestID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());
    if (random < 1 / 7)
      do
        binding.addsamlp_AssertionArtifact(getAssertionArtifact());
      while(Math.random() < ADD_SEED);
    else if (random < 2 / 7)
      do
        binding.addsaml_AssertionIDReference(SamlRegressionTest.getAssertionIDReference());
      while(Math.random() < ADD_SEED);
    else if (random < 3 / 7)
      binding.addsamlp_AttributeQuery(getAttributeQuery());
    else if (random < 4 / 7)
      binding.addsamlp_AuthenticationQuery(getAuthenticationQuery());
    else if (random < 5 / 7)
      binding.addsamlp_AuthorizationDecisionQuery(getAuthorizationDecisionQuery());
    else if (random < 6 / 7)
      binding.addsamlp_Query(getQuery());
    else
      binding.addsamlp_SubjectQuery(getSubjectQuery());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_AssertionArtifact getAssertionArtifact() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_AssertionArtifact binding = new samlp_AssertionArtifact();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  // FIXME: Query is a direct element of an abstract complexType.
  public static $samlp_QueryAbstractType getQuery() {
    return getAttributeQuery();
  }

  // FIXME: SubjectQuery is a direct element of an abstract complexType.
  public static $samlp_SubjectQueryAbstractType getSubjectQuery() {
    return getAttributeQuery();
  }

  public static samlp_AuthenticationQuery getAuthenticationQuery() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_AuthenticationQuery binding = new samlp_AuthenticationQuery();
    binding.set_AuthenticationMethod$(new samlp_AuthenticationQuery._AuthenticationMethod$(getRandomString()));
    binding.addsaml_Subject(SamlRegressionTest.getSubject());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_AttributeQuery getAttributeQuery() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_AttributeQuery binding = new samlp_AttributeQuery();
    while (Math.random() < ADD_SEED)
      binding.addsaml_AttributeDesignator(SamlRegressionTest.getAttributeDesignator());

    binding.set_Resource$(new samlp_AttributeQuery._Resource$(getRandomString()));
    binding.addsaml_Subject(SamlRegressionTest.getSubject());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_AuthorizationDecisionQuery getAuthorizationDecisionQuery() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_AuthorizationDecisionQuery binding = new samlp_AuthorizationDecisionQuery();
    do
      binding.addsaml_Action(SamlRegressionTest.getAction());
    while(Math.random() < ADD_SEED);
    binding.set_Resource$(new samlp_AuthorizationDecisionQuery._Resource$(getRandomString()));
    binding.addsaml_Subject(SamlRegressionTest.getSubject());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_Response getResponse() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_Response binding = new samlp_Response();
    while (Math.random() < ADD_SEED)
      binding.addsaml_Assertion(SamlRegressionTest.getAssertion());
    binding.set_InResponseTo$(new samlp_Response._InResponseTo$(getRandomString()));
    binding.set_IssueInstant$(new samlp_Response._IssueInstant$(getRandomDateTime()));
    binding.set_MajorVersion$(new samlp_Response._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new samlp_Response._MinorVersion$(getRandomInteger()));
    binding.set_Recipient$(new samlp_Response._Recipient$(getRandomString()));
    binding.set_ResponseID$(new samlp_Response._ResponseID$(getRandomString()));
    binding.addds_Signature(DsRegressionTest.getSignature());
    binding.addsamlp_Status(getStatus());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_Status getStatus() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_Status binding = new samlp_Status();
    binding.addsamlp_StatusCode(getStatusCode());
    binding.addsamlp_StatusDetail(getStatusDetail());
    binding.addsamlp_StatusMessage(getStatusMessage());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_StatusCode getStatusCode() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_StatusCode binding = new samlp_StatusCode();

    // NOTE: We do not want a continuous recursion.
    if (Math.random() < RECURSION_SEED)
      binding.addsamlp_StatusCode(getStatusCode());
    binding.set_Value$(new samlp_StatusCode._Value$(getRandomQName()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_StatusMessage getStatusMessage() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_StatusMessage binding = new samlp_StatusMessage();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static samlp_StatusDetail getStatusDetail() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    samlp_StatusDetail binding = new samlp_StatusDetail();
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }
}