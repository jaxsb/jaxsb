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

import java.util.ArrayList;

import org.jaxsb.compiler.processor.model.ElementableModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.MultiplicableModel;
import org.jaxsb.compiler.processor.model.element.AnyModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;

public final class AnyNormalizer extends Normalizer<AnyModel> {
  public AnyNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final AnyModel model) {
  }

  @Override
  protected void stage2(final AnyModel model) {
  }

  @Override
  protected void stage3(final AnyModel model) {
  }

  @Override
  protected void stage4(final AnyModel model) {
    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
      if (parent instanceof ElementableModel) {
        final ArrayList<MultiplicableModel> multiplicableModels = ((ElementableModel)parent).getMultiplicableModels();
        for (int i = 0, i$ = multiplicableModels.size(); i < i$; ++i) // [RA]
          if (multiplicableModels.get(i) instanceof AnyModel)
            return;

        ((ElementableModel)parent).addMultiplicableModel(model);
        return;
      }
    }
  }

  @Override
  protected void stage5(final AnyModel model) {
  }

  @Override
  protected void stage6(final AnyModel model) {
  }
}