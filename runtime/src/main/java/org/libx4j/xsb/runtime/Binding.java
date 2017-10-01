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
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lib4j.xml.validate.ValidationException;
import org.w3.x2001.xmlschema.xe.$xs_anySimpleType;
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

  protected static QName typeName(final Binding binding) {
    return binding.typeName();
  }

  protected static boolean _$$iSsubstitutionGroup(final QName elementName, final String namespaceURI, final String localName) throws ParseException {
    if (elementName == null || namespaceURI == null || localName == null)
      return false;

    final Class<? extends Binding> element = lookupElement(elementName);
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
    return parseElement(element, defaultClass);
  }

  protected static Binding parse(final Element element) throws ParseException, ValidationException {
    return parseElement(element, null);
  }

  protected static Binding parseAttr(final Element element, final Node node) throws ParseException {
    final String localName = node.getLocalName();
    final String namespaceURI = node.getNamespaceURI();
    final Class<?> classBinding = lookupElement(new QName(namespaceURI != null ? namespaceURI.intern() : null, localName.intern()));
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
  protected static Binding parseElement(final Element node, Class<? extends Binding> defaultClass) throws ParseException, ValidationException {
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
      classBinding = defaultClass != null ? defaultClass : lookupElement(new QName(namespaceURI.intern(), localName.intern()));
      Binding binding = null;
      if (classBinding != null) {
        final Constructor<?> constructor = classBinding.getDeclaredConstructor();
        constructor.setAccessible(true);
        binding = (Binding)constructor.newInstance();
      }

      if (xsiTypeName != null) {
        if (xsiPrefix != null)
          namespaceURI = node.getOwnerDocument().getDocumentElement().lookupNamespaceURI(xsiPrefix);

        final Class<? extends Binding> xsiBinding = lookupType(new QName(namespaceURI, xsiTypeName.intern()));
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

  private transient final Object elementsLock = new Object();
  private CompositeElementStore elementDirectory = null;
  private CompositeAttributeStore attributeDirectory = null;
  private Binding inherits;
  private $xs_anySimpleType owner;

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
      this.elementDirectory = copy.elementDirectory.clone();
    // this.owner = copy.owner;
  }

  protected $xs_anySimpleType _$$getOwner() {
    return owner;
  }

  protected void _$$setOwner(final $xs_anySimpleType owner) {
    this.owner = owner;
  }

  protected final boolean _$$hasElements() {
    return elementDirectory != null && elementDirectory.size() != 0;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected final void _$$marshalElements(final Element parent) throws MarshalException {
    if (elementDirectory == null || elementDirectory.size() == 0)
      return;

    synchronized (elementsLock) {
      for (int i = 0; i < elementDirectory.size(); i++) {
        final Binding element = elementDirectory.getElement(i);
        final ElementAudit elementAudit = elementDirectory.getElementAudits(i);
        elementAudit.marshal(parent, element);
      }
    }
  }

  protected final void _$$replaceElement(final Binding original, final ElementAudit<? extends Binding> elementAudit, final Binding element) {
    if (elementDirectory != null)
      elementDirectory.replace(original, element, elementAudit);
  }

  protected final void _$$addElementBefore(final Binding before, final ElementAudit<? extends Binding> elementAudit, final Binding element) {
    if (elementDirectory != null)
      elementDirectory.addBefore(before, element, elementAudit);
  }

  protected final void _$$addElementAfter(final Binding after, final ElementAudit<? extends Binding> elementAudit, final Binding element) {
    if (elementDirectory != null)
      elementDirectory.addAfter(after, element, elementAudit);
  }

  protected final <B extends Binding>boolean _$$addElement(final ElementAudit<B> elementAudit, final B element) {
    return _$$addElement(elementAudit, element, true);
  }

  protected final <B extends Binding>boolean _$$addElementNoAudit(final ElementAudit<B> elementAudit, final B element) {
    return _$$addElement(elementAudit, element, false);
  }

  private CompositeElementStore getCreateElementDirectory() {
    if (elementDirectory != null)
      return elementDirectory;

    synchronized (elementsLock) {
      if (elementDirectory != null)
        return elementDirectory;

      return elementDirectory = new CompositeElementStore(2);
    }
  }

  private <B extends Binding>boolean _$$addElement(final ElementAudit<B> elementAudit, final B element, final boolean addToAudit) {
    final CompositeElementStore elementDirectory = getCreateElementDirectory();
    if (!elementDirectory.add(element, elementAudit, addToAudit))
      throw new BindingRuntimeException("Elements list should have changed here.");

    if (element != null)
      element._$$setOwner(($xs_anySimpleType)this);

    return true;
  }

  protected static <B extends $xs_anySimpleType>boolean _$$setAttribute(final AttributeAudit<B> audit, final Binding binding, final B attribute) {
    if (attribute != null)
      attribute._$$setOwner(($xs_anySimpleType)binding);

    return audit.setAttribute(attribute);
  }

  private CompositeAttributeStore getCreateAttributeStore() {
    return attributeDirectory == null ? attributeDirectory = new CompositeAttributeStore() : attributeDirectory;
  }

  protected final <B extends $xs_anySimpleType>AttributeAudit<B> __$$registerAttributeAudit(final AttributeAudit<B> audit) {
    getCreateAttributeStore().add(audit);
    return audit;
  }

  protected Iterator<Binding> elementIterator() {
    return getCreateElementDirectory().getElements().iterator();
  }

  protected ListIterator<Binding> elementListIterator() {
    return getCreateElementDirectory().getElements().listIterator();
  }

  protected ListIterator<Binding> elementListIterator(final int index) {
    return getCreateElementDirectory().getElements().listIterator(index);
  }

  protected Iterator<? extends $xs_anySimpleType> attributeIterator() {
    return getCreateAttributeStore().iterator();
  }

  protected final boolean _$$removeElement(final Binding element) {
    synchronized (elementsLock) {
      return elementDirectory.remove(element);
    }
  }

  protected abstract Binding inherits();

  protected final Binding _$$inheritsInstance() {
    return inherits == null ? inherits = inherits() : inherits;
  }

  public abstract QName name();

  protected QName typeName() {
    return null;
  }

  private static final Map<Class<? extends Binding>,Binding> nulls = new HashMap<Class<? extends Binding>,Binding>();

  protected static Binding NULL(final Class<? extends Binding> clazz) {
    Binding NULL = nulls.get(clazz);
    if (NULL != null)
      return NULL;

    try {
      synchronized (clazz) {
        NULL = nulls.get(clazz);
        if (NULL != null)
          return NULL;

        final Constructor<? extends Binding> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        nulls.put(clazz, NULL = constructor.newInstance());
        NULL.isNull = true;
        return NULL;
      }
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
  protected Element marshal(final Element parent, QName name, final QName typeName) throws MarshalException {
    boolean substitutionGroup = _$$isSubstitutionGroup(name) || _$$isSubstitutionGroup(name(inherits()));
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
    if (!substitutionGroup && typeName() != null && ((typeName != null && !typeName().equals(typeName)) || !typeName().equals(typeName(inherits())))) {
      final String prefix = _$$getPrefix(parent, typeName());
      parent.getOwnerDocument().getDocumentElement().setAttributeNS(XMLNS.getNamespaceURI(), XMLNS.getLocalPart() + ":" + XSI_TYPE.getPrefix(), XSI_TYPE.getNamespaceURI());
      element.setAttributeNS(XML.getNamespaceURI(), XSI_TYPE.getPrefix() + ":" + XSI_TYPE.getLocalPart(), prefix + ":" + typeName().getLocalPart());
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
    return marshal(root, name(), typeName());
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
}