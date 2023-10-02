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
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import org.jaxsb.compiler.lang.NamespaceURI;

public abstract class PipelineProcessor<C extends PipelineContext,I extends PipelineEntity,O extends PipelineEntity> {
  protected static boolean matches(final boolean ifNullReturn, final Set<Pattern> patterns, final NamespaceURI namespaceURI) {
    if (patterns == null || patterns.size() == 0)
      return ifNullReturn;

    for (final Pattern pattern : patterns)
      if (pattern.matcher(namespaceURI.toString()).matches())
        return true;

    return false;
  }

  public abstract Collection<O> process(C pipelineContext, Collection<? extends I> documents, PipelineDirectory<C,? super I,O> directory) throws IOException;
}