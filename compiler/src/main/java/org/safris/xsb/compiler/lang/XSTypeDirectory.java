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

package org.safris.xsb.compiler.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.safris.commons.xml.binding.Base64Binary;
import org.safris.commons.xml.binding.Date;
import org.safris.commons.xml.binding.DateTime;
import org.safris.commons.xml.binding.Day;
import org.safris.commons.xml.binding.Decimal;
import org.safris.commons.xml.binding.Duration;
import org.safris.commons.xml.binding.HexBinary;
import org.safris.commons.xml.binding.Language;
import org.safris.commons.xml.binding.Month;
import org.safris.commons.xml.binding.MonthDay;
import org.safris.commons.xml.binding.Time;
import org.safris.commons.xml.binding.Year;
import org.safris.commons.xml.binding.YearMonth;
import org.safris.xsb.compiler.runtime.Binding;
import org.safris.xsb.compiler.runtime.NotationType;
import org.safris.xsb.lexer.lang.UniqueQName;
import org.w3.x2001.xmlschema.xe.$xs_ENTITIES;
import org.w3.x2001.xmlschema.xe.$xs_ENTITY;
import org.w3.x2001.xmlschema.xe.$xs_ID;
import org.w3.x2001.xmlschema.xe.$xs_IDREF;
import org.w3.x2001.xmlschema.xe.$xs_IDREFS;
import org.w3.x2001.xmlschema.xe.$xs_NCName;
import org.w3.x2001.xmlschema.xe.$xs_NMTOKEN;
import org.w3.x2001.xmlschema.xe.$xs_NMTOKENS;
import org.w3.x2001.xmlschema.xe.$xs_NOTATION;
import org.w3.x2001.xmlschema.xe.$xs_Name;
import org.w3.x2001.xmlschema.xe.$xs_QName;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;
import org.w3.x2001.xmlschema.xe.$xs_anyURI;
import org.w3.x2001.xmlschema.xe.$xs_base64Binary;
import org.w3.x2001.xmlschema.xe.$xs_boolean;
import org.w3.x2001.xmlschema.xe.$xs_byte;
import org.w3.x2001.xmlschema.xe.$xs_date;
import org.w3.x2001.xmlschema.xe.$xs_dateTime;
import org.w3.x2001.xmlschema.xe.$xs_decimal;
import org.w3.x2001.xmlschema.xe.$xs_double;
import org.w3.x2001.xmlschema.xe.$xs_duration;
import org.w3.x2001.xmlschema.xe.$xs_float;
import org.w3.x2001.xmlschema.xe.$xs_gDay;
import org.w3.x2001.xmlschema.xe.$xs_gMonth;
import org.w3.x2001.xmlschema.xe.$xs_gMonthDay;
import org.w3.x2001.xmlschema.xe.$xs_gYear;
import org.w3.x2001.xmlschema.xe.$xs_gYearMonth;
import org.w3.x2001.xmlschema.xe.$xs_hexBinary;
import org.w3.x2001.xmlschema.xe.$xs_int;
import org.w3.x2001.xmlschema.xe.$xs_integer;
import org.w3.x2001.xmlschema.xe.$xs_language;
import org.w3.x2001.xmlschema.xe.$xs_long;
import org.w3.x2001.xmlschema.xe.$xs_negativeInteger;
import org.w3.x2001.xmlschema.xe.$xs_nonNegativeInteger;
import org.w3.x2001.xmlschema.xe.$xs_nonPositiveInteger;
import org.w3.x2001.xmlschema.xe.$xs_normalizedString;
import org.w3.x2001.xmlschema.xe.$xs_positiveInteger;
import org.w3.x2001.xmlschema.xe.$xs_short;
import org.w3.x2001.xmlschema.xe.$xs_string;
import org.w3.x2001.xmlschema.xe.$xs_time;
import org.w3.x2001.xmlschema.xe.$xs_token;
import org.w3.x2001.xmlschema.xe.$xs_unsignedByte;
import org.w3.x2001.xmlschema.xe.$xs_unsignedInt;
import org.w3.x2001.xmlschema.xe.$xs_unsignedLong;
import org.w3.x2001.xmlschema.xe.$xs_unsignedShort;

@SuppressWarnings("unused")
public final class XSTypeDirectory {
  private static final Map<UniqueQName,XSTypeDirectory> defaultTypes = new HashMap<UniqueQName,XSTypeDirectory>();
  private static final Map<UniqueQName,UniqueQName> typeHierarchy = new HashMap<UniqueQName,UniqueQName>();

