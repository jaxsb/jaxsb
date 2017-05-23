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

package org.libx4j.xsb.generator.processor.write.element;

import java.io.StringWriter;
import java.util.List;

import org.libx4j.xsb.generator.processor.plan.Plan;
import org.libx4j.xsb.generator.processor.plan.element.AnyPlan;
import org.libx4j.xsb.runtime.Binding;
import org.libx4j.xsb.runtime.Bindings;
import org.libx4j.xsb.runtime.ElementAudit;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class AnyWriter extends ElementWriter<AnyPlan> {
  @Override
  protected void appendDeclaration(final StringWriter writer, final AnyPlan plan, final Plan<?> parent) {
//      if (plan.getMaxOccurs() > 1)
    writer.write("private " + ElementAudit.class.getName() + "<" + Binding.class.getName() + "> any = new " + ElementAudit.class.getName() + "<" + Binding.class.getName() + ">(this, " + plan.getDefaultInstance(parent) + ", null, null, true, " + plan.isNillable() + ", " + plan.getMinOccurs() + ", " + plan.getMaxOccurs() + ");\n");
//      else
//          writer.write("private " + ElementAudit.class.getName() + "<" + Binding.class.getName() + "> any = new " + ElementAudit.class.getName() + "<" + Binding.class.getName() + ">(" + plan.getDefaultInstance(parent) + ", null, null, true, " + plan.isNillable() + ", " + plan.getMinOccurs() + ", " + plan.getMaxOccurs() + ");\n");
  }

  @Override
  protected void appendGetMethod(final StringWriter writer, final AnyPlan plan, final Plan<?> parent) {
//      if (plan.getMaxOccurs() > 1)
    writer.write("public " + List.class.getName() + "<" +  Binding.class.getName() + "> getAny()\n");
//      else
//          writer.write("public " + Binding.class.getName() + " getAny()\n");

    writer.write("{\n");
    writer.write("return any.getElements();\n");
    writer.write("}\n");

    writer.write("public " + Binding.class.getName() + " any(final int index)\n");
    writer.write("{\n");
    writer.write("final " + List.class.getName() + "<" + Binding.class.getName() + "> values = getAny();\n");
    writer.write("return values != null && -1 < index && index < values.size() ? values.get(index) : null;\n");
    writer.write("}\n");
  }

  @Override
  protected void appendSetMethod(final StringWriter writer, final AnyPlan plan, final Plan<?> parent) {
//      if (plan.getMaxOccurs() > 1)
    writer.write("public " + Binding.class.getName() + " addAny(" +  Binding.class.getName() + " any)\n");
//      else
//          writer.write("public void setAny(" +  Binding.class.getName() + " any)\n");

    writer.write("{\n");
//      if (plan.getMaxOccurs() > 1)
//      {
    writer.write("_$$addElement(this.any, any);\n");
//      }
//      else
//      {
//          writer.write("if (this." + plan.getInstanceName() + ".$value() != null)\n");
//          writer.write("_$$dequeueElement(this." + plan.getInstanceName() + ");\n");
//          writer.write("this.any.$value(any);\n");
//      }

    writer.write("return any;\n");
    writer.write("}\n");
  }

  @Override
  protected void appendMarshal(final StringWriter writer, final AnyPlan plan, final Plan<?> parent) {
//      writer.write("any.marshal(element);\n");
  }

  @Override
  protected void appendParse(final StringWriter writer, final AnyPlan plan, final Plan<?> parent) {
//      if (plan.getMaxOccurs() > 1)
//      {
    writer.write("if (element.getNodeType() != " + Node.class.getName() + ".ELEMENT_NODE)\n");
    writer.write("return;\n");
    writer.write("_$$addElement(this.any, " + Bindings.class.getName() + ".parse((" + Element.class.getName() + ")element));\n");
//      }
//      else
//          writer.write("this.any.$value(" + Bindings.class.getName() + ".parse((" + Element.class.getName() + ")childNode));\n");
  }

  @Override
  public void appendCopy(final StringWriter writer, final AnyPlan plan, final Plan<?> parent, final String variable) {
    writer.write("this.any = " + variable + ".any;\n");
  }

  @Override
  protected void appendEquals(final StringWriter writer, final AnyPlan plan, final Plan<?> parent) {
    writer.write("if (any != null ? !any.equals(that.any) : that.any != null)\n");
    writer.write("return _$$failEquals();\n");
  }

  @Override
  protected void appendHashCode(final StringWriter writer, final AnyPlan plan, final Plan<?> parent) {
    writer.write("hashCode += any != null ? any.hashCode() : -1;\n");
  }

  @Override
  protected void appendClass(final StringWriter writer, final AnyPlan plan, final Plan<?> parent) {
  }
}