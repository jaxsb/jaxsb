/* Copyright (c) 2008 Seva Safris
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

package org.safris.xsb.runtime.processor.timestamp;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.List;

import org.safris.commons.io.Files;
import org.safris.commons.pipeline.PipelineDirectory;
import org.safris.commons.pipeline.PipelineEntity;
import org.safris.commons.pipeline.PipelineProcessor;
import org.safris.xsb.generator.lexer.processor.GeneratorContext;
import org.safris.xsb.runtime.processor.bundle.Bundle;

public final class TimestampProcessor implements PipelineEntity, PipelineProcessor<GeneratorContext,Bundle,Bundle> {
  private static final FileFilter fileFilter = new FileFilter() {
    @Override
    public boolean accept(final File pathname) {
      return pathname != null && pathname.isFile();
    }
  };

  private static final FileFilter dirFileFilter = new FileFilter() {
    @Override
    public boolean accept(final File pathname) {
      return pathname != null && pathname.isDirectory();
    }
  };

  protected TimestampProcessor() {
  }

  @Override
  public Collection<Bundle> process(final GeneratorContext pipelineContext, final Collection<Bundle> documents, final PipelineDirectory<GeneratorContext,Bundle,Bundle> directory) {
    // Get the earliest lastModified time of all the files
    long lastModified = Long.MAX_VALUE;
    final List<File> files = Files.listAll(pipelineContext.getDestdir(), fileFilter);
    if (files != null)
      for (final File file : files)
        if (file.lastModified() < lastModified)
          lastModified = file.lastModified();

    // Set the lastModified time of all directories to just before the value from above
    final List<File> dirs = Files.listAll(pipelineContext.getDestdir(), dirFileFilter);
    if (dirs != null)
      for (final File dir : dirs)
        dir.setLastModified(lastModified - 100);

    return null;
  }
}