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

package org.libx4j.xsb.runtime;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.lib4j.xml.datatype.Base64Binary;
import org.lib4j.xml.datatype.Date;
import org.lib4j.xml.datatype.DateTime;
import org.lib4j.xml.datatype.Day;
import org.lib4j.xml.datatype.Duration;
import org.lib4j.xml.datatype.HexBinary;
import org.lib4j.xml.datatype.Language;
import org.lib4j.xml.datatype.Month;
import org.lib4j.xml.datatype.MonthDay;
import org.lib4j.xml.datatype.Time;
import org.lib4j.xml.datatype.Year;
import org.lib4j.xml.datatype.YearMonth;
import org.libx4j.xsb.compiler.lang.UniqueQName;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyURI;
import org.w3.www._2001.XMLSchema.yAA.$Base64Binary;
import org.w3.www._2001.XMLSchema.yAA.$Boolean;
import org.w3.www._2001.XMLSchema.yAA.$Byte;
import org.w3.www._2001.XMLSchema.yAA.$Date;
import org.w3.www._2001.XMLSchema.yAA.$DateTime;
import org.w3.www._2001.XMLSchema.yAA.$Decimal;
import org.w3.www._2001.XMLSchema.yAA.$Double;
import org.w3.www._2001.XMLSchema.yAA.$Duration;
import org.w3.www._2001.XMLSchema.yAA.$ENTITIES;
import org.w3.www._2001.XMLSchema.yAA.$ENTITY;
import org.w3.www._2001.XMLSchema.yAA.$Float;
import org.w3.www._2001.XMLSchema.yAA.$GDay;
import org.w3.www._2001.XMLSchema.yAA.$GMonth;
import org.w3.www._2001.XMLSchema.yAA.$GMonthDay;
import org.w3.www._2001.XMLSchema.yAA.$GYear;
import org.w3.www._2001.XMLSchema.yAA.$GYearMonth;
import org.w3.www._2001.XMLSchema.yAA.$HexBinary;
import org.w3.www._2001.XMLSchema.yAA.$ID;
import org.w3.www._2001.XMLSchema.yAA.$IDREF;
import org.w3.www._2001.XMLSchema.yAA.$IDREFS;
import org.w3.www._2001.XMLSchema.yAA.$Int;
import org.w3.www._2001.XMLSchema.yAA.$Integer;
import org.w3.www._2001.XMLSchema.yAA.$Language;
import org.w3.www._2001.XMLSchema.yAA.$Long;
import org.w3.www._2001.XMLSchema.yAA.$NMTOKEN;
import org.w3.www._2001.XMLSchema.yAA.$NMTOKENS;
import org.w3.www._2001.XMLSchema.yAA.$NOTATION;
import org.w3.www._2001.XMLSchema.yAA.$NegativeInteger;
import org.w3.www._2001.XMLSchema.yAA.$NonNegativeInteger;
import org.w3.www._2001.XMLSchema.yAA.$NonPositiveInteger;
import org.w3.www._2001.XMLSchema.yAA.$NormalizedString;
import org.w3.www._2001.XMLSchema.yAA.$PositiveInteger;
import org.w3.www._2001.XMLSchema.yAA.$Short;
import org.w3.www._2001.XMLSchema.yAA.$String;
import org.w3.www._2001.XMLSchema.yAA.$Time;
import org.w3.www._2001.XMLSchema.yAA.$Token;
import org.w3.www._2001.XMLSchema.yAA.$UnsignedByte;
import org.w3.www._2001.XMLSchema.yAA.$UnsignedInt;
import org.w3.www._2001.XMLSchema.yAA.$UnsignedLong;
import org.w3.www._2001.XMLSchema.yAA.$UnsignedShort;
import org.w3.www._2001.XMLSchema.yAA.$nCName;
import org.w3.www._2001.XMLSchema.yAA.$name;
import org.w3.www._2001.XMLSchema.yAA.$qName;

@SuppressWarnings("unused")
public final class XSTypeDirectory {
  private static final Map<UniqueQName,XSTypeDirectory> defaultTypes = new HashMap<UniqueQName,XSTypeDirectory>();
  private static final Map<UniqueQName,UniqueQName> typeHierarchy = new HashMap<UniqueQName,UniqueQName>();

