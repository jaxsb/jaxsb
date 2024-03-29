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

package org.jaxsb.compiler.processor.normalize.element;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;
import org.libj.net.URLs;

public final class SchemaNormalizer extends Normalizer<SchemaModel> {
  private static final Path CWD = new File("").getAbsoluteFile().toPath();

  public SchemaNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final SchemaModel model) {
    final URL url = model.getURL();
    if (url == null)
      return;

    final String display = (URLs.isLocal(url) ? CWD.relativize(new File(url.getFile()).getAbsoluteFile().toPath()) : url).toString();
    if (logger.isInfoEnabled()) { logger.info("Lexing {" + model.getTargetNamespace() + "} from " + display); }
  }

  @Override
  protected void stage2(final SchemaModel model) {
  }

  @Override
  protected void stage3(final SchemaModel model) {
  }

  @Override
  protected void stage4(final SchemaModel model) {
  }

  @Override
  protected void stage5(final SchemaModel model) {
  }

  @Override
  protected void stage6(final SchemaModel model) {
  }
}