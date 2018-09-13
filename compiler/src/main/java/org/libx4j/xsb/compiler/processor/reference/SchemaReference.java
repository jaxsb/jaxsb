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

package org.libx4j.xsb.compiler.processor.reference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.libx4j.xsb.helper.pipeline.PipelineEntity;
import org.libx4j.xsb.compiler.lang.LexerFailureException;
import org.libx4j.xsb.compiler.lang.NamespaceURI;
import org.libx4j.xsb.compiler.lang.Prefix;
import org.libx4j.xsb.compiler.lang.UniqueQName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class SchemaReference implements PipelineEntity {
  private static final Logger logger = LoggerFactory.getLogger(SchemaReference.class);
  private static final Map<NamespaceURI,Prefix> namespaceURIToPrefix = new HashMap<>();
  private static final Map<Prefix,NamespaceURI> prefixToNamespaceURI = new HashMap<>();

  // to dereference the schemaReference to a targetNamespace
  private final AtomicBoolean isConnected = new AtomicBoolean();
  private final AtomicBoolean isResolved = new AtomicBoolean();
  private final URL location;
  private NamespaceURI namespaceURI;
  private Prefix prefix;
  private final boolean isInclude;
  private long lastModified = Long.MIN_VALUE;
  private InputStream inputStream = null;

  public SchemaReference(final URL location, final boolean isInclude) {
    this.location = location;
    if (location == null)
      throw new IllegalArgumentException("location == null");

    this.isInclude = isInclude;
    if (logger.isDebugEnabled())
      logger.debug("new SchemaReference(\"" + this.location.toExternalForm() + "\", " + isInclude + ")");
  }

  public SchemaReference(final URL location, final NamespaceURI namespaceURI, final Prefix prefix, final boolean isInclude) {
    this.location = location;
    this.namespaceURI = namespaceURI;
    this.prefix = prefix;
    this.isInclude = isInclude;
    logger.debug("new SchemaReference(\"" + this.location.toExternalForm() + "\", \"" + namespaceURI + "\", \"" + prefix + "\")");
  }

  public SchemaReference(final URL location, final NamespaceURI namespaceURI, final boolean isInclude) {
    this.location = location;
    this.namespaceURI = namespaceURI;
    this.isInclude = isInclude;
    logger.debug("new SchemaReference(\"" + this.location.toExternalForm() + "\", \"" + namespaceURI + "\")");
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
    if (isResolved.get())
      return;

    if (namespaceURI != null) {
      if (prefix == null)
        prefix = namespaceURIToPrefix.get(namespaceURI);

      if (prefix != null) {
        isResolved.set(true);
        return;
      }
    }
    else if (prefix != null) {
      if (namespaceURI == null)
        namespaceURI = prefixToNamespaceURI.get(prefix);

      if (namespaceURI != null) {
        isResolved.set(true);
        return;
      }
    }

    synchronized (location) {
      if (isResolved.get())
        return;

      try {
        checkOpenConnection();
      }
      catch (final IOException e) {
        throw new LexerFailureException(e);
      }

      try {
        final SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        saxParser.parse(new InputSource(inputStream), new SchemaNamespaceHandler());
      }
      catch (final FileNotFoundException e) {
        throw new LexerFailureException(e.getMessage());
      }
      catch (final IOException | ParserConfigurationException e) {
        throw new LexerFailureException(e);
      }
      catch (final ReferenceSAXException e) {
        if (namespaceURI == null)
          namespaceURI = NamespaceURI.getInstance(e.getNamespaceURI());
        else if (!namespaceURI.toString().equals(e.getNamespaceURI()))
          throw new LexerFailureException("This should never happen: " + namespaceURI + " != " + e.getNamespaceURI());

        this.prefix = Prefix.getInstance(e.getPrefix());
        logger.debug("linking \"" + namespaceURI + "\" to \"" + this.prefix + "\"");
        UniqueQName.linkPrefixNamespace(namespaceURI, this.prefix);
        isResolved.set(true);
      }
      catch (final SAXException e) {
        throw new LexerFailureException(location.toString(), e);
      }
    }
  }

  private void checkOpenConnection() throws IOException {
    if (isConnected.get())
      return;

    synchronized (location.toString()) {
      if (isConnected.get())
        return;

      final URLConnection connection = location.openConnection();
      try {
        this.inputStream = connection.getInputStream();
        logger.debug("opened connection to: " + location.toExternalForm());
      }
      catch (final FileNotFoundException e) {
        throw new LexerFailureException("File not found: " + location.toExternalForm());
      }

      this.lastModified = connection.getLastModified();
      isConnected.set(true);
    }
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