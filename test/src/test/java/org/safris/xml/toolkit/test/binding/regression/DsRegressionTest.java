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
import org.w3.x2000.x09.xmldsig.$ds_X509IssuerSerialType;
import org.w3.x2000.x09.xmldsig.ds_CanonicalizationMethod;
import org.w3.x2000.x09.xmldsig.ds_DSAKeyValue;
import org.w3.x2000.x09.xmldsig.ds_DigestMethod;
import org.w3.x2000.x09.xmldsig.ds_DigestValue;
import org.w3.x2000.x09.xmldsig.ds_KeyInfo;
import org.w3.x2000.x09.xmldsig.ds_KeyName;
import org.w3.x2000.x09.xmldsig.ds_KeyValue;
import org.w3.x2000.x09.xmldsig.ds_Manifest;
import org.w3.x2000.x09.xmldsig.ds_MgmtData;
import org.w3.x2000.x09.xmldsig.ds_Object;
import org.w3.x2000.x09.xmldsig.ds_PGPData;
import org.w3.x2000.x09.xmldsig.ds_RSAKeyValue;
import org.w3.x2000.x09.xmldsig.ds_Reference;
import org.w3.x2000.x09.xmldsig.ds_RetrievalMethod;
import org.w3.x2000.x09.xmldsig.ds_SPKIData;
import org.w3.x2000.x09.xmldsig.ds_Signature;
import org.w3.x2000.x09.xmldsig.ds_SignatureMethod;
import org.w3.x2000.x09.xmldsig.ds_SignatureProperties;
import org.w3.x2000.x09.xmldsig.ds_SignatureProperty;
import org.w3.x2000.x09.xmldsig.ds_SignatureValue;
import org.w3.x2000.x09.xmldsig.ds_SignedInfo;
import org.w3.x2000.x09.xmldsig.ds_Transform;
import org.w3.x2000.x09.xmldsig.ds_Transforms;
import org.w3.x2000.x09.xmldsig.ds_X509Data;

@Ignore("Make this a real test!")
public class DsRegressionTest extends RegressionTest {
  private static final String namespaceURI = "http://www.w3.org/2000/09/xmldsig#";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new DsRegressionTest();

  public static void main(String[] args) {
    getSignatureProperties();
    getManifest();
    getSignature();
  }

