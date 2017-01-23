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

package org.safris.xsb.runtime;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.safris.commons.lang.Classes;
import org.safris.commons.lang.Resource;
import org.safris.commons.lang.Resources;
import org.safris.commons.net.URLs;
import org.safris.commons.xml.NamespaceBinding;
import org.safris.maven.common.Log;

public abstract class AbstractBinding implements Cloneable {
  protected static final QName XSI_TYPE = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");
  protected static final QName XSI_NIL = new QName("http://www.w3.org/2001/XMLSchema-instance", "nil", "xsi");
  protected static final QName XMLNS = new QName("http://www.w3.org/2000/xmlns/", "xmlns");
  protected static final QName XML = new QName("http://www.w3.org/XML/1998/namespace", "xml");

  private static final Map<QName,Class<? extends Binding>> elementBindings = new HashMap<QName,Class<? extends Binding>>();
  private static final Map<QName,Class<? extends Binding>> typeBindings = new HashMap<QName,Class<? extends Binding>>();

  protected static void _$$registerSchemaLocation(final String namespaceURI, final Class<?> className, final String schemaReference) {
    final String simpleName = className.getName().replace('.', '/') + ".class";
    final Resource resource = Resources.getResource(simpleName);
    if (resource == null)
      throw new BindingError("Cannot register: systemId=\"" + namespaceURI + "\"\n\tclassName=\"" + className.getName() + "\"\n\tschemaReference=\"" + schemaReference + "\"");

    final URL parent = URLs.getParent(resource.getURL());
    try {
      BindingEntityResolver.registerSchemaLocation(namespaceURI, new URL(parent + "/" + schemaReference));
    }
    catch (final MalformedURLException e) {
      Log.error("Cannot register: systemId=\"" + namespaceURI + "\"\n\tclassName=\"" + className.getName() + "\"\n\tschemaReference=\"" + schemaReference + "\"");
    }
  }

  protected static void _$$registerElement(final QName name, final Class<? extends Binding> cls) {
    elementBindings.put(name, cls);
  }

  private static void loadPackage(final String namespaceURI) {
    // FIXME: Look this over. Also make a dedicated RuntimeException for this.
    try {
      final Class<?> schemaClass = Class.forName(NamespaceBinding.getPackageFromNamespace(namespaceURI) + ".xe");
      final Method method = schemaClass.getDeclaredMethod("_$$register");
      method.setAccessible(true);
      method.invoke(null);
    }
    catch (final ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  protected static Class<? extends Binding> lookupElement(final QName name) {
    final Class<? extends Binding> clazz = elementBindings.get(name);
    if (clazz != null)
      return clazz;

    loadPackage(name.getNamespaceURI());
    return elementBindings.get(name);
  }

  protected static void _$$registerType(final QName name, final Class<? extends Binding> cls) {
    typeBindings.put(name, cls);
  }

  protected static Class<? extends Binding> lookupType(final QName name) {
    final Class<? extends Binding> clazz = typeBindings.get(name);
    if (clazz != null)
      return clazz;

    loadPackage(name.getNamespaceURI());
    return typeBindings.get(name);
  }

  protected static Object _$$getTEXT(final Binding binding) {
    return binding.text();
  }

  protected static QName getClassQName(final Class<? extends Binding> binding) {
    final org.safris.xsb.runtime.QName name = Classes.getDeclaredAnnotation(binding, org.safris.xsb.runtime.QName.class);
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