  // may not need this...
  public static final XSTypeDirectory TYPE = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "Type"), new NativeBinding.GenericClass(Binding.class)), null);
  public static final XSTypeDirectory ANYTYPE = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anyType"), new NativeBinding.GenericClass($AnySimpleType.class), new NativeBinding.GenericClass(Serializable.class)), null);

  public static final XSTypeDirectory ANYSIMPLETYPE = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anySimpleType"), new NativeBinding.GenericClass($AnySimpleType.class), new NativeBinding.GenericClass(Serializable.class)), null);

  public static final XSTypeDirectory QNAME;

  static {
    // This section creates simpleType bindings for all of the base xs simple types.
    try {
      // FIXME: Have the nativeClasses hardcoded here be looked up using reflection during the generation process!!!!
      final XSTypeDirectory ENTITIES = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "ENTITIES"), new NativeBinding.GenericClass($ENTITIES.class), new NativeBinding.GenericClass(List.class, String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory string = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "string"), new NativeBinding.GenericClass($String.class), new NativeBinding.GenericClass(String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory normalizedString = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "normalizedString"), new NativeBinding.GenericClass($NormalizedString.class), new NativeBinding.GenericClass(String.class)), string);
      final XSTypeDirectory token = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "token"), new NativeBinding.GenericClass($Token.class), new NativeBinding.GenericClass(String.class)), normalizedString);
      final XSTypeDirectory Name = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "Name"), new NativeBinding.GenericClass($name.class), new NativeBinding.GenericClass(String.class)), token);
      final XSTypeDirectory NCName = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "NCName"), new NativeBinding.GenericClass($nCName.class), new NativeBinding.GenericClass(String.class)), Name);
      final XSTypeDirectory ENTITY = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "ENTITY"), new NativeBinding.GenericClass($ENTITY.class), new NativeBinding.GenericClass(String.class)), NCName);
      final XSTypeDirectory ID = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "ID"), new NativeBinding.GenericClass($ID.class), new NativeBinding.GenericClass(String.class)), NCName);
      final XSTypeDirectory IDREF = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "IDREF"), new NativeBinding.GenericClass($IDREF.class), new NativeBinding.GenericClass(String.class)), NCName);
      final XSTypeDirectory IDREFS = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "IDREFS"), new NativeBinding.GenericClass($IDREFS.class), new NativeBinding.GenericClass(List.class, String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory NMTOKEN = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "NMTOKEN"), new NativeBinding.GenericClass($NMTOKEN.class), new NativeBinding.GenericClass(String.class)), token);
      final XSTypeDirectory NMTOKENS = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "NMTOKENS"), new NativeBinding.GenericClass($NMTOKENS.class), new NativeBinding.GenericClass(List.class, String.class)), ANYSIMPLETYPE);
      QNAME = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "QName"), new NativeBinding.GenericClass($qName.class), new NativeBinding.GenericClass(QName.class), QName.class.getDeclaredMethod("valueOf", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory anyURI = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anyURI"), new NativeBinding.GenericClass($AnyURI.class), new NativeBinding.GenericClass(String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory base64Binary = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "base64Binary"), new NativeBinding.GenericClass($Base64Binary.class), new NativeBinding.GenericClass(Base64Binary.class), Base64Binary.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _boolean = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "boolean"), new NativeBinding.GenericClass($Boolean.class), new NativeBinding.GenericClass(Boolean.class), $Boolean.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _long = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "long"), new NativeBinding.GenericClass($Long.class), new NativeBinding.GenericClass(Long.class), Long.class.getDeclaredMethod("parseLong", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _int = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "int"), new NativeBinding.GenericClass($Int.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), _long);
      final XSTypeDirectory _short = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "short"), new NativeBinding.GenericClass($Short.class), new NativeBinding.GenericClass(Short.class), Short.class.getDeclaredMethod("parseShort", String.class)), _int);
      final XSTypeDirectory _byte = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "byte"), new NativeBinding.GenericClass($Byte.class), new NativeBinding.GenericClass(Byte.class), Byte.class.getDeclaredMethod("parseByte", String.class)), _short);
      final XSTypeDirectory date = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "date"), new NativeBinding.GenericClass($Date.class), new NativeBinding.GenericClass(Date.class), Date.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory dateTime = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "dateTime"), new NativeBinding.GenericClass($DateTime.class), new NativeBinding.GenericClass(DateTime.class), DateTime.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory decimal = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "decimal"), new NativeBinding.GenericClass($Decimal.class), new NativeBinding.GenericClass(BigDecimal.class), BigDecimal.class.getDeclaredConstructor(String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _double = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "double"), new NativeBinding.GenericClass($Double.class), new NativeBinding.GenericClass(Double.class), Double.class.getDeclaredMethod("parseDouble", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory duration = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "duration"), new NativeBinding.GenericClass($Duration.class), new NativeBinding.GenericClass(Duration.class), Duration.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _float = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "float"), new NativeBinding.GenericClass($Float.class), new NativeBinding.GenericClass(Float.class), Float.class.getDeclaredMethod("parseFloat", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gDay = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gDay"), new NativeBinding.GenericClass($GDay.class), new NativeBinding.GenericClass(Day.class), Day.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gMonth = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gMonth"), new NativeBinding.GenericClass($GMonth.class), new NativeBinding.GenericClass(Month.class), Month.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gMonthDay = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gMonthDay"), new NativeBinding.GenericClass($GMonthDay.class), new NativeBinding.GenericClass(MonthDay.class), MonthDay.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gYear = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gYear"), new NativeBinding.GenericClass($GYear.class), new NativeBinding.GenericClass(Year.class), Year.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gYearMonth = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gYearMonth"), new NativeBinding.GenericClass($GYearMonth.class), new NativeBinding.GenericClass(YearMonth.class), YearMonth.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory hexBinary = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "hexBinary"), new NativeBinding.GenericClass($HexBinary.class), new NativeBinding.GenericClass(HexBinary.class), HexBinary.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory integer = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "integer"), new NativeBinding.GenericClass($Integer.class), new NativeBinding.GenericClass(BigInteger.class), BigInteger.class.getDeclaredConstructor(String.class)), decimal);
      final XSTypeDirectory language = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "language"), new NativeBinding.GenericClass($Language.class), new NativeBinding.GenericClass(Language.class), Language.class.getDeclaredMethod("parse", String.class)), token);
      final XSTypeDirectory positiveInteger = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "positiveInteger"), new NativeBinding.GenericClass($PositiveInteger.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), integer);
      final XSTypeDirectory negativeInteger = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "negativeInteger"), new NativeBinding.GenericClass($NegativeInteger.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), positiveInteger);
      final XSTypeDirectory NOTATION = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "NOTATION"), new NativeBinding.GenericClass($NOTATION.class), new NativeBinding.GenericClass(NotationType.class), NotationType.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory nonNegativeInteger = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "nonNegativeInteger"), new NativeBinding.GenericClass($NonNegativeInteger.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), integer);
      final XSTypeDirectory nonPositiveInteger = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "nonPositiveInteger"), new NativeBinding.GenericClass($NonPositiveInteger.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), integer);
      final XSTypeDirectory time = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "time"), new NativeBinding.GenericClass($Time.class), new NativeBinding.GenericClass(Time.class), Time.class.getDeclaredMethod("parse", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory unsignedLong = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "unsignedLong"), new NativeBinding.GenericClass($UnsignedLong.class), new NativeBinding.GenericClass(BigInteger.class), BigInteger.class.getDeclaredConstructor(String.class)), nonNegativeInteger);
      final XSTypeDirectory unsignedInt = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "unsignedInt"), new NativeBinding.GenericClass($UnsignedInt.class), new NativeBinding.GenericClass(Long.class), Long.class.getDeclaredMethod("parseLong", String.class)), unsignedLong);
      final XSTypeDirectory unsignedShort = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "unsignedShort"), new NativeBinding.GenericClass($UnsignedShort.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), unsignedInt);
      final XSTypeDirectory unsignedByte = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "unsignedByte"), new NativeBinding.GenericClass($UnsignedByte.class), new NativeBinding.GenericClass(Short.class), Short.class.getDeclaredMethod("parseShort", String.class)), unsignedShort);
    }
    catch (final NoSuchMethodException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static final XSTypeDirectory parseType(final UniqueQName name) {
    return defaultTypes.get(name);
  }

  public static UniqueQName lookupSuperType(final UniqueQName name) {
    return name != null ? typeHierarchy.get(name) : null;
  }

  private final NativeBinding nativeBinding;
  private final String nativeFactory;

  public NativeBinding getNativeBinding() {
    return nativeBinding;
  }

  public String getNativeFactory() {
    return nativeFactory;
  }

  private XSTypeDirectory(final NativeBinding nativeBinding, final XSTypeDirectory superType) {
    this.nativeBinding = nativeBinding;
    if (nativeBinding.getFactoryMethod() == null) {
      this.nativeFactory = null;
    }
    else {
      if (nativeBinding.getFactoryMethod() instanceof Method)
        this.nativeFactory = ((Method)nativeBinding.getFactoryMethod()).getDeclaringClass().getCanonicalName() + "." + ((Method)nativeBinding.getFactoryMethod()).getName();
      else if (nativeBinding.getFactoryMethod() instanceof Constructor<?>)
        this.nativeFactory = "new " + ((Constructor<?>)nativeBinding.getFactoryMethod()).getDeclaringClass().getCanonicalName();
      else
        throw new UnsupportedOperationException("Unknown native binding factoryMethod type: " + nativeBinding.getFactoryMethod().getClass().getCanonicalName());
    }

    defaultTypes.put(nativeBinding.getName(), this);
    if (superType != null)
      typeHierarchy.put(nativeBinding.getName(), superType.getNativeBinding().getName());
  }
}