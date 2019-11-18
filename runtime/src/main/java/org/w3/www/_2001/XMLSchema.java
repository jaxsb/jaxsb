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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

public final class XMLSchema {
  public static final class yAA {
    public static abstract class $AnySimpleType extends Binding {
      private static final long serialVersionUID = 3006785147423317837L;

      private Serializable text;

      public $AnySimpleType(final $AnySimpleType copy) {
        super(copy);
        this.text = copy.text;
      }

      public $AnySimpleType(final Object text) {
        super();
        if (text instanceof $AnySimpleType && (($AnySimpleType)text)._$$hasElements())
          merge(($AnySimpleType)text);
        else
          this.text = (Serializable)text;
      }

      protected $AnySimpleType() {
        super();
      }

      public void text(final Serializable text) {
        if (isNull())
          throw new UnsupportedOperationException("NULL Object is immutable");

        this.text = text;
      }

      @Override
      public Object text() {
        return text;
      }

      @Override
      public boolean isDefault() {
        return super.isDefault();
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        text(decode(value, false));
      }

      protected static Serializable decode(final String value, final boolean collectionable) {
        if (value == null || !collectionable)
          return value;

        final ArrayList<String> list = new ArrayList<>();
        final StringTokenizer tokenizer = new StringTokenizer(value);
        while (tokenizer.hasMoreTokens())
          list.add(tokenizer.nextToken());

        return list;
      }

      @Override
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

      private transient Element parent = null;

      @Override
      protected Element marshal(final Element parent, final QName name, final QName typeName) throws MarshalException {
        this.parent = parent;
        final Element element = super.marshal(parent, name, typeName);
        if (text != null)
          element.appendChild(parent.getOwnerDocument().createTextNode(String.valueOf(_$$encode(parent))));

        return element;
      }

      @Override
      protected Attr marshalAttr(final String name, final Element parent) throws MarshalException {
        this.parent = parent;
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
        return name(inherits());
      }

      @Override
      public QName type() {
        return null;
      }

      @Override
      public $AnySimpleType clone() {
        return ($AnySimpleType)super.clone();
      }

      @Override
      public boolean equals(final Object obj) {
        if (this == obj)
          return true;

        if (!(obj instanceof $AnySimpleType))
          return false;

        final $AnySimpleType that = ($AnySimpleType)obj;
        try {
          final String thisEncoded = _$$encode(parent);
          final String thatEncoded = that._$$encode(parent);
          return thisEncoded != null ? thisEncoded.equals(thatEncoded) : thatEncoded == null;
        }
        catch (final MarshalException e) {
          return false;
        }
      }

      @Override
      public int hashCode() {
        final String value;
        try {
          value = _$$encode(parent);
        }
        catch (final MarshalException e) {
          return super.hashCode();
        }

        if (value == null)
          return super.hashCode();

        return value.hashCode();
      }
    }

    public static abstract class $AnyType extends $AnySimpleType {
      private static final long serialVersionUID = -6510869738097560771L;

      private final List<Binding> anys = new ArrayList<>(7);
      private final List<Binding> anys$ = new ArrayList<>(7);

      public $AnyType(final $AnyType binding) {
        super(binding);
      }

      public $AnyType(final Object text) {
        super(text);
      }

      protected $AnyType(final String text) {
        super(text);
      }

      protected $AnyType() {
        super();
      }

      public void addAny$(final Binding any) {
        this.anys$.add(any);
      }

      public List<Binding> getAny$() {
        return anys$;
      }

      public void addAny(final Binding any) {
        this.anys.add(any);
      }

      public List<Binding> getAny() {
        return anys;
      }

      @Override
      public $AnyType clone() {
        return new $AnyType(this) {
          private static final long serialVersionUID = 7881269189540416455L;

          @Override
          public QName name() {
            return this.name();
          }

          @Override
          protected $AnyType inherits() {
            return this;
          }
        };
      }

