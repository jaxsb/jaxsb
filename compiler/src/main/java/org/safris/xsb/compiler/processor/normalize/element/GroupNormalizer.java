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

package org.safris.xsb.compiler.processor.normalize.element;

import java.util.HashMap;
import java.util.Map;

import org.safris.xsb.compiler.lang.LexerFailureException;
import org.safris.xsb.compiler.lang.UniqueQName;
import org.safris.xsb.compiler.processor.model.ElementableModel;
import org.safris.xsb.compiler.processor.model.Model;
import org.safris.xsb.compiler.processor.model.element.GroupModel;
import org.safris.xsb.compiler.processor.model.element.RedefineModel;
import org.safris.xsb.compiler.processor.normalize.Normalizer;
import org.safris.xsb.compiler.processor.normalize.NormalizerDirectory;

public final class GroupNormalizer extends Normalizer<GroupModel> {
  private final Map<UniqueQName,GroupModel> all = new HashMap<UniqueQName,GroupModel>();

  public GroupNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  public GroupModel parseGroup(final UniqueQName name) {
    return all.get(name);
  }

  @Override
  protected void stage1(final GroupModel model) {
    if (model.getName() == null)
      return;

    if (!all.containsKey(model.getName()))
      all.put(model.getName(), model);
  }

  @Override
  protected void stage2(final GroupModel model) {
    if (model.getRef() == null || !(model.getRef() instanceof GroupModel.Reference))
      return;

    GroupModel ref = parseGroup(model.getRef().getName());
    if (ref == null)
      ref = parseGroup(model.getName());

    if (ref == null)
      throw new LexerFailureException("ref == null for " + model.getName());

    model.setRef(ref);
  }

  @Override
  protected void stage3(final GroupModel model) {
    if (model.getRef() == null)
      return;

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent.getParent() instanceof RedefineModel && parent instanceof GroupModel && model.getRef().getName().equals(((GroupModel)parent).getName())) {
        model.getRef().setRedefine((GroupModel)parent);
        break;
      }
    }
  }

  @Override
  protected void stage4(final GroupModel model) {
    if (model.getRef() == null)
      return;

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof ElementableModel) {
        ((ElementableModel)parent).addMultiplicableModel(model.getRef());
        break;
      }
    }
  }

  @Override
  protected void stage5(final GroupModel model) {
  }

  @Override
  protected void stage6(final GroupModel model) {
  }
}