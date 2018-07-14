/* Copyright (c) 2008 lib4j
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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.lib4j.lang.PackageLoader;
import org.lib4j.lang.PackageNotFoundException;
import org.lib4j.net.CachedURL;
import org.lib4j.net.URLs;
import org.lib4j.xml.sax.LSInputImpl;
import org.libx4j.xsb.compiler.lang.NamespaceBinding;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public final class BindingEntityResolver implements LSResourceResolver {
  public static void registerSchemaLocation(final String namespaceURI, final URL schemaReference) {
    final CachedURL present = schemaReferences.get(namespaceURI);
    if (present != null) {
      if (!present.toURL().equals(schemaReference))
        throw new ValidatorError("We should not be resetting {" + namespaceURI + "} from " + present + " to " + schemaReference);

      return;
    }

    schemaReferences.put(namespaceURI, new CachedURL(schemaReference));
  }

  public static CachedURL lookupSchemaLocation(final String namespaceURI) {
    if (namespaceURI == null)
      return null;

    final CachedURL schemaReference = schemaReferences.get(namespaceURI);
    if (schemaReference != null)
      return schemaReference;

    // The schemaReference may not have been registered yet
    synchronized (namespaceURI) {
      // When loading the classes, the static block of each binding will call the
      // registerSchemaLocation() function.
      // FIXME: Look this over. Also make a dedicated RuntimeException for this.
      if (!schemaReferences.containsKey(namespaceURI)) {
        try {
          PackageLoader.getContextPackageLoader().loadPackage(NamespaceBinding.parseNamespace(namespaceURI).getPackageName());
        }
        catch (final PackageNotFoundException e) {
          throw new UnsupportedOperationException(e);
        }
      }
    }

    return schemaReferences.get(namespaceURI);
  }

  protected static final Map<String,CachedURL> schemaReferences = new HashMap<>();

  @Override
  public LSInput resolveResource(final String type, final String namespaceURI, final String publicId, final String systemId, final String baseURI) {
    if (systemId == null) {
//      systemId = resourceIdentifier.getExpandedSystemId();
    }

    // for some reason, this happens every once in a while
    if (namespaceURI == null && systemId == null)
      return null;

    final CachedURL url;
//    if (((XSDDescription)resourceIdentifier).getContextType() == XSDDescription.CONTEXT_INCLUDE) {
//      final String localName = Paths.getName(resourceIdentifier.getExpandedSystemId());
//      schemaReference = new URL(Paths.getCanonicalParent(systemId) + "/" + localName);
//    }
//    else {
      url = lookupSchemaLocation(namespaceURI);
//    }

    if (url == null)
      throw new ValidatorError("The schemaReference for namespaceURI: " + namespaceURI + ", publicId: " + publicId + ", systemId: " + systemId + ", baseURI: " + baseURI + " is null!");

    try {
      final LSInput input = new LSInputImpl(URLs.toExternalForm(url), publicId, baseURI);
      input.setByteStream(url.openStream());
      return input;
    }
    catch (final IOException e) {
      throw new RuntimeException("Cannot obtain externalForm of " + url, e);
    }
  }
}