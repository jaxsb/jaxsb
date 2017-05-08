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

package org.safris.xsb.compiler.processor.reference;

import java.io.File;
import java.net.URLConnection;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.safris.commons.net.URLs;
import org.safris.commons.pipeline.PipelineDirectory;
import org.safris.commons.pipeline.PipelineEntity;
import org.safris.commons.pipeline.PipelineProcessor;
import org.safris.commons.xml.sax.DocumentHandler;
import org.safris.commons.xml.sax.SAXInterruptException;
import org.safris.commons.xml.sax.XMLDocuments;
import org.safris.maven.common.Log;
import org.safris.xsb.compiler.lang.LexerFailureException;
import org.safris.xsb.compiler.processor.GeneratorContext;

public final class SchemaReferenceProcessor implements PipelineEntity, PipelineProcessor<GeneratorContext,SchemaReference,SchemaReference> {
//  private static final class Counter {
//    protected volatile int count = 0;
//  }

  @Override
  public Collection<SchemaReference> process(final GeneratorContext pipelineContext, final Collection<SchemaReference> schemaReferences, final PipelineDirectory<GeneratorContext,SchemaReference,SchemaReference> directory) {
    final File destDir = pipelineContext.getDestdir();
    Log.debug("destDir = " + (destDir != null ? destDir.getAbsolutePath() : null));

    final Collection<SchemaReference> selectedSchemas = new LinkedHashSet<SchemaReference>(3);
    try {
      // select schemas that should be generated based on timestamps
//      final Counter counter = new Counter();

//      final ThreadGroup threadGroup = new ThreadGroup("SchemaReferenceProcess");
//      Log.debug("created SchemaReferenceProcess ThreadGroup");
      // download and cache the schemas into a temporary directory
      for (final SchemaReference schemaReference : schemaReferences) {
//        new Thread(threadGroup, schemaReference.getURL().toString()) {
//          @Override
//          public void run() {
            try {
              final File containerClass = new File(destDir, schemaReference.getNamespaceURI().getPackage().replace('.', File.separatorChar) + File.separator + "xe.java");
              Log.debug("checking whether class is up-to-date: " + containerClass.getAbsolutePath());
              if (pipelineContext.getOverwrite() || !containerClass.exists()) {
                Log.debug("adding: " + containerClass.getAbsolutePath());
                selectedSchemas.add(schemaReference);
              }
              else {
                try {
                  XMLDocuments.parse(schemaReference.getURL(), new DocumentHandler() {
                    @Override
                    public void schemaLocation(final URLConnection connection) throws SAXInterruptException {
                      if (containerClass.lastModified() < connection.getLastModified())
                        throw new SAXInterruptException();
                    }
                  }, false, false);
                }
                catch (final SAXInterruptException e) {
                  Log.debug("adding: " + containerClass.getAbsolutePath());
                  selectedSchemas.add(schemaReference);
                  continue;
                }

                Log.info("Bindings for " + URLs.getName(schemaReference.getURL()) + " are up-to-date.");
              }

//              synchronized (counter) {
//                ++counter.count;
//                counter.notify();
//              }
            }
            catch (final Exception e) {
              throw new LexerFailureException(e);
            }
//          }
//        }.start();
//      }

//      synchronized (schemas) {
//        synchronized (counter) {
//          while (counter.count != schemas.size())
//            counter.wait();
//        }
      }
    }
    catch (final Exception e) {
      throw new LexerFailureException(e);
    }

    return selectedSchemas;
  }
}