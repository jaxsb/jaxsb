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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.jaxsb.compiler.lang.NamespaceBinding;
import org.libj.lang.PackageLoader;
import org.libj.lang.PackageNotFoundException;
import org.libj.net.URLs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;

@SuppressWarnings("rawtypes")
public abstract class AbstractBinding implements Cloneable {
  private static final Logger logger = LoggerFactory.getLogger(AbstractBinding.class);

  protected static final QName XSI_TYPE = new QName(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type", "xsi");
  protected static final QName XSI_NIL = new QName(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil", "xsi");
  protected static final QName XMLNS = new QName(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns");
  protected static final QName XML = new QName(XMLConstants.XML_NS_URI, "xml");

  private static final Map<QName,Class<? extends $AnySimpleType>> attributeBindings = new HashMap<>();
  private static final Map<QName,Class<? extends $AnyType>> elementBindings = new HashMap<>();
  private static final Map<QName,Class<? extends $AnyType>> typeBindings = new HashMap<>();
  private static final Map<QName,Object> notations = new HashMap<>();

  protected static NotationType _$$getNotation(final QName name) {
    final Object object = notations.get(name);
    if (object instanceof NotationType)
      return (NotationType)object;

    if (object == null)
      throw new IllegalArgumentException("Notation not found: " + name);

    if (!(object instanceof Class))
      throw new UnsupportedOperationException("Unsupported object type in notations map: " + object.getClass().getName());

    try {
      final Constructor<?> constructor = ((Class<?>)object).getDeclaredConstructor();
      constructor.setAccessible(true);
      final NotationType notation = (NotationType)constructor.newInstance();
      notations.put(name, notation);
      return notation;
    }
    catch (final IllegalAccessException | InstantiationException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      throw new RuntimeException(e.getCause());
    }
  }

  /**
   * Registers a {@code NOTATION} with the specified parameters.
   *
   * @param name The {@link QName} of the {@code NOTATION}.
   * @param publicName The {@code publicName} of the {@code NOTATION}.
   * @param systemName The {@code systemName} of the {@code NOTATION}.
   * @param notation The {@link NotationType Class} of the notation.
   */
  // FIXME: How does systemName play into this?
  protected static void _$$registerNotation(final QName name, final String publicName, final String systemName, final Class<? extends NotationType> notation) {
    notations.put(name, notation);
  }

  protected static void _$$registerSchemaLocation(final String namespaceURI, final Class<?> cls, final String schemaReference) {
    final String classPath = cls.getName().replace('.', '/') + ".class";
    final URL url = Thread.currentThread().getContextClassLoader().getResource(classPath);
    if (url == null) {
      if (logger.isDebugEnabled())
        logger.debug("Cannot register: systemId=\"" + namespaceURI + "\"\n\tclassName=\"" + cls.getName() + "\"\n\tschemaReference=\"" + schemaReference + "\"");

      return;
    }

    final URL parent = URLs.getCanonicalParent(url);
    try {
      BindingEntityResolver.registerSchemaLocation(namespaceURI, new URL(parent + "/" + schemaReference));
    }
    catch (final MalformedURLException e) {
      logger.error("Cannot register: systemId=\"" + namespaceURI + "\"\n\tclassName=\"" + cls.getName() + "\"\n\tschemaReference=\"" + schemaReference + "\"", e);
    }
  }

  protected static void _$$registerAttribute(final QName name, final Class<? extends $AnySimpleType> cls) {
    attributeBindings.put(name, cls);
  }

  protected static void _$$registerElement(final QName name, final Class<? extends $AnyType<?>> cls) {
    elementBindings.put(name, cls);
  }

  private static void loadPackage(final String namespaceURI, final ClassLoader classLoader) {
    // FIXME: Look this over. Also make a dedicated RuntimeException for this.
    try {
      PackageLoader.getPackageLoader(classLoader).loadPackage(NamespaceBinding.parseNamespace(namespaceURI).getPackageName(), Schema.class::isAssignableFrom);
    }
    catch (final IOException | PackageNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  protected static Class<? extends $AnySimpleType> lookupAttribute(final QName name, final ClassLoader classLoader) {
    final Class<? extends $AnySimpleType> cls = attributeBindings.get(name);
    if (cls != null)
      return cls;

    loadPackage(name.getNamespaceURI(), classLoader);
    return attributeBindings.get(name);
  }

  protected static Class<? extends $AnyType> lookupElement(final QName name, final ClassLoader classLoader) {
    final Class<? extends $AnyType> cls = elementBindings.get(name);
    if (cls != null)
      return cls;

    loadPackage(name.getNamespaceURI(), classLoader);
    return elementBindings.get(name);
  }

  protected static void _$$registerType(final QName name, final Class<? extends $AnyType<?>> cls) {
    typeBindings.put(name, cls);
  }

  protected static Class<? extends $AnyType> lookupType(final QName name, final ClassLoader classLoader) {
    final Class<? extends $AnyType> cls = typeBindings.get(name);
    if (cls != null)
      return cls;

    loadPackage(name.getNamespaceURI(), classLoader);
    return typeBindings.get(name);
  }

  protected static Object _$$getTEXT(final Binding binding) {
    return binding.text();
  }

  protected static QName getClassQName(final Class<? extends Binding> binding) {
    final org.jaxsb.runtime.QName name = binding.getDeclaredAnnotation(org.jaxsb.runtime.QName.class);
    return new QName(name.namespaceURI(), name.localPart(), name.prefix());
  }

  protected static QName stringToQName(final String name) {
    if (name == null || name.length() == 0)
      return null;

    final int index = name.indexOf(':');
    return index == -1 ? new QName(name) : new QName(null, name.substring(index + 1), name.substring(0, index));
  }

  protected static String parsePrefix(final String name) {
    if (name == null)
      return null;

    final int index = name.indexOf(':');
    return index == -1 ? null : name.substring(0, index);
  }

  protected static String parseLocalName(final String name) {
    if (name == null)
      return null;

    int start = name.indexOf('{');
    if (start != -1) {
      final int end = name.indexOf('}', start);
      if (end != -1)
        return name.substring(end + 1);
    }

    start = name.indexOf(':');
    return start == -1 ? name : name.substring(start + 1);
  }

  @Override
  public AbstractBinding clone() {
    try {
      return (AbstractBinding)super.clone();
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}