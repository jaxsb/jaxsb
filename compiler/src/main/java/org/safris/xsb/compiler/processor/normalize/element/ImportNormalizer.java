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

import java.util.Collection;
import java.util.HashSet;

import org.safris.xsb.compiler.processor.model.element.ImportModel;
import org.safris.xsb.compiler.processor.normalize.Normalizer;
import org.safris.xsb.compiler.processor.normalize.NormalizerDirectory;

public final class ImportNormalizer extends Normalizer<ImportModel> {
  private final Collection<String> messages = new HashSet<String>();

  public ImportNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final ImportModel model) {
  }

  @Override
  protected void stage2(final ImportModel model) {
    final String message = "Importing " + model.getSchemaLocation() + " for {" + model.getTargetNamespace() + "}";
    if (messages.contains(message))
      return;

    messages.add(message);
    logger.info(message);
  }

  @Override
  protected void stage3(final ImportModel model) {
  }

  @Override
  protected void stage4(final ImportModel model) {
  }

  @Override
  protected void stage5(final ImportModel model) {
  }

  @Override
  protected void stage6(final ImportModel model) {
  }
}