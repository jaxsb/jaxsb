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

package org.jaxsb.compiler.processor.composite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.document.SchemaDocument;

public final class SchemaCompositeProcessor extends PipelineProcessor<GeneratorContext,SchemaDocument,SchemaComposite> implements PipelineEntity {
  @Override
  public Collection<SchemaComposite> process(final GeneratorContext pipelineContext, final Collection<? extends SchemaDocument> documents, final PipelineDirectory<GeneratorContext,? super SchemaDocument,SchemaComposite> directory) {
    final int i$ = documents.size();
    if (i$ == 0)
      return Collections.EMPTY_LIST;

    final ArrayList<SchemaComposite> selectors = new ArrayList<>(i$);
    for (final SchemaDocument document : documents) // [C]
      selectors.add(new SchemaModelComposite(document));

    return selectors;
  }
}