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

package org.jaxsb.compiler.processor.model.element;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

import org.jaxsb.compiler.lang.NamespaceURI;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.schema.attribute.BlockDefault;
import org.jaxsb.compiler.schema.attribute.FinalDefault;
import org.jaxsb.compiler.schema.attribute.Form;
import org.libj.net.URLs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class SchemaModel extends Model {
  private static final Path CWD = new File("").getAbsoluteFile().toPath();
  private Form attributeFormDefault = Form.UNQUALIFIED;
  private BlockDefault blockDefault;
  private Form elementFormDefault = Form.UNQUALIFIED;
  private FinalDefault finalDefault;
  private String lang;
  private NamespaceURI targetNamespace;
  private String version;
  private URL url;

  protected SchemaModel(final Node node, @SuppressWarnings("unused") final Model parent) {
    // NOTE: A SchemaModel does not have a parent.
    super(node, null);
    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
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

  public void setURL(final URL url) {
    this.url = url;
    final String display = (URLs.isLocal(url) ? CWD.relativize(new File(url.getFile()).getAbsoluteFile().toPath()) : url).toString();
    if (logger.isInfoEnabled()) logger.info("Scanning {" + getTargetNamespace() + "} from " + display);
  }

  public URL getURL() {
    return url;
  }

  public Form getAttributeFormDefault() {
    return attributeFormDefault;
  }

  public BlockDefault getBlockDefault() {
    return blockDefault;
  }

  public Form getElementFormDefault() {
    return elementFormDefault;
  }

  public FinalDefault getFinalDefault() {
    return finalDefault;
  }

  public String getLang() {
    return lang;
  }

  public void setTargetNamespace(final NamespaceURI targetNamespace) {
    this.targetNamespace = targetNamespace;
    if (targetNamespace == null)
      throw new IllegalArgumentException("targetNamespace == null");
  }

  @Override
  public NamespaceURI getTargetNamespace() {
    return targetNamespace;
  }

  public String getVersion() {
    return version;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof SchemaModel))
      return false;

    final SchemaModel that = (SchemaModel)obj;
    return Objects.equals(targetNamespace, that.targetNamespace) && Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + Objects.hashCode(targetNamespace);
    hashCode = 31 * hashCode + Objects.hashCode(url);
    return hashCode;
  }

  @Override
  public String toString() {
    return UniqueQName.XS.getNamespaceURI() + " " + targetNamespace;
  }
}