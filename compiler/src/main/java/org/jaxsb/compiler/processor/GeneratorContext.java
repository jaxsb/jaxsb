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

package org.jaxsb.compiler.processor;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.jaxsb.compiler.lang.NamespaceURI;
import org.jaxsb.compiler.pipeline.PipelineContext;
import org.libj.util.CollectionUtil;

public final class GeneratorContext implements PipelineContext {
  private final File destDir;
  private final boolean overwrite;
  private final File compileDir;
  private final boolean pack;
  private final Set<NamespaceURI> includes;
  private final Set<NamespaceURI> excludes;

  public GeneratorContext(final File destDir, final boolean overwrite, final File compileDir, final boolean pack, final Set<NamespaceURI> includes, final Set<NamespaceURI> excludes) {
    File tempDestDir;
    try {
      tempDestDir = destDir.getCanonicalFile();
    }
    catch (final IOException e) {
      tempDestDir = destDir;
    }
    this.destDir = tempDestDir;
    this.overwrite = overwrite;
    this.compileDir = compileDir;
    this.pack = pack;
    this.includes = includes;
    this.excludes = excludes;
  }

  public File getDestDir() {
    return destDir;
  }

  public boolean getOverwrite() {
    return overwrite;
  }

  public File getCompileDir() {
    return compileDir;
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

  @Override
  public String toString() {
    return destDir.getAbsolutePath() + " " + overwrite + " " + (compileDir == null ? "null" : compileDir.getAbsolutePath()) + " " + pack + " " + CollectionUtil.toString(includes, ' ') + " " + CollectionUtil.toString(excludes, ' ');
  }
}