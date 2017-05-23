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

package org.safris.xsb.compiler.processor.document;

import java.net.URL;
import java.util.Collection;

import org.lib4j.pipeline.PipelineEntity;
import org.safris.xsb.compiler.processor.reference.SchemaReference;
import org.w3c.dom.Document;

public final class SchemaDocument implements PipelineEntity {
  private final SchemaReference schemaReference;
  private final Document document;
  private Collection<URL> includes;

  public SchemaDocument(final SchemaReference schemaReference, final Document document) {
    this.schemaReference = schemaReference;
    this.document = document;
  }

  public SchemaReference getSchemaReference() {
    return schemaReference;
  }

  public Document getDocument() {
    return document;
  }

  public Collection<URL> getIncludes() {
    return includes;
  }

  public void setIncludes(final Collection<URL> includes) {
    this.includes = includes;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof SchemaDocument))
      return false;

    final SchemaDocument that = (SchemaDocument)obj;
    return schemaReference != null ? schemaReference.equals(that.schemaReference) : that.schemaReference == null;
  }

  @Override
  public int hashCode() {
    return (schemaReference != null ? schemaReference.hashCode() : -1) + (document != null ? 1 : -1);
  }
}