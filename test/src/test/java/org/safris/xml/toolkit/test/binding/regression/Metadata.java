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

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import org.junit.Ignore;
import org.safris.commons.xml.binding.Base64Binary;

@Ignore("Make this a real test!")
public class Metadata {
  private static final String PASSWORD = "liberty";
  private static final String ALIAS = "client";

  protected static Base64Binary getKeyInfo(String host) {
    File keyStoreFile = null;
    try {
      final KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
      keyStoreFile = new File("src/main/resources/keystore");
      if (!keyStoreFile.exists())
        keyStoreFile = new File("src/test/resources/keystore");

      keyStore.load(new FileInputStream(keyStoreFile), PASSWORD.toCharArray());
      Certificate certificate = keyStore.getCertificate(ALIAS);
      return new Base64Binary(certificate.getEncoded());
    }
    catch (Exception e) {
      if (keyStoreFile != null)
        throw new Error(keyStoreFile.getAbsolutePath(), e);

      throw new Error(e);
    }
  }
}
