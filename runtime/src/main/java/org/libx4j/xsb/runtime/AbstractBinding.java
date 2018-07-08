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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.lib4j.lang.Classes;
import org.lib4j.lang.PackageLoader;
import org.lib4j.lang.PackageNotFoundException;
import org.lib4j.net.URLs;
import org.libx4j.xsb.compiler.lang.NamespaceBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBinding implements Cloneable {
  private static final Logger logger = LoggerFactory.getLogger(AbstractBinding.class);

  protected static final QName XSI_TYPE = new QName(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type", "xsi");
  protected static final QName XSI_NIL = new QName(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil", "xsi");
  protected static final QName XMLNS = new QName(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns");
  protected static final QName XML = new QName(XMLConstants.XML_NS_URI, "xml");

  private static final Map<QName,Class<? extends Binding>> elementBindings = new HashMap<QName,Class<? extends Binding>>();
  private static final Map<QName,Class<? extends Binding>> typeBindings = new HashMap<QName,Class<? extends Binding>>();
  private static final Map<String,Object> notations = new HashMap<String,Object>();

  protected static NotationType _$$getNotation(final String name) {
    final Object object = notations.get(name);
    if (object instanceof NotationType)
      return (NotationType)object;


    if (!(object instanceof Class))
      throw new UnsupportedOperationException("Unsupported object type in notations map: " + object.getClass().getName());

    try {
      final NotationType notation = (NotationType)((Class<?>)object).getDeclaredConstructor().newInstance();
      notations.put(name, notation);
      return notation;
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  // FIXME: How does systemName play into this?
  protected static void _$$registerNotation(final String publicName, final String systemName, final Class<? extends NotationType> notation) {
    notations.put(publicName, notation);
  }

  protected static void _$$registerSchemaLocation(final String namespaceURI, final Class<?> className, final String schemaReference) {
    final String simpleName = className.getName().replace('.', '/') + ".class";
    final URL url = Thread.currentThread().getContextClassLoader().getResource(simpleName);
    if (url == null) {
      logger.debug("Cannot register: systemId=\"" + namespaceURI + "\"\n\tclassName=\"" + className.getName() + "\"\n\tschemaReference=\"" + schemaReference + "\"");
      return;
    }

    final URL parent = URLs.getCanonicalParent(url);
    try {
      BindingEntityResolver.registerSchemaLocation(namespaceURI, new URL(parent + "/" + schemaReference));
    }
    catch (final MalformedURLException e) {
      logger.error("Cannot register: systemId=\"" + namespaceURI + "\"\n\tclassName=\"" + className.getName() + "\"\n\tschemaReference=\"" + schemaReference + "\"");
    }
  }

  protected static void _$$registerElement(final QName name, final Class<? extends Binding> cls) {
    elementBindings.put(name, cls);
  }

  private static void loadPackage(final String namespaceURI, final ClassLoader classLoader) {
    // FIXME: Look this over. Also make a dedicated RuntimeException for this.
    try {
      final Set<Class<?>> classes = PackageLoader.getPackageLoader(classLoader).loadPackage(NamespaceBinding.parseNamespace(namespaceURI).getPackageName());
      for (final Class<?> cls : classes) {
        if (Schema.class.isAssignableFrom(cls)) {
          final Method method = cls.getDeclaredMethod("_$$register");
          method.setAccessible(true);
          method.invoke(null);
        }
      }
    }
    catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException | PackageNotFoundException e) {
      throw new BindingRuntimeException(e);
    }
  }

  protected static Class<? extends Binding> lookupElement(final QName name, final ClassLoader classLoader) {
    final Class<? extends Binding> clazz = elementBindings.get(name);
    if (clazz != null)
      return clazz;

    loadPackage(name.getNamespaceURI(), classLoader);
    return elementBindings.get(name);
  }

  protected static void _$$registerType(final QName name, final Class<? extends Binding> cls) {
    typeBindings.put(name, cls);
  }

  protected static Class<? extends Binding> lookupType(final QName name, final ClassLoader classLoader) {
    final Class<? extends Binding> clazz = typeBindings.get(name);
    if (clazz != null)
      return clazz;

    loadPackage(name.getNamespaceURI(), classLoader);
    return typeBindings.get(name);
  }

  protected static Object _$$getTEXT(final Binding binding) {
    return binding.text();
  }

  protected static QName getClassQName(final Class<? extends Binding> binding) {
    final org.libx4j.xsb.runtime.QName name = Classes.getDeclaredAnnotation(binding, org.libx4j.xsb.runtime.QName.class);
    return new QName(name.namespaceURI().intern(), name.localPart().intern(), name.prefix().intern());
  }

  protected static QName stringToQName(final java.lang.String name) {
    if (name == null || name.length() == 0)
      return null;

    int index = name.indexOf(":");
    if (index != -1)
      return new QName(null, name.substring(index + 1).intern(), name.substring(0, index).intern());

    return new QName(name.intern());
  }

  protected static String parsePrefix(final String name) {
    if (name == null)
      return null;

    int index = name.indexOf(":");
    if (index != -1)
      return name.substring(0, index);

    return null;
  }

  protected static String parseLocalName(final String name) {
    if (name == null)
      return null;

    int start = name.indexOf("{");
    if (start != -1) {
      int end = name.indexOf("}", start);
      if (end != -1)
        return name.substring(end + 1);
    }

    start = name.indexOf(":");
    if (start != -1)
      return name.substring(start + 1);

    return name;
  }
}