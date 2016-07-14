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

import _0_assertion.oasis_names_tc_saml_1.$saml_SubjectStatementAbstractType;
import _0_assertion.oasis_names_tc_saml_1.saml_Action;
import _0_assertion.oasis_names_tc_saml_1.saml_Advice;
import _0_assertion.oasis_names_tc_saml_1.saml_Assertion;
import _0_assertion.oasis_names_tc_saml_1.saml_AssertionIDReference;
import _0_assertion.oasis_names_tc_saml_1.saml_Attribute;
import _0_assertion.oasis_names_tc_saml_1.saml_AttributeDesignator;
import _0_assertion.oasis_names_tc_saml_1.saml_AttributeStatement;
import _0_assertion.oasis_names_tc_saml_1.saml_AttributeValue;
import _0_assertion.oasis_names_tc_saml_1.saml_Audience;
import _0_assertion.oasis_names_tc_saml_1.saml_AudienceRestrictionCondition;
import _0_assertion.oasis_names_tc_saml_1.saml_AuthenticationStatement;
import _0_assertion.oasis_names_tc_saml_1.saml_AuthorityBinding;
import _0_assertion.oasis_names_tc_saml_1.saml_AuthorizationDecisionStatement;
import _0_assertion.oasis_names_tc_saml_1.saml_Conditions;
import _0_assertion.oasis_names_tc_saml_1.saml_ConfirmationMethod;
import _0_assertion.oasis_names_tc_saml_1.saml_Evidence;
import _0_assertion.oasis_names_tc_saml_1.saml_NameIdentifier;
import _0_assertion.oasis_names_tc_saml_1.saml_Subject;
import _0_assertion.oasis_names_tc_saml_1.saml_SubjectConfirmation;
import _0_assertion.oasis_names_tc_saml_1.saml_SubjectConfirmationData;
import _0_assertion.oasis_names_tc_saml_1.saml_SubjectLocality;
import org.junit.Ignore;

@Ignore("Make this a real test!")
public class SamlRegressionTest extends RegressionTest {
  private static final String namespaceURI = "urn:oasis:names:tc:saml_:1.0:assertion";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new SamlRegressionTest();

  public static void main(String[] args) {
    getAssertion();
    getAttributeDesignator();
  }

