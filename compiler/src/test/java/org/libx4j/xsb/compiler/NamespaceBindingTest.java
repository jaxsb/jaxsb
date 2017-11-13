/* Copyright (c) 2006 lib4j
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

package org.libx4j.xsb.compiler;

import org.junit.Assert;
import org.junit.Test;
import org.libx4j.xsb.compiler.lang.NamespaceBinding;

public class NamespaceBindingTest {
  private static void assertEquals(final String expected, final String uri) {
    final NamespaceBinding namespaceBinding = NamespaceBinding.parseNamespace(uri);
    final NamespaceBinding decodedBinding = NamespaceBinding.parseClassName(namespaceBinding.getClassName());
//    System.err.println(namespaceBinding.getClassName());
    Assert.assertEquals("Direct", expected, namespaceBinding.getClassName());
    Assert.assertEquals("Diff", uri, decodedBinding.getNamespaceUri().toString());
  }

  @Test
  public void testNamespaceToPackage() {
    assertEquals("org.w3.www._2001.XMLSchema.yAA", "http://www.w3.org/2001/XMLSchema");
    assertEquals("org._3w.www._2001.XMLSchema.yAA", "http://www.3w.org/2001/XMLSchema");
    assertEquals("org._3w.www._2001.XMLSchema.pKCMjL2A", "ssh://www.3w.org/2001/XMLSchema");
    assertEquals("org._3w.www._2001.XMLSchema.pKGeHhzOi8v9g", "xxs://www.3w.org/2001/XMLSchema");
    assertEquals("com.sun.java.xml.ns.j2ee.yAA", "http://java.sun.com/xml/ns/j2ee");
    assertEquals("org.openuri.nameworld.jJ3CXgA", "file://openuri.org/nameworld");
    assertEquals("org.xmlsoap.schemas.soap.envelope.yNwgJeA", "http://schemas.xmlsoap.org/soap/envelope/");
    assertEquals("com.safris.www.schema.test.yAA", "http://www.safris.com/schema/test");
    assertEquals("org.w3._1999.xhtml.wH6Je", "urn://w3.org/1999/xhtml");
    assertEquals("org._3w._1999._xlink.yH$JeFfA", "http://3w.org/_1999/_xlink");
    assertEquals("org.w3.www._2000._09.xmldsig.yL8AkYA", "http://www.w3.org/2000/09/xmldsig#");
    assertEquals("org.w3.www._2001._04.xmlenc.yL7AkYA", "http://www.w3.org/2001/04/xmlenc#");
    assertEquals("org.w3.www._2001._10.xmlexcc14n.yL4AluMCW5AJG", "http://www.w3.org/2001/10/xml-exc-c14n#");
    assertEquals("org.w3.www.XML._1998.namespace.yAA", "http://www.w3.org/XML/1998/namespace");
    assertEquals("com.safris.www.schema.testtwo.bAA", "data://www.safris.com/schema/testtwo");
    assertEquals("org.safris.xml.schema.binding.test.unit.attributes.xAA", "http://xml.safris.org/schema/binding/test/unit/attributes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.complexTypes.xAA", "http://xml.safris.org/schema/binding/test/unit/complexTypes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.everything.xAA", "http://xml.safris.org/schema/binding/test/unit/everything.xsd");
    assertEquals("org.safris.xml.schema.binding_._test_.unit._everything.xL9gr$aCvgA", "http://xml.safris.org/schema/binding_/_test_/unit/_everything.xsd");
    assertEquals("xml.safrisorg.schema.binding.test.unit.lists.sHuJf4JduJeA", "https://xml/safris.org/schema/binding/test/unit/lists.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.mixed.sAA", "https://xml.safris.org/schema/binding/test/unit/mixed.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.namespace.xAA", "http://xml.safris.org/schema/binding/test/unit/namespace.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.simpleTypes.xAA", "http://xml.safris.org/schema/binding/test/unit/simpleTypes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.types.xAA", "http://xml.safris.org/schema/binding/test/unit/types.xsd");
    assertEquals("org.safris.xml.schema.binding.dbb.xAA", "http://xml.safris.org/schema/binding/dbb.xsd");
    assertEquals("org.safris.xml.toolkit.binding.manifest.xAA", "http://xml.safris.org/toolkit/binding/manifest.xsd");
    assertEquals("org.safris.xml.toolkit.binding.test.maven.xAA", "http://xml.safris.org/toolkit/binding/test/maven.xsd");
    assertEquals("org.safris.xml.toolkit.binding.tutorial.invoice.xAA", "http://xml.safris.org/toolkit/binding/tutorial/invoice.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.any.xAA", "http://xml.safris.org/toolkit/sample/binding/any.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.enums.xAA", "http://xml.safris.org/toolkit/sample/binding/enums.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.simple.xAA", "http://xml.safris.org/toolkit/sample/binding/simple.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.xsitype.xAA", "http://xml.safris.org/toolkit/sample/binding/xsitype.xsd");
    assertEquals("org.safris.xml.toolkit.tutorial.binding.beginner.invoice.xAA", "http://xml.safris.org/toolkit/tutorial/binding/beginner/invoice.xsd");
    assertEquals("test_namespace_targetNamespace.pJpCW8wlo", "test-namespace-targetNamespace");
    assertEquals("aol_liberty_config.uAA", "urn:aol:liberty:config");
    assertEquals("aol_liberty_config.uLyAnQA", "urn:aol:liberty:config:");
    assertEquals("berkeley_safris_game_chess.uAA", "urn:berkeley:safris:game:chess");
    assertEquals("liberty_ac__2003_08.uJ3Cv6Qlo", "urn:liberty:ac:_2003-08");
    assertEquals("liberty__ac__2003_08.uJxCv5Qr$sJaA", "urn:liberty:_ac_:2003-08");
    assertEquals("liberty_ac_2003_08.uJ_CWgA", "urn:liberty:ac:2003-08");
    assertEquals("liberty_ac_2004_12.uJ_CWgA", "urn:liberty:ac:2004-12");
    assertEquals("liberty_disco_2003_08.uLygloA", "urn:liberty:disco:2003-08");
    assertEquals("liberty_id_sis_pp_2003_08.uJ1CW5wlu8JaA", "urn:liberty:id-sis-pp:2003-08");
    assertEquals("liberty_iff_2003_08.uLwgloA", "urn:liberty:iff:2003-08");
    assertEquals("liberty_metadata_2003_08.uL1gloA", "urn:liberty:metadata:2003-08");
    assertEquals("liberty_sb_2003_08.uJ_CWgA", "urn:liberty:sb:2003-08");
    assertEquals("oasis_names_tc_SAML_1_0_assertion.uL1glwA", "urn:oasis:names:tc:SAML:1.0:assertion");
    assertEquals("oasis_names_tc_SAML_1_0_protocol.uL1glwA", "urn:oasis:names:tc:SAML:1.0:protocol");
  }
}