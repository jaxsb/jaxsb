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

package org.jaxsb.compiler.processor.normalize.element;

import java.util.HashMap;
import java.util.Map;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.element.ComplexTypeModel;
import org.jaxsb.compiler.processor.model.element.ElementModel;
import org.jaxsb.compiler.processor.model.element.RedefineModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;

public final class ComplexTypeNormalizer extends Normalizer<ComplexTypeModel<?>> {
  protected final Map<UniqueQName,ComplexTypeModel<?>> all = new HashMap<>();

  public ComplexTypeNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  public ComplexTypeModel<?> parseComplexType(final UniqueQName name) {
    return all.get(name);
  }

  @Override
  protected void stage1(final ComplexTypeModel<?> model) {
    if (model.getName() == null || model.getParent() instanceof RedefineModel)
      return;

    final ComplexTypeModel<?> complexTypeModel = parseComplexType(model.getName());
    if (complexTypeModel == null)
      all.put(model.getName(), model);
  }

  @Override
  protected void stage2(final ComplexTypeModel<?> model) {
    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
      if (parent instanceof ElementModel && parent instanceof Nameable && ((Nameable<?>)parent).getName() != null) {
        ((ElementModel)parent).setExtension(true);
        break;
      }
    }
  }

  @Override
  protected void stage3(final ComplexTypeModel<?> model) {
  }

  @Override
  protected void stage4(final ComplexTypeModel<?> model) {
  }

  @Override
  protected void stage5(final ComplexTypeModel<?> model) {
  }

  @Override
  protected void stage6(final ComplexTypeModel<?> model) {
    if (model.getName() == null)
      return;

    if (model.getSuperType() == null)
      model.setSuperType(ComplexTypeModel.Undefined.parseComplexType(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anyType")));
  }
}