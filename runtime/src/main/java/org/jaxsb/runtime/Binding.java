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

package org.jaxsb.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.libj.lang.Classes;
import org.libj.util.ArrayUtil;
import org.libj.util.CollectionUtil;
import org.libj.util.HashBiMap;
import org.openjax.xml.api.ValidationException;
import org.openjax.xml.dom.DOMStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

@SuppressWarnings("rawtypes")
public abstract class Binding extends AbstractBinding {
  private static final Logger logger = LoggerFactory.getLogger(Binding.class);

  protected static DocumentBuilder newDocumentBuilder() {
    try {
      final DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
      builder.setEntityResolver((publicId, systemId) -> {
        if (logger.isDebugEnabled()) logger.debug("resolveEntity(\"" + publicId + "\", \"" + systemId + "\")");
        return null;
      });
      return builder;
    }
    catch (final ParserConfigurationException e) {
      throw new IllegalStateException(e);
    }
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

  public static class PrefixToNamespace extends HashBiMap<String,String> {
    private int nsIndex = 1;

    public String getPrefix(final QName name) {
      final String namespace = name.getNamespaceURI();
      if (namespace == null || namespace.length() == 0)
        return null;

      String prefix = reverse().get(namespace);
      if (prefix != null)
        return prefix;

      if (XSI_TYPE.getNamespaceURI().equals(namespace)) {
        prefix = XSI_TYPE.getPrefix();
      }
      else {
        prefix = name.getPrefix();
        if (prefix == null || prefix.length() == 0)
          prefix = "ns" + nsIndex++;
      }

      put(prefix, namespace);
      return prefix;
    }

    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder();
      if (size() > 0)
        for (final Map.Entry<String,String> entry : entrySet()) // [S]
          str.append(" xmlns:").append(entry.getKey()).append("=\"").append(entry.getValue()).append('"');

      return str.toString();
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

  protected static $AnyType<?> owner(final Binding binding) {
    return binding.owner;
  }

  protected static boolean _$$iSsubstitutionGroup(final QName elementName, final String namespaceURI, final String localName) {
    if (elementName == null || elementName.getNamespaceURI().length() == 0 || namespaceURI == null || localName == null)
      return false;

    final Class<? extends Binding> element = lookupElement(elementName, Thread.currentThread().getContextClassLoader());
    if (element == null)
      return false;

    final Field[] fields = element.getDeclaredFields();
    try {
      for (final Field field : fields) { // [A]
        if (!"SUBSTITUTION_GROUP".equals(field.getName()))
          continue;

        field.setAccessible(true);
        final QName substitutionGroup = (QName)field.get(null);
        return namespaceURI.equals(substitutionGroup.getNamespaceURI()) && localName.equals(substitutionGroup.getLocalPart());
      }
    }
    catch (final IllegalAccessException e) {
      throw new IllegalStateException(e);
    }

    return false;
  }

  protected static $AnyType<?> parse(final Element element, final Class<? extends $AnyType> defaultClass) throws ValidationException {
    return parseElement(element, defaultClass, Thread.currentThread().getContextClassLoader());
  }

  protected static $AnyType<?> parse(final Element element) throws ValidationException {
    return parseElement(element, null, Thread.currentThread().getContextClassLoader());
  }

  protected static $AnyType<?> parseElement(final Element element, final Class<? extends $AnyType> defaultClass, final ClassLoader classLoader) throws ValidationException {
    String namespaceURI = element.getNamespaceURI();
    if (namespaceURI == null)
      throw new IllegalArgumentException("Element does not declare a namespace");

    final String localName = element.getLocalName();

    String xsiTypeName = null;
    String xsiPrefix = null;

    final NamedNodeMap rootAttributes = element.getAttributes();
    for (int i = 0, i$ = rootAttributes.getLength(); i < i$; ++i) { // [RA]
      final Node attribute = rootAttributes.item(i);
      if (XSI_TYPE.getNamespaceURI().equals(attribute.getNamespaceURI()) && XSI_TYPE.getLocalPart().equals(attribute.getLocalName())) {
        xsiPrefix = parsePrefix(attribute.getNodeValue());
        xsiTypeName = parseLocalName(attribute.getNodeValue());
      }
    }

    Class<? extends Binding> classBinding;
    try {
      classBinding = defaultClass != null ? defaultClass : lookupElement(new QName(namespaceURI, localName), classLoader);
      Binding binding = null;
      if (classBinding != null) {
        final Constructor<?> constructor = classBinding.getDeclaredConstructor();
        constructor.setAccessible(true);
        binding = (Binding)constructor.newInstance();
      }

      if (xsiTypeName != null) {
        if (xsiPrefix != null)
          namespaceURI = element.getOwnerDocument().getDocumentElement().lookupNamespaceURI(xsiPrefix);

        final QName name = new QName(namespaceURI, xsiTypeName);
        final Class<? extends Binding> xsiBinding = lookupType(name, classLoader);
        if (xsiBinding == null)
          throw new IllegalStateException("Unable to find class binding for xsi:type: " + name);

        final Method method = Classes.getDeclaredMethod(xsiBinding, "newInstance");
        method.setAccessible(true);
        binding = ($AnyType<?>)method.invoke(null, binding);
      }

      if (binding == null) {
        if (namespaceURI != null)
          throw new IllegalStateException("Unable to find class binding for <" + localName + " xmlns=\"" + namespaceURI + "\">");

        throw new IllegalStateException("Unable to find class binding for <" + localName + "/>");
      }

      binding.parseAnyType(element);
      return ($AnyType<?>)binding;
    }
    catch (final IllegalAccessException | InstantiationException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      final Throwable cause = e.getCause();
      if (cause instanceof RuntimeException)
        throw (RuntimeException)cause;

      if (cause instanceof ValidationException)
        throw (ValidationException)cause;

      throw new RuntimeException(cause);
    }
  }

  protected abstract void parseAnyType(Element node) throws ValidationException;

  private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

  static {
    documentBuilderFactory.setNamespaceAware(true);
    documentBuilderFactory.setValidating(false);
    documentBuilderFactory.setXIncludeAware(true);
  }

  private ElementCompositeList elements;
  private CompositeAttributeStore attributeDirectory;
  private Binding inherits;
  private $AnyType<?> owner;
  private boolean isDefault;

  protected Binding(final Binding copy) {
    this();
    this.isDefault = copy.isDefault;
    // FIXME: This looks like an overly complicated copy constructor
    if (copy._$$hasElements())
      this.elements = copy.elements.cloneList(($AnyType<?>)this);
  }

  protected Binding() {
    if ((inherits = inherits()) == null)
      return;

    if (this instanceof NotationType)
      return;

    boolean legalInheritance = false;
    final Constructor<?>[] constructors = _$$inheritsInstance().getClass().getDeclaredConstructors();
    for (int i = 0, i$ = constructors.length; i < i$; ++i) { // [A]
      if (constructors[i].getParameterTypes().length > 0 && constructors[i].getParameterTypes()[0].isAssignableFrom(getClass())) {
        legalInheritance = true;
        break;
      }
    }

    if (!legalInheritance)
      throw new IllegalArgumentException("Invalid inheritance hierarchy");
  }

  protected boolean isDefault() {
    return isDefault;
  }

  protected static void markDefault(final Binding binding) {
    binding.isDefault = true;
  }

  @SuppressWarnings("unchecked")
  protected BindingList<$AnyType> fetchChild(final QName name) {
    if (name.getLocalPart() == null)
      throw new IllegalArgumentException("name.getLocalPart() is null");

    try {
      final Method[] methods = getClass().getDeclaredMethods();
      for (final Method method : methods) { // [A]
        if (method.getReturnType() != null && method.getParameterTypes().length == 0) {
          final org.jaxsb.runtime.QName qName = method.getAnnotation(org.jaxsb.runtime.QName.class);
          if (qName != null && name.getLocalPart().equals(qName.localPart()) && (name.getNamespaceURI() != null ? name.getNamespaceURI().equals(qName.namespaceURI()) : qName.namespaceURI() == null))
            return (BindingList<$AnyType>)method.invoke(this);
        }
      }
    }
    catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      final Throwable cause = e.getCause();
      if (cause instanceof RuntimeException)
        throw (RuntimeException)cause;

      throw new RuntimeException(cause);
    }

    return null;
  }

  public $AnyType<?> owner() {
    return owner;
  }

  protected void _$$setOwner(final $AnyType<?> owner) {
    if (this.owner != null && this.owner != owner)
      throw new IllegalArgumentException("Cannot add another document's element. Hint: Use element.clone()");

    this.owner = owner;
  }

  protected final boolean _$$hasElements() {
    return elements != null && elements.size() != 0;
  }

  @SuppressWarnings("unchecked")
  protected final void _$$marshalElements(final Element parent) throws MarshalException {
    if (elements == null || elements.size() == 0)
      return;

    for (int i = 0, i$ = elements.size(); i < i$; ++i) { // [RA]
      $AnyType<?> element = elements.get(i);
      if (element instanceof BindingProxy)
        element = ((BindingProxy<$AnyType<?>>)element).getBinding();

      final ElementAudit<$AnyType<?>> elementAudit = (ElementAudit<$AnyType<?>>)elements.getComponentList(i).getAudit();
      elementAudit.marshal(parent, element);
    }
  }

  protected static void textToString(final StringBuilder str, Object text, final PrefixToNamespace prefixToNamespace) {
    if (text == null)
      return;

    if (text instanceof NotationType) {
      ((NotationType)text).toString(str, prefixToNamespace, true, false);
    }
    else if (text instanceof QName) {
      final QName qName = (QName)text;
      str.append(prefixToNamespace.getPrefix(qName)).append(':').append(qName.getLocalPart());
    }
    else if (text instanceof Collection) {
      str.append(CollectionUtil.toString((Collection)text, ' '));
    }
    else if (text.getClass().isArray()) {
      str.append(ArrayUtil.toString((Object[])text, ' '));
    }
    else {
      str.append(text);
    }
  }

  @SuppressWarnings("unchecked")
  protected int toString(final StringBuilder str, final PrefixToNamespace prefixToNamespace, final boolean qualified, final boolean nillable) {
    QName name = name();
    final boolean substitutionGroup = _$$isSubstitutionGroup(name) || _$$isSubstitutionGroup(name(inherits()));
    if (substitutionGroup)
      name = name(); // FIXME: Not sure what this is for

    final QName type = type();
    final boolean hasXsiType = !substitutionGroup && type() != null && (type != null && !type().equals(type) || !type().equals(type(inherits())));
    final String prefix = hasXsiType ? prefixToNamespace.getPrefix(type()) : qualified ? prefixToNamespace.getPrefix(name) : null;

    str.append('<');
    if (prefix != null)
      str.append(prefix).append(':');

    str.append(name.getLocalPart());
    final int index = str.length();

    if (hasXsiType)
      AttributeAudit.toString(str, prefixToNamespace, XSI_TYPE, true, prefix + ":" + type().getLocalPart());

    final boolean hasElements = _$$hasElements();
    final boolean hasText = text() != null;

    if (!hasElements && !hasText && nillable)
      AttributeAudit.toString(str, prefixToNamespace, XSI_NIL, true, "true");

    if (attributeDirectory != null)
      attributeDirectory.toString(str, prefixToNamespace);

    str.append('>');

    if (hasText)
      textToString(str, text(), prefixToNamespace);

    if (hasElements) {
      for (int i = 0, i$ = elements.size(); i < i$; ++i) { // [RA]
        $AnyType<?> element = elements.get(i);
        if (element instanceof BindingProxy)
          element = ((BindingProxy<$AnyType<?>>)element).getBinding();

        final ElementAudit<$AnyType<?>> elementAudit = (ElementAudit<$AnyType<?>>)elements.getComponentList(i).getAudit();
        element.toString(str, prefixToNamespace, elementAudit.qualified(), elementAudit.nillable());
      }
    }

    str.append("</");
    if (prefix != null)
      str.append(prefix).append(':');

    str.append(name.getLocalPart()).append('>');
    return index;
  }


  protected ElementCompositeList getCreateElementDirectory() {
    return elements == null ? elements = new ElementCompositeList(($AnyType<?>)this, nameToAudit) : elements;
  }

  protected final <B extends $AnyType<?>>boolean _$$addElement(final ElementAudit<B> elementAudit, final B element) {
    final boolean added = elementAudit.addElement(element);
    if (added && element != null)
      element._$$setOwner(($AnyType<?>)this); // FIXME: Can we get rid of owner altogether?

    return added;
  }

  private HashMap<QName,ElementAudit<?>> nameToAudit;

  protected final void _$$registerElementAudit(final ElementAudit<?> elementAudit) {
    if (nameToAudit == null)
      nameToAudit = new HashMap<>();

    nameToAudit.put(elementAudit.getName() != null ? elementAudit.getName() : ElementCompositeList.ANY, elementAudit);
  }

  @SuppressWarnings("unchecked")
  protected final <T extends $AnyType<?>>ElementAudit<T> getAudit(final ElementAudit<T> audit) {
    return (ElementAudit<T>)nameToAudit.get(audit.getName());
  }

  protected static <B extends $AnySimpleType>boolean _$$setAttribute(final AttributeAudit<B> audit, final $AnyType binding, final B attribute) {
    if (attribute != null)
      attribute._$$setOwner(binding); // FIXME: Can we get rid of owner altogether?

    return audit.setAttribute(attribute);
  }

  private CompositeAttributeStore getCreateAttributeStore() {
    return attributeDirectory == null ? attributeDirectory = new CompositeAttributeStore() : attributeDirectory;
  }

  protected final <B extends $AnySimpleType<?>>AttributeAudit<B> __$$registerAttributeAudit(final AttributeAudit<B> audit) {
    getCreateAttributeStore().add(audit);
    return audit;
  }

  protected Iterator<$AnyType> elementIterator() {
    return getCreateElementDirectory().iterator();
  }

  protected ListIterator<$AnyType> elementListIterator() {
    return getCreateElementDirectory().listIterator();
  }

  protected ListIterator<$AnyType> elementListIterator(final int index) {
    return getCreateElementDirectory().listIterator(index);
  }

  protected Iterator<$AnySimpleType> attributeIterator() {
    return getCreateAttributeStore().valueIterator();
  }

  protected abstract Binding inherits();

  protected final Binding _$$inheritsInstance() {
    return inherits == null ? inherits = inherits() : inherits;
  }

  public abstract QName name();

  public QName type() {
    return null;
  }

  protected boolean qualified() {
    return true;
  }

  protected boolean nilable() {
    return false;
  }

  /**
   * Returns a new {@link Attr} representing the marshaled attribute of the specified name, corresponding to the provided parent
   * {@link Element}.
   *
   * @param name The name of the attribute.
   * @param parent The parent {@link Element} of the attribute.
   * @return A new {@link Attr} representing the marshaled attribute of the specified name, corresponding to the provided parent
   *         {@link Element}.
   * @throws MarshalException If a marshal exception has occurred.
   */
  protected Attr marshalAttr(final String name, final Element parent) throws MarshalException {
    throw new UnsupportedOperationException("This is a template that must be overridden");
  }

  /**
   * Returns a new {@link Element} representing the marshaled element of the specified name and type, corresponding to the provided
   * parent {@link Element}.
   *
   * @param parent The parent {@link Element}.
   * @param name The name of the element.
   * @param type The type name of the element.
   * @return A new {@link Element} representing the marshaled element of the specified name and type, corresponding to the provided
   *         parent {@link Element}.
   * @throws MarshalException If a marshal exception has occurred.
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
    if (!substitutionGroup && type() != null && (type != null && !type().equals(type) || !type().equals(type(inherits())))) {
      final String prefix = _$$getPrefix(parent, type());
      parent.getOwnerDocument().getDocumentElement().setAttributeNS(XMLNS.getNamespaceURI(), XMLNS.getLocalPart() + ":" + XSI_TYPE.getPrefix(), XSI_TYPE.getNamespaceURI());
      element.setAttributeNS(XSI_TYPE.getNamespaceURI(), XSI_TYPE.getPrefix() + ":" + XSI_TYPE.getLocalPart(), prefix + ":" + type().getLocalPart());
    }

    return element;
  }

  /**
   * Specifies whether the provided {@link QName} is a substitution group.
   *
   * @param name The {@link QName}.
   * @return Whether the provided {@link QName} is a substitution group.
   */
  protected boolean _$$isSubstitutionGroup(final QName name) {
    return false;
  }

  /**
   * Returns a new {@link Element} representing this binding.
   *
   * @return A new {@link Element} representing this binding.
   * @throws MarshalException If a marshal exception has occurred.
   */
  protected Element marshal() throws MarshalException {
    final Element root = createElementNS(name().getNamespaceURI(), name().getLocalPart());
    return marshal(root, name(), type());
  }

  /**
   * Parse the specified {@link Element}.
   *
   * @param element The {@link Element}.
   * @return {@code true} if the specified {@link Element} was parsed successfully, otherwise {@code false}.
   * @throws ValidationException If a validation error has occurred.
   */
  protected boolean parseElement(final Element element) throws ValidationException {
    return false;
  }

  /**
   * Parse the specified {@link Attr}.
   *
   * @param attribute The {@link Attr}.
   * @return {@code true} if the specified {@link Attr} was parsed successfully, otherwise {@code false}.
   */
  protected boolean parseAttribute(final Attr attribute) {
    return false;
  }

  /**
   * Parse the specified {@code <any>}.
   *
   * @param element The {@code <any>}.
   * @throws ValidationException If a validation error has occurred.
   */
  protected void parseAny(final Element element) throws ValidationException {
  }

  /**
   * Parse the specified {@code <anyAttribute>}.
   *
   * @param attribute The {@code <anyAttribute>}.
   * @throws ValidationException If a validation error has occurred.
   */
  protected void parseAnyAttribute(final Attr attribute) throws ValidationException {
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
    return obj == this || obj instanceof Binding;
  }

  @Override
  public int hashCode() {
    return 31 + getClass().getName().hashCode();
  }

  @Override
  public Binding clone() {
    final Binding clone = (Binding)super.clone();
    if (elements != null) {
      clone.elements = elements.cloneList(($AnyType<?>)clone);
      clone.nameToAudit = elements.nameToAudit;
    }

    clone.attributeDirectory = attributeDirectory == null ? null : attributeDirectory.clone(($AnySimpleType<?>)clone);
    clone.inherits = inherits;
    clone.owner = null;
    return clone;
  }

  private boolean dirty;
  private Element cacheElement;
  private final String[] cacheString = new String[DOMStyle.combinations()];

  protected final void setDirty() {
    this.dirty = true;
    if (owner() != null)
      owner().setDirty();
  }

  protected boolean dirty() {
    final boolean dirty = this.dirty || owner != null && owner.dirty();
    this.dirty = false;
    return dirty;
  }

  public Element toDOM() {
    return cacheElement == null || dirty() ? cacheElement = marshal() : cacheElement;
  }

  public String toString(final DOMStyle ... styles) {
    final int combination = DOMStyle.combination(styles);
    final String cached;
    if (!dirty() && (cached = cacheString[combination]) != null)
      return cached;

    final StringBuilder str = new StringBuilder();
    final PrefixToNamespace prefixToNamespace = new PrefixToNamespace();
    final int index = toString(str, prefixToNamespace, qualified(), nilable());
    str.insert(index, prefixToNamespace.toString());
    return cacheString[combination] = str.toString();
  }

  @Override
  public String toString() {
    return toString((DOMStyle[])null);
  }
}