      @Override
      public boolean equals(final Object obj) {
        if (this == obj)
          return true;

        if (!(obj instanceof $AnyType))
          return false;

        final $AnyType that = ($AnyType)obj;
        if (anys != null) {
          if (that.anys != null && anys.size() == that.anys.size()) {
            for (int i = 0; i < anys.size(); ++i)
              if (!anys.get(i).equals(that.anys.get(i)))
                return false;
          }
          else {
            return false;
          }
        }
        else if (that.anys != null) {
          return false;
        }

        if (anys$ != null) {
          if (that.anys$ != null && anys$.size() == that.anys$.size()) {
            for (int i = 0; i < anys$.size(); ++i)
              if (!anys$.get(i).equals(that.anys$.get(i)))
                return false;
          }
          else {
            return false;
          }
        }
        else if (that.anys$ != null) {
          return false;
        }

        return true;
      }
    }

    public static abstract class $AnyURI extends $AnySimpleType {
      private static final long serialVersionUID = 7515449882634738035L;

      public $AnyURI(final $AnyURI binding) {
        super(binding);
      }

      public $AnyURI(final String value) {
        super(value);
      }

      protected $AnyURI() {
        super();
      }

      @Override
      public String text() {
        return (String)super.text();
      }

      public void text(final String text) {
        super.text(text);
      }

      @Override
      public $AnyURI clone() {
        return ($AnyURI)super.clone();
      }
    }

    public static abstract class $Base64Binary extends $AnySimpleType {
      private static final long serialVersionUID = -5862425707789169319L;

      public $Base64Binary(final $Base64Binary binding) {
        super(binding);
      }

      public $Base64Binary(final Base64Binary value) {
        super(value);
      }

      protected $Base64Binary() {
        super();
      }

      @Override
      public Base64Binary text() {
        return (Base64Binary)super.text();
      }

      public void text(final Base64Binary text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Base64Binary.parse(String.valueOf(value)));
      }

      @Override
      public $Base64Binary clone() {
        return ($Base64Binary)super.clone();
      }
    }

    public static abstract class $Boolean extends $AnySimpleType {
      private static final long serialVersionUID = 5805207783730082952L;
      private static final Map<Boolean,String[]> valueMap = new HashMap<>();

      public static Boolean parse(final String s) {
        if (s == null)
          return false;

        if (s.length() == 1)
          return "1".equals(s);

        return Boolean.parseBoolean(s);
      }

      static {
        valueMap.put(true, new String[] {
          "true", "1"
        });
        valueMap.put(false, new String[] {
          "false", "0"
        });
      }

      public $Boolean(final $Boolean binding) {
        super(binding);
      }

      public $Boolean(final Boolean value) {
        super(value);
      }

      protected $Boolean() {
        super();
      }

      @Override
      public Boolean text() {
        return (Boolean)super.text();
      }

      public void text(final Boolean text) {
        super.text(text);
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

        for (final String pattern : _$$getPattern()) {
          String[] ret = valueMap.get(super.text());
          for (int i = 0; i < ret.length; ++i) {
            if (ret[i].matches(pattern))
              return ret[i];
          }
        }

        throw new MarshalException("No valid return type. Schema error!!!");
      }

      @Override
      public $Boolean clone() {
        return ($Boolean)super.clone();
      }
    }

    public static abstract class $Byte extends $Short {
      private static final long serialVersionUID = 8992563175185047222L;

      public $Byte(final $Byte binding) {
        super(binding);
      }

      public $Byte(final Byte value) {
        super(value);
      }

      protected $Byte() {
        super();
      }

      @Override
      public Byte text() {
        return (Byte)super.text();
      }

      public void text(final Byte text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Byte.parseByte(String.valueOf(value)));
      }

