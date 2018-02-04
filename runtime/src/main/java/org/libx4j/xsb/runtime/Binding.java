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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lib4j.xml.dom.DOMStyle;
import org.lib4j.xml.dom.DOMs;
import org.lib4j.xml.validate.ValidationException;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public abstract class Binding extends AbstractBinding implements Serializable {
  private static final long serialVersionUID = -1760699437761774773L;

  protected static DocumentBuilder newDocumentBuilder() {
    final DocumentBuilder documentBuilder;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }
    catch (final ParserConfigurationException e) {
      throw new BindingError(e);
    }

    return documentBuilder;
  }

  protected static Element createElementNS(final String namespaceURI, final String localName) {
    return newDocumentBuilder().getDOMImplementation().createDocument(namespaceURI, localName, null).getDocumentElement();
  }

  protected static String _$$getPrefix(Element parent, final QName name) {
    String prefix = name.getPrefix();
    if (prefix == null || prefix.length() == 0) {
      if (name.getNamespaceURI() == null || name.getNamespaceURI().length() == 0)
        return null;

      parent = parent.getOwnerDocument().getDocumentElement();
      prefix = parent.lookupPrefix(name.getNamespaceURI());
      if (prefix != null)
        return prefix;

      short i = 0;
      while (parent.lookupNamespaceURI("ns" + ++i) != null);
      prefix = "ns" + i;
    }

    parent.getOwnerDocument().getDocumentElement().setAttributeNS(XMLNS.getNamespaceURI(), XMLNS.getLocalPart() + ":" + prefix, name.getNamespaceURI());
    return prefix;
  }

  protected static Binding _$$parseAttr(final Class<?> xmlClass, final Element parent, final Node attribute) {
    final Binding binding;
    try {
      final Constructor<?> constructor = xmlClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      binding = (Binding)constructor.newInstance();
      binding._$$decode(parent, attribute.getNodeValue());
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | ParseException e) {
      throw new BindingError(e);
    }

    return binding;
  }

  protected static void _$$decode(final Binding binding, final Element element, final String value) throws ParseException {
    binding._$$decode(element, value);
  }

  protected static String _$$encode(final Binding binding, final Element parent) throws MarshalException {
    return binding._$$encode(parent);
  }

  protected static void parse(final Binding binding, final Element node) throws ParseException, ValidationException {
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++)
      if (attributes.item(i) instanceof Attr && !binding.parseAttribute((Attr)attributes.item(i)))
        binding.parseAnyAttribute((Attr)attributes.item(i));

    final NodeList elements = node.getChildNodes();
    for (int i = 0; i < elements.getLength(); i++) {
      final Node child = elements.item(i);
      if (child instanceof Text)
        binding.parseText((Text)child);
      else if (child instanceof Element && !binding.parseElement((Element)elements.item(i)))
        binding.parseAny((Element)elements.item(i));
    }
  }

  protected static String id(final Binding binding) {
    return binding == null ? null : binding.id();
  }

  protected static QName name(final Binding binding) {
    return binding == null ? null : binding.name();
  }

  protected static QName type(final Binding binding) {
    return binding.type();
  }

  protected static $AnySimpleType owner(final Binding binding) {
    return binding.owner;
  }

  protected static boolean _$$iSsubstitutionGroup(final QName elementName, final String namespaceURI, final String localName) throws ParseException {
    if (elementName == null || namespaceURI == null || localName == null)
      return false;

    final Class<? extends Binding> element = lookupElement(elementName, Thread.currentThread().getContextClassLoader());
    if (element == null)
      return false;

    final Field[] fields = element.getDeclaredFields();
    try {
      for (final Field field : fields) {
        if (!"SUBSTITUTION_GROUP".equals(field.getName()))
          continue;

        field.setAccessible(true);
        final QName substitutionGroup = (QName)field.get(null);
        return namespaceURI.equals(substitutionGroup.getNamespaceURI()) && localName.equals(substitutionGroup.getLocalPart());
      }
    }
    catch (final IllegalAccessException e) {
      throw new ParseException(e);
    }

    return false;
  }

  protected static Binding parse(final Element element, Class<? extends Binding> defaultClass) throws ParseException, ValidationException {
    return parseElement(element, defaultClass, Thread.currentThread().getContextClassLoader());
  }

  protected static Binding parse(final Element element) throws ParseException, ValidationException {
    return parseElement(element, null, Thread.currentThread().getContextClassLoader());
  }

  protected static Binding parseAttr(final Element element, final Node node) throws ParseException {
    final String localName = node.getLocalName();
    final String namespaceURI = node.getNamespaceURI();
    final Class<?> classBinding = lookupElement(new QName(namespaceURI != null ? namespaceURI.intern() : null, localName.intern()), Thread.currentThread().getContextClassLoader());
    if (classBinding == null) {
      if (namespaceURI != null)
        throw new ParseException("Unable to find final class binding for <" + localName + " xmlns=\"" + namespaceURI + "\">");

      throw new ParseException("Unable to find final class binding for <" + localName + "/>");
    }

    return Binding._$$parseAttr(classBinding, element, node);
  }

  /**
   * @throws ValidationException
   */
  protected static Binding parseElement(final Element node, Class<? extends Binding> defaultClass, final ClassLoader classLoader) throws ParseException, ValidationException {
    final String localName = node.getLocalName();
    String namespaceURI = node.getNamespaceURI();

    String xsiTypeName = null;
    String xsiPrefix = null;

    final NamedNodeMap rootAttributes = node.getAttributes();
    for (int i = 0; i < rootAttributes.getLength(); i++) {
      final Node attribute = rootAttributes.item(i);
      if (XSI_TYPE.getNamespaceURI().equals(attribute.getNamespaceURI()) && XSI_TYPE.getLocalPart().equals(attribute.getLocalName())) {
        xsiPrefix = parsePrefix(attribute.getNodeValue());
        xsiTypeName = parseLocalName(attribute.getNodeValue());
      }
    }

    Class<? extends Binding> classBinding = null;
    try {
      classBinding = defaultClass != null ? defaultClass : lookupElement(new QName(namespaceURI.intern(), localName.intern()), classLoader);
      Binding binding = null;
      if (classBinding != null) {
        final Constructor<?> constructor = classBinding.getDeclaredConstructor();
        constructor.setAccessible(true);
        binding = (Binding)constructor.newInstance();
      }

      if (xsiTypeName != null) {
        if (xsiPrefix != null)
          namespaceURI = node.getOwnerDocument().getDocumentElement().lookupNamespaceURI(xsiPrefix);

        final Class<? extends Binding> xsiBinding = lookupType(new QName(namespaceURI, xsiTypeName.intern()), classLoader);
        if (xsiBinding == null) {
          if (namespaceURI != null)
            throw new ParseException("Unable to find final class binding for xsi:type <" + xsiTypeName + " xmlns=\"" + namespaceURI + "\">");

          throw new ParseException("Unable to find final class binding for xsi:type <" + xsiTypeName + "/>");
        }

        Method method = null;
        final Method[] methods = xsiBinding.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
          if ("newInstance".equals(methods[i].getName())) {
            method = methods[i];
            break;
          }
        }

        method.setAccessible(true);
        binding = (Binding)method.invoke(null, binding);
      }

      if (binding == null) {
        if (namespaceURI != null)
          throw new ParseException("Unable to find final class binding for <" + localName + " xmlns=\"" + namespaceURI + "\">");

        throw new ParseException("Unable to find final class binding for <" + localName + "/>");
      }

      Binding.parse(binding, node);
      return binding;
    }
    catch (final ParseException e) {
      throw e;
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
      throw new ParseException(e);
    }
  }

  private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

  static {
    documentBuilderFactory.setNamespaceAware(true);
    documentBuilderFactory.setValidating(false);
  }

  private ElementSuperList elements = null;
  private CompositeAttributeStore attributeDirectory = null;
  private Binding inherits;
  private $AnySimpleType owner;

  protected Binding(final Binding copy) {
    this();
    merge(copy);
  }

  protected Binding() {
    if ((inherits = inherits()) == null)
      return;

    if (this instanceof NotationType)
      return;

    boolean legalInheritance = false;
    final Constructor<?>[] constructors = _$$inheritsInstance().getClass().getDeclaredConstructors();
    for (int i = 0; i < constructors.length; i++) {
      if (constructors[i].getParameterTypes().length > 0 && constructors[i].getParameterTypes()[0].isAssignableFrom(getClass())) {
        legalInheritance = true;
        break;
      }
    }

    if (!legalInheritance)
      throw new IllegalArgumentException("Invalid inheritance hierarchy.");
  }

  @SuppressWarnings("unchecked")
  protected BindingList<? extends Binding> fetchChild(final QName name) {
    if (name == null)
      throw new IllegalArgumentException("name == null");

    if (name.getLocalPart() == null)
      throw new IllegalArgumentException("name.getLocalPart() == null");

    final Method[] methods = getClass().getDeclaredMethods();
    for (final Method method : methods) {
      if (method.getReturnType() != null && method.getParameterTypes().length == 0) {
        org.libx4j.xsb.runtime.QName qName = method.getAnnotation(org.libx4j.xsb.runtime.QName.class);
        if (qName != null) {
          if (name.getLocalPart().equals(qName.localPart()) && (name.getNamespaceURI() != null ? name.getNamespaceURI().equals(qName.namespaceURI()) : qName.namespaceURI() == null)) {
            try {
              return (BindingList<? extends Binding>)method.invoke(this);
            }
            catch (final IllegalAccessException | InvocationTargetException e) {
              throw new BindingError(e);
            }
          }
        }
      }
    }

    return null;
  }

  protected void merge(final Binding copy) {
    if (copy._$$hasElements())
      this.elements = copy.elements.clone();
    // this.owner = copy.owner;
  }

  protected $AnySimpleType _$$getOwner() {
    return owner;
  }

  protected void _$$setOwner(final $AnySimpleType owner) {
    this.owner = owner;
  }

  protected final boolean _$$hasElements() {
    return elements != null && elements.size() != 0;
  }

  @SuppressWarnings("unchecked")
  protected final void _$$marshalElements(final Element parent) throws MarshalException {
    if (elements == null || elements.size() == 0)
      return;

    for (int i = 0; i < elements.size(); i++) {
      Binding element = elements.get(i);
      if (element instanceof BindingProxy)
        element = ((BindingProxy<Binding>)element).getBinding();

      final ElementAudit<Binding> elementAudit = (ElementAudit<Binding>)elements.getPartition(i).getAudit();
      elementAudit.marshal(parent, element);
    }
  }

  protected ElementSuperList getCreateElementDirectory() {
    return elements == null ? elements = new ElementSuperList(typeToAudit) : elements;
  }

  protected final <B extends Binding>boolean _$$addElement(final ElementAudit<B> elementAudit, final B element) {
    final boolean added = elementAudit.addElement(element);
    if (added && element != null)
      element._$$setOwner(($AnySimpleType)this);

    return added;
  }

  private IdentityHashMap<Class<? extends Binding>,ElementAudit<?>> typeToAudit;

  protected final void _$$registerElementAudit(final ElementAudit<?> elementAudit) {
    if (typeToAudit == null)
      typeToAudit = new IdentityHashMap<Class<? extends Binding>,ElementAudit<?>>();

    typeToAudit.put(elementAudit.getType(), elementAudit);
  }

  protected static <B extends $AnySimpleType>boolean _$$setAttribute(final AttributeAudit<B> audit, final Binding binding, final B attribute) {
    if (attribute != null)
      attribute._$$setOwner(($AnySimpleType)binding);

    return audit.setAttribute(attribute);
  }

  private CompositeAttributeStore getCreateAttributeStore() {
    return attributeDirectory == null ? attributeDirectory = new CompositeAttributeStore() : attributeDirectory;
  }

  protected final <B extends $AnySimpleType>AttributeAudit<B> __$$registerAttributeAudit(final AttributeAudit<B> audit) {
    getCreateAttributeStore().add(audit);
    return audit;
  }

  protected Iterator<Binding> elementIterator() {
    return getCreateElementDirectory().iterator();
  }

  protected ListIterator<Binding> elementListIterator() {
    return getCreateElementDirectory().listIterator();
  }

  protected ListIterator<Binding> elementListIterator(final int index) {
    return getCreateElementDirectory().listIterator(index);
  }

  protected Iterator<? extends $AnySimpleType> attributeIterator() {
    return getCreateAttributeStore().iterator();
  }

  protected abstract Binding inherits();

  protected final Binding _$$inheritsInstance() {
    return inherits == null ? inherits = inherits() : inherits;
  }

  public abstract QName name();

  public QName type() {
    return null;
  }

  private static final Map<Class<? extends Binding>,Binding> nulls = new HashMap<Class<? extends Binding>,Binding>();

  protected static Binding NULL(final Class<? extends Binding> clazz) {
    Binding NULL = nulls.get(clazz);
    if (NULL != null)
      return NULL;

    try {
      NULL = nulls.get(clazz);
      if (NULL != null)
        return NULL;

      final Constructor<? extends Binding> constructor = clazz.getDeclaredConstructor();
      constructor.setAccessible(true);
      nulls.put(clazz, NULL = constructor.newInstance());
      NULL.isNull = true;
      return NULL;
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
      throw new BindingError(e);
    }
  }

  private volatile boolean isNull = false;

  public final boolean isNull() {
    return isNull;
  }

  /**
   * @throws MarshalException
   */
  protected Attr marshalAttr(final String name, final Element parent) throws MarshalException {
    throw new BindingError("This is a template that must be overridden");
  }

  /**
   * @throws MarshalException
   */
  protected Element marshal(final Element parent, QName name, final QName type) throws MarshalException {
    final boolean substitutionGroup = _$$isSubstitutionGroup(name) || _$$isSubstitutionGroup(name(inherits()));
    if (substitutionGroup)
      name = name();

    Element element = parent;
    if (parent.getPrefix() != null || parent.getNamespaceURI() == null)
      element = parent.getOwnerDocument().createElementNS(name.getNamespaceURI(), name.getLocalPart());

    element.setPrefix(_$$getPrefix(parent, name));

    // There is 1 way to exclude an xsi:type attribute:
    // 1. The element being marshaled is a substitutionGroup for the expected element
    // There are 2 ways to require an xsi:type attribute:
    // 1. The element being marshaled is not global, and its typeName comes from its containing complexType
    // 2. The complexType being marshaled is global, and its name comes from the element it inherits from
    if (!substitutionGroup && type() != null && ((type != null && !type().equals(type)) || !type().equals(type(inherits())))) {
      final String prefix = _$$getPrefix(parent, type());
      parent.getOwnerDocument().getDocumentElement().setAttributeNS(XMLNS.getNamespaceURI(), XMLNS.getLocalPart() + ":" + XSI_TYPE.getPrefix(), XSI_TYPE.getNamespaceURI());
      element.setAttributeNS(XSI_TYPE.getNamespaceURI(), XSI_TYPE.getPrefix() + ":" + XSI_TYPE.getLocalPart(), prefix + ":" + type().getLocalPart());
    }

    return element;
  }

  protected boolean _$$isSubstitutionGroup(final QName name) {
    return false;
  }

  /**
   * @throws MarshalException
   * @throws ValidationException
   */
  protected Element marshal() throws MarshalException, ValidationException {
    final Element root = createElementNS(name().getNamespaceURI(), name().getLocalPart());
    return marshal(root, name(), type());
  }

  /**
   * @throws ParseException
   * @throws ValidationException
   */
  protected boolean parseElement(final Element element) throws ParseException, ValidationException {
    return false;
  }

  /**
   * @throws ParseException
   * @throws ValidationException
   */
  protected boolean parseAttribute(final Attr attribute) throws ParseException, ValidationException {
    return false;
  }

  /**
   * @throws ParseException
   * @throws ValidationException
   */
  protected void parseText(final Text text) throws ParseException, ValidationException {
  }

  /**
   * @throws ParseException
   * @throws ValidationException
   */
  protected void parseAny(final Element element) throws ParseException, ValidationException {
  }

  /**
   * @throws ParseException
   * @throws ValidationException
   */
  protected void parseAnyAttribute(final Attr attribute) throws ParseException, ValidationException {
  }

  /**
   * @throws ParseException
   */
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    throw new BindingError("This is a template that must be overridden, otherwise it shouldn't be called");
  }

  /**
   * @throws MarshalException
   */
  protected String _$$encode(final Element parent) throws MarshalException {
    throw new BindingError("This is a template that must be overridden, otherwise it shouldn't be called");
  }

  protected String[] _$$getPattern() {
    return null;
  }

  protected static boolean _$$failEquals() {
    return false;
  }

  protected String id() {
    return null;
  }

  protected Object text() {
    return null;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Binding))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    return getClass().getName().hashCode();
  }

  @Override
  public Binding clone() {
    return null;
  }

  @Override
  public String toString() {
    return DOMs.domToString(marshal(), DOMStyle.INDENT);
  }

  public Element toDOM() {
    return marshal();
  }
}