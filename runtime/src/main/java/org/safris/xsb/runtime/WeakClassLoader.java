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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLDecoder;
import java.security.SecureClassLoader;

import org.safris.commons.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WeakClassLoader extends SecureClassLoader {
  private static final Logger logger = LoggerFactory.getLogger(WeakClassLoader.class);

  private final java.lang.ClassLoader parent;

  public WeakClassLoader() {
    super();
    this.parent = null;
  }

  public WeakClassLoader(final java.lang.ClassLoader parent) {
    super(parent);
    this.parent = parent;
  }

  @Override
  public final Class<?> loadClass(final String name) throws ClassNotFoundException {
    return loadClass(name, false);
  }

  @Override
  public synchronized final Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
    final WeakReference<Class<?>> ref = new WeakReference<Class<?>>(findClass(name));
    final Class<?> cls = ref.get();
    if (resolve)
      resolveClass(cls);

    return cls;
  }

  @Override
  protected Class<?> findClass(final String name) throws ClassNotFoundException {
    if (Binding.class.getName().equals(name))
      return parent != null ? parent.loadClass(name) : WeakClassLoader.getSystemClassLoader().loadClass(name);

    String fileName = name;
    fileName = fileName.replace('.', '/');
    if (!fileName.startsWith("/"))
      fileName = '/' + fileName;

    fileName += ".class";
    URL url = WeakClassLoader.class.getResource("/");
    String decodedUrl = null;
    try {
      decodedUrl = URLDecoder.decode(url.getFile(), "UTF-8");
    }
    catch (final UnsupportedEncodingException e) {
      logger.error("ClassLoader: findClass(" + name + ")\n" + e.getMessage());
    }

    Class<?> bindingClass = null;
    try {
      byte[] bytes = Files.getBytes(new File(decodedUrl + fileName));
      bindingClass = defineClass(name, bytes, 0, bytes.length);
    }
    catch (final FileNotFoundException e) {
      return parent != null ? parent.loadClass(name) : WeakClassLoader.getSystemClassLoader().loadClass(name);
    }
    catch (final IOException e) {
      logger.error("ClassLoader: findClass(" + name + ")\n" + e.getMessage());
    }

    return bindingClass;
  }
}