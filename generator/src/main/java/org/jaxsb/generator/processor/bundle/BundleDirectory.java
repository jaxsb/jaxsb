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

package org.jaxsb.generator.processor.bundle;

import java.io.File;
import java.util.Set;

import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.composite.SchemaComposite;

public final class BundleDirectory implements PipelineDirectory<GeneratorContext,SchemaComposite,Bundle> {
  private final BundleProcessor processor;

  public BundleDirectory(final Set<File> classPath, final boolean skipXsd) {
    this.processor = new BundleProcessor(classPath, skipXsd);
  }

  @Override
  public PipelineEntity getEntity(final SchemaComposite entity, final Bundle parent) {
    return processor;
  }

  @Override
  public PipelineProcessor<GeneratorContext,SchemaComposite,Bundle> getProcessor() {
    return processor;
  }

  @Override
  public void clear() {
  }
}