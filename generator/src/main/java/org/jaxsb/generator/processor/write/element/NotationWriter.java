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

package org.jaxsb.generator.processor.write.element;

import java.io.StringWriter;

import javax.xml.namespace.QName;

import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.generator.processor.plan.element.NotationPlan;
import org.jaxsb.generator.processor.write.Writer;
import org.jaxsb.runtime.CompilerFailureException;
import org.jaxsb.runtime.NotationType;

public final class NotationWriter extends Writer<NotationPlan> {
  @Override
  protected void appendRegistration(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    writer.write("_$$registerNotation(new " + QName.class.getName() + "(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\"), \"" + plan.getPublic() + "\", \"" + plan.getSystem() + "\", " + plan.getClassName(plan) + ".class);\n");
    writer.write("_$$registerSchemaLocation(" + plan.getClassName(parent) + ".NAME.getNamespaceURI(), " + plan.getClassName(null) + ".class, \"" + plan.getXsdLocation() + "\");\n");
  }

  @Override
  protected void appendDeclaration(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    throw new CompilerFailureException("notation cannot have a declaration");
  }

  @Override
  protected void appendGetMethod(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    throw new CompilerFailureException("notation cannot have a get method");
  }

  @Override
  protected void appendSetMethod(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    throw new CompilerFailureException("notation cannot have a set method");
  }

  @Override
  protected void appendMarshal(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    throw new CompilerFailureException("notation cannot have a marshal method");
  }

  @Override
  protected void appendParse(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    throw new CompilerFailureException("notation cannot have a parse method");
  }

  @Override
  public void appendCopy(final StringWriter writer, final NotationPlan plan, final Plan<?> parent, final String variable) {
    throw new CompilerFailureException("notation cannot have a copy statement");
  }

  @Override
  protected void appendEquals(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    throw new CompilerFailureException("notation cannot have a equals statement");
  }

  @Override
  protected void appendHashCode(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    throw new CompilerFailureException("notation cannot have a hashCode statement");
  }

  @Override
  protected void appendClone(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    throw new CompilerFailureException("notation cannot have a clone statement");
  }

  @Override
  protected void appendClass(final StringWriter writer, final NotationPlan plan, final Plan<?> parent) {
    // DOCUMENTATION
    writer.write(plan.getDocumentation());

    writer.write("public static final class " + plan.getClassSimpleName() + " extends " + NotationType.class.getName() + " {\n");
    writer.write("private static final " + QName.class.getName() + " NAME = new " + QName.class.getName() + "(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\", \"" + plan.getName().getPrefix() + "\");\n");

    writer.write("private final " + String.class.getName() + " _name = \"" + plan.getName().getLocalPart() + "\";\n");
    writer.write("private final " + String.class.getName() + " _public = " + (plan.getPublic() != null ? "\"" + plan.getPublic() + "\"" : "null") + ";\n");
    writer.write("private final " + String.class.getName() + " _system = " + (plan.getSystem() != null ? "\"" + plan.getSystem() + "\"" : "null") + ";\n");

    writer.write("protected " + plan.getClassSimpleName() + "() {\n");
    writer.write("super();\n");
    writer.write("}\n");

    // ID
    if (plan.getId() != null) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + String.class.getName() + " id() {\n");
      writer.write("return \"" + plan.getId() + "\";\n");
      writer.write("}\n");
    }

    // NAME
    writer.write("public " + QName.class.getName() + " name() {\n");
    writer.write("return NAME;\n");
    writer.write("}\n");

    // GETNAME
    writer.write("public " + String.class.getName() + " getName() {\n");
    writer.write("return _name;\n");
    writer.write("}\n");

    // PUBLIC
    writer.write("public " + String.class.getName() + " getPublic() {\n");
    writer.write("return _public;\n");
    writer.write("}\n");

    // SYSTEM
    writer.write("public " + String.class.getName() + " getSystem() {\n");
    writer.write("return _system;\n");
    writer.write("}\n");

    // CLONE
    writer.write("public " + plan.getClassName(null) + " clone() {\n");
    writer.write("return (" + plan.getClassName(null) + ")super.clone();\n");
    writer.write("}\n");

    // EQUALS
    writer.write("public boolean equals(final " + Object.class.getName() + " obj) {\n");
    writer.write("if (this == obj)\n");
    writer.write("return true;\n");
    writer.write("if (!(obj instanceof " + plan.getClassSimpleName() + "))\n");
    writer.write("return _$$failEquals();\n");
    writer.write("final " + plan.getClassSimpleName() + " that = (" + plan.getClassSimpleName() + ")obj;\n");
    writer.write("return (_name != null ? _name.equals(that._name) : that._name == null) && ");
    writer.write("(_public != null ? _public.equals(that._public) : that._public == null) && ");
    writer.write("(_system != null ? _system.equals(that._system) : that._system == null);\n");
    writer.write("}\n");

    // HASHCODE
    writer.write("public int hashCode() {\n");
    writer.write("int hashCode = super.hashCode();\n");
    writer.write("if (_name != null)\n");
    writer.write("hashCode = 31 * hashCode + _name.hashCode();\n");
    writer.write("if (_public != null)\n");
    writer.write("hashCode = 31 * hashCode + _public.hashCode();\n");
    writer.write("if (_system != null)\n");
    writer.write("hashCode = 31 * hashCode + _system.hashCode();\n");
    writer.write("return hashCode;\n");
    writer.write("}\n");

    writer.write("}\n");
  }
}