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

package org.jaxsb.compiler.processor.composite;

import java.util.Objects;

import org.jaxsb.compiler.processor.document.SchemaDocument;
import org.jaxsb.compiler.processor.model.element.SchemaModel;

public final class SchemaModelComposite implements SchemaComposite {
  private final SchemaDocument schemaDocument;
  private SchemaModel schemaModel;

  public SchemaModelComposite(final SchemaDocument schemaDocument) {
    this.schemaDocument = schemaDocument;
  }

  public SchemaDocument getSchemaDocument() {
    return schemaDocument;
  }

  public void setSchemaModel(final SchemaModel schemaModel) {
    this.schemaModel = schemaModel;
  }

  public SchemaModel getSchemaModel() {
    return schemaModel;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof SchemaModelComposite))
      return false;

    final SchemaModelComposite that = (SchemaModelComposite)obj;
    return (Objects.equals(schemaDocument, that.schemaDocument)) && (Objects.equals(schemaModel, that.schemaModel));
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    if (schemaDocument != null)
      hashCode = 31 * hashCode + schemaDocument.hashCode();

    if (schemaModel != null)
      hashCode = 31 * hashCode + schemaModel.hashCode();

    return hashCode;
  }
}