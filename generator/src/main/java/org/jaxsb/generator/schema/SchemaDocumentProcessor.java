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

package org.jaxsb.generator.schema;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Stack;

import org.jaxsb.compiler.lang.NamespaceURI;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.pipeline.PipelineProcessor;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.document.SchemaDocument;
import org.jaxsb.compiler.processor.reference.SchemaReference;
import org.jaxsb.generator.AbstractGenerator;
import org.libj.net.URLs;
import org.libj.util.StringPaths;
import org.openjax.xml.schema.SchemaResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class SchemaDocumentProcessor implements PipelineEntity, PipelineProcessor<GeneratorContext,SchemaReference,SchemaDocument> {
  private static final Logger logger = LoggerFactory.getLogger(SchemaDocumentProcessor.class);
  private static final String[] includeStrings = {"include", "redefine"};

  @Override
  public Collection<SchemaDocument> process(final GeneratorContext pipelineContext, final Collection<? extends SchemaReference> selectedSchemas, final PipelineDirectory<GeneratorContext,? super SchemaReference,SchemaDocument> directory) throws IOException {
    if (selectedSchemas == null || selectedSchemas.size() == 0)
      return null;

    final LinkedHashSet<SchemaDocument> schemas = new LinkedHashSet<>();
    final HashMap<NamespaceURI,URL> importLoopCheck = new HashMap<>();
    final HashMap<NamespaceURI,ArrayList<URL>> includeLoopCheck = new HashMap<>();

    for (final SchemaReference schemaReference : selectedSchemas) { // [C]
      if (schemaReference == null)
        continue;

      final Stack<SchemaDocument> schemasToGenerate = new Stack<>();

      // First we need to find all of the imports and includes
      Stack<SchemaDocument> outer = new Stack<>();
      outer.add(AbstractGenerator.parse(schemaReference));
      importLoopCheck.put(schemaReference.getNamespaceURI(), schemaReference.getURL());
      while (outer.size() != 0) {
        schemasToGenerate.addAll(0, outer);
        final Stack<SchemaDocument> inner = new Stack<>();
        for (int i = 0, i$ = outer.size(); i < i$; ++i) { // [RA]
          final SchemaDocument schemaDocument = outer.get(i);
          NodeList includeNodeList;
          for (final String includeString : includeStrings) { // [A]
            includeNodeList = schemaDocument.getDocument().getElementsByTagNameNS(UniqueQName.XS.getNamespaceURI().toString(), includeString);
            for (int j = 0, j$ = includeNodeList.getLength(); j < j$; ++j) { // [RA]
              final Element includeElement = (Element)includeNodeList.item(j);
              final URL schemaLocationURL = getSchemaLocation(schemaDocument.getSchemaReference().getURL(), includeElement);

              // Don't want to get into an infinite loop
              ArrayList<URL> duplicates = includeLoopCheck.get(schemaDocument.getSchemaReference().getNamespaceURI());
              if (schemaLocationURL.equals(schemaDocument.getSchemaReference().getURL()) || (duplicates != null && duplicates.contains(schemaLocationURL)))
                  continue;

              final SchemaReference includeSchemaReference = new SchemaReference(schemaLocationURL, schemaDocument.getSchemaReference().getNamespaceURI(), schemaDocument.getSchemaReference().getPrefix(), true);
              inner.insertElementAt(AbstractGenerator.parse(includeSchemaReference), 0);
              if (duplicates == null)
                  duplicates = new ArrayList<>();

              duplicates.add(schemaLocationURL);
              logger.info("Adding " + URLs.getName(schemaLocationURL) + " for {" + schemaDocument.getSchemaReference().getNamespaceURI() + "}");
              includeLoopCheck.put(schemaDocument.getSchemaReference().getNamespaceURI(), duplicates);
            }
          }

          final NodeList importNodeList = schemaDocument.getDocument().getElementsByTagNameNS(UniqueQName.XS.getNamespaceURI().toString(), "import");
          for (int j = 0, j$ = importNodeList.getLength(); j < j$; ++j) { // [RA]
            final Element importElement = (Element)importNodeList.item(j);
            final String namespaceUri = importElement.getAttribute("namespace");
            final URL schemaUri = getSchemaLocation(schemaDocument.getSchemaReference().getURL(), importElement);
            final URL schemaUrl = SchemaResolver.resolve(namespaceUri, schemaUri.toString());
            final URL schemaLocationURI = schemaUrl != null ? schemaUrl : schemaUri;

            // Check if we have two schemaReferences for a single targetNamespace
            // This should not happen for import, but can happen for include!
            final NamespaceURI importNamespaceURI = NamespaceURI.getInstance(namespaceUri);
            final URL duplicate = importLoopCheck.get(importNamespaceURI);
            if (duplicate == null) {
              importLoopCheck.put(importNamespaceURI, schemaLocationURI);
              inner.insertElementAt(AbstractGenerator.parse(new SchemaReference(schemaLocationURI, importNamespaceURI, false)), 0);
              continue;
            }

            if (!duplicate.equals(schemaLocationURI))
              logger.info("Redefining {" + schemaDocument.getSchemaReference().getNamespaceURI() + "} from " + URLs.getName(schemaLocationURI) + " with " + duplicate);
          }
        }

        outer = inner;
      }

      schemas.addAll(schemasToGenerate);
    }

    for (final SchemaDocument schema : schemas) { // [S]
      final ArrayList<URL> includes = includeLoopCheck.get(schema.getSchemaReference().getNamespaceURI());
      if (includes == null || includes.size() == 0)
        continue;

      final ArrayList<URL> externalIncludes = new ArrayList<>(includes.size());
      for (int i = 0, i$ = includes.size(); i < i$; ++i) { // [RA]
        final URL include = includes.get(i);
        if (!include.equals(schema.getSchemaReference().getURL()))
          externalIncludes.add(include);
      }

      if (externalIncludes.size() != 0)
        schema.setIncludes(externalIncludes);
    }

    return schemas;
  }

  private static URL getSchemaLocation(final URL baseURI, final Element element) throws MalformedURLException {
    final String schemaLocation = element.getAttribute("schemaLocation");
    if (StringPaths.isAbsolutePublicId(schemaLocation))
      return URLs.create(StringPaths.canonicalize(schemaLocation));

    if (StringPaths.isAbsoluteSystemId(schemaLocation))
      return URLs.canonicalize(new File(schemaLocation).toURI().toURL());

    final URL parent = URLs.getCanonicalParent(baseURI);
    if (parent != null)
      return URLs.create(parent, StringPaths.canonicalize(schemaLocation));

    // If the parent is null, it means we're at the root of the protocol.
    // In this case, canonicalize backwards to eliminate "../" that lead with sub-dirs that follow.
    final String canonical = StringPaths.canonicalize(new StringBuilder(schemaLocation).reverse()).reverse().toString();
    final String protocol = baseURI.getProtocol();
    return URLs.create(protocol != null ? protocol + "://" + canonical : canonical);
  }
}