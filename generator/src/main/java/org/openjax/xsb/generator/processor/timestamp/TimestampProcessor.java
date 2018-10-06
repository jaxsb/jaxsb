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

package org.openjax.xsb.generator.processor.timestamp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Predicate;

import org.openjax.xsb.compiler.processor.GeneratorContext;
import org.openjax.xsb.generator.processor.bundle.Bundle;
import org.openjax.xsb.helper.pipeline.PipelineDirectory;
import org.openjax.xsb.helper.pipeline.PipelineEntity;
import org.openjax.xsb.helper.pipeline.PipelineProcessor;

public final class TimestampProcessor implements PipelineEntity, PipelineProcessor<GeneratorContext,Bundle,Bundle> {
  private static final Predicate<Path> fileFilter = new Predicate<Path>() {
    @Override
    public boolean test(final Path t) {
      return t != null && t.toFile().isFile();
    }
  };

  private static final Predicate<Path> dirFileFilter = new Predicate<Path>() {
    @Override
    public boolean test(final Path t) {
      return t != null && t.toFile().isDirectory();
    }
  };

  protected TimestampProcessor() {
  }

  @Override
  public Collection<Bundle> process(final GeneratorContext pipelineContext, final Collection<Bundle> documents, final PipelineDirectory<GeneratorContext,Bundle,Bundle> directory) {
    try {
      // Get the earliest lastModified time of all the files
      final long lastModified = Files.
        walk(pipelineContext.getDestDir().toPath()).
        filter(fileFilter).
        map(p -> p.toFile().lastModified()).
        reduce(Math::min).get();

      // Set the lastModified time of all directories to just before the value from above
      Files.
        walk(pipelineContext.getDestDir().toPath()).
        filter(dirFileFilter).
        forEach(p -> p.toFile().setLastModified(lastModified - 100));
    }
    catch (final IOException e) {
      throw new IllegalStateException(e);
    }

    return null;
  }
}