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

package org.libx4j.xsb.compiler.processor.normalize.element;

import java.util.HashMap;
import java.util.Map;

import org.libx4j.xsb.compiler.lang.LexerFailureException;
import org.libx4j.xsb.compiler.lang.UniqueQName;
import org.libx4j.xsb.compiler.processor.Nameable;
import org.libx4j.xsb.compiler.processor.model.AttributableModel;
import org.libx4j.xsb.compiler.processor.model.Model;
import org.libx4j.xsb.compiler.processor.model.element.AttributeGroupModel;
import org.libx4j.xsb.compiler.processor.model.element.RedefineModel;
import org.libx4j.xsb.compiler.processor.normalize.Normalizer;
import org.libx4j.xsb.compiler.processor.normalize.NormalizerDirectory;

public final class AttributeGroupNormalizer extends Normalizer<AttributeGroupModel> {
  private final Map<UniqueQName,AttributeGroupModel> all = new HashMap<UniqueQName,AttributeGroupModel>();

  public AttributeGroupNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  public AttributeGroupModel parseAttributeGroup(final UniqueQName name) {
    return all.get(name);
  }

  @Override
  protected void stage1(final AttributeGroupModel model) {
    if (model.getName() == null || model.getParent() instanceof RedefineModel)
      return;

    if (parseAttributeGroup(model.getName()) == null)
      all.put(model.getName(), model);
  }

  @Override
  protected void stage2(final AttributeGroupModel model) {
    if (!(model.getRef() instanceof AttributeGroupModel.Reference))
      return;

    final AttributeGroupModel ref = parseAttributeGroup(model.getRef().getName());
    if (ref == null)
      throw new LexerFailureException("ref == null for " + model.getRef().getName());

    model.setRef(ref);

    // If the parent of this attributeGroup is also an attributeGroup, then propogate the attributes
    if (model.getParent() instanceof AttributeGroupModel)
      ((AttributeGroupModel)model.getParent()).getAttributes().addAll(model.getRef().getAttributes());
  }

  @Override
  protected void stage3(final AttributeGroupModel model) {
    if (model.getRef() == null)
      return;

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent.getParent() instanceof RedefineModel && parent instanceof AttributeGroupModel && model.getRef().getName().equals(((AttributeGroupModel)parent).getName())) {
        model.getRef().setRedefine((AttributeGroupModel)parent);
        break;
      }
    }
  }

  @Override
  protected void stage4(final AttributeGroupModel model) {
    if (model.getRef() == null)
      return;

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof AttributableModel && parent instanceof Nameable && ((Nameable<?>)parent).getName() != null) {
        if (model.getRef().getRedefine() != null && model.getRef().getRedefine() != parent)
          ((AttributableModel)parent).getAttributes().addAll(model.getRef().getRedefine().getAttributes());
        else
          ((AttributableModel)parent).getAttributes().addAll(model.getRef().getAttributes());

        break;
      }
    }
  }

  @Override
  protected void stage5(final AttributeGroupModel model) {
  }

  @Override
  protected void stage6(final AttributeGroupModel model) {
  }
}