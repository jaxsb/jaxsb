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

package org.safris.xsb.generator.lexer.processor.reference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.net.URLs;
import org.safris.commons.pipeline.PipelineEntity;
import org.safris.commons.xml.NamespaceURI;
import org.safris.commons.xml.Prefix;
import org.safris.commons.xml.sax.SAXFeature;
import org.safris.commons.xml.sax.SAXParser;
import org.safris.commons.xml.sax.SAXParsers;
import org.safris.maven.common.Log;
import org.safris.xsb.generator.lexer.lang.LexerError;
import org.safris.xsb.generator.lexer.lang.UniqueQName;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class SchemaReference implements PipelineEntity {
  private static final Map<NamespaceURI,Prefix> namespaceURIToPrefix = new HashMap<NamespaceURI,Prefix>();
  private static final Map<Prefix,NamespaceURI> prefixToNamespaceURI = new HashMap<Prefix,NamespaceURI>();

  // to dereference the schemaReference to a targetNamespace
  private volatile boolean isConnected = false;
  private volatile boolean isResolved = false;
  private URL location;
  private NamespaceURI namespaceURI;
  private Prefix prefix;
  private final Boolean isInclude;
  private long lastModified = Long.MIN_VALUE;
  private InputStream inputStream = null;

  public SchemaReference(final String location, final boolean isInclude) {
    this(null, location, isInclude);
  }

  public SchemaReference(final String basedir, final String location, final boolean isInclude) {
    if (location == null)
      throw new IllegalArgumentException("location cannot be null");

    try {
      if (basedir != null)
        this.location = URLs.makeUrlFromPath(basedir, location);
      else
        this.location = new URL(location);
    }
    catch (final MalformedURLException e) {
      try {
        this.location = basedir != null ? new File(basedir, location).toURI().toURL() : new File(location).toURI().toURL();
      }
      catch (final MalformedURLException ex) {
        throw new IllegalArgumentException("Unknown URL format: " + location);
      }
    }

    this.isInclude = isInclude;
    Log.debug("new SchemaReference(\"" + this.location.toExternalForm() + "\")");
  }

  public SchemaReference(final URL location) {
    this.location = location;
    this.isInclude = null;
    Log.debug("new SchemaReference(\"" + this.location.toExternalForm() + "\")");
  }

  public SchemaReference(final URL location, final NamespaceURI namespaceURI, final Prefix prefix, final boolean isInclude) {
    this.location = location;
    this.namespaceURI = namespaceURI;
    this.prefix = prefix;
    this.isInclude = isInclude;
    Log.debug("new SchemaReference(\"" + this.location.toExternalForm() + "\", \"" + namespaceURI + "\", \"" + prefix + "\")");
  }

  public SchemaReference(final URL location, final NamespaceURI namespaceURI, final boolean isInclude) {
    this.location = location;
    this.namespaceURI = namespaceURI;
    this.isInclude = isInclude;
    Log.debug("new SchemaReference(\"" + this.location.toExternalForm() + "\", \"" + namespaceURI + "\")");
  }

  public NamespaceURI getNamespaceURI() {
    resolveUnknowns();
    return namespaceURI;
  }

  public Prefix getPrefix() {
    resolveUnknowns();
    return prefix;
  }

  public boolean isInclude() {
    return isInclude;
  }

  private void resolveUnknowns() {
    if (isResolved)
      return;

    if (namespaceURI != null) {
      if (prefix == null)
        prefix = namespaceURIToPrefix.get(namespaceURI);

      if (prefix != null) {
        isResolved = true;
        return;
      }
    }
    else if (prefix != null) {
      if (namespaceURI == null)
        namespaceURI = prefixToNamespaceURI.get(prefix);

      if (namespaceURI != null) {
        isResolved = true;
        return;
      }
    }

    synchronized (location) {
      if (isResolved)
        return;

      try {
        checkOpenConnection();
      }
      catch (final IOException e) {
        throw new LexerError(e);
      }

      try {
        final SAXParser saxParser = SAXParsers.createParser();
        saxParser.setFeature(SAXFeature.NAMESPACE_PREFIXES, true);
        saxParser.setContentHandler(new SchemaNamespaceHandler(getURL()));
        saxParser.parse(new InputSource(inputStream));
      }
      catch (final FileNotFoundException e) {
        throw new LexerError(e.getMessage());
      }
      catch (final IOException e) {
        throw new LexerError(e);
      }
      catch (final SAXException e) {
        if (e.getMessage() == null)
          throw new LexerError(location.toString(), e);

        final String code = location.hashCode() + "\"";
        if (e.getMessage().indexOf(code) != 0)
          throw new LexerError(location.toString(), e);

        final int delimiter = e.getMessage().lastIndexOf("\"");
        if (delimiter == -1)
          throw new LexerError(location.toString(), e);

        final String namespace = e.getMessage().substring(code.length(), delimiter);
        final String prefix = e.getMessage().substring(delimiter + 1);
        // This links the namespaceURI to the prefix
        if (namespaceURI == null)
          namespaceURI = NamespaceURI.getInstance(namespace);
        else if (!namespaceURI.toString().equals(namespace))
          throw new LexerError("This should never happen: " + namespaceURI + " != " + namespace);

        this.prefix = Prefix.getInstance(prefix);
        Log.debug("linking \"" + namespaceURI + "\" to \"" + this.prefix + "\"");
        UniqueQName.linkPrefixNamespace(namespaceURI, this.prefix);
        isResolved = true;
      }
    }
  }

  private void checkOpenConnection() throws IOException {
    if (isConnected)
      return;

    synchronized (location.toString()) {
      if (isConnected)
        return;

      final URLConnection connection = location.openConnection();
      int tryCount = 0;
      while (tryCount++ < 10) {
        try {
          this.inputStream = connection.getInputStream();
          Log.debug("opened connection to: " + location.toExternalForm());
        }
        catch (final FileNotFoundException e) {
          throw new LexerError("File not found: " + location.toExternalForm());
        }
        catch (final IOException e) {
          if ("Connection refused".equals(e.getMessage()) && tryCount == 10)
            throw new LexerError("Connection refused: " + location);

          throw e;
        }
      }

      this.lastModified = connection.getLastModified();
      isConnected = true;
    }

    return;
  }

  public long getLastModified() throws IOException {
    checkOpenConnection();
    return lastModified;
  }

  public URL getURL() {
    return location;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof SchemaReference))
      return false;

    final SchemaReference that = (SchemaReference)obj;
    return location.equals(that.location) && namespaceURI.equals(that.namespaceURI);
  }

  @Override
  public int hashCode() {
    return location.hashCode() ^ (namespaceURI != null ? namespaceURI.hashCode() : 89432);
  }
}