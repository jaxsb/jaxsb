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

package org.jaxsb.generator.processor.timestamp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Predicate;

import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.generator.processor.bundle.Bundle;

public final class TimestampProcessor extends PipelineProcessor<GeneratorContext,Bundle,Bundle> implements PipelineEntity {
  private static final Predicate<Path> fileFilter = path -> path != null && path.toFile().isFile();
  private static final Predicate<Path> dirFileFilter = path -> path != null && path.toFile().isDirectory();

  protected TimestampProcessor() {
  }

  @Override
  public Collection<Bundle> process(final GeneratorContext pipelineContext, final Collection<? extends Bundle> documents, final PipelineDirectory<GeneratorContext,? super Bundle,Bundle> directory) throws IOException {
    // Get the earliest lastModified time of all the files
    final long lastModified = Files.walk(pipelineContext.getDestDir().toPath())
      .filter(fileFilter)
      .mapToLong(p -> p.toFile().lastModified())
      .reduce(Math::min)
      .orElse(System.currentTimeMillis());

    // Set the lastModified time of all directories to just before the value from above
    Files.walk(pipelineContext.getDestDir().toPath())
      .filter(dirFileFilter)
      .forEach(p -> p.toFile().setLastModified(lastModified - 100));

    return null;
  }
}