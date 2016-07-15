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

package org.safris.xml.generator.lexer.processor;

import java.io.File;
import java.io.IOException;

import org.safris.commons.pipeline.PipelineContext;

public final class GeneratorContext implements PipelineContext {
  private final long manifestLastModified;
  private final File destDir;
  private final boolean explodeJars;
  private final boolean overwrite;

  public GeneratorContext(final long manifestLastModified, final File destDir, boolean explodeJars, final boolean overwrite) {
    this.manifestLastModified = manifestLastModified;
    File tempDestDir;
    try {
      tempDestDir = destDir.getCanonicalFile();
    }
    catch (final IOException e) {
      tempDestDir = destDir;
    }
    this.destDir = tempDestDir;
    this.explodeJars = explodeJars;
    this.overwrite = overwrite;
  }

  public long getManifestLastModified() {
    return manifestLastModified;
  }

  public File getDestdir() {
    return destDir;
  }

  public boolean getExplodeJars() {
    return explodeJars;
  }

  public boolean getOverwrite() {
    return overwrite;
  }
}