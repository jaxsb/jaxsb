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

package org.safris.xsb.compiler.processor;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.lib4j.pipeline.PipelineContext;
import org.safris.commons.xml.NamespaceURI;

public final class GeneratorContext implements PipelineContext {
  private final File destDir;
  private final boolean overwrite;
  private final boolean compale;
  private final boolean pack;
  private final Set<NamespaceURI> includes;
  private final Set<NamespaceURI> excludes;

  public GeneratorContext(final File destDir, final boolean overwrite, final boolean compile, final boolean pack, final Set<NamespaceURI> includes, final Set<NamespaceURI> excludes) {
    File tempDestDir;
    try {
      tempDestDir = destDir.getCanonicalFile();
    }
    catch (final IOException e) {
      tempDestDir = destDir;
    }
    this.destDir = tempDestDir;
    this.overwrite = overwrite;
    this.compale = compile;
    this.pack = pack;
    this.includes = includes;
    this.excludes = excludes;
  }

  public File getDestdir() {
    return destDir;
  }

  public boolean getOverwrite() {
    return overwrite;
  }

  public boolean getCompile() {
    return compale;
  }

  public boolean getPackage() {
    return pack;
  }

  public Set<NamespaceURI> getIncludes() {
    return includes;
  }

  public Set<NamespaceURI> getExcludes() {
    return excludes;
  }
}