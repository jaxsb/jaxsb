/* Copyright (c) 2008 OpenJAX
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

package org.openjax.xsb.compiler.processor.normalize;

import org.openjax.xsb.compiler.processor.model.Model;
import org.openjax.xsb.helper.pipeline.PipelineEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Normalizer<T extends Model> implements PipelineEntity {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private final NormalizerDirectory directory;

  public Normalizer(final NormalizerDirectory directory) {
    this.directory = directory;
  }

  public final NormalizerDirectory getDirectory() {
    return directory;
  }

  // NOTE: This stage used for fixing globally accessible types
  protected abstract void stage1(T model);

  // NOTE: This stage used for dereferencing qName references to correct types
  protected abstract void stage2(T model);

  // NOTE: This stage used for dereferencing <redefine/> rules
  protected abstract void stage3(T model);

  // NOTE: This stage used for injection of information into parent elements as per the physical schema structure
  protected abstract void stage4(T model);

  // NOTE: This stage used for injection of information into parent elements as per the logical schema structure
  protected abstract void stage5(T model);

  // NOTE: This stage used to amend information in certain edge-case situations.
  protected abstract void stage6(T model);
}