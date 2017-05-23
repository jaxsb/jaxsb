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

package org.libx4j.xsb.generator.processor.write;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;

public final class BindingFileFilter implements FileFilter {
  private final boolean acceptHidden;

  public BindingFileFilter(final boolean acceptHidden) {
    this.acceptHidden = acceptHidden;
  }

  // FIXME: This is a hack, & is broken when the license format changes
  @Override
  public boolean accept(final File pathname) {
    if (!acceptHidden && pathname.isHidden())
      return false;

    if (pathname.isDirectory())
      return true;

    try (final InputStream in = pathname.toURI().toURL().openStream()) {
      final byte[] bytes = new byte[33];
      in.read(bytes);
      return new String(bytes).contains("lib4j");
    }
    catch (final IOException e) {
      return false;
    }
  }
}