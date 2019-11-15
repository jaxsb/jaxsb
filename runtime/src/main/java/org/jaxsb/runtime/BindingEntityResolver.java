/* Copyright (c) 2008 JAX-SB
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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.libj.lang.PackageLoader;
import org.libj.lang.PackageNotFoundException;
import org.openjax.xml.sax.LSInputImpl;
import org.jaxsb.compiler.lang.NamespaceBinding;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public final class BindingEntityResolver implements LSResourceResolver {
  public static void registerSchemaLocation(final String namespaceURI, final URL schemaReference) {
    final URL present = schemaReferences.get(namespaceURI);
    if (present == null)
      schemaReferences.put(namespaceURI, schemaReference);
    else if (!present.equals(schemaReference))
      throw new IllegalStateException("Attempted to reset {" + namespaceURI + "} from " + present + " to " + schemaReference);
  }

  public static URL lookupSchemaLocation(final String namespaceURI) {
    if (namespaceURI == null)
      return null;

    final URL schemaReference = schemaReferences.get(namespaceURI);
    if (schemaReference != null)
      return schemaReference;

    // The schemaReference may not have been registered yet
    synchronized (namespaceURI.intern()) {
      // When loading the classes, the static block of each binding will call the
      // registerSchemaLocation() function.
      // FIXME: Look this over. Also make a dedicated RuntimeException for this.
      if (!schemaReferences.containsKey(namespaceURI)) {
        try {
          PackageLoader.getContextPackageLoader().loadPackage(NamespaceBinding.parseNamespace(namespaceURI).getPackageName());
        }
        catch (final IOException | PackageNotFoundException e) {
          throw new IllegalStateException(e);
        }
      }
    }

    return schemaReferences.get(namespaceURI);
  }

  protected static final Map<String,URL> schemaReferences = new HashMap<>();

  @Override
  public LSInput resolveResource(final String type, final String namespaceURI, final String publicId, final String systemId, final String baseURI) {
    // for some reason, this happens every once in a while
    if (namespaceURI == null && systemId == null)
      return null;

    final URL url = lookupSchemaLocation(namespaceURI);
    if (url == null)
      throw new IllegalStateException("The schemaReference for namespaceURI: " + namespaceURI + ", publicId: " + publicId + ", systemId: " + systemId + ", baseURI: " + baseURI + " is null!");

    try {
      final LSInput input = new LSInputImpl(publicId, url.toString(), baseURI);
      input.setByteStream(url.openStream());
      return input;
    }
    catch (final IOException e) {
      throw new IllegalArgumentException("Cannot obtain externalForm of " + url, e);
    }
  }
}