  // may not need this...
  public static final XSTypeDirectory TYPE = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "Type"), new NativeBinding.GenericClass(Binding.class)), null);
  public static final XSTypeDirectory ANYTYPE = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anyType"), new NativeBinding.GenericClass($xs_anySimpleType.class), new NativeBinding.GenericClass(Object.class)), null);

  public static final XSTypeDirectory ANYSIMPLETYPE = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anySimpleType"), new NativeBinding.GenericClass($xs_anySimpleType.class), new NativeBinding.GenericClass(Object.class)), null);

  public static final XSTypeDirectory QNAME;

  static {
    // This section creates simpleType bindings for all of the base xs simple types.
    try {
      // FIXME: Have the nativeClasses hardcoded here be looked up using reflection during the generation process!!!!
      final XSTypeDirectory ENTITIES = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "ENTITIES"), new NativeBinding.GenericClass($xs_ENTITIES.class), new NativeBinding.GenericClass(List.class, String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory string = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "string"), new NativeBinding.GenericClass($xs_string.class), new NativeBinding.GenericClass(String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory normalizedString = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "normalizedString"), new NativeBinding.GenericClass($xs_normalizedString.class), new NativeBinding.GenericClass(String.class)), string);
      final XSTypeDirectory token = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "token"), new NativeBinding.GenericClass($xs_token.class), new NativeBinding.GenericClass(String.class)), normalizedString);
      final XSTypeDirectory Name = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "Name"), new NativeBinding.GenericClass($xs_Name.class), new NativeBinding.GenericClass(String.class)), token);
      final XSTypeDirectory NCName = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "NCName"), new NativeBinding.GenericClass($xs_NCName.class), new NativeBinding.GenericClass(String.class)), Name);
      final XSTypeDirectory ENTITY = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "ENTITY"), new NativeBinding.GenericClass($xs_ENTITY.class), new NativeBinding.GenericClass(String.class)), NCName);
      final XSTypeDirectory ID = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "ID"), new NativeBinding.GenericClass($xs_ID.class), new NativeBinding.GenericClass(String.class)), NCName);
      final XSTypeDirectory IDREF = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "IDREF"), new NativeBinding.GenericClass($xs_IDREF.class), new NativeBinding.GenericClass(String.class)), NCName);
      final XSTypeDirectory IDREFS = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "IDREFS"), new NativeBinding.GenericClass($xs_IDREFS.class), new NativeBinding.GenericClass(List.class, String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory NMTOKEN = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "NMTOKEN"), new NativeBinding.GenericClass($xs_NMTOKEN.class), new NativeBinding.GenericClass(String.class)), token);
      final XSTypeDirectory NMTOKENS = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "NMTOKENS"), new NativeBinding.GenericClass($xs_NMTOKENS.class), new NativeBinding.GenericClass(List.class, String.class)), ANYSIMPLETYPE);
      QNAME = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "QName"), new NativeBinding.GenericClass($xs_QName.class), new NativeBinding.GenericClass(QName.class), QName.class.getDeclaredMethod("valueOf", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory anyURI = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anyURI"), new NativeBinding.GenericClass($xs_anyURI.class), new NativeBinding.GenericClass(String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory base64Binary = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "base64Binary"), new NativeBinding.GenericClass($xs_base64Binary.class), new NativeBinding.GenericClass(Base64Binary.class), Base64Binary.class.getDeclaredMethod("parseBase64Binary", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _boolean = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "boolean"), new NativeBinding.GenericClass($xs_boolean.class), new NativeBinding.GenericClass(Boolean.class), $xs_boolean.class.getDeclaredMethod("parseBoolean", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _long = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "long"), new NativeBinding.GenericClass($xs_long.class), new NativeBinding.GenericClass(Long.class), Long.class.getDeclaredMethod("parseLong", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _int = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "int"), new NativeBinding.GenericClass($xs_int.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), _long);
      final XSTypeDirectory _short = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "short"), new NativeBinding.GenericClass($xs_short.class), new NativeBinding.GenericClass(Short.class), Short.class.getDeclaredMethod("parseShort", String.class)), _int);
      final XSTypeDirectory _byte = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "byte"), new NativeBinding.GenericClass($xs_byte.class), new NativeBinding.GenericClass(Byte.class), Byte.class.getDeclaredMethod("parseByte", String.class)), _short);
      final XSTypeDirectory date = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "date"), new NativeBinding.GenericClass($xs_date.class), new NativeBinding.GenericClass(Date.class), Date.class.getDeclaredMethod("parseDate", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory dateTime = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "dateTime"), new NativeBinding.GenericClass($xs_dateTime.class), new NativeBinding.GenericClass(DateTime.class), DateTime.class.getDeclaredMethod("parseDateTime", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory decimal = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "decimal"), new NativeBinding.GenericClass($xs_decimal.class), new NativeBinding.GenericClass(Decimal.class), Decimal.class.getDeclaredMethod("parseDecimal", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _double = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "double"), new NativeBinding.GenericClass($xs_double.class), new NativeBinding.GenericClass(Double.class), Double.class.getDeclaredMethod("parseDouble", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory duration = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "duration"), new NativeBinding.GenericClass($xs_duration.class), new NativeBinding.GenericClass(Duration.class), Duration.class.getDeclaredMethod("parseDuration", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory _float = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "float"), new NativeBinding.GenericClass($xs_float.class), new NativeBinding.GenericClass(Float.class), Float.class.getDeclaredMethod("parseFloat", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gDay = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gDay"), new NativeBinding.GenericClass($xs_gDay.class), new NativeBinding.GenericClass(Day.class), Day.class.getDeclaredMethod("parseDay", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gMonth = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gMonth"), new NativeBinding.GenericClass($xs_gMonth.class), new NativeBinding.GenericClass(Month.class), Month.class.getDeclaredMethod("parseMonth", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gMonthDay = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gMonthDay"), new NativeBinding.GenericClass($xs_gMonthDay.class), new NativeBinding.GenericClass(MonthDay.class), MonthDay.class.getDeclaredMethod("parseMonthDay", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gYear = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gYear"), new NativeBinding.GenericClass($xs_gYear.class), new NativeBinding.GenericClass(Year.class), Year.class.getDeclaredMethod("parseYear", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory gYearMonth = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "gYearMonth"), new NativeBinding.GenericClass($xs_gYearMonth.class), new NativeBinding.GenericClass(YearMonth.class), YearMonth.class.getDeclaredMethod("parseYearMonth", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory hexBinary = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "hexBinary"), new NativeBinding.GenericClass($xs_hexBinary.class), new NativeBinding.GenericClass(HexBinary.class), HexBinary.class.getDeclaredMethod("parseHexBinary", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory integer = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "integer"), new NativeBinding.GenericClass($xs_integer.class), new NativeBinding.GenericClass(BigInteger.class), BigInteger.class.getDeclaredConstructor(String.class)), decimal);
      final XSTypeDirectory language = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "language"), new NativeBinding.GenericClass($xs_language.class), new NativeBinding.GenericClass(Language.class), Language.class.getDeclaredMethod("parseLanguage", String.class)), token);
      final XSTypeDirectory positiveInteger = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "positiveInteger"), new NativeBinding.GenericClass($xs_positiveInteger.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), integer);
      final XSTypeDirectory negativeInteger = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "negativeInteger"), new NativeBinding.GenericClass($xs_negativeInteger.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), positiveInteger);
      final XSTypeDirectory NOTATION = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "NOTATION"), new NativeBinding.GenericClass($xs_NOTATION.class), new NativeBinding.GenericClass(NotationType.class), NotationType.class.getDeclaredMethod("parseNotation", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory nonNegativeInteger = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "nonNegativeInteger"), new NativeBinding.GenericClass($xs_nonNegativeInteger.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), integer);
      final XSTypeDirectory nonPositiveInteger = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "nonPositiveInteger"), new NativeBinding.GenericClass($xs_nonPositiveInteger.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), integer);
      final XSTypeDirectory time = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "time"), new NativeBinding.GenericClass($xs_time.class), new NativeBinding.GenericClass(Time.class), Time.class.getDeclaredMethod("parseTime", String.class)), ANYSIMPLETYPE);
      final XSTypeDirectory unsignedLong = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "unsignedLong"), new NativeBinding.GenericClass($xs_unsignedLong.class), new NativeBinding.GenericClass(BigInteger.class), BigInteger.class.getDeclaredConstructor(String.class)), nonNegativeInteger);
      final XSTypeDirectory unsignedInt = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "unsignedInt"), new NativeBinding.GenericClass($xs_unsignedInt.class), new NativeBinding.GenericClass(Long.class), Long.class.getDeclaredMethod("parseLong", String.class)), unsignedLong);
      final XSTypeDirectory unsignedShort = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "unsignedShort"), new NativeBinding.GenericClass($xs_unsignedShort.class), new NativeBinding.GenericClass(Integer.class), Integer.class.getDeclaredMethod("parseInt", String.class)), unsignedInt);
      final XSTypeDirectory unsignedByte = new XSTypeDirectory(new NativeBinding(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "unsignedByte"), new NativeBinding.GenericClass($xs_unsignedByte.class), new NativeBinding.GenericClass(Short.class), Short.class.getDeclaredMethod("parseShort", String.class)), unsignedShort);
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
        this.nativeFactory = ((Method)nativeBinding.getFactoryMethod()).getDeclaringClass().getName() + "." + ((Method)nativeBinding.getFactoryMethod()).getName();
      else if (nativeBinding.getFactoryMethod() instanceof Constructor<?>)
        this.nativeFactory = "new " + ((Constructor<?>)nativeBinding.getFactoryMethod()).getDeclaringClass().getName();
      else
        throw new UnsupportedOperationException("Unknown native binding factoryMethod type: " + nativeBinding.getFactoryMethod().getClass().getName());
    }

    defaultTypes.put(nativeBinding.getName(), this);
    if (superType != null)
      typeHierarchy.put(nativeBinding.getName(), superType.getNativeBinding().getName());
  }
}