  public static ds_Signature getSignature() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_Signature binding = new ds_Signature();
    binding.set_Id$(new ds_Signature._Id$(getRandomString()));
    binding.addds_KeyInfo(getKeyInfo());
    do
      binding.addds_Object(getObject());
    while(Math.random() < ADD_SEED);
    binding.addds_SignatureValue(getSignatureValue());
    binding.addds_SignedInfo(getSignedInfo());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_SignatureValue getSignatureValue() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_SignatureValue binding = new ds_SignatureValue();
    binding.set_Id$(new ds_SignatureValue._Id$(getRandomString()));
    binding.setText(getBase64Binary());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_SignedInfo getSignedInfo() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_SignedInfo binding = new ds_SignedInfo();
    binding.addds_CanonicalizationMethod(getCanonicalizationMethod());
    binding.set_Id$(new ds_SignedInfo._Id$(getRandomString()));
    do
      binding.addds_Reference(getReference());
    while(Math.random() < ADD_SEED);
    binding.addds_SignatureMethod(getSignatureMethod());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_CanonicalizationMethod getCanonicalizationMethod() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_CanonicalizationMethod binding = new ds_CanonicalizationMethod();
    binding.set_Algorithm$(new ds_CanonicalizationMethod._Algorithm$(getRandomString()));
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_SignatureMethod getSignatureMethod() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_SignatureMethod binding = new ds_SignatureMethod();
    binding.set_Algorithm$(new ds_SignatureMethod._Algorithm$(getRandomString()));
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
    binding.add_HMACOutputLength(new ds_SignatureMethod._HMACOutputLength(getRandomInteger()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_Reference getReference() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_Reference binding = new ds_Reference();
    binding.addds_DigestMethod(getDigestMethod());
    binding.addds_DigestValue(getDigestValue());
    binding.set_Id$(new ds_Reference._Id$(getRandomString()));
    binding.addds_Transforms(getTransforms());
    binding.set_Type$(new ds_Reference._Type$(getRandomString()));
    binding.set_URI$(new ds_Reference._URI$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_Transforms getTransforms() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_Transforms binding = new ds_Transforms();
    do
      binding.addds_Transform(getTransform());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_Transform getTransform() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_Transform binding = new ds_Transform();
    binding.set_Algorithm$(new ds_Transform._Algorithm$(getRandomString()));
    double random = Math.random();
    if (random < 1 / 2)
      while (Math.random() < ADD_SEED)
        binding.addAny(instance.getAny());
    else
      while (Math.random() < ADD_SEED)
        binding.add_XPath(new ds_Transform._XPath(getRandomString()));

    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_DigestMethod getDigestMethod() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_DigestMethod binding = new ds_DigestMethod();
    binding.set_Algorithm$(new ds_DigestMethod._Algorithm$(getRandomString()));
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_DigestValue getDigestValue() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_DigestValue binding = new ds_DigestValue();
    binding.setText(getBase64Binary());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_KeyInfo getKeyInfo() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_KeyInfo binding = new ds_KeyInfo();
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
    do
      binding.addds_KeyName(getKeyName());
    while(Math.random() < ADD_SEED);
    do
      binding.addds_KeyValue(getKeyValue());
    while(Math.random() < ADD_SEED);
    do
      binding.addds_MgmtData(getMgmtData());
    while(Math.random() < ADD_SEED);
    do
      binding.addds_PGPData(getPGPData());
    while(Math.random() < ADD_SEED);
    do
      binding.addds_RetrievalMethod(getRetrievalMethod());
    while(Math.random() < ADD_SEED);
    do
      binding.addds_SPKIData(getSPKIData());
    while(Math.random() < ADD_SEED);
    do
      binding.addds_X509Data(getX509Data());
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_KeyName getKeyName() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_KeyName binding = new ds_KeyName();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_MgmtData getMgmtData() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_MgmtData binding = new ds_MgmtData();
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_KeyValue getKeyValue() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_KeyValue binding = new ds_KeyValue();
    double random = Math.random();
    if (random < 1 / 3)
      binding.addAny(instance.getAny());
    else if (random < 2 / 3)
      binding.addds_DSAKeyValue(getds_AKeyValue());
    else
      binding.addds_RSAKeyValue(getRSAKeyValue());
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_RetrievalMethod getRetrievalMethod() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_RetrievalMethod binding = new ds_RetrievalMethod();
    binding.addds_Transforms(getTransforms());
    binding.set_Type$(new ds_RetrievalMethod._Type$(getRandomString()));
    binding.set_URI$(new ds_RetrievalMethod._URI$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_X509Data getX509Data() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_X509Data binding = new ds_X509Data();
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());

    do
      binding.add_X509Certificate(new ds_X509Data._X509Certificate(getBase64Binary()));
    while(Math.random() < ADD_SEED);
    do
      binding.add_X509CRL(new ds_X509Data._X509CRL(getBase64Binary()));
    while(Math.random() < ADD_SEED);
    do
      binding.add_X509IssuerSerial(getIX509$ssuerSerialType());
    while(Math.random() < ADD_SEED);
    do
      binding.add_X509SKI(new ds_X509Data._X509SKI(getBase64Binary()));
    while(Math.random() < ADD_SEED);
    do
      binding.add_X509SubjectName(new ds_X509Data._X509SubjectName(getRandomString()));
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_PGPData getPGPData() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_PGPData binding = new ds_PGPData();
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
    binding.add_PGPKeyID(new ds_PGPData._PGPKeyID(getBase64Binary()));
    binding.add_PGPKeyPacket(new ds_PGPData._PGPKeyPacket(getBase64Binary()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_SPKIData getSPKIData() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_SPKIData binding = new ds_SPKIData();
    // FIXME: This is an "if" because this is a
    // FIXME: <sequence maxOccurs="unbounded">. How do we deal with this?
    // FIXME: Make Sequence inner classes? Blah!
    if (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());

    do
      binding.add_SPKISexp(new ds_SPKIData._SPKISexp(getBase64Binary()));
    while(Math.random() < ADD_SEED);

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_Object getObject() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_Object binding = new ds_Object();
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
    binding.set_Encoding$(new ds_Object._Encoding$(getRandomString()));
    binding.set_Id$(new ds_Object._Id$(getRandomString()));
    binding.set_MimeType$(new ds_Object._MimeType$(getRandomString()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_Manifest getManifest() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_Manifest binding = new ds_Manifest();
    do
      binding.addds_Reference(getReference());
    while(Math.random() < ADD_SEED);
    binding.set_Id$(new ds_Manifest._Id$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_SignatureProperties getSignatureProperties() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_SignatureProperties binding = new ds_SignatureProperties();
    do
      binding.addds_SignatureProperty(getSignatureProperty());
    while(Math.random() < ADD_SEED);
    binding.set_Id$(new ds_SignatureProperties._Id$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_SignatureProperty getSignatureProperty() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_SignatureProperty binding = new ds_SignatureProperty();
    do
      binding.addAny(instance.getAny());
    while(Math.random() < ADD_SEED);
    binding.set_Id$(new ds_SignatureProperty._Id$(getRandomString()));
    binding.set_Target$(new ds_SignatureProperty._Target$(getRandomString()));
    binding.setText(getRandomString());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_DSAKeyValue getds_AKeyValue() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_DSAKeyValue binding = new ds_DSAKeyValue();
    binding.add_G(new ds_DSAKeyValue._G(getBase64Binary()));
    binding.add_J(new ds_DSAKeyValue._J(getBase64Binary()));
    binding.add_P(new ds_DSAKeyValue._P(getBase64Binary()));
    binding.add_PgenCounter(new ds_DSAKeyValue._PgenCounter(getBase64Binary()));
    binding.add_Q(new ds_DSAKeyValue._Q(getBase64Binary()));
    binding.add_Seed(new ds_DSAKeyValue._Seed(getBase64Binary()));
    binding.add_Y(new ds_DSAKeyValue._Y(getBase64Binary()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static ds_RSAKeyValue getRSAKeyValue() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    ds_RSAKeyValue binding = new ds_RSAKeyValue();
    binding.add_Exponent(new ds_RSAKeyValue._Exponent(getBase64Binary()));
    binding.add_Modulus(new ds_RSAKeyValue._Modulus(getBase64Binary()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }

    return binding;
  }

  public static final $ds_X509IssuerSerialType getIX509$ssuerSerialType() {
    final ds_X509Data._X509IssuerSerial binding = new ds_X509Data._X509IssuerSerial();
    binding.add_X509IssuerName(new ds_X509Data._X509IssuerSerial._X509IssuerName(getRandomString()));
    binding.add_X509SerialNumber(new ds_X509Data._X509IssuerSerial._X509SerialNumber(getRandomInteger()));
    return binding;
  }
}