  public static saml_AssertionIDReference getAssertionIDReference() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_AssertionIDReference binding = new saml_AssertionIDReference();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_Assertion getAssertion() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_Assertion binding = new saml_Assertion();
    binding.addsaml_Advice(getAdvice());
    binding.set_AssertionID$(new saml_Assertion._AssertionID$(getRandomString()));
    while (Math.random() < ADD_SEED)
      binding.addsaml_AttributeStatement(getAttributeStatement());
    while (Math.random() < ADD_SEED)
      binding.addsaml_AuthenticationStatement(getAuthenticationStatement());
    while (Math.random() < ADD_SEED)
      binding.addsaml_AuthorizationDecisionStatement(getAuthorizationDecisionStatement());
    binding.addsaml_Conditions(getConditions());
    do
      binding.addsaml_Statement(getStatement());
    while(Math.random() < ADD_SEED);
    while (Math.random() < ADD_SEED)
      binding.addsaml_SubjectStatement(getSubjectStatement());
    binding.set_IssueInstant$(new saml_Assertion._IssueInstant$(getRandomDateTime()));
    binding.set_Issuer$(new saml_Assertion._Issuer$(getRandomString()));
    binding.set_MajorVersion$(new saml_Assertion._MajorVersion$(getRandomInteger()));
    binding.set_MinorVersion$(new saml_Assertion._MinorVersion$(getRandomInteger()));
    binding.addds_Signature(DsRegressionTest.getSignature());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_Conditions getConditions() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_Conditions binding = new saml_Conditions();
    while (Math.random() < ADD_SEED)
      binding.addsaml_AudienceRestrictionCondition(getAudienceRestrictionCondition());
    while (Math.random() < ADD_SEED)
      binding.addsaml_Condition(getCondition());
    binding.set_NotBefore$(new saml_Conditions._NotBefore$(getRandomDateTime()));
    binding.set_NotOnOrAfter$(new saml_Conditions._NotOnOrAfter$(getRandomDateTime()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  // FIXME: Condition is a direct element of an abstract complexType.
  public static saml_AudienceRestrictionCondition getCondition() {
    return getAudienceRestrictionCondition();
  }

  public static saml_AudienceRestrictionCondition getAudienceRestrictionCondition() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_AudienceRestrictionCondition binding = new saml_AudienceRestrictionCondition();
    do
      binding.addsaml_Audience(getAudience());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_Audience getAudience() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_Audience binding = new saml_Audience();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_Advice getAdvice() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_Advice binding = new saml_Advice();
    while (Math.random() < ADD_SEED)
      binding.addsaml_Assertion(getAssertion());
    while (Math.random() < ADD_SEED)
      binding.addsaml_AssertionIDReference(getAssertionIDReference());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  // FIXME: Statement is a direct element of an abstract complexType.
  public static $saml_SubjectStatementAbstractType getStatement() {
    return getAuthenticationStatement();
  }

  // FIXME: SubjectStatement is a direct element of an abstract complexType.
  public static $saml_SubjectStatementAbstractType getSubjectStatement() {
    return getAttributeStatement();
  }

  public static saml_Subject getSubject() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_Subject binding = new saml_Subject();
    binding.addsaml_NameIdentifier(getNameIdentifier());
    binding.addsaml_SubjectConfirmation(getSubjectConfirmation());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_NameIdentifier getNameIdentifier() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_NameIdentifier binding = new saml_NameIdentifier();
    binding.set_Format$(new saml_NameIdentifier._Format$(getRandomString()));
    binding.set_NameQualifier$(new saml_NameIdentifier._NameQualifier$(getRandomString()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_SubjectConfirmation getSubjectConfirmation() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_SubjectConfirmation binding = new saml_SubjectConfirmation();
    do
    binding.addsaml_ConfirmationMethod(getConfirmationMethod());
    while(Math.random() < ADD_SEED);
    binding.addds_KeyInfo(DsRegressionTest.getKeyInfo());
    binding.addsaml_SubjectConfirmationData(getSubjectConfirmationData());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_SubjectConfirmationData getSubjectConfirmationData() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_SubjectConfirmationData binding = new saml_SubjectConfirmationData();
//      binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static saml_ConfirmationMethod getConfirmationMethod() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_ConfirmationMethod binding = new saml_ConfirmationMethod();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_AuthenticationStatement getAuthenticationStatement() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_AuthenticationStatement binding = new saml_AuthenticationStatement();
    binding.set_AuthenticationInstant$(new saml_AuthenticationStatement._AuthenticationInstant$(getRandomDateTime()));
    binding.set_AuthenticationMethod$(new saml_AuthenticationStatement._AuthenticationMethod$(getRandomString()));
    while (Math.random() < ADD_SEED)
      binding.addsaml_AuthorityBinding(getAuthorityBinding());
    binding.addsaml_Subject(getSubject());
    binding.addsaml_SubjectLocality(getSubjectLocality());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_SubjectLocality getSubjectLocality() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_SubjectLocality binding = new saml_SubjectLocality();
    binding.set_DNSAddress$(new saml_SubjectLocality._DNSAddress$(getRandomString()));
    binding.set_IPAddress$(new saml_SubjectLocality._IPAddress$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_AuthorityBinding getAuthorityBinding() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_AuthorityBinding binding = new saml_AuthorityBinding();
    binding.set_AuthorityKind$(new saml_AuthorityBinding._AuthorityKind$(getRandomQName()));
    binding.set_Binding$(new saml_AuthorityBinding._Binding$(getRandomString()));
    binding.set_Location$(new saml_AuthorityBinding._Location$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_AuthorizationDecisionStatement getAuthorizationDecisionStatement() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_AuthorizationDecisionStatement binding = new saml_AuthorizationDecisionStatement();
    do
      binding.addsaml_Action(getAction());
    while(Math.random() < ADD_SEED);
    double random = Math.random();
    if (random < 1 / 3)
      binding.set_Decision$(new saml_AuthorizationDecisionStatement._Decision$(saml_AuthorizationDecisionStatement._Decision$.INDETERMINATE));
    else if (random < 2 / 3)
      binding.set_Decision$(new saml_AuthorizationDecisionStatement._Decision$(saml_AuthorizationDecisionStatement._Decision$.DENY));
    else
      binding.set_Decision$(new saml_AuthorizationDecisionStatement._Decision$(saml_AuthorizationDecisionStatement._Decision$.PERMIT));
    if (Math.random() < ADD_SEED)
      binding.addsaml_Evidence(getEvidence());
    binding.set_Resource$(new saml_AuthorizationDecisionStatement._Resource$(getRandomString()));
    binding.addsaml_Subject(getSubject());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_Action getAction() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_Action binding = new saml_Action();
    binding.set_Namespace$(new saml_Action._Namespace$(getRandomString()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_Evidence getEvidence() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_Evidence binding = new saml_Evidence();
    do
      binding.addsaml_Assertion(getAssertion());
    while(Math.random() < ADD_SEED);
    do
      binding.addsaml_AssertionIDReference(getAssertionIDReference());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_AttributeStatement getAttributeStatement() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_AttributeStatement binding = new saml_AttributeStatement();
    do
      binding.addsaml_Attribute(getAttribute());
    while(Math.random() < ADD_SEED);
    binding.addsaml_Subject(getSubject());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_AttributeDesignator getAttributeDesignator() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_AttributeDesignator binding = new saml_AttributeDesignator();
    binding.set_AttributeName$(new saml_AttributeDesignator._AttributeName$(getRandomString()));
    binding.set_AttributeNamespace$(new saml_AttributeDesignator._AttributeNamespace$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_Attribute getAttribute() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_Attribute binding = new saml_Attribute();
    binding.set_AttributeName$(new saml_Attribute._AttributeName$(getRandomString()));
    binding.set_AttributeNamespace$(new saml_Attribute._AttributeNamespace$(getRandomString()));
    do
      binding.addsaml_AttributeValue(getAttributeValue());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static saml_AttributeValue getAttributeValue() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    saml_AttributeValue binding = new saml_AttributeValue();
//      binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }
}