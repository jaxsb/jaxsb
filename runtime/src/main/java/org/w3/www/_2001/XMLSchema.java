/* Copyright (c) 2006 JAX-SB
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

package org.w3.www._2001;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.namespace.QName;

import org.apache.xerces.jaxp.datatype.Duration;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.runtime.Binding;
import org.jaxsb.runtime.MarshalException;
import org.jaxsb.runtime.NotationType;
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
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

// https://www.liquid-technologies.com/Reference/XmlStudio/XsdEditorNotation_BuiltInXsdTypes.html
// https://www.eclipse.org/modeling/emf/docs/xsd/dW/os-schema2/os-schema2-3-2.html
public final class XMLSchema {
  public static final class yAA {
    @SuppressWarnings("rawtypes")
    public abstract static class $AnyType<T> extends Binding {
      private List<$AnyType> any;
      private List<$AnySimpleType> anySimple;
      private T text; // FIXME: Make this final

      public $AnyType(final T text) {
        super();
        this.text = text;
      }

      protected $AnyType(final $AnyType<T> copy) {
        super(copy);
        this.text = copy.text;
      }

      protected $AnyType() {
        super();
        this.text = null;
      }

      protected void text(final T text) {
        if (this.text != text && owner() != null)
          owner().setDirty(); // FIXME: Should this do Objects.equals(this.text, text)?

        this.text = text;
      }

      @Override
      protected T text() {
        return text;
      }

      protected void addAny$(final $AnySimpleType any) {
        if (this.anySimple == null)
          this.anySimple = new ArrayList<>();

        this.anySimple.add(any);
      }

      protected List<$AnySimpleType> getAny$() {
        return anySimple;
      }

      protected void add$Any(final $AnyType<?> any) {
        if (this.any == null)
          this.any = new ArrayList<>();

        this.any.add(any);
      }

      protected List<$AnyType> get$Any() {
        return any;
      }

      @Override
      public boolean isDefault() {
        return super.isDefault();
      }

      @Override
      @SuppressWarnings("unchecked")
      protected void _$$decode(final Element parent, final String value) {
        text((T)value); // FIXME: By default, AnySimpleType is a string
      }

      protected static List<String> decodeAsList(final String value) {
        return value == null ? null : Arrays.asList(value.split(" "));
      }

      /**
       * Returns a string representation of the encoded value of this binding,
       * corresponding to the provided parent {@link Element}.
       *
       * @param parent The parent {@link Element}.
       * @return A string representation of the encoded value of this binding,
       *         corresponding to the provided parent {@link Element}.
       * @throws MarshalException If a marshal exception has occurred.
       */
      protected String _$$encode(final Element parent) throws MarshalException {
        return encode(text(), false);
      }

      protected static String encode(final Object value, final boolean collectionable) {
        if (value == null)
          return "";

        if (!(value instanceof Collection))
          return value.toString();

        if (!collectionable)
          throw new IllegalArgumentException("Why is this a Collection? The collection logic should be in the appropriate subclass");

        if (((Collection<?>)value).size() == 0)
          return null;

        final StringBuilder builder = new StringBuilder();
        final Iterator<?> iterator = ((Collection<?>)value).iterator();
        for (int i = 0; iterator.hasNext(); ++i) {
          if (i > 0)
            builder.append(' ');

          builder.append(iterator.next());
        }

        return builder.toString();
      }

      @Override
      protected Element marshal() throws MarshalException {
        final Element node = marshal(createElementNS(name().getNamespaceURI(), name().getLocalPart()), name(), type(_$$inheritsInstance()));
        _$$marshalElements(node);
        return node;
      }

      @Override
      protected Element marshal(final Element parent, final QName name, final QName typeName) throws MarshalException {
        final Element element = super.marshal(parent, name, typeName);
        if (text != null)
          element.appendChild(parent.getOwnerDocument().createTextNode(String.valueOf(_$$encode(parent))));

        return element;
      }

      @Override
      protected Attr marshalAttr(final String name, final Element parent) throws MarshalException {
        final Attr attr = parent.getOwnerDocument().createAttribute(name);
        attr.setNodeValue(_$$encode(parent));
        return attr;
      }

      @Override
      protected void parseText(final Text text) {
        // Ignore all attributes that have a xsi prefix because these are
        // controlled implicitly by the framework
        if (XSI_NIL.getPrefix().equals(text.getPrefix()))
          return;

        String value = "";
        if (text.getNodeValue() != null)
          value += text.getNodeValue().trim();

        if (value.length() != 0)
          _$$decode((Element)text.getParentNode(), value);
      }

      @Override
      public String id() {
        return inherits() == this ? null : id(inherits());
      }

      @Override
      public QName name() {
        return name(_$$inheritsInstance());
      }

      @Override
      @SuppressWarnings("unchecked")
      public $AnyType<T> clone() {
        return ($AnyType<T>)super.clone();
      }

      @Override
      public boolean equals(final Object obj) {
        if (this == obj)
          return true;

        if (!(obj instanceof $AnyType))
          return false;

        final $AnyType<?> that = ($AnyType<?>)obj;
        if (!Objects.equals(text, that.text))
          return false;

        if (any != null) {
          if (that.any == null || any.size() != that.any.size())
            return false;

          for (int i = 0, len = any.size(); i < len; ++i)
            if (!any.get(i).equals(that.any.get(i)))
              return false;
        }
        else if (that.any != null) {
          return false;
        }

        if (anySimple != null) {
          if (that.anySimple == null || anySimple.size() != that.anySimple.size())
            return false;

          for (int i = 0, len = anySimple.size(); i < len; ++i)
            if (!anySimple.get(i).equals(that.anySimple.get(i)))
              return false;
        }
        else if (that.anySimple != null) {
          return false;
        }

        return true;
      }

      @Override
      public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + Objects.hashCode(text);
        hashCode = 31 * hashCode + Objects.hashCode(any);
        hashCode = 31 * hashCode + Objects.hashCode(anySimple);
        return hashCode;
      }
    }

    public abstract static class $AnySimpleType<T> extends $AnyType<T> {
      public $AnySimpleType(final T text) {
        super(text);
      }

      protected $AnySimpleType(final $AnySimpleType<T> copy) {
        super(copy);
      }

      protected $AnySimpleType() {
        super();
      }

      @Override
      public T text() {
        return super.text();
      }

      @Override
      public $AnySimpleType<T> clone() {
        return new $AnySimpleType<T>(this) {
          @Override
          public QName name() {
            return $AnySimpleType.this.name();
          }

          @Override
          protected $AnySimpleType<T> inherits() {
            return this;
          }
        };
      }

      @Override
      public boolean equals(final Object obj) {
        return this == obj || obj instanceof $AnySimpleType && super.equals(obj);
      }

      @Override
      public int hashCode() {
        return super.hashCode() ^ 31;
      }
    }

    public abstract static class $AnyURI extends $AnySimpleType<URI> {
      public $AnyURI(final URI text) {
        super(text);
      }

      protected $AnyURI(final $AnyURI copy) {
        super(copy);
      }

      protected $AnyURI() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(URI.create(value));
      }

      @Override
      public $AnyURI clone() {
        return ($AnyURI)super.clone();
      }
    }

    public abstract static class $Base64Binary extends $AnySimpleType<Base64Binary> {
      public $Base64Binary(final Base64Binary text) {
        super(text);
      }

      protected $Base64Binary(final $Base64Binary copy) {
        super(copy);
      }

      protected $Base64Binary() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Base64Binary.parse(value));
      }

      @Override
      public $Base64Binary clone() {
        return ($Base64Binary)super.clone();
      }
    }

    public abstract static class $Boolean extends $AnySimpleType<Boolean> {
      public static Boolean parse(final String s) {
        return s != null && ("1".equals(s) || Boolean.parseBoolean(s));
      }

      public $Boolean(final Boolean text) {
        super(text);
      }

      protected $Boolean(final $Boolean copy) {
        super(copy);
      }

      protected $Boolean() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text("true".equals(value) || "1".equals(value));
      }

      @Override
      protected String _$$encode(final Element parent) throws MarshalException {
        if (super.text() == null)
          return "";

        if (_$$getPattern() == null)
          return String.valueOf(super.text());

        if (super.text()) {
          for (final String pattern : _$$getPattern()) {
            if ("true".matches(pattern))
              return "true";

            if ("1".matches(pattern))
              return "1";
          }
        }
        else {
          for (final String pattern : _$$getPattern()) {
            if ("false".matches(pattern))
              return "false";

            if ("0".matches(pattern))
              return "0";
          }
        }

        throw new MarshalException("No valid return type. Schema error!!!");
      }

      @Override
      public $Boolean clone() {
        return ($Boolean)super.clone();
      }
    }

    public abstract static class $Byte extends $Short {
      public $Byte(final Byte text) {
        super(text);
      }

      protected $Byte(final $Byte copy) {
        super(copy);
      }

      protected $Byte() {
        super();
      }

      @Override
      public BigInteger textAsBigInteger() {
        return text() == null ? null : BigInteger.valueOf((Byte)text());
      }

      @Override
      public BigDecimal textAsBigDecimal() {
        return text() == null ? null : BigDecimal.valueOf((Byte)text());
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Byte.parseByte(value));
      }

      @Override
      public $Byte clone() {
        return ($Byte)super.clone();
      }
    }

    public abstract static class $Date extends $AnySimpleType<Date> {
      public $Date(final Date text) {
        super(text);
      }

      protected $Date(final $Date copy) {
        super(copy);
      }

      protected $Date() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Date.parse(value));
      }

      @Override
      public $Date clone() {
        return ($Date)super.clone();
      }
    }

    /**
     * This final class represents the Java binding of the dateTime instance of
     * time.
     *
     * @see <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">Definition</a>
     */
    public abstract static class $DateTime extends $AnySimpleType<DateTime> {
      public $DateTime(final DateTime text) {
        super(text);
      }

      protected $DateTime(final $DateTime copy) {
        super(copy);
      }

      /**
       * Allocates a {@link Date} object and initializes it so that it
       * represents the time at which it was allocated. Milliseconds are
       * <b>NOT</b> significant figures and are not represented.
       *
       * @see System#currentTimeMillis()
       */
      protected $DateTime() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(DateTime.parse(value));
      }

      @Override
      public $DateTime clone() {
        return ($DateTime)super.clone();
      }
    }

    public abstract static class $Decimal extends $AnySimpleType<Number> {
      public $Decimal(final BigDecimal text) {
        super(text);
      }

      protected $Decimal(final $Decimal copy) {
        super(copy);
      }

      protected $Decimal(final Number text) {
        super(text);
      }

      protected $Decimal() {
        super();
      }

      /**
       * Returns the text of this node as a {@link Byte}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Byte}.
       */
      public Byte byteValue() {
        return text() == null ? null : text().byteValue();
      }

      /**
       * Returns the text of this node as a {@link Short}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@code short}.
       */
      public Short textAsShort() {
        return text() == null ? null : text().shortValue();
      }

      /**
       * Returns the text of this node as an {@link Integer}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Integer}.
       */
      public Integer textAsInteger() {
        return text() == null ? null : text().intValue();
      }

      /**
       * Returns the text of this node as a {@link Long}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Long}.
       */
      public Long textAsLong() {
        return text() == null ? null : text().longValue();
      }

      /**
       * Returns the text of this node as a {@link Float}, which may involve
       * rounding.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Float}.
       */
      public Float textAsFloat() {
        return text() == null ? null : text().floatValue();
      }

      /**
       * Returns the text of this node as a {@link Double}, which may involve
       * rounding.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Double}.
       */
      public Double textAsDouble() {
        return text() == null ? null : text().doubleValue();
      }

      /**
       * Returns the text of this node as a {@link BigInteger}, which may
       * involve rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link BigInteger}.
       */
      public BigInteger textAsBigInteger() {
        return ((BigDecimal)text()).toBigInteger();
      }

      /**
       * Returns the text of this node as a {@link BigDecimal}, which may
       * involve rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link BigDecimal}.
       */
      public BigDecimal textAsBigDecimal() {
        return (BigDecimal)text();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(new BigDecimal(value));
      }

      @Override
      public $Decimal clone() {
        return ($Decimal)super.clone();
      }
    }

    // -INF, -1E4, -0, 0, 12.78E-2, 12, INF, NaN
    public abstract static class $Double extends $AnySimpleType<Double> {
      public $Double(final Double text) {
        super(text);
      }

      protected $Double(final $Double copy) {
        super(copy);
      }

      protected $Double() {
        super();
      }

      /**
       * Returns the text of this node as a {@link Byte}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Byte}.
       */
      public Byte byteValue() {
        return text() == null ? null : text().byteValue();
      }

      /**
       * Returns the text of this node as a {@link Short}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@code short}.
       */
      public Short textAsShort() {
        return text() == null ? null : text().shortValue();
      }

      /**
       * Returns the text of this node as an {@link Integer}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Integer}.
       */
      public Integer textAsInteger() {
        return text() == null ? null : text().intValue();
      }

      /**
       * Returns the text of this node as a {@link Long}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Long}.
       */
      public Long textAsLong() {
        return text() == null ? null : text().longValue();
      }

      /**
       * Returns the text of this node as a {@link Float}, which may involve
       * rounding.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Float}.
       */
      public Float textAsFloat() {
        return text() == null ? null : text().floatValue();
      }

      /**
       * Returns the text of this node as a {@link Double}, which may involve
       * rounding.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Double}.
       */
      public Double textAsDouble() {
        return text() == null ? null : text().doubleValue();
      }

      /**
       * Returns the text of this node as a {@link BigInteger}, which may
       * involve rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link BigInteger}.
       */
      public BigInteger textAsBigInteger() {
        return text() == null ? null : BigInteger.valueOf(text().longValue());
      }

      /**
       * Returns the text of this node as a {@link BigDecimal}, which may
       * involve rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link BigDecimal}.
       */
      public BigDecimal textAsBigDecimal() {
        return text() == null ? null : BigDecimal.valueOf(text());
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Double.parseDouble(value));
      }

      @Override
      public $Double clone() {
        return ($Double)super.clone();
      }
    }

    public abstract static class $Duration extends $AnySimpleType<Duration> {
      public $Duration(final Duration text) {
        super(text);
      }

      protected $Duration(final $Duration copy) {
        super(copy);
      }

      protected $Duration() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Duration.parse(value));
      }

      @Override
      public $Duration clone() {
        return ($Duration)super.clone();
      }
    }

    public abstract static class $ENTITIES extends $AnySimpleType<List<String>> {
      public $ENTITIES(final List<String> text) {
        super(text);
      }

      protected $ENTITIES(final $ENTITIES copy) {
        super(copy);
      }

      protected $ENTITIES() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        text(decodeAsList(value));
      }

      @Override
      protected String _$$encode(final Element parent) throws MarshalException {
        return encode(text(), true);
      }

      @Override
      public $ENTITIES clone() {
        return ($ENTITIES)super.clone();
      }
    }

    public abstract static class $ENTITY extends $nCName {
      public $ENTITY(final String text) {
        super(text);
      }

      protected $ENTITY(final $ENTITY copy) {
        super(copy);
      }

      protected $ENTITY() {
        super();
      }

      @Override
      public $ENTITY clone() {
        return ($ENTITY)super.clone();
      }
    }

    public abstract static class $Float extends $AnySimpleType<Float> {
      public $Float(final Float text) {
        super(text);
      }

      protected $Float(final $Float copy) {
        super(copy);
      }

      protected $Float() {
        super();
      }

      /**
       * Returns the text of this node as a {@link Byte}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Byte}.
       */
      public Byte byteValue() {
        return text() == null ? null : text().byteValue();
      }

      /**
       * Returns the text of this node as a {@link Short}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@code short}.
       */
      public Short textAsShort() {
        return text() == null ? null : text().shortValue();
      }

      /**
       * Returns the text of this node as an {@link Integer}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Integer}.
       */
      public Integer textAsInteger() {
        return text() == null ? null : text().intValue();
      }

      /**
       * Returns the text of this node as a {@link Long}, which may involve
       * rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Long}.
       */
      public Long textAsLong() {
        return text() == null ? null : text().longValue();
      }

      /**
       * Returns the text of this node as a {@link Float}, which may involve
       * rounding.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Float}.
       */
      public Float textAsFloat() {
        return text() == null ? null : text().floatValue();
      }

      /**
       * Returns the text of this node as a {@link Double}, which may involve
       * rounding.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link Double}.
       */
      public Double textAsDouble() {
        return text() == null ? null : text().doubleValue();
      }

      /**
       * Returns the text of this node as a {@link BigInteger}, which may
       * involve rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link BigInteger}.
       */
      public BigInteger textAsBigInteger() {
        return text() == null ? null : BigInteger.valueOf(text().intValue());
      }

      /**
       * Returns the text of this node as a {@link BigDecimal}, which may
       * involve rounding or truncation.
       *
       * @return The numeric value represented by this node after conversion to
       *         type {@link BigDecimal}.
       */
      public BigDecimal textAsBigDecimal() {
        return text() == null ? null : BigDecimal.valueOf(text());
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Float.parseFloat(value));
      }

      @Override
      public $Float clone() {
        return ($Float)super.clone();
      }
    }

    public abstract static class $GDay extends $AnySimpleType<Day> {
      public $GDay(final Day text) {
        super(text);
      }

      protected $GDay(final $GDay copy) {
        super(copy);
      }

      protected $GDay() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Day.parse(value));
      }

      @Override
      public $GDay clone() {
        return ($GDay)super.clone();
      }
    }

    public abstract static class $GMonth extends $AnySimpleType<Month> {
      public $GMonth(final Month text) {
        super(text);
      }

      protected $GMonth(final $GMonth copy) {
        super(copy);
      }

      protected $GMonth() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Month.parse(value));
      }

      @Override
      public $GMonth clone() {
        return ($GMonth)super.clone();
      }
    }

    public abstract static class $GMonthDay extends $AnySimpleType<MonthDay> {
      public $GMonthDay(final MonthDay text) {
        super(text);
      }

      protected $GMonthDay(final $GMonthDay copy) {
        super(copy);
      }

      protected $GMonthDay() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(MonthDay.parse(value));
      }

      @Override
      public $GMonthDay clone() {
        return ($GMonthDay)super.clone();
      }
    }

    public abstract static class $GYear extends $AnySimpleType<Year> {
      public $GYear(final Year text) {
        super(text);
      }

      protected $GYear(final $GYear copy) {
        super(copy);
      }

      protected $GYear() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Year.parse(value));
      }

      @Override
      public $GYear clone() {
        return ($GYear)super.clone();
      }
    }

    public abstract static class $GYearMonth extends $AnySimpleType<YearMonth> {
      public $GYearMonth(final YearMonth text) {
        super(text);
      }

      protected $GYearMonth(final $GYearMonth copy) {
        super(copy);
      }

      protected $GYearMonth() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(YearMonth.parse(value));
      }

      @Override
      public $GYearMonth clone() {
        return ($GYearMonth)super.clone();
      }
    }

    public abstract static class $HexBinary extends $AnySimpleType<HexBinary> {
      public $HexBinary(final HexBinary text) {
        super(text);
      }

      protected $HexBinary(final $HexBinary copy) {
        super(copy);
      }

      protected $HexBinary() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(HexBinary.parse(value));
      }

      @Override
      public $HexBinary clone() {
        return ($HexBinary)super.clone();
      }
    }

    public abstract static class $ID extends $nCName {
      protected static final Map<String,Map<String,$ID>> namespaceIds = new HashMap<>();

      private static void persist(final String namespace, final String value, final $ID id) {
        Map<String,$ID> idMap = namespaceIds.get(namespace);
        if (idMap == null)
          namespaceIds.put(namespace, idMap = new HashMap<>());

        idMap.put(value, id);
      }

      private static void remove(final String namespace, final Object value) {
        final Map<String,$ID> ids = namespaceIds.get(namespace);
        if (ids != null)
          ids.remove(value);
      }

      public static $ID lookupId(final Object id) {
        final Map<String,$ID> ids = namespaceIds.get(UniqueQName.XS.getNamespaceURI().toString());
        return ids == null ? null : ids.get(id);
      }

      public $ID(final String text) {
        super(text);
        persist(name().getNamespaceURI(), text, this);
      }

      protected $ID(final $ID copy) {
        super(copy);
      }

      public $ID(final Attr node) {
        super(node.getNodeValue());
        persist(node.getOwnerElement().getNamespaceURI(), node.getNodeValue(), this);
      }

      protected $ID() {
        super();
      }

      @Override
      protected void text(final String text) {
        final String old = text();
        super.text(text);
        if (old != null)
          remove(name().getNamespaceURI(), old);

        persist(name().getNamespaceURI(), text, this);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        persist(parent.getNamespaceURI(), value, this);
        super.text(value);
      }

      @Override
      public $ID clone() {
        return ($ID)super.clone();
      }
    }

    public abstract static class $IDREF extends $nCName {
      public $IDREF(final String text) {
        super(text);
      }

      protected $IDREF(final $IDREF copy) {
        super(copy);
      }

      protected $IDREF() {
        super();
      }

      @Override
      public $IDREF clone() {
        return ($IDREF)super.clone();
      }
    }

    public abstract static class $IDREFS extends $AnySimpleType<List<String>> {
      public $IDREFS(final List<String> text) {
        super(text);
      }

      protected $IDREFS(final $IDREFS copy) {
        super(copy);
      }

      protected $IDREFS() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        text(decodeAsList(value));
      }

      @Override
      protected String _$$encode(final Element parent) throws MarshalException {
        return encode(text(), true);
      }

      @Override
      public $IDREFS clone() {
        return ($IDREFS)super.clone();
      }
    }

    public abstract static class $Int extends $Long {
      public $Int(final Integer text) {
        super(text);
      }

      protected $Int(final $Int copy) {
        super(copy);
      }

      protected $Int(final Number value) {
        super(value);
      }

      protected $Int() {
        super();
      }

      @Override
      public BigInteger textAsBigInteger() {
        return text() == null ? null : BigInteger.valueOf((Integer)text());
      }

      @Override
      public BigDecimal textAsBigDecimal() {
        return text() == null ? null : BigDecimal.valueOf((Integer)text());
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Integer.parseInt(value));
      }

      @Override
      public $Int clone() {
        return ($Int)super.clone();
      }
    }

    public abstract static class $Integer extends $Decimal {
      public $Integer(final BigInteger text) {
        super(text);
      }

      protected $Integer(final $Integer copy) {
        super(copy);
      }

      protected $Integer(final Number text) {
        super(text);
      }

      protected $Integer() {
        super();
      }

      @Override
      protected Binding inherits() {
        return null;
      }

      @Override
      public BigInteger textAsBigInteger() {
        return (BigInteger)text();
      }

      @Override
      public BigDecimal textAsBigDecimal() {
        return text() == null ? null : new BigDecimal(textAsBigInteger());
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(new BigInteger(value));
      }

      @Override
      public $Integer clone() {
        return ($Integer)super.clone();
      }
    }

    public abstract static class $Language extends $AnySimpleType<Language> {
      public $Language(final Language text) {
        super(text);
      }

      protected $Language(final $Language copy) {
        super(copy);
      }

      protected $Language() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Language.parse(value));
      }

      @Override
      public $Language clone() {
        return ($Language)super.clone();
      }
    }

    public abstract static class $Long extends $Integer {
      public $Long(final Long text) {
        super(text);
      }

      protected $Long(final $Long copy) {
        super(copy);
      }

      protected $Long(final Number value) {
        super(value);
      }

      protected $Long() {
        super();
      }

      @Override
      public BigInteger textAsBigInteger() {
        return text() == null ? null : BigInteger.valueOf((Long)text());
      }

      @Override
      public BigDecimal textAsBigDecimal() {
        return text() == null ? null : BigDecimal.valueOf((Long)text());
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Long.parseLong(value));
      }

      @Override
      public $Long clone() {
        return ($Long)super.clone();
      }
    }

    public abstract static class $MonthDay extends $AnySimpleType<MonthDay> {
      public $MonthDay(final MonthDay text) {
        super(text);
      }

      protected $MonthDay(final $MonthDay copy) {
        super(copy);
      }

      protected $MonthDay() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(MonthDay.parse(value));
      }

      @Override
      public $MonthDay clone() {
        return ($MonthDay)super.clone();
      }
    }

    public abstract static class $NMTOKEN extends $Token {
      public $NMTOKEN(final String text) {
        super(text);
      }

      protected $NMTOKEN(final $NMTOKEN copy) {
        super(copy);
      }

      protected $NMTOKEN() {
        super();
      }

      @Override
      public $NMTOKEN clone() {
        return ($NMTOKEN)super.clone();
      }
    }

    public abstract static class $NMTOKENS extends $AnySimpleType<List<String>> {
      public $NMTOKENS(final List<String> text) {
        super(text);
      }

      protected $NMTOKENS(final $NMTOKENS copy) {
        super(copy);
      }

      protected $NMTOKENS() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        text(decodeAsList(value));
      }

      @Override
      protected String _$$encode(final Element parent) throws MarshalException {
        return encode(text(), true);
      }

      @Override
      public $NMTOKENS clone() {
        return ($NMTOKENS)super.clone();
      }
    }

    public abstract static class $NOTATION extends $AnySimpleType<NotationType> {
      public $NOTATION(final NotationType text) {
        super(text);
      }

      protected $NOTATION(final $NOTATION copy) {
        super(copy);
      }

      protected $NOTATION() {
        super();
      }

      @Override
      protected String _$$encode(final Element parent) throws MarshalException {
        final String name = super._$$encode(parent);
        if (parent.getNamespaceURI().equals(text().name().getNamespaceURI()))
          return name;

        final String prefix = _$$getPrefix(parent, text().name());
        return prefix + ":" + name;
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        final int colon = value.indexOf(':');
        final QName qName;
        if (colon == -1)
          qName = new QName(parent.getOwnerDocument().getDocumentElement().getNamespaceURI(), value);
        else
          qName = new QName(parent.getOwnerDocument().lookupNamespaceURI(value.substring(0, colon)), value.substring(colon + 1));

        super.text(NotationType.parse(qName));
        if (super.text() == null)
          throw new IllegalStateException("Notation \"" + value + "\" is not registered. The code that instantiates the Notation binding for \"" + value + "\" must be run before it is possible for the Binding engine to know about it.");
      }

      @Override
      public $NOTATION clone() {
        return ($NOTATION)super.clone();
      }
    }

    public abstract static class $NegativeInteger extends $NonPositiveInteger {
      public $NegativeInteger(final BigInteger text) {
        super(text);
        // FIXME: Check?
      }

      protected $NegativeInteger(final $NegativeInteger copy) {
        super(copy);
      }

      protected $NegativeInteger(final Number number) {
        super(number);
      }

      protected $NegativeInteger() {
        super();
      }

      @Override
      public $NegativeInteger clone() {
        return ($NegativeInteger)super.clone();
      }
    }

    public abstract static class $NonNegativeInteger extends $Integer {
      public $NonNegativeInteger(final BigInteger text) {
        super(text);
        // FIXME: Check?
      }

      protected $NonNegativeInteger(final $NonNegativeInteger copy) {
        super(copy);
      }

      protected $NonNegativeInteger(final Number value) {
        super(value);
      }

      protected $NonNegativeInteger() {
        super();
      }

      @Override
      public $NonNegativeInteger clone() {
        return ($NonNegativeInteger)super.clone();
      }
    }

    public abstract static class $NonPositiveInteger extends $Integer {
      public $NonPositiveInteger(final BigInteger text) {
        super(text);
        // FIXME: Check?
      }

      protected $NonPositiveInteger(final $NonPositiveInteger copy) {
        super(copy);
      }

      protected $NonPositiveInteger(final Number value) {
        super(value);
      }

      protected $NonPositiveInteger() {
        super();
      }

      @Override
      public $NonPositiveInteger clone() {
        return ($NonPositiveInteger)super.clone();
      }
    }

    public abstract static class $NormalizedString extends $String {
      public $NormalizedString(final String text) {
        super(text);
      }

      protected $NormalizedString(final $NormalizedString copy) {
        super(copy);
      }

      protected $NormalizedString() {
        super();
      }

      @Override
      public $NormalizedString clone() {
        return ($NormalizedString)super.clone();
      }
    }

    public abstract static class $PositiveInteger extends $NonNegativeInteger {
      public $PositiveInteger(final BigInteger text) {
        super(text);
        // FIXME: Check?
      }

      protected $PositiveInteger(final $PositiveInteger copy) {
        super(copy);
      }

      protected $PositiveInteger(final Number value) {
        super(value);
      }

      protected $PositiveInteger() {
        super();
      }

      @Override
      public $PositiveInteger clone() {
        return ($PositiveInteger)super.clone();
      }
    }

    public abstract static class $Short extends $Int {
      public $Short(final Short text) {
        super(text);
      }

      protected $Short(final $Short copy) {
        super(copy);
      }

      protected $Short(final Number value) {
        super(value);
      }

      protected $Short() {
        super();
      }

      @Override
      public BigInteger textAsBigInteger() {
        return text() == null ? null : BigInteger.valueOf((Short)text());
      }

      @Override
      public BigDecimal textAsBigDecimal() {
        return text() == null ? null : BigDecimal.valueOf((Short)text());
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Short.parseShort(value));
      }

      @Override
      public $Short clone() {
        return ($Short)super.clone();
      }
    }

    public abstract static class $String extends $AnySimpleType<String> {
      public $String(final String text) {
        super(text);
      }

      protected $String(final $String copy) {
        super(copy);
      }

      protected $String() {
        super();
      }

      @Override
      public $String clone() {
        return ($String)super.clone();
      }
    }

    public abstract static class $Time extends $AnySimpleType<Time> {
      public $Time(final Time text) {
        super(text);
      }

      protected $Time(final $Time copy) {
        super(copy);
      }

      protected $Time() {
        super();
      }

      @Override
      protected void text(final Time text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Time.parse(value));
      }

      @Override
      public $Time clone() {
        return ($Time)super.clone();
      }
    }

    public abstract static class $Token extends $NormalizedString {
      public $Token(final String text) {
        super(text);
      }

      protected $Token(final $Token copy) {
        super(copy);
      }

      protected $Token() {
        super();
      }

      @Override
      public $Token clone() {
        return ($Token)super.clone();
      }
    }

    public abstract static class $UnsignedByte extends $UnsignedShort {
      public $UnsignedByte(final Short text) {
        super(text);
        // FIXME: Check?
      }

      protected $UnsignedByte(final $UnsignedByte copy) {
        super(copy);
      }

      protected $UnsignedByte() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Short.parseShort(value));
      }

      @Override
      public $UnsignedByte clone() {
        return ($UnsignedByte)super.clone();
      }
    }

    public abstract static class $UnsignedInt extends $UnsignedLong {
      public $UnsignedInt(final Long text) {
        super(text);
      }

      protected $UnsignedInt(final $UnsignedInt copy) {
        super(copy);
      }

      protected $UnsignedInt(final Number value) {
        super(value);
        // FIXME: Check?
      }

      protected $UnsignedInt() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Long.parseLong(value));
      }

      @Override
      public $UnsignedInt clone() {
        return ($UnsignedInt)super.clone();
      }
    }

    public abstract static class $UnsignedLong extends $NonNegativeInteger {
      public $UnsignedLong(final BigInteger text) {
        super(text);
        // FIXME: Check?
      }

      protected $UnsignedLong(final $UnsignedLong copy) {
        super(copy);
      }

      protected $UnsignedLong(final Number value) {
        super(value);
      }

      protected $UnsignedLong() {
        super();
      }

      @Override
      public $UnsignedLong clone() {
        return ($UnsignedLong)super.clone();
      }
    }

    public abstract static class $UnsignedShort extends $UnsignedInt {
      public $UnsignedShort(final Integer text) {
        super(text);
        // FIXME: Check?
      }

      protected $UnsignedShort(final $UnsignedShort copy) {
        super(copy);
      }

      protected $UnsignedShort(final Number value) {
        super(value);
      }

      protected $UnsignedShort() {
        super();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Integer.parseInt(value));
      }

      @Override
      public $UnsignedShort clone() {
        return ($UnsignedShort)super.clone();
      }
    }

    public abstract static class $nCName extends $name {
      public $nCName(final String text) {
        super(text);
      }

      protected $nCName(final $nCName copy) {
        super(copy);
      }

      protected $nCName() {
        super();
      }

      @Override
      public $nCName clone() {
        return ($nCName)super.clone();
      }
    }

    public abstract static class $name extends $Token {
      public $name(final String text) {
        super(text);
      }

      protected $name(final $name copy) {
        super(copy);
      }

      protected $name() {
        super();
      }

      @Override
      public $name clone() {
        return ($name)super.clone();
      }
    }

    public abstract static class $qName extends $AnySimpleType<QName> {
      public $qName(final QName text) {
        super(text);
      }

      protected $qName(final $qName copy) {
        super(copy);
      }

      protected $qName() {
        super();
      }

      @Override
      protected void _$$decode(final Element element, final String value) {
        final QName qName = stringToQName(value);
        super.text(qName);
        if (element != null)
          super.text(new QName(element.getOwnerDocument().getDocumentElement().lookupNamespaceURI(qName.getPrefix()), qName.getLocalPart()));
      }

      @Override
      protected String _$$encode(final Element parent) throws MarshalException {
        final QName value = text();
        if (value == null)
          return "";

        if (parent != null && value.getNamespaceURI() != null)
          return _$$getPrefix(parent, value) + ":" + value.getLocalPart();

        return value.getLocalPart();
      }

      @Override
      public $qName clone() {
        return ($qName)super.clone();
      }
    }

    private yAA() {
    }
  }

  private XMLSchema() {
  }
}