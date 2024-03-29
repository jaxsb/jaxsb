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

package org.jaxsb.compiler.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

public final class UniqueQName {
  private static final Map<NamespaceURI,Prefix> namespaceURIToPrefix = new HashMap<>();
  private static final Map<QName,UniqueQName> instances = new HashMap<>();

  // subjectively chosen
  public static final UniqueQName XS = linkPrefixNamespace(UniqueQName.getInstance(NamespaceURI.XS));
  public static final UniqueQName XSI = linkPrefixNamespace(UniqueQName.getInstance(NamespaceURI.XSI));

  // statically defined
  public static final UniqueQName XML = linkPrefixNamespace(UniqueQName.getInstance(NamespaceURI.XML));
  public static final UniqueQName XMLNS = linkPrefixNamespace(UniqueQName.getInstance(NamespaceURI.XMLNS));

  public static Prefix getPrefix(final NamespaceURI namespaceURI) {
    final Prefix prefix = namespaceURIToPrefix.get(namespaceURI);
    return prefix == null ? Prefix.DEFAULT : prefix;

  }

  public static UniqueQName getInstance(final QName name) {
    final UniqueQName bindingQName = new UniqueQName(name);
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  public static UniqueQName getInstance(final String namespaceURI, final String localPart) {
    final QName name = new QName(namespaceURI, localPart);
    final UniqueQName bindingQName = new UniqueQName(name);
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  public static UniqueQName getInstance(final NamespaceURI namespaceURI, final String localPart) {
    final QName name = new QName(namespaceURI.toString(), localPart);
    final UniqueQName bindingQName = new UniqueQName(new QName(namespaceURI.toString(), localPart));
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  public static UniqueQName getInstance(final String namespaceURI, final String localPart, final String prefix) {
    final QName name = new QName(namespaceURI, localPart);
    final UniqueQName bindingQName = new UniqueQName(new QName(namespaceURI, localPart, prefix));
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  public static UniqueQName getInstance(final NamespaceURI namespaceURI, final String localPart, final String prefix) {
    final QName name = new QName(namespaceURI.toString(), localPart);
    final UniqueQName bindingQName = new UniqueQName(new QName(namespaceURI.toString(), localPart, prefix));
    UniqueQName instance = instances.get(name);
    if (instance == null)
      instances.put(name, instance = bindingQName);

    return instance;
  }

  private final String localPart;
  private final NamespaceURI namespaceURI;
  private Prefix prefix;

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
      else
        this.prefix = Prefix.getInstance(name.getPrefix());
    }
    else {
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

    this.localPart = name.getLocalPart();
  }

  private static UniqueQName linkPrefixNamespace(final UniqueQName qName) {
    linkPrefixNamespace(qName.getNamespaceURI(), qName.getPrefix());
    return qName;
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
    return (Objects.equals(namespaceURI, that.namespaceURI)) && localPart.equals(that.localPart);
  }

  @Override
  public int hashCode() {
    return 31 * localPart.hashCode() + (namespaceURI != null ? namespaceURI.hashCode() : XMLConstants.NULL_NS_URI.hashCode());
  }

  @Override
  public String toString() {
    if (namespaceURI == null || XMLConstants.NULL_NS_URI.equals(namespaceURI.toString()))
      return localPart;

    return "{" + namespaceURI + "}" + localPart;
  }
}