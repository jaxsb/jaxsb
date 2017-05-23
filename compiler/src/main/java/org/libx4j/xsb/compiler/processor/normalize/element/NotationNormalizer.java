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

import org.libx4j.xsb.compiler.lang.UniqueQName;
import org.libx4j.xsb.compiler.processor.model.element.NotationModel;
import org.libx4j.xsb.compiler.processor.normalize.Normalizer;
import org.libx4j.xsb.compiler.processor.normalize.NormalizerDirectory;

public final class NotationNormalizer extends Normalizer<NotationModel> {
  private final Map<UniqueQName,NotationModel> all = new HashMap<UniqueQName,NotationModel>();

  public NotationNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  public NotationModel parseNotation(final UniqueQName name) {
    return all.get(name);
  }

  @Override
  protected void stage1(final NotationModel model) {
    if (model.getName() == null)
      return;

    final NotationModel notationModel = parseNotation(model.getName());
    if (notationModel == null)
      all.put(model.getName(), model);
  }

  @Override
  protected void stage2(final NotationModel model) {
  }

  @Override
  protected void stage3(final NotationModel model) {
  }

  @Override
  protected void stage4(final NotationModel model) {
  }

  @Override
  protected void stage5(final NotationModel model) {
  }

  @Override
  protected void stage6(final NotationModel model) {
  }
}