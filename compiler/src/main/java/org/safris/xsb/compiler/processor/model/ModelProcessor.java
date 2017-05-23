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

package org.safris.xsb.compiler.processor.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.lib4j.pipeline.PipelineDirectory;
import org.lib4j.pipeline.PipelineProcessor;
import org.safris.commons.xml.NamespaceURI;
import org.safris.xsb.compiler.lang.LexerFailureException;
import org.safris.xsb.compiler.processor.GeneratorContext;
import org.safris.xsb.compiler.processor.composite.SchemaComposite;
import org.safris.xsb.compiler.processor.composite.SchemaModelComposite;
import org.safris.xsb.compiler.processor.composite.SchemaNodeComposite;
import org.safris.xsb.compiler.processor.document.SchemaDocument;
import org.safris.xsb.compiler.processor.model.element.SchemaModel;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class ModelProcessor implements PipelineProcessor<GeneratorContext,SchemaComposite,Model> {
  private Model root;

  @Override
  public Collection<Model> process(final GeneratorContext pipelineContext, final Collection<SchemaComposite> documents, final PipelineDirectory<GeneratorContext,SchemaComposite,Model> directory) {
    root = new Model(null, null) {};
    // Then we parse all of the schemas that have been included and imported
    final Collection<Model> schemaModels = new ArrayList<Model>();

    for (final SchemaComposite schemaComposite : documents) {
      final SchemaModelComposite schemaModelComposite = (SchemaModelComposite)schemaComposite;
      final SchemaDocument schemaDocument = schemaModelComposite.getSchemaDocument();
      final SchemaModel model = recurse(root, schemaDocument.getSchemaReference().getNamespaceURI(), schemaDocument.getDocument().getChildNodes(), schemaDocument.getSchemaReference().getURL(), directory);
      if (model == null)
        throw new LexerFailureException("We should have found a schema!");

      schemaModelComposite.setSchemaModel(model);
      schemaModels.add(model);
    }

    return schemaModels;
  }

  private final SchemaModel recurse(final Model model, final NamespaceURI targetNamespace, NodeList children, URL url, final PipelineDirectory<GeneratorContext,SchemaComposite,Model> directory) {
    if (children == null || children.getLength() == 0)
      return null;

    // FIXME: This looks ugly!
    SchemaModel schema = null;
    if (model instanceof SchemaModel) {
      schema = (SchemaModel)model;
      if (model.getTargetNamespace() == null) {
        // This means that this is an included schema
        schema.setTargetNamespace(targetNamespace);
        URL schemaReference = model.lookupSchemaLocation(targetNamespace);
        if (schemaReference == null)
          model.registerSchemaLocation(targetNamespace, schemaReference = url);
      }
      else {
        model.registerSchemaLocation(targetNamespace, url);
      }

      schema.setURL(url);
      url = null;
    }

    Model current = null;
    for (int i = 0; i < children.getLength(); i++) {
      final Node child = children.item(i);
      if (child.getLocalName() == null)
        continue;

      final SchemaNodeComposite nodeComposite = new SchemaNodeComposite(child);
      final Model handler = (Model)directory.getEntity(nodeComposite, model);
      if (current != null) {
        handler.setPrevious(current);
        current.setNext(handler);
      }

      current = handler;

      final SchemaModel temp = recurse(handler, targetNamespace, child.getChildNodes(), url, directory);
      if (temp != null)
        schema = temp;
    }

    return schema;
  }
}