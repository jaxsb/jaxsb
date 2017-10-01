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

package org.libx4j.xsb.compiler.processor.reference;

import org.lib4j.pipeline.PipelineDirectory;
import org.lib4j.pipeline.PipelineEntity;
import org.lib4j.pipeline.PipelineProcessor;
import org.libx4j.xsb.compiler.processor.GeneratorContext;

public final class SchemaReferenceDirectory implements PipelineDirectory<GeneratorContext,SchemaReference,SchemaReference> {
  private final SchemaReferenceProcessor schemaReferenceProcessor = new SchemaReferenceProcessor();

  @Override
  public PipelineEntity getEntity(final SchemaReference entity, final SchemaReference parent) {
    return schemaReferenceProcessor;
  }

  @Override
  public PipelineProcessor<GeneratorContext,SchemaReference,SchemaReference> getProcessor() {
    return schemaReferenceProcessor;
  }

  @Override
  public void clear() {
  }
}