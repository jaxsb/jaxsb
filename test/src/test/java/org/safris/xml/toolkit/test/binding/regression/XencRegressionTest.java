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
import org.w3.x2001.x04.xmlenc.$xenc_EncryptedDataType;
import org.w3.x2001.x04.xmlenc.$xenc_EncryptedKeyType;
import org.w3.x2001.x04.xmlenc.$xenc_EncryptionMethodType;
import org.w3.x2001.x04.xmlenc.$xenc_ReferenceType;
import org.w3.x2001.x04.xmlenc.$xenc_TransformsType;
import org.w3.x2001.x04.xmlenc.xenc_AgreementMethod;
import org.w3.x2001.x04.xmlenc.xenc_CipherData;
import org.w3.x2001.x04.xmlenc.xenc_CipherReference;
import org.w3.x2001.x04.xmlenc.xenc_EncryptedData;
import org.w3.x2001.x04.xmlenc.xenc_EncryptedKey;
import org.w3.x2001.x04.xmlenc.xenc_EncryptionProperties;
import org.w3.x2001.x04.xmlenc.xenc_EncryptionProperty;
import org.w3.x2001.x04.xmlenc.xenc_ReferenceList;

@Ignore("Make this a real test!")
public class XencRegressionTest extends RegressionTest {
  private static final String namespaceURI = "http://www.w3.org/2001/04/xmlenc#";

  public static String getNamespaceURI() {
    return namespaceURI;
  }

  private static RegressionTest instance = new XencRegressionTest();

  public static void main(String[] args) {
    getCipherData();
    getEncryptedData();
    getEncryptedKey();
    getAgreementMethod();
    getEncryptionProperties();
  }

