/* Copyright (c) 2019 JAX-SB
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

package org.jaxsb.generator;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.apache.xerces.jaxp.datatype.Duration;
import org.jaxsb.runtime.Binding;
import org.jaxsb.runtime.Bindings;
import org.jaxsb.www.test.attribute.xAA.Attribute;
import org.junit.Ignore;
import org.junit.Test;
import org.libj.util.CollectionUtil;
import org.openjax.xml.datatype.Base64Binary;
import org.openjax.xml.datatype.Date;
import org.openjax.xml.datatype.DateTime;
import org.openjax.xml.datatype.Day;
import org.openjax.xml.datatype.HexBinary;
import org.openjax.xml.datatype.Language;
import org.openjax.xml.datatype.Month;
import org.openjax.xml.datatype.MonthDay;
import org.openjax.xml.datatype.Time;
import org.openjax.xml.datatype.Year;
import org.openjax.xml.datatype.YearMonth;
import org.openjax.xml.dom.DOMStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class ParseMarshalITest {
  private static final Logger logger = LoggerFactory.getLogger(ParseMarshalITest.class);

  @Test
  public void testAttribute() throws IOException, SAXException, URISyntaxException {
    final Attribute binding = (Attribute)Bindings.parse(ClassLoader.getSystemClassLoader().getResource("attribute.xml"));
    String xml = binding.toString(DOMStyle.INDENT);
    String xml2 = binding.toString(DOMStyle.INDENT);
    assertSame(xml, xml2);

    final Attribute.Local local = binding.getLocal();

    local.setLocalRaw$("bar");
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalAnyURI$(new URI("http://example2.com"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalBase64Binary$(Base64Binary.parse("bGVsbG8K"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalBoolean$(false);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalByte$((byte)129);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalDate$(Date.parse("2020-08-24"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalDateTime$(DateTime.parse("2020-08-24T17:58:49Z"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalDecimal$(new BigDecimal("4.24"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalDouble$(.58392);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalDuration$(Duration.parse("P3Y6M5DT12H35M30S"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalENTITIES$(CollectionUtil.asCollection(new ArrayList<>(), "prod563", "prod557"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalENTITY$("prod557");
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalFloat$(.4f);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalGDay$(Day.parse("---14"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalGMonth$(Month.parse("--08"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalGMonthDay$(MonthDay.parse("--11-30"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalGYear$(Year.parse("2020"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalGYearMonth$(YearMonth.parse("2021-10"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalHexBinary$(HexBinary.parse("4f3c6d78"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalID$("ref");
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalIDREF$("ref");
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalIDREFS$(CollectionUtil.asCollection(new ArrayList<>(), "ref", "local"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalInt$(421);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalInteger$(BigInteger.valueOf(23));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalLanguage$(Language.parse("th"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalLong$(991832L);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalName$("name2");
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalNCName$("ncname2");
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalNegativeInteger$(BigInteger.valueOf(-35));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalNMTOKEN$("nm_token2");
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalNMTOKENS$(CollectionUtil.asCollection(new ArrayList<>(), "nm_token2", "nm_token1"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalNonNegativeInteger$(BigInteger.valueOf(433));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalNonPositiveInteger$(BigInteger.valueOf(-13));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalNOTATION$(Attribute.Local.LocalNOTATION$.Enum.valueOf(new QName("http://www.jaxsb.org/test/attribute.xsd", "attrNotation")));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalPositiveInteger$(BigInteger.valueOf(523));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalQName$(new QName("xsi:type"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalShort$((short)5343);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalTime$(Time.parse("18:58:49Z"));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalToken$("token2");
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalUnsignedByte$((short)0);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalUnsignedInt$(84832L);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalUnsignedLong$(BigInteger.valueOf(31341L));
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
    local.setLocalUnsignedShort$(0);
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));

    final Binding b2 = Bindings.parse(xml);
    assertEquals(binding, b2);

    binding.setLocal(new Attribute.Local());
    assertNotEquals(xml, xml = binding.toString(DOMStyle.INDENT));
  }

  @Test
  @Ignore
  public void testAnyAttribute() throws IOException, SAXException {
    final Binding binding = Bindings.parse(ClassLoader.getSystemClassLoader().getResource("anyAttribute.xml"));
    final String xml = binding.toString(DOMStyle.INDENT);
    logger.info(xml);

    final Binding b2 = Bindings.parse(xml);
    assertEquals(binding, b2);
  }
}