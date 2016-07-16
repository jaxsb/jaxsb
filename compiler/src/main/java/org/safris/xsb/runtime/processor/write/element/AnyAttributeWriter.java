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

package org.safris.xsb.compiler.processor.write.element;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.safris.xsb.compiler.processor.plan.Plan;
import org.safris.xsb.compiler.processor.plan.element.AnyAttributePlan;
import org.safris.xsb.compiler.processor.write.Writer;
import org.safris.xsb.compiler.runtime.AttributeAudit;
import org.safris.xsb.compiler.runtime.Binding;
import org.safris.xsb.lexer.schema.attribute.Form;
import org.safris.xsb.lexer.schema.attribute.Use;
import org.w3c.dom.Element;

public final class AnyAttributeWriter extends Writer<AnyAttributePlan> {
  @Override
  protected void appendRegistration(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
  }

  @Override
  protected void appendDeclaration(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
    writer.write("private " + AttributeAudit.class.getName() + "<" + List.class.getName() + "<" + Binding.class.getName() + ">> anyAttribute = new " + AttributeAudit.class.getName() + "<" + List.class.getName() + "<" + Binding.class.getName() + ">>(this, null, null, " + Form.QUALIFIED.equals(plan.getFormDefault()) + ", " + Use.REQUIRED.equals(plan.getUse()) + ");\n");
  }

  @Override
  protected void appendGetMethod(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
    writer.write("public " + List.class.getName() + "<" +  Binding.class.getName() + "> getAny$()\n");
    writer.write("{\n");
    writer.write("return anyAttribute.getAttribute();\n");
    writer.write("}\n");

    writer.write("public " + Binding.class.getName() + " any$(final int index)\n");
    writer.write("{\n");
    writer.write("final " + List.class.getName() + "<" + Binding.class.getName() + "> values = getAny$();\n");
    writer.write("return values != null && -1 < index && index < values.size() ? values.get(index) : null;\n");
    writer.write("}\n");
  }

  @Override
  protected void appendSetMethod(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
    writer.write("public void addAny$(" + Binding.class.getName() + " anyAttribute)\n");
    writer.write("{\n");
    writer.write("if (this.anyAttribute.getAttribute() == null)\n");
    writer.write("this.anyAttribute.setAttribute(new " + ArrayList.class.getName() + ">());\n");
    writer.write("this.anyAttribute.getAttribute().add(anyAttribute);\n");
    writer.write("}\n");
  }

  @Override
  protected void appendMarshal(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
    writer.write("anyAttribute.marshal(node);\n");
  }

  @Override
  protected void appendParse(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
//      writer.write("else\n");
//      writer.write("{\n");
    writer.write("if (this.anyAttribute.getAttribute() == null)\n");
    writer.write("this.anyAttribute.setAttribute(new " + ArrayList.class.getName() + "<" + Binding.class.getName() + ">());\n");
    writer.write("this.anyAttribute.getAttribute().add(" + Binding.class.getName() + ".parseAttr((" + Element.class.getName() + ")attribute.getParentNode(), attribute));\n");
//      writer.write("}\n");
  }

  @Override
  public void appendCopy(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent, final String variable) {
    writer.write("this.anyAttribute = " + variable + ".anyAttribute;\n");
  }

  @Override
  protected void appendEquals(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
    writer.write("if (anyAttribute != null ? !anyAttribute.equals(that.anyAttribute) : that.anyAttribute != null)\n");
    writer.write("return _$$failEquals();\n");
  }

  @Override
  protected void appendHashCode(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
    writer.write("hashCode += anyAttribute != null ? anyAttribute.hashCode() : -1;\n");
  }

  @Override
  protected void appendClass(final StringWriter writer, final AnyAttributePlan plan, final Plan<?> parent) {
  }
}