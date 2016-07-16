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

package org.safris.xsb.generator.processor.write.element;

import java.io.StringWriter;

import javax.xml.namespace.QName;

import org.safris.xsb.compiler.schema.attribute.Form;
import org.safris.xsb.compiler.schema.attribute.Use;
import org.safris.xsb.generator.processor.plan.Plan;
import org.safris.xsb.generator.processor.plan.element.AttributePlan;
import org.safris.xsb.generator.processor.plan.element.SimpleTypePlan;
import org.safris.xsb.runtime.AttributeAudit;
import org.safris.xsb.runtime.AttributeSpec;
import org.safris.xsb.runtime.Binding;
import org.safris.xsb.runtime.SimpleType;
import org.safris.xsb.runtime.XSTypeDirectory;

public final class AttributeWriter extends SimpleTypeWriter<AttributePlan> {
  @Override
  protected void appendRegistration(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    // REASON: Attributes that are not defined globally do not need to be resolvable globally.
    if (!plan.isNested())
      writer.write("_$$registerSchemaLocation(" + plan.getClassName(parent) + ".NAME.getNamespaceURI(), " + plan.getClassName(null) + ".class, \"" + plan.getXsdLocation() + "\");\n");
  }

  @Override
  protected void appendDeclaration(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRestriction())
      return;

