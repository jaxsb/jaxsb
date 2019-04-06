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

package org.openjax.xsb.generator.schema;

import org.openjax.xsb.compiler.pipeline.PipelineDirectory;
import org.openjax.xsb.compiler.pipeline.PipelineProcessor;
import org.openjax.xsb.compiler.processor.GeneratorContext;
import org.openjax.xsb.compiler.processor.document.SchemaDocument;
import org.openjax.xsb.compiler.processor.reference.SchemaReference;

public final class SchemaDocumentDirectory implements PipelineDirectory<GeneratorContext,SchemaReference,SchemaDocument> {
  private final SchemaDocumentProcessor processor = new SchemaDocumentProcessor();

  @Override
  public SchemaDocumentProcessor getEntity(final SchemaReference entity, final SchemaDocument parent) {
    return processor;
  }

  @Override
  public PipelineProcessor<GeneratorContext,SchemaReference, SchemaDocument> getProcessor() {
    return processor;
  }

  @Override
  public void clear() {
  }
}