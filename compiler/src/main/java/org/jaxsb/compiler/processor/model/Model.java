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

package org.jaxsb.compiler.processor.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jaxsb.compiler.lang.LexerFailureException;
import org.jaxsb.compiler.lang.NamespaceURI;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class Model implements PipelineEntity {
  protected static final String TO_STRING_DELIMITER = "TO_STRING_DELIMITER";

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final ArrayList<Model> children = new ArrayList<>();
  private Map<NamespaceURI,URL> schemaReferences;

  private Model parent;
  private Model previous;
  private Model next;

  private String id;

  private NamespaceURI targetNamespace;
  private SchemaModel schema;

  protected Model(final Node node, final Model parent) {
    if (node != null) {
      final NamedNodeMap attributes = node.getAttributes();
      for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
        final Node attribute = attributes.item(i);
        if ("id".equals(attribute.getLocalName()))
          id = attribute.getNodeValue();
      }
    }

    if (parent == null)
      return;

    this.parent = parent;
    parent.children.add(this);
  }

  protected final void registerSchemaLocation(final NamespaceURI namespaceURI, final URL schemaReference) {
    if (getParent() != null) {
      if (logger.isDebugEnabled()) { logger.debug("registering schema location \"" + namespaceURI + "\" to \"" + schemaReference + "\""); }
      getParent().registerSchemaLocation(namespaceURI, schemaReference);
      return;
    }

    if (schemaReferences == null)
      schemaReferences = new HashMap<>();

    if (schemaReferences.containsKey(namespaceURI))
      return;

    schemaReferences.put(namespaceURI, schemaReference);
  }

  protected final URL lookupSchemaLocation(final NamespaceURI namespaceURI) {
    if (getParent() != null)
      return getParent().lookupSchemaLocation(namespaceURI);

    return schemaReferences != null ? schemaReferences.get(namespaceURI) : null;
  }

  public final String getId() {
    return id;
  }

  protected final void setPrevious(final Model previous) {
    this.previous = previous;
  }

  public final Model getPrevious() {
    return previous;
  }

  protected final void setNext(final Model next) {
    this.next = next;
  }

  public final Model getNext() {
    return next;
  }

  public final ArrayList<Model> getChildren() {
    return children;
  }

  public final Model getParent() {
    return parent;
  }

  public final SchemaModel getSchema() {
    if (schema != null)
      return schema;

    Model model = this;
    while (!(model instanceof SchemaModel))
      model = model.getParent();

    return schema = (SchemaModel)model;
  }

  public NamespaceURI getTargetNamespace() {
    if (targetNamespace != null)
      return targetNamespace;

    Model handler = this;
    while ((handler = handler.getParent()) != null)
      if (handler instanceof SchemaModel)
        return targetNamespace = handler.getTargetNamespace();

    throw new LexerFailureException("should have found a schema! what's going on?");
  }

  public final QName parseQNameValue(final String nodeValue, Node parent) {
    final int i = nodeValue.indexOf(':');
    final String prefix;
    final String ns;
    if (i != -1) {
      prefix = nodeValue.substring(0, i);
      ns = NamespaceURI.lookupNamespaceURI(parent, prefix);
      if (ns != null) {
        final NamespaceURI namespaceURI = NamespaceURI.getInstance(ns);
        return new QName(namespaceURI.toString(), nodeValue.substring(i + 1), prefix);
      }
    }

    Node xs;
    do {
      if (parent.getAttributes() == null)
        return new QName(getTargetNamespace().toString(), nodeValue);

      xs = parent.getAttributes().getNamedItem(UniqueQName.XMLNS.getPrefix().toString());
      if (xs == null)
        parent = parent.getParentNode();
      else
        break;
    }
    while (parent != null);

    if (xs == null)
      throw new LexerFailureException("Namespace problem");

    return new QName(xs.getNodeValue(), nodeValue);
  }

  public boolean isQualified(final boolean nested) {
    return false;
  }

  @Override
  public String toString() {
    return "<" + getClass().getSimpleName() + " " + TO_STRING_DELIMITER + "/>";
  }
}