      @Override
      public $Byte clone() {
        return ($Byte)super.clone();
      }
    }

    public static abstract class $Date extends $AnySimpleType {
      private static final long serialVersionUID = -1955542798279135254L;

      public $Date(final $Date binding) {
        super(binding);
      }

      public $Date(final Date value) {
        super(value);
      }

      protected $Date() {
        super();
      }

      @Override
      public Date text() {
        return (Date)super.text();
      }

      public void text(final Date text) {
        super.text(text);
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
    public static abstract class $DateTime extends $AnySimpleType {
      private static final long serialVersionUID = 2247372411847607768L;

      public $DateTime(final $DateTime binding) {
        super(binding);
      }

      public $DateTime(final DateTime value) {
        super(value);
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
      public DateTime text() {
        return (DateTime)super.text();
      }

      public void text(final DateTime text) {
        super.text(text);
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

    public static abstract class $Decimal extends $AnySimpleType {
      private static final long serialVersionUID = 2611735387171278709L;

      public $Decimal(final $Decimal binding) {
        super(binding);
      }

      public $Decimal(final BigDecimal value) {
        super(value);
      }

      protected $Decimal(final Number value) {
        super(value);
      }

      protected $Decimal() {
        super();
      }

      @Override
      public Number text() {
        return (Number)super.text();
      }

      public void text(final Number text) {
        super.text(text);
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

    public static abstract class $Double extends $AnySimpleType {
      private static final long serialVersionUID = 5696304973581941043L;

      public $Double(final $Double binding) {
        super(binding);
      }

      public $Double(final Double value) {
        super(value);
      }

      protected $Double() {
        super();
      }

      @Override
      public Double text() {
        return (Double)super.text();
      }

      public void text(final Double text) {
        super.text(text);
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

    public static abstract class $Duration extends $AnySimpleType {
      private static final long serialVersionUID = 4604277237345201267L;

      public $Duration(final $Duration binding) {
        super(binding);
      }

      public $Duration(final Duration value) {
        super(value);
      }

      protected $Duration() {
        super();
      }

      @Override
      public Duration text() {
        return (Duration)super.text();
      }

      protected void text(final Duration text) {
        super.text(text);
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

    @SuppressWarnings("unchecked")
    public static abstract class $ENTITIES extends $AnySimpleType {
      private static final long serialVersionUID = -4444456617105792286L;

      public $ENTITIES(final $ENTITIES binding) {
        super(binding);
      }

      public $ENTITIES(final List<String> value) {
        super(value);
      }

      protected $ENTITIES() {
        super();
      }

      @Override
      public List<String> text() {
        return (List<String>)super.text();
      }

      public <T extends List<String> & Serializable>void text(final T text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        text(decode(value, true));
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

    public static abstract class $ENTITY extends $nCName {
      private static final long serialVersionUID = 2131270403814548561L;

      public $ENTITY(final $ENTITY binding) {
        super(binding);
      }

      public $ENTITY(final String value) {
        super(value);
      }

      protected $ENTITY() {
        super();
      }

      @Override
      public $ENTITY clone() {
        return ($ENTITY)super.clone();
      }
    }

    public static abstract class $Float extends $AnySimpleType {
      private static final long serialVersionUID = 8396002187294788098L;

      public $Float(final $Float binding) {
        super(binding);
      }

      public $Float(final Float value) {
        super(value);
      }

      protected $Float() {
        super();
      }

      @Override
      public Float text() {
        return (Float)super.text();
      }

      public void text(final Float text) {
        super.text(text);
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

    public static abstract class $GDay extends $AnySimpleType {
      private static final long serialVersionUID = -7306738302337538690L;

      public $GDay(final $GDay binding) {
        super(binding);
      }

      public $GDay(final Day value) {
        super(value);
      }

      protected $GDay() {
        super();
      }

      @Override
      public Day text() {
        return (Day)super.text();
      }

      public void text(final Day text) {
        super.text(text);
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

    public static abstract class $GMonth extends $AnySimpleType {
      private static final long serialVersionUID = -9051270336948297692L;

      public $GMonth(final $GMonth binding) {
        super(binding);
      }

      public $GMonth(final Month value) {
        super(value);
      }

      protected $GMonth() {
        super();
      }

      @Override
      public Month text() {
        return (Month)super.text();
      }

      public void text(final Month text) {
        super.text(text);
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

    public static abstract class $GMonthDay extends $AnySimpleType {
      private static final long serialVersionUID = 2892185404567465759L;

      public $GMonthDay(final $GMonthDay binding) {
        super(binding);
      }

      public $GMonthDay(final MonthDay value) {
        super(value);
      }

      protected $GMonthDay() {
        super();
      }

      @Override
      public MonthDay text() {
        return (MonthDay)super.text();
      }

      public void text(final MonthDay text) {
        super.text(text);
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

    public static abstract class $GYear extends $AnySimpleType {
      private static final long serialVersionUID = 7929109327684024810L;

      public $GYear(final $GYear binding) {
        super(binding);
      }

      public $GYear(final Year value) {
        super(value);
      }

      protected $GYear() {
        super();
      }

      @Override
      public Year text() {
        return (Year)super.text();
      }

      public void text(final Year text) {
        super.text(text);
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

    public static abstract class $GYearMonth extends $AnySimpleType {
      private static final long serialVersionUID = -6730855611061489701L;

      public $GYearMonth(final $GYearMonth binding) {
        super(binding);
      }

      public $GYearMonth(final YearMonth value) {
        super(value);
      }

      protected $GYearMonth() {
        super();
      }

      @Override
      public YearMonth text() {
        return (YearMonth)super.text();
      }

      public void text(final YearMonth text) {
        super.text(text);
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

    public static abstract class $HexBinary extends $AnySimpleType {
      private static final long serialVersionUID = -3672541717905058784L;

      public $HexBinary(final $HexBinary binding) {
        super(binding);
      }

      public $HexBinary(final HexBinary value) {
        super(value);
      }

      protected $HexBinary() {
        super();
      }

      @Override
      public HexBinary text() {
        return (HexBinary)super.text();
      }

      public void text(final HexBinary text) {
        super.text(text);
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

    public static abstract class $ID extends $nCName {
      private static final long serialVersionUID = 8671692505211063717L;
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

      public $ID(final $ID binding) {
        super(binding);
      }

      public $ID(final String value) {
        super(value);
        persist(name().getNamespaceURI(), value, this);
      }

      protected $ID() {
        super();
      }

      @Override
      public void text(final String text) {
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

    public static abstract class $IDREF extends $nCName {
      private static final long serialVersionUID = 3413021684301501386L;

      public $IDREF(final $IDREF binding) {
        super(binding);
      }

      public $IDREF(final String value) {
        super(value);
      }

      protected $IDREF() {
        super();
      }

      @Override
      public $IDREF clone() {
        return ($IDREF)super.clone();
      }
    }

    @SuppressWarnings("unchecked")
    public static abstract class $IDREFS extends $AnySimpleType {
      private static final long serialVersionUID = 8278637562824948791L;

      public $IDREFS(final $IDREFS binding) {
        super(binding);
      }

      public $IDREFS(final List<String> value) {
        super(value);
      }

      protected $IDREFS() {
        super();
      }

      @Override
      public List<String> text() {
        return (List<String>)super.text();
      }

      public <T extends List<String> & Serializable>void text(final T text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        text(decode(value, true));
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

    public static abstract class $Int extends $Long {
      private static final long serialVersionUID = 6845008408792068840L;

      public $Int(final $Int binding) {
        super(binding);
      }

      public $Int(final Integer value) {
        super(value);
      }

      protected $Int(final Number value) {
        super(value);
      }

      protected $Int() {
        super();
      }

      public void text(final Integer text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Integer.parseInt(String.valueOf(value)));
      }

      @Override
      public $Int clone() {
        return ($Int)super.clone();
      }
    }

    public static abstract class $Integer extends $Decimal {
      private static final long serialVersionUID = 4688966863845290706L;

      public $Integer(final $Integer binding) {
        super(binding);
      }

      public $Integer(final BigInteger value) {
        super(value);
      }

      protected $Integer(final Number value) {
        super(value);
      }

      protected $Integer() {
        super();
      }

      @Override
      protected Binding inherits() {
        return null;
      }

      public void text(final BigInteger text) {
        super.text(text);
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

    public static abstract class $Language extends $AnySimpleType {
      private static final long serialVersionUID = -2141452623750225147L;

      public $Language(final $Language binding) {
        super(binding);
      }

      public $Language(final Language value) {
        super(value);
      }

      protected $Language() {
        super();
      }

      @Override
      public Language text() {
        return (Language)super.text();
      }

      public void text(final Language text) {
        super.text(text);
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

    public static abstract class $Long extends $Integer {
      private static final long serialVersionUID = 3110685220531748900L;

      public $Long(final $Long binding) {
        super(binding);
      }

      public $Long(final Long value) {
        super(value);
      }

      protected $Long(final Number value) {
        super(value);
      }

      protected $Long() {
        super();
      }

      public void text(final Long text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        super.text(Long.parseLong(String.valueOf(value)));
      }

      @Override
      public $Long clone() {
        return ($Long)super.clone();
      }
    }

    public static abstract class $MonthDay extends $AnySimpleType {
      private static final long serialVersionUID = -5881188958964982827L;

      public $MonthDay(final $MonthDay binding) {
        super(binding);
      }

      public $MonthDay(final MonthDay value) {
        super(value);
      }

      protected $MonthDay() {
        super();
      }

      @Override
      public MonthDay text() {
        return (MonthDay)super.text();
      }

      public void text(final MonthDay text) {
        super.text(text);
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

    public static abstract class $NMTOKEN extends $Token {
      private static final long serialVersionUID = -8208935544015142206L;

      public $NMTOKEN(final $NMTOKEN binding) {
        super(binding);
      }

      public $NMTOKEN(final String value) {
        super(value);
      }

      protected $NMTOKEN() {
        super();
      }

      @Override
      public $NMTOKEN clone() {
        return ($NMTOKEN)super.clone();
      }
    }

    @SuppressWarnings("unchecked")
    public static abstract class $NMTOKENS extends $AnySimpleType {
      private static final long serialVersionUID = -5260386241351935007L;

      public $NMTOKENS(final $NMTOKENS binding) {
        super(binding);
      }

      public $NMTOKENS(final List<String> value) {
        super(value);
      }

      protected $NMTOKENS() {
        super();
      }

      @Override
      public List<String> text() {
        return (List<String>)super.text();
      }

      public <T extends List<String> & Serializable>void text(final T text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element parent, final String value) {
        text(decode(value, true));
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

    public static abstract class $NOTATION extends $AnySimpleType {
      private static final long serialVersionUID = 5701767081230621619L;

      public $NOTATION(final $NOTATION binding) {
        super(binding);
      }

      public $NOTATION(final NotationType value) {
        super(value);
      }

      protected $NOTATION() {
        super();
      }

      @Override
      public NotationType text() {
        return (NotationType)super.text();
      }

      public void text(final NotationType text) {
        super.text(text);
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
          qName = new QName(parent.getOwnerDocument().getNamespaceURI(), value);
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

    public static abstract class $NegativeInteger extends $NonPositiveInteger {
      private static final long serialVersionUID = -1150394155850884963L;

      public $NegativeInteger(final $NegativeInteger binding) {
        super(binding);
      }

      public $NegativeInteger(final BigInteger value) {
        super(value);
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

    public static abstract class $NonNegativeInteger extends $Integer {
      private static final long serialVersionUID = 7211473945782812029L;

      public $NonNegativeInteger(final $NonNegativeInteger binding) {
        super(binding);
      }

      public $NonNegativeInteger(final BigInteger value) {
        super(value);
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

    public static abstract class $NonPositiveInteger extends $Integer {
      private static final long serialVersionUID = 6601655744973800161L;

      public $NonPositiveInteger(final $NonPositiveInteger binding) {
        super(binding);
      }

      public $NonPositiveInteger(final BigInteger value) {
        super(value);
      }

      protected $NonPositiveInteger(final Number value) {
        super(value);
      }

      protected $NonPositiveInteger() {
        super();
      }

      @Override
      public BigInteger text() {
        return (BigInteger)super.text();
      }

      @Override
      public $NonPositiveInteger clone() {
        return ($NonPositiveInteger)super.clone();
      }
    }

    public static abstract class $NormalizedString extends $String {
      private static final long serialVersionUID = 3288734660660079355L;

      public $NormalizedString(final $NormalizedString binding) {
        super(binding);
      }

      public $NormalizedString(final String value) {
        super(value);
      }

      protected $NormalizedString() {
        super();
      }

      @Override
      public $NormalizedString clone() {
        return ($NormalizedString)super.clone();
      }
    }

    public static abstract class $PositiveInteger extends $NonNegativeInteger {
      private static final long serialVersionUID = 2815146494897113558L;

      public $PositiveInteger(final $PositiveInteger binding) {
        super(binding);
      }

      public $PositiveInteger(final BigInteger value) {
        super(value);
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

    public static abstract class $Short extends $Int {
      private static final long serialVersionUID = 2591829673501941175L;

      public $Short(final $Short binding) {
        super(binding);
      }

      public $Short(final Short value) {
        super(value);
      }

      protected $Short(final Number value) {
        super(value);
      }

      protected $Short() {
        super();
      }

      public void text(final Short text) {
        super.text(text);
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

    public static abstract class $String extends $AnySimpleType {
      private static final long serialVersionUID = 8341193547169457336L;

      public $String(final $String binding) {
        super(binding);
      }

      public $String(final String value) {
        super(value);
      }

      protected $String() {
        super();
      }

      @Override
      public String text() {
        return (String)super.text();
      }

      public void text(final String text) {
        super.text(text);
      }

      @Override
      public $String clone() {
        return ($String)super.clone();
      }
    }

    public static abstract class $Time extends $AnySimpleType {
      private static final long serialVersionUID = -3591270457108634772L;

      public $Time(final $Time binding) {
        super(binding);
      }

      public $Time(final Time value) {
        super(value);
      }

      protected $Time() {
        super();
      }

      @Override
      public Time text() {
        return (Time)super.text();
      }

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

    public static abstract class $Token extends $NormalizedString {
      private static final long serialVersionUID = 9075199116743643063L;

      public $Token(final $Token binding) {
        super(binding);
      }

      public $Token(final String value) {
        super(value);
      }

      protected $Token() {
        super();
      }

      @Override
      public $Token clone() {
        return ($Token)super.clone();
      }
    }

    public static abstract class $UnsignedByte extends $UnsignedShort {
      private static final long serialVersionUID = -1366687334956097755L;

      public $UnsignedByte(final $UnsignedByte binding) {
        super(binding);
      }

      public $UnsignedByte(final Short value) {
        super(value);
      }

      protected $UnsignedByte() {
        super();
      }

      public void text(final Short text) {
        super.text(text);
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

    public static abstract class $UnsignedInt extends $UnsignedLong {
      private static final long serialVersionUID = -6858787856727414457L;

      public $UnsignedInt(final $UnsignedInt binding) {
        super(binding);
      }

      public $UnsignedInt(final Long value) {
        super(value);
      }

      protected $UnsignedInt(final Number value) {
        super(value);
      }

      protected $UnsignedInt() {
        super();
      }

      public void text(final Long text) {
        super.text(text);
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

    public static abstract class $UnsignedLong extends $NonNegativeInteger {
      private static final long serialVersionUID = 2576671958539402360L;

      public $UnsignedLong(final $UnsignedLong binding) {
        super(binding);
      }

      public $UnsignedLong(final BigInteger value) {
        super(value);
      }

      protected $UnsignedLong(final Number value) {
        super(value);
      }

      protected $UnsignedLong() {
        super();
      }

      @Override
      public void text(final BigInteger text) {
        super.text(text);
      }

      @Override
      public $UnsignedLong clone() {
        return ($UnsignedLong)super.clone();
      }
    }

    public static abstract class $UnsignedShort extends $UnsignedInt {
      private static final long serialVersionUID = -1117185446138982528L;

      public $UnsignedShort(final $UnsignedShort binding) {
        super(binding);
      }

      public $UnsignedShort(final Integer value) {
        super(value);
      }

      protected $UnsignedShort(final Number value) {
        super(value);
      }

      protected $UnsignedShort() {
        super();
      }

      public void text(final Integer text) {
        super.text(text);
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

    public static abstract class $nCName extends $name {
      private static final long serialVersionUID = 7261736605051866911L;

      public $nCName(final $nCName binding) {
        super(binding);
      }

      public $nCName(final String value) {
        super(value);
      }

      protected $nCName() {
        super();
      }

      @Override
      public $nCName clone() {
        return ($nCName)super.clone();
      }
    }

    public static abstract class $name extends $Token {
      private static final long serialVersionUID = 636502937738084071L;

      public $name(final $name binding) {
        super(binding);
      }

      public $name(final String value) {
        super(value);
      }

      protected $name() {
        super();
      }

      @Override
      public $name clone() {
        return ($name)super.clone();
      }
    }

    public static abstract class $qName extends $AnySimpleType {
      private static final long serialVersionUID = -6159227210752729335L;

      public $qName(final $qName binding) {
        super(binding);
      }

      public $qName(final QName value) {
        super(value);
      }

      protected $qName() {
        super();
      }

      @Override
      public QName text() {
        return (QName)super.text();
      }

      public void text(final QName text) {
        super.text(text);
      }

      @Override
      protected void _$$decode(final Element element, final String value) {
        final QName qName = stringToQName(value);
        super.text(qName);
        if (element != null)
          super.text(new javax.xml.namespace.QName(element.getOwnerDocument().getDocumentElement().lookupNamespaceURI(qName.getPrefix()), qName.getLocalPart()));
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