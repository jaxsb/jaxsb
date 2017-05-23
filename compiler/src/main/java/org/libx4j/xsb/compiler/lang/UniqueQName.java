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

package org.libx4j.xsb.compiler.lang;

import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.safris.commons.xml.NamespaceURI;
import org.safris.commons.xml.Prefix;

public final class UniqueQName {
  private static final Map<NamespaceURI,Prefix> namespaceURIToPrefix = new HashMap<NamespaceURI,Prefix>();
  private static final Map<QName,UniqueQName> instances = new HashMap<QName,UniqueQName>();

  // subjectively chosen
  public static final UniqueQName XS = UniqueQName.getInstance(NamespaceURI.XS);
  public static final UniqueQName XSI = UniqueQName.getInstance(NamespaceURI.XSI);

  // staticly defined
  public static final UniqueQName XML = UniqueQName.getInstance(NamespaceURI.XML);
  public static final UniqueQName XMLNS = UniqueQName.getInstance(NamespaceURI.XMLNS);

  public static UniqueQName getInstance(final QName name) {
    final UniqueQName bindingQName = new UniqueQName(name);
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  public static UniqueQName getInstance(final String namespaceURI, final String localPart) {
    final QName name = new QName(namespaceURI != null ? namespaceURI.intern() : null, localPart.intern());
    final UniqueQName bindingQName = new UniqueQName(name);
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  public static UniqueQName getInstance(final NamespaceURI namespaceURI, final String localPart) {
    final QName name = new QName(namespaceURI.toString().intern(), localPart.intern());
    final UniqueQName bindingQName = new UniqueQName(new QName(namespaceURI.toString().intern(), localPart.intern()));
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  public static UniqueQName getInstance(final String namespaceURI, final String localPart, final String prefix) {
    final QName name = new QName(namespaceURI != null ? namespaceURI.intern() : null, localPart.intern());
    final UniqueQName bindingQName = new UniqueQName(new QName(namespaceURI != null ? namespaceURI.intern() : null, localPart.intern(), prefix.intern()));
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  public static UniqueQName getInstance(final NamespaceURI namespaceURI, final String localPart, final String prefix) {
    final QName name = new QName(namespaceURI.toString().intern(), localPart.intern());
    final UniqueQName bindingQName = new UniqueQName(new QName(namespaceURI.toString().intern(), localPart.intern(), prefix.intern()));
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  private final String localPart;
  private final NamespaceURI namespaceURI;
  private Prefix prefix = null;

  private UniqueQName(final QName name) {
    if (name.getNamespaceURI() != null) {
      this.namespaceURI = NamespaceURI.getInstance(name.getNamespaceURI());
      if (XMLConstants.XML_NS_URI.equals(name.getNamespaceURI()))
        this.prefix = Prefix.getInstance(XMLConstants.XML_NS_PREFIX);
      else if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(name.getNamespaceURI()))
        this.prefix = Prefix.getInstance(XMLConstants.XMLNS_ATTRIBUTE);
      else if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(name.getNamespaceURI()))
        this.prefix = Prefix.getInstance(NamespaceURI.W3C_XML_SCHEMA_PREFIX);
      else if (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(name.getNamespaceURI()))
        this.prefix = Prefix.getInstance(NamespaceURI.W3C_XML_SCHEMA_INSTANCE_PREFIX);
      else if (name.getPrefix() != null)
        this.prefix = Prefix.getInstance(name.getPrefix());
      else
        this.prefix = null;
    }
    else {
      if (name.getPrefix() != null) {
        this.prefix = Prefix.getInstance(name.getPrefix());
        if (XMLConstants.XML_NS_PREFIX.equals(name.getPrefix()))
          this.namespaceURI = NamespaceURI.getInstance(XMLConstants.XML_NS_URI);
        else if (XMLConstants.XMLNS_ATTRIBUTE.equals(name.getPrefix()))
          this.namespaceURI = NamespaceURI.getInstance(XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
        else if (NamespaceURI.W3C_XML_SCHEMA_PREFIX.equals(name.getPrefix()))
          this.namespaceURI = NamespaceURI.getInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        else if (NamespaceURI.W3C_XML_SCHEMA_INSTANCE_PREFIX.equals(name.getPrefix()))
          this.namespaceURI = NamespaceURI.getInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        else
          throw new IllegalArgumentException("Unknown prefix used: \"" + name.getPrefix() + "\"");
      }
      else
        throw new IllegalArgumentException("Both namespaceURI and prefix are null.");
    }

    this.localPart = name.getLocalPart();
  }

  public static void linkPrefixNamespace(final NamespaceURI namespaceURI, final Prefix prefix) {
    if (namespaceURI == null || XMLConstants.NULL_NS_URI.equals(namespaceURI.toString()) || prefix == null || XMLConstants.DEFAULT_NS_PREFIX.equals(prefix.toString()))
      return;

    final Prefix exists = namespaceURIToPrefix.get(namespaceURI);
    if (exists != null && !exists.equals(prefix))
      throw new IllegalStateException("Prefix for namespace {" + namespaceURI + "} is being redefined from \"" + exists + "\" to \"" + prefix + "\"");

    namespaceURIToPrefix.put(namespaceURI, prefix);
  }

  public NamespaceURI getNamespaceURI() {
    return namespaceURI;
  }

  public String getLocalPart() {
    return localPart;
  }

  public Prefix getPrefix() {
    if (prefix != null && !XMLConstants.DEFAULT_NS_PREFIX.equals(prefix.toString()))
      return prefix;

    final Prefix exists = namespaceURIToPrefix.get(namespaceURI);
    if (exists != null && (prefix == null || XMLConstants.DEFAULT_NS_PREFIX.equals(prefix.toString())))
      return prefix = exists;

    return prefix;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof UniqueQName))
      return false;

    final UniqueQName that = (UniqueQName)obj;
    return (namespaceURI != null ? namespaceURI.equals(that.namespaceURI) : that.namespaceURI == null) && localPart.equals(that.localPart);
  }

  @Override
  public int hashCode() {
    return (namespaceURI != null ? namespaceURI.hashCode() : XMLConstants.NULL_NS_URI.hashCode()) * localPart.hashCode();
  }

  @Override
  public String toString() {
    if (namespaceURI == null || XMLConstants.NULL_NS_URI.equals(namespaceURI.toString()))
      return localPart;

    return "{" + namespaceURI + "}" + localPart;
  }
}