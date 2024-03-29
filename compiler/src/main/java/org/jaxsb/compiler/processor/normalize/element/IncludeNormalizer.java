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

import java.util.Collection;
import java.util.HashSet;

import org.jaxsb.compiler.processor.model.element.IncludeModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;

public final class IncludeNormalizer extends Normalizer<IncludeModel> {
  private final Collection<String> messages = new HashSet<>();

  public IncludeNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final IncludeModel model) {
  }

  @Override
  protected void stage2(final IncludeModel model) {
    final String message = "Including " + model.getSchemaLocation() + " for {" + model.getTargetNamespace() + "}";
    if (messages.contains(message))
      return;

    messages.add(message);
    if (logger.isInfoEnabled()) { logger.info(message); }
  }

  @Override
  protected void stage3(final IncludeModel model) {
  }

  @Override
  protected void stage4(final IncludeModel model) {
  }

  @Override
  protected void stage5(final IncludeModel model) {
  }

  @Override
  protected void stage6(final IncludeModel model) {
  }
}