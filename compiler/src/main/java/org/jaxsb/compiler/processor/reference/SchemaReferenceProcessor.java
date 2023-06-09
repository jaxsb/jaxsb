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

package org.jaxsb.compiler.processor.reference;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import org.jaxsb.compiler.lang.LexerFailureException;
import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.libj.net.URLConnections;
import org.libj.net.URLs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SchemaReferenceProcessor implements PipelineEntity, PipelineProcessor<GeneratorContext,SchemaReference,SchemaReference> {
  private static final Logger logger = LoggerFactory.getLogger(SchemaReferenceProcessor.class);

  @Override
  public Collection<SchemaReference> process(final GeneratorContext pipelineContext, final Collection<? extends SchemaReference> schemaReferences, final PipelineDirectory<GeneratorContext,? super SchemaReference,SchemaReference> directory) {
    final File destDir = pipelineContext.getDestDir();
    if (logger.isDebugEnabled()) logger.debug("destDir = " + (destDir != null ? destDir.getAbsolutePath() : null));

    if (schemaReferences.size() == 0)
      return Collections.EMPTY_LIST;

    final LinkedHashSet<SchemaReference> selectedSchemas = new LinkedHashSet<>(3);
    try {
      for (final SchemaReference schemaReference : schemaReferences) { // [C]
        try {
          final File javaFile = new File(destDir, schemaReference.getNamespaceURI().getNamespaceBinding().getJavaPath());
          if (logger.isDebugEnabled()) logger.debug("checking whether class is up-to-date: " + javaFile.getAbsolutePath());

          if (!pipelineContext.getOverwrite() && javaFile.exists()) {
            final URL url = schemaReference.getURL();
            final long lastModified = URLConnections.checkFollowRedirect(url.openConnection()).getLastModified();
            if (lastModified != 0 && javaFile.lastModified() >= lastModified) {
              if (logger.isInfoEnabled()) logger.info("Bindings for " + URLs.getName(schemaReference.getURL()) + " are up-to-date.");
              continue;
            }
          }

          if (logger.isDebugEnabled()) logger.debug("adding: " + javaFile.getAbsolutePath());
          selectedSchemas.add(schemaReference);
        }
        catch (final IOException e) {
          throw new LexerFailureException(e);
        }
      }
    }
    catch (final LexerFailureException e) {
      throw e;
    }
    catch (final Exception e) {
      throw new LexerFailureException(e);
    }

    return selectedSchemas;
  }
}