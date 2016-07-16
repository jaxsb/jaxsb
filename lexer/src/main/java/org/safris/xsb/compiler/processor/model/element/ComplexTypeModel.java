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

package org.safris.xsb.compiler.processor.model.element;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.safris.xsb.compiler.lang.UniqueQName;
import org.safris.xsb.compiler.processor.Referenceable;
import org.safris.xsb.compiler.processor.Undefineable;
import org.safris.xsb.compiler.processor.model.AttributableModel;
import org.safris.xsb.compiler.processor.model.ElementableModel;
import org.safris.xsb.compiler.processor.model.MixableModel;
import org.safris.xsb.compiler.processor.model.Model;
import org.safris.xsb.compiler.processor.model.MultiplicableModel;
import org.safris.xsb.compiler.schema.attribute.Block;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ComplexTypeModel<T extends SimpleTypeModel<?>> extends SimpleTypeModel<T> implements AttributableModel, ElementableModel, MixableModel {
  private final LinkedHashSet<AttributeModel> attributes = new LinkedHashSet<AttributeModel>();
  private final LinkedHashSet<MultiplicableModel> multiplicableModels = new LinkedHashSet<MultiplicableModel>();
  private Boolean _abstract = false;
  private Block block = null;
  private Boolean mixed = null;
  private boolean extension = false;

  protected ComplexTypeModel(final Node node, final Model parent) {
    super(node, parent);
    if (node == null)
      return;

    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      if ("abstract".equals(attribute.getLocalName()))
        _abstract = Boolean.parseBoolean(attribute.getNodeValue());
      else if ("block".equals(attribute.getLocalName()))
        block = Block.parseBlock(attribute.getNodeValue());
      else if ("mixed".equals(attribute.getLocalName()))
        mixed = Boolean.parseBoolean(attribute.getNodeValue());
    }
  }

  @Override
  public final void addMultiplicableModel(final MultiplicableModel multiplicableModel) {
    if (!this.equals(multiplicableModel))
      this.multiplicableModels.add(multiplicableModel);
  }

  @Override
  public final LinkedHashSet<MultiplicableModel> getMultiplicableModels() {
    return multiplicableModels;
  }

  public Boolean getAbstract() {
    return _abstract;
  }

  public final Block getBlock() {
    return block;
  }

  @Override
  public final Boolean getMixed() {
    for (final Model model : getChildren()) {
      if (model instanceof ComplexContentModel && ((ComplexContentModel)model).getMixed() != null)
        return ((ComplexContentModel)model).getMixed();

      if (model instanceof ComplexTypeModel && ((ComplexTypeModel<?>)model).getMixed() != null)
        return ((ComplexTypeModel<?>)model).getMixed();
    }

    return mixed;
  }

  public void setExtension(final boolean extension) {
    this.extension = extension;
  }

  public boolean isExtension() {
    return extension;
  }

  @Override
  public final void addAttribute(final AttributeModel attribute) {
    attributes.add(attribute);
  }

  @Override
  public final LinkedHashSet<AttributeModel> getAttributes() {
    return attributes;
  }

  public static final class Reference extends ComplexTypeModel<SimpleTypeModel<?>> implements Referenceable {
    private static final Map<UniqueQName,Reference> all = new HashMap<UniqueQName,Reference>();

    protected Reference(final Model parent) {
      super(null, parent);
    }

    public static Reference parseComplexType(final UniqueQName name) {
      Reference type = all.get(name);
      if (type != null)
        return type;

      type = new Reference(null);
      type.setName(name);
      Reference.all.put(name, type);
      return type;
    }
  }

  public static final class Undefined extends ComplexTypeModel<SimpleTypeModel<?>> implements Undefineable {
    private static final Map<UniqueQName,Undefined> all = new HashMap<UniqueQName,Undefined>();

    protected Undefined(final Model parent) {
      super(null, parent);
    }

    public static Undefined parseComplexType(final UniqueQName name) {
      if (name == null)
        return null;

      Undefined type = all.get(name);
      if (type != null)
        return type;

      type = new Undefined(null);
      type.setName(name);
      Undefined.all.put(name, type);
      return type;
    }
  }
}