  public static xenc_CipherData getCipherData() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    xenc_CipherData binding = new xenc_CipherData();
    if (Math.random() < CHOICE_SEED)
      binding.add_CipherValue(new xenc_CipherData._CipherValue(getBase64Binary()));
    else
      binding.addxenc_CipherReference(getCipherReference());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static xenc_CipherReference getCipherReference() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    xenc_CipherReference binding = new xenc_CipherReference();
    $xenc_TransformsType transforms = new $xenc_TransformsType() {
      protected $xenc_TransformsType inherits() {
        return null;
      }
    };
    do
    transforms.addds_Transform(DsRegressionTest.getTransform());
    while(Math.random() < ADD_SEED);
    binding.set_URI$(new xenc_CipherReference._URI$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static $xenc_EncryptedDataType getEncryptedData() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    xenc_EncryptedData binding = new xenc_EncryptedData(getEncryptedDataType());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static xenc_EncryptedKey getEncryptedKey() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    xenc_EncryptedKey binding = new xenc_EncryptedKey(getEncryptedKeyType());
    binding.addxenc_ReferenceList(getReferenceList());
    binding.add_CarriedKeyName(new xenc_EncryptedKey._CarriedKeyName(getRandomString()));
    binding.set_Recipient$(new xenc_EncryptedKey._Recipient$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static xenc_AgreementMethod getAgreementMethod() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    xenc_AgreementMethod binding = new xenc_AgreementMethod();
    binding.add_KA_Nonce(new xenc_AgreementMethod._KA_Nonce(getBase64Binary()));
    binding.add_OriginatorKeyInfo(new xenc_AgreementMethod._OriginatorKeyInfo(DsRegressionTest.getKeyInfo()));
    binding.add_RecipientKeyInfo(DsRegressionTest.getKeyInfo());
    binding.set_Algorithm$(new xenc_AgreementMethod._Algorithm$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static xenc_ReferenceList getReferenceList() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    xenc_ReferenceList binding = new xenc_ReferenceList();
    if (Math.random() < CHOICE_SEED)
      binding.add_DataReference(getReferenceType());
    else
      binding.add_KeyReference(getReferenceType());

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static final $xenc_ReferenceType getReferenceType() {
    $xenc_ReferenceType binding = new $xenc_ReferenceType()
    {
      protected $xenc_ReferenceType inherits() {
        return null;
      }
    };
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
    binding.set_URI$(new $xenc_ReferenceType._URI$(getRandomString()));
    return binding;
  }

  public static xenc_EncryptionProperties getEncryptionProperties() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    xenc_EncryptionProperties binding = new xenc_EncryptionProperties();
    do
    binding.addxenc_EncryptionProperty(getEncryptionProperty());
    while(Math.random() < ADD_SEED);
    binding.set_Id$(new xenc_EncryptionProperties._Id$(getRandomString()));

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static xenc_EncryptionProperty getEncryptionProperty() {
    boolean verifiable = isVerifiable();
    if (verifiable)
      setVerifiable(false);

    xenc_EncryptionProperty binding = new xenc_EncryptionProperty(getRandomString());
    do
    binding.addAny(instance.getAny());
    while(Math.random() < ADD_SEED);
    binding.set_Target$(new xenc_EncryptionProperty._Target$(getRandomString()));
    binding.set_Id$(new xenc_EncryptionProperty._Id$(getRandomString()));
    // FIXME: $ cannot figure out how this works... What am $ missing?
    /*      do
     binding.addAny$ibute(new $ibute("http://www.w3.org/XML/1998/namespace", "lang", new QName(getRandomString() + ":" + getRandomString(), getRandomString()), getRandomString().substring(0, 2)));
     while(Math.random() < ADD_SEED);

     do
     binding.addAny$ibute(new $ibute("http://www.w3.org/XML/1998/namespace", "space", new QName(getRandomString() + ":" + getRandomString(), getRandomString()), getRandomString().substring(0, 2)));
     while(Math.random() < ADD_SEED);*/

    if (verifiable) {
      verify(binding);
      setVerifiable(true);
    }
    return binding;
  }

  public static final $xenc_EncryptedKeyType getEncryptedKeyType() {
    $xenc_EncryptedKeyType binding = new $xenc_EncryptedKeyType() {
      protected $xenc_EncryptedKeyType inherits() {
        return null;
      }
    };

    binding.add_EncryptionMethod(new $xenc_EncryptedDataType._EncryptionMethod(getEncryptionMethodType()));
    binding.addds_KeyInfo(DsRegressionTest.getKeyInfo());
    binding.addxenc_CipherData(getCipherData());
    binding.addxenc_EncryptionProperties(getEncryptionProperties());
    binding.set_Id$(new $xenc_EncryptedDataType._Id$(getRandomString()));
    binding.set_Type$(new $xenc_EncryptedDataType._Type$(getRandomString()));
    binding.set_MimeType$(new $xenc_EncryptedDataType._MimeType$(getRandomString()));
    binding.set_Encoding$(new $xenc_EncryptedDataType._Encoding$(getRandomString()));
    binding.addxenc_ReferenceList(getReferenceList());
    binding.add_CarriedKeyName(new $xenc_EncryptedKeyType._CarriedKeyName(getRandomString()));
    return binding;
  }

  public static final $xenc_EncryptedDataType getEncryptedDataType() {
    $xenc_EncryptedDataType binding = new $xenc_EncryptedDataType() {
      protected $xenc_EncryptedDataType inherits() {
        return null;
      }
    };

    binding.add_EncryptionMethod(new $xenc_EncryptedDataType._EncryptionMethod(getEncryptionMethodType()));
    binding.addds_KeyInfo(DsRegressionTest.getKeyInfo());
    binding.addxenc_CipherData(getCipherData());
    binding.addxenc_EncryptionProperties(getEncryptionProperties());
    binding.set_Id$(new $xenc_EncryptedDataType._Id$(getRandomString()));
    binding.set_Type$(new $xenc_EncryptedDataType._Type$(getRandomString()));
    binding.set_MimeType$(new $xenc_EncryptedDataType._MimeType$(getRandomString()));
    binding.set_Encoding$(new $xenc_EncryptedDataType._Encoding$(getRandomString()));
    return binding;
  }

  public static final $xenc_EncryptionMethodType getEncryptionMethodType() {
    $xenc_EncryptionMethodType binding = new $xenc_EncryptionMethodType() {
      protected $xenc_EncryptionMethodType inherits() {
        return null;
      }
    };

    binding.add_KeySize(new $xenc_EncryptionMethodType._KeySize(getRandomInteger()));
    binding.add_OAEPparams(new $xenc_EncryptionMethodType._OAEPparams(getBase64Binary()));
    while (Math.random() < ADD_SEED)
      binding.addAny(instance.getAny());
    binding.set_Algorithm$(new $xenc_EncryptionMethodType._Algorithm$(getRandomString()));
    return binding;
  }
}