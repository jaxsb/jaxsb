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

package org.jaxsb.compiler.pipeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public final class Pipeline<C extends PipelineContext> {
  private final class Entry<I extends PipelineEntity,O extends PipelineEntity> {
    private final Collection<I> input;
    private final Collection<O> output;
    private final PipelineDirectory<C,I,O> directory;

    private Entry(final Collection<I> input, final Collection<O> output, final PipelineDirectory<C,I,O> directory) {
      this.input = input;
      this.output = output;
      this.directory = directory;
    }

    public PipelineProcessor<C,I,O> getProcessor() {
      return directory.getProcessor();
    }

    public Collection<I> getInput() {
      return input;
    }

    public Collection<O> getOutput() {
      return output;
    }

    public PipelineDirectory<C,I,O> getDirectory() {
      return directory;
    }
  }

  private final ArrayList<Entry<?,?>> entries = new ArrayList<>();
  private final C pipelineContext;

  public Pipeline(final C pipelineContext) {
    this.pipelineContext = pipelineContext;
  }

  public <I extends PipelineEntity,O extends PipelineEntity> void addProcessor(final Collection<I> input, final Collection<O> output, final PipelineDirectory<C,I,O> handlerDirectory) {
    synchronized (entries) {
      final Entry<I,O> modulePair = new Entry<>(input, output, handlerDirectory);
      entries.add(modulePair);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public void begin() throws IOException {
    final ArrayList<PipelineDirectory<?,?,?>> directories = new ArrayList<>();
    synchronized (entries) {
      for (int i = 0, i$ = entries.size(); i < i$; ++i) { // [RA]
        final Entry modulePair = entries.get(i);
        directories.add(modulePair.getDirectory());
        final Collection<?> output = modulePair.getProcessor().process(pipelineContext, modulePair.getInput(), modulePair.getDirectory());
        if (output != null && modulePair.getOutput() != null)
          modulePair.getOutput().addAll(output);
      }
    }

    for (int i = 0, i$ = directories.size(); i < i$; ++i) // [RA]
      directories.get(i).clear();
  }
}