    writer.write("private " + AttributeAudit.class.getName() + "<" + plan.getThisClassNameWithType(parent) + "> " + plan.getInstanceName() + " = new " + AttributeAudit.class.getName() + "<" + plan.getThisClassNameWithType(parent) + ">(this, " + plan.getDefaultInstance(parent) + ", new " + QName.class.getName() + "(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\", \"" + plan.getName().getPrefix() + "\"), " + Form.QUALIFIED.equals(plan.getFormDefault()) + ", " + Use.REQUIRED.equals(plan.getUse()) + ");\n");
  }

  @Override
  protected void appendGetMethod(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    writeQualifiedName(writer, plan);
    writer.write("public " + plan.getDeclarationRestrictionGeneric(parent) + " " + plan.getClassSimpleName() + "()\n");
    writer.write("{\n");
    if (plan.isRestriction())
      writer.write("return super." + plan.getDeclarationRestrictionSimpleName() + "();\n");
    else
      writer.write("return " + plan.getInstanceName() + ".getAttribute() != null ? " + plan.getInstanceName() + ".getAttribute() : (" + plan.getClassName(parent) + ")NULL(" + plan.getClassName(parent) + ".class);\n");
    writer.write("}\n");
  }

  @Override
  protected void appendSetMethod(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRestriction()) {
      writer.write("/**\n");
      writer.write(" * NOTE: This method has been restricted by a more specific signature.\n");
      writer.write(" * Use of this method WILL CAUSE an IllegalArgumentException!\n");
      writer.write(" * Please correct your argument to use the alternate method signature.\n");
      writer.write(" */\n");
      writer.write("public void " + plan.getDeclarationRestrictionSimpleName() + "(" + plan.getDeclarationRestrictionGeneric(parent) + " " + plan.getInstanceName() + ")\n");
      writer.write("{\n");
      writer.write("throw new " + IllegalArgumentException.class.getName() + "(\"This method has been restricted by a more specific signature. Please correct your argument to use the alternate method signature.\");\n");
      writer.write("}\n");
    }

    writer.write("@" + AttributeSpec.class.getName() + "(use=\"" + plan.getUse().getValue() + "\")\n");
    writeQualifiedName(writer, plan);
    writer.write("public void " + plan.getClassSimpleName() + "(" + plan.getThisClassNameWithType(parent) + " " + plan.getInstanceName() + ")\n");
    writer.write("{\n");
    if (plan.isRestriction())
      writer.write("super." + plan.getDeclarationRestrictionSimpleName() + "(" + plan.getInstanceName() + ");\n");
    else
      writer.write("_$$setAttribute(this." + plan.getInstanceName() + ", this, " + plan.getInstanceName() + ");\n");
    writer.write("}\n");
  }

  @Override
  protected void appendMarshal(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRestriction()) {
      if (!plan.isFixed())
        return;

      if (Form.QUALIFIED.equals(plan.getFormDefault())) {
        writer.write("if (!node.hasAttributeNS(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\"))\n");
        writer.write("{\n");
        if (XSTypeDirectory.QNAME.getNativeBinding().getName().equals(plan.getBaseXSItemTypeName()))
          writer.write("node.setAttributeNS(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getPrefix() + "\" + \":" + plan.getName().getLocalPart() + "\", \"" + plan.getDefault().getLocalPart() + "\");\n");
        else
          writer.write("node.setAttributeNS(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getPrefix() + "\" + \":\" + \"" + plan.getName().getLocalPart() + "\", \"" + plan.getDefault().getLocalPart() + "\");\n");
        writer.write("}\n");
      }
      else {
        writer.write("if (!node.hasAttribute(\"" + plan.getName().getLocalPart() + "\"))\n");
        if (XSTypeDirectory.QNAME.getNativeBinding().getName().equals(plan.getBaseXSItemTypeName())) {
          writer.write("{\n");
          writer.write("node.setAttribute(\"" + plan.getName().getLocalPart() + "\", \"" + plan.getDefault().getPrefix() + "\" + \":" + plan.getDefault().getLocalPart() + "\");\n");
          writer.write("}\n");
        }
        else
          writer.write("node.setAttribute(\"" + plan.getName().getLocalPart() + "\", \"" + plan.getDefault().getLocalPart() + "\");\n");
      }

      return;
    }

    writer.write(plan.getInstanceName() + ".marshal(node);\n");
  }

  @Override
  protected void appendParse(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRestriction())
      return;

    if (Form.QUALIFIED.equals(plan.getFormDefault()))
      writer.write("if (\"" + plan.getName().getNamespaceURI() + "\".equals(attribute.getNamespaceURI()) && \"" + plan.getName().getLocalPart() + "\".equals(attribute.getLocalName()))\n");
    else
      writer.write("if (attribute.getNamespaceURI() == null && \"" + plan.getName().getLocalPart() + "\".equals(attribute.getLocalName()))\n");

    writer.write("{\n");
    writer.write("return _$$setAttribute(this." + plan.getInstanceName() + ", this, (" + plan.getThisClassNameWithType(parent) + ")" + Binding.class.getName() + "._$$parseAttr(" + plan.getClassName(parent) + ".class, attribute.getOwnerElement(), attribute));\n");
    writer.write("}\n");
  }

  @Override
  public void appendCopy(final StringWriter writer, final AttributePlan plan, final Plan<?> parent, final String variable) {
    if (plan.isRestriction())
      return;

    writer.write("this." + plan.getInstanceName() + " = " + variable + "." + plan.getInstanceName() + ";\n");
  }

  @Override
  protected void appendEquals(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRestriction())
      return;

    writer.write("if (" + plan.getInstanceName() + " != null ? !" + plan.getInstanceName() + ".equals(that." + plan.getInstanceName() + ") : that." + plan.getInstanceName() + " != null)\n");
    writer.write("return _$$failEquals();\n");
  }

  @Override
  protected void appendHashCode(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRestriction())
      return;

    writer.write("hashCode += " + plan.getInstanceName() + " != null ? " + plan.getInstanceName() + ".hashCode() : -1;\n");
  }

  @Override
  protected void appendClass(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRef())
      return;

    writeQualifiedName(writer, plan);
    writer.write("public static class " + plan.getClassSimpleName() + " extends " + plan.getSuperClassNameWithType() + " implements " + SimpleType.class.getName() + "\n");
    writer.write("{\n");
    writer.write("private static final " + QName.class.getName() + " NAME = getClassQName(" + plan.getClassName(parent) + ".class);\n");

    // ID LOOKUP
    writeIdLookup(writer, plan, parent);

    // ENUMERATIONS CONSTRUCTOR
    getRestrictions(writer, plan, parent);

    // COPY CONSTRUCTOR
    writer.write(plan.getDocumentation());
    if (plan.hasEnumerations())
      writer.write("public " + plan.getClassSimpleName() + "(final " + plan.getClassName(parent) + " copy)\n");
    else
      writer.write("public " + plan.getClassSimpleName() + "(final " + plan.getCopyClassName(parent) + " copy)\n");
    writer.write("{\n");
    writer.write("super(copy);\n");
    writer.write("}\n");

    // NATIVE CONSTRUCTORS
    getNativeConstructors(writer, plan, parent);

    // DEFAULT CONSTRUCTOR
    if (plan.hasEnumerations())
      writer.write("protected ");
    else {
      // DOCUMENTATION
      writer.write(plan.getDocumentation());
      writer.write("public ");
    }
    writer.write(plan.getClassSimpleName() + "()\n");
    writer.write("{\n");
    writer.write("super();\n");
    writer.write("}\n");

    // DECODE & ENCODE
    getEncodeDecode(writer, plan, parent);

    // INHERITS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + plan.getClassSimpleName() + " inherits()\n");
    writer.write("{\n");
    writer.write("return this;\n");
    writer.write("}\n");

    // OWNER
    appendOwner(writer);

    // GETNAME
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + QName.class.getName() + " name()\n");
    writer.write("{\n");
    writer.write("return NAME;\n");
    writer.write("}\n");

    // PATTERN
    appendPattern(writer, plan.getPatterns());

    // GETVALUE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + plan.getNativeItemClassNameInterface() + " text()\n");
    writer.write("{\n");
    if (plan.isRestriction())
      writer.write("return super.text();\n");
    else if (!Object.class.getName().equals(plan.getNativeItemClassNameInterface()))
      writer.write("return (" + plan.getNativeItemClassNameInterface() + ")super.text();\n");
    else
      writer.write("return super.text();\n");
    writer.write("}\n");

    if (plan.isList()) {
      writer.write("public " + plan.getNativeItemClassName() + " text(final int index)\n");
      writer.write("{\n");
      writer.write("final " + plan.getNativeItemClassNameInterface() + " values = text();\n");
      writer.write("return values != null && -1 < index && index < values.size() ? values.get(index) : null;\n");
      writer.write("}\n");
    }

    // SETVALUE
    if (!plan.hasEnumerations()) {
      // FIXME: This misses some @Override(s) for situations that inherit from xs types, cause the type of the parameter to the text() method is not known here
      if (parent != null && ((SimpleTypePlan<?>)parent).getNativeItemClassNameInterface().equals(plan.getNativeItemClassNameInterface()))
        writer.write("@" + Override.class.getName() + "\n");

      writer.write("public void text(" + plan.getNativeItemClassNameInterface() + " text)\n");
      writer.write("{\n");
      writer.write("super.text(text);\n");
      writer.write("}\n");
    }

    // CLONE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + plan.getCopyClassName(parent) + " clone()\n");
    writer.write("{\n");
    writer.write("return new " + plan.getClassName(parent) + "(this);\n");
    writer.write("}\n");

    // EQUALS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public boolean equals(" + Object.class.getName() + " obj)\n");
    writer.write("{\n");
    // NOTE: This is not checking whether getValue() is equal between this and obj
    // NOTE: because this final class does not contain the value field.
    writer.write("return super.equals(obj);\n");
    writer.write("}\n");

    // HASHCODE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public int hashCode()\n");
    writer.write("{\n");
    writer.write("int hashCode = super.hashCode();\n");
    writer.write("hashCode += NAME.hashCode();\n");
//      writer.write("hashCode += getValue() != null ? getValue().hashCode() : -1;\n");
    writer.write("return hashCode;\n");
    writer.write("}\n");

    writer.write("}\n");
  }
}