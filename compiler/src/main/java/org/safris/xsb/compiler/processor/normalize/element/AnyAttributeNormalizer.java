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

import org.safris.xsb.compiler.processor.Nameable;
import org.safris.xsb.compiler.processor.model.AttributableModel;
import org.safris.xsb.compiler.processor.model.Model;
import org.safris.xsb.compiler.processor.model.element.AnyAttributeModel;
import org.safris.xsb.compiler.processor.model.element.AttributeGroupModel;
import org.safris.xsb.compiler.processor.normalize.Normalizer;
import org.safris.xsb.compiler.processor.normalize.NormalizerDirectory;

public final class AnyAttributeNormalizer extends Normalizer<AnyAttributeModel> {
  public AnyAttributeNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final AnyAttributeModel model) {
  }

  @Override
  protected void stage2(final AnyAttributeModel model) {
    // add the handler attribute to the attributeGroup
    if (model.getParent() instanceof AttributeGroupModel)
      ((AttributeGroupModel)model.getParent()).addAttribute(model);
  }

  @Override
  protected void stage3(final AnyAttributeModel model) {
  }

  @Override
  protected void stage4(final AnyAttributeModel model) {
    // Add the handler to the Attributable final class with a name
    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      if (parent instanceof AttributableModel && parent instanceof Nameable && ((Nameable<?>)parent).getName() != null) {
        ((AttributableModel)parent).addAttribute(model);
        break;
      }
    }
  }

  @Override
  protected void stage5(final AnyAttributeModel model) {
  }

  @Override
  protected void stage6(final AnyAttributeModel model) {
  }
}