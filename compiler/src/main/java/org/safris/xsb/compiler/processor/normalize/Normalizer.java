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

package org.safris.xsb.compiler.processor.normalize;

import org.lib4j.pipeline.PipelineEntity;
import org.safris.xsb.compiler.processor.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Normalizer<T extends Model> implements PipelineEntity {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final NormalizerDirectory directory;

  public Normalizer(final NormalizerDirectory directory) {
    this.directory = directory;
  }

  public NormalizerDirectory getDirectory() {
    return directory;
  }

  // NOTE: This stage used for fixing globally accessible types
  protected abstract void stage1(final T handler);

  // NOTE: This stage used for de-referencing qName references to correct types
  protected abstract void stage2(final T handler);

  // NOTE: This stage used for de-referencing <redefine/> rules
  protected abstract void stage3(final T handler);

  // NOTE: This stage used for injection of information into parent elements as per the physical schema structure
  protected abstract void stage4(final T handler);

  // NOTE: This stage used for injection of information into parent elements as per the logical schema structure
  protected abstract void stage5(final T handler);

  // NOTE: This stage used to amend information in certain edge-case situations.
  protected abstract void stage6(final T handler);
}