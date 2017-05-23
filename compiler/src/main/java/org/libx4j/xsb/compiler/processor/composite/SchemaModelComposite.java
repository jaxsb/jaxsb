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

package org.libx4j.xsb.compiler.processor.composite;

import org.libx4j.xsb.compiler.processor.document.SchemaDocument;
import org.libx4j.xsb.compiler.processor.model.element.SchemaModel;

public final class SchemaModelComposite implements SchemaComposite {
  private final SchemaDocument schemaDocument;
  private SchemaModel schemaModel = null;

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
    return (schemaDocument != null ? schemaDocument.equals(that.schemaDocument) : that.schemaDocument == null) && (schemaModel != null ? schemaModel.equals(that.schemaModel) : that.schemaModel == null);
  }

  @Override
  public int hashCode() {
    return (schemaDocument != null ? schemaDocument.hashCode() : -7) * (schemaModel != null ? schemaModel.hashCode() : -3);
  }
}