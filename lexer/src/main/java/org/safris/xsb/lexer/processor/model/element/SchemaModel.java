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

package org.safris.xsb.lexer.processor.model.element;

import java.io.File;
import java.net.URL;

import org.safris.commons.io.Files;
import org.safris.commons.net.URLs;
import org.safris.commons.xml.NamespaceURI;
import org.safris.maven.common.Log;
import org.safris.xsb.lexer.lang.UniqueQName;
import org.safris.xsb.lexer.processor.model.Model;
import org.safris.xsb.lexer.schema.attribute.BlockDefault;
import org.safris.xsb.lexer.schema.attribute.FinalDefault;
import org.safris.xsb.lexer.schema.attribute.Form;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class SchemaModel extends Model {
  private Form attributeFormDefault = Form.UNQUALIFIED;
  private BlockDefault blockDefault = null;
  private Form elementFormDefault = Form.UNQUALIFIED;
  private FinalDefault finalDefault = null;
  private String lang = null;
  private NamespaceURI targetNamespace = null;
  private String version = null;
  private URL url = null;

  protected SchemaModel(final Node node, final Model parent) {
    // NOTE: A SchemaModel does not have a parent.
    super(node, null);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      if ("attributeFormDefault".equals(attribute.getLocalName()))
        attributeFormDefault = Form.parseForm(attribute.getNodeValue());
      else if ("blockDefault".equals(attribute.getLocalName()))
        blockDefault = BlockDefault.parseBlockDefault(attribute.getNodeValue());
      else if ("elementFormDefault".equals(attribute.getLocalName()))
        elementFormDefault = Form.parseForm(attribute.getNodeValue());
      else if ("finalDefault".equals(attribute.getLocalName()))
        finalDefault = FinalDefault.parseFinalDefault(attribute.getNodeValue());
      else if ("lang".equals(attribute.getLocalName()))
        lang = attribute.getNodeValue();
      else if ("targetNamespace".equals(attribute.getLocalName()))
        setTargetNamespace(NamespaceURI.getInstance(attribute.getNodeValue()));
      else if ("version".equals(attribute.getLocalName()))
        version = attribute.getNodeValue();
    }
  }

  public final void setURL(final URL url) {
    this.url = url;
    final String display = URLs.isLocal(url) ? Files.relativePath(Files.getCwd().getAbsoluteFile(), new File(url.getFile()).getAbsoluteFile()) : url.toExternalForm();
    Log.info("Scanning {" + getTargetNamespace() + "} from " + display);
  }

  public final URL getURL() {
    return url;
  }

  public final Form getAttributeFormDefault() {
    return attributeFormDefault;
  }

  public final BlockDefault getBlockDefault() {
    return blockDefault;
  }

  public final Form getElementFormDefault() {
    return elementFormDefault;
  }

  public final FinalDefault getFinalDefault() {
    return finalDefault;
  }

  public final String getLang() {
    return lang;
  }

  public final void setTargetNamespace(final NamespaceURI targetNamespace) {
    if (targetNamespace == null)
      throw new IllegalArgumentException("targetNamespace == null");

    this.targetNamespace = targetNamespace;
  }

  @Override
  public final NamespaceURI getTargetNamespace() {
    return targetNamespace;
  }

  public final String getVersion() {
    return version;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof SchemaModel))
      return false;

    final SchemaModel that = (SchemaModel)obj;
    return (targetNamespace != null ? targetNamespace.equals(that.targetNamespace) : that.targetNamespace == null) && (url != null ? url.equals(that.url) : that.url == null);
  }

  @Override
  public int hashCode() {
    return (targetNamespace != null ? targetNamespace.hashCode() : -7) * (url != null ? url.hashCode() : -9);
  }

  @Override
  public String toString() {
    return UniqueQName.XS.getNamespaceURI() + " " + targetNamespace;
  }
}