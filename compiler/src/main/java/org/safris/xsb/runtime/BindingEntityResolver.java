/* Copyright (c) 2008 Seva Safris
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

package org.safris.xsb.compiler.runtime;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.lang.PackageLoader;
import org.safris.commons.lang.Paths;
import org.safris.commons.net.URLs;
import org.safris.commons.xml.NamespaceBinding;
import org.safris.commons.xml.validator.ValidatorError;

import com.sun.org.apache.xerces.internal.impl.xs.XSDDescription;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;

@SuppressWarnings("restriction")
public final class BindingEntityResolver implements XMLEntityResolver {
  public static void registerSchemaLocation(final String namespaceURI, final URL schemaReference) {
    final URL present = schemaReferences.get(namespaceURI);
    if (present != null) {
      if (!present.equals(schemaReference))
        throw new ValidatorError("We should not be resetting {" + namespaceURI + "} from " + present + " to " + schemaReference);

      return;
    }

    schemaReferences.put(namespaceURI, schemaReference);
  }

  public static URL lookupSchemaLocation(final String namespaceURI) {
    if (namespaceURI == null)
      return null;

    final URL schemaReference = schemaReferences.get(namespaceURI);
    if (schemaReference != null)
      return schemaReference;

    // The schemaReference may not have been registered yet
    synchronized (namespaceURI) {
      // When loading the classes, the static block of each binding will call the
      // registerSchemaLocation() function.
      // FIXME: Look this over. Also make a dedicated RuntimeException for this.
      if (!schemaReferences.containsKey(namespaceURI)) {
        try {
          PackageLoader.getSystemPackageLoader().loadPackage(NamespaceBinding.getPackageFromNamespace(namespaceURI));
        }
        catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    return schemaReferences.get(namespaceURI);
  }

  private static final Map<String,URL> schemaReferences = new HashMap<String,URL>();

  @Override
  public XMLInputSource resolveEntity(final XMLResourceIdentifier resourceIdentifier) throws XNIException, IOException {
    if (resourceIdentifier == null)
      return null;

    final String namespaceURI = resourceIdentifier.getNamespace();
    String baseId = resourceIdentifier.getBaseSystemId();
    if (baseId == null)
      baseId = resourceIdentifier.getExpandedSystemId();

    // for some reason, this happens every once in a while
    if (namespaceURI == null && baseId == null)
      return null;

    try {
      final URL schemaReference;
      if (((XSDDescription)resourceIdentifier).getContextType() == XSDDescription.CONTEXT_INCLUDE) {
        final String localName = Paths.getName(resourceIdentifier.getExpandedSystemId());
        schemaReference = new URL(Paths.getParent(baseId) + "/" + localName);
      }
      else {
        schemaReference = lookupSchemaLocation(namespaceURI);
      }

      if (schemaReference == null)
        throw new ValidatorError("The schemaReference for " + resourceIdentifier + " is null!");

      final String expandedSystemId;
      try {
        expandedSystemId = URLs.toExternalForm(schemaReference);
      }
      catch (final MalformedURLException e) {
        final IOException ioException = new IOException("Cannot obtain externalForm of " + schemaReference);
        ioException.initCause(e);
        throw ioException;
      }

      resourceIdentifier.setExpandedSystemId(expandedSystemId);
      resourceIdentifier.setLiteralSystemId(expandedSystemId);

      final XMLInputSource inputSource = new XMLInputSource(resourceIdentifier);
      inputSource.setByteStream(schemaReference.openStream());
      return inputSource;
    }
    catch (final IOException e) {
      throw e;
    }
    catch (final Exception e) {
      throw new ValidatorError(resourceIdentifier.toString(), e);
    }
  }
}