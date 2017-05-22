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

package org.safris.xsb.generator.processor.write.element;

import java.io.StringWriter;

import org.safris.xsb.generator.processor.plan.Plan;
import org.safris.xsb.generator.processor.plan.element.HasPropertyPlan;
import org.safris.xsb.generator.processor.write.Writer;

public final class HasPropertyWriter extends Writer<HasPropertyPlan> {
  @Override
  protected void appendRegistration(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }

  @Override
  protected void appendDeclaration(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }

  @Override
  protected void appendGetMethod(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }

  @Override
  protected void appendSetMethod(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }

  @Override
  protected void appendMarshal(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }

  @Override
  protected void appendParse(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }

  @Override
  public void appendCopy(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent, final String variable) {
  }

  @Override
  protected void appendEquals(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }

  @Override
  protected void appendHashCode(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }

  @Override
  protected void appendClass(final StringWriter writer, final HasPropertyPlan plan, final Plan<?> parent) {
  }
}