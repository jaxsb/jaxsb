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
import java.util.List;

import javax.xml.namespace.QName;

import org.jaxsb.compiler.schema.attribute.Form;
import org.jaxsb.compiler.schema.attribute.Use;
import org.jaxsb.generator.processor.plan.ExtensiblePlan;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.generator.processor.plan.element.AttributePlan;
import org.jaxsb.runtime.Attribute;
import org.jaxsb.runtime.AttributeAudit;
import org.jaxsb.runtime.AttributeSpec;
import org.jaxsb.runtime.Binding;
import org.jaxsb.runtime.XSTypeDirectory;

public final class AttributeWriter extends SimpleTypeWriter<AttributePlan> {
  @Override
  protected void appendRegistration(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    // REASON: Attributes that are not defined globally do not need to be resolvable globally.
    if (!plan.isNested()) {
      writer.write("_$$registerAttribute(" + plan.getClassName(parent) + ".NAME, " + plan.getClassName(parent) + ".class);\n");
      writer.write("_$$registerSchemaLocation(" + plan.getClassName(parent) + ".NAME.getNamespaceURI(), " + plan.getClassName(null) + ".class, \"" + plan.getXsdLocation() + "\");\n");
    }
  }

  @Override
  protected void appendDeclaration(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (!plan.isRestriction())
      writer.write("private " + AttributeAudit.class.getName() + "<" + plan.getThisClassNameWithType(parent) + "> " + plan.getInstanceName() + " = __$$registerAttributeAudit(new " + AttributeAudit.class.getName() + "<>(this, " + plan.getDefaultInstance(parent) + ", new " + QName.class.getName() + "(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\", \"" + plan.getName().getPrefix() + "\"), " + Form.QUALIFIED.equals(plan.getFormDefault()) + ", " + Use.REQUIRED.equals(plan.getUse()) + "));\n");
  }

  @Override
  protected void appendGetMethod(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    writer.write("public " + plan.getDeclarationRestrictionGeneric(parent) + " get" + plan.getMethodName() + "()\n{\n");
    if (plan.isRestriction())
      writer.write("return super.get" + plan.getDeclarationRestrictionSimpleName() + "();\n");
    else
      writer.write("return " + plan.getInstanceName() + ".getAttribute();\n");
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
      writer.write("public void set" + plan.getDeclarationRestrictionSimpleName() + "(final " + plan.getDeclarationRestrictionGeneric(parent) + " " + plan.getInstanceName() + ") {\n");
      writer.write("throw new " + IllegalArgumentException.class.getName() + "(\"This method has been restricted by a more specific signature. Please correct your argument to use the alternate method signature.\");\n");
      writer.write("}\n");
    }

    writer.write("@" + AttributeSpec.class.getName() + "(use=\"" + plan.getUse().getValue() + "\")\n");
    writer.write("public void set" + plan.getMethodName() + "(final " + plan.getThisClassNameWithType(parent) + " " + plan.getInstanceName() + ") {\n");
    if (plan.isRestriction())
      writer.write("super.set" + plan.getDeclarationRestrictionSimpleName() + "(" + plan.getInstanceName() + ");\n");
    else
      writer.write("_$$setAttribute(this." + plan.getInstanceName() + ", this, " + plan.getInstanceName() + ");\n");
    writer.write("}\n");

    writer.write(plan.getDocumentation());

    String paramClass = "";
    final String paramClass2;
    if (plan.hasEnumerations()) {
      if (plan.isList())
        paramClass = List.class.getName() + "<";

      final String type;
      if (plan.hasSuperEnumerations())
        type = ((ExtensiblePlan)plan).getSuperClassNameWithoutGenericType() + ".Enum";
      else
        type = plan.getThisClassNameWithType(parent) + ".Enum";

      paramClass += type;
      paramClass2 = type;

      if (plan.isList())
        paramClass += ">";
    }
    else {
      paramClass = plan.getNativeNonEnumItemClassNameInterface();
      paramClass2 = plan.getNativeNonEnumItemClassName();
    }

    if (plan.isList()) {
      writer.write("public void set" + plan.getMethodName() + "(final " + paramClass + " text) {\n");
      writer.write("set" + plan.getMethodName() + "(text == null ? null : new " + plan.getThisClassNameWithType(parent) + "(text));\n");
      writer.write("}\n");

      writer.write("public void set" + plan.getMethodName() + "(final " + paramClass2 + " ... text) {\n");
      writer.write("set" + plan.getMethodName() + "(text == null ? null : new " + plan.getThisClassNameWithType(parent) + "(text));\n");
      writer.write("}\n");
    }
    else {
      writer.write("public void set" + plan.getMethodName() + "(final " + paramClass + " text) {\n");
      writer.write("set" + plan.getMethodName() + "(text == null ? null : new " + plan.getThisClassNameWithType(parent) + "(text));\n");
      writer.write("}\n");
    }

//    if (plan.getNativeItemClassName() == null && XSTypeDirectory.ANYSIMPLETYPE.getNativeBinding().getName().equals(plan.getBaseXSItemTypeName())) {
//      writer.write(accessibility + plan.getClassSimpleName() + "(" + List.class.getName() + "<" + plan.getNativeItemClassNameInterface() + "> text)\n");
//      writer.write("{\n");
//      writer.write("super(text);\n");
//      writer.write("}\n");
//    }
  }

  @Override
  protected void appendMarshal(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRestriction()) {
      if (!plan.isFixed())
        return;

      if (Form.QUALIFIED.equals(plan.getFormDefault())) {
        writer.write("if (!node.hasAttributeNS(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\")) {\n");
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
    writer.write("return _$$setAttribute(this." + plan.getInstanceName() + ", this, " + Binding.class.getName() + "._$$parseAttr(new " + plan.getClassName(parent) + "(), attribute));\n");
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

    writer.write("if (" + plan.getInstanceName() + " != null)\n");
    writer.write("hashCode = 31 * hashCode + " + plan.getInstanceName() + ".hashCode();\n");
  }

  @Override
  protected void appendClone(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRestriction())
      return;

    writer.write("clone." + plan.getInstanceName() + " = " + plan.getInstanceName() + ".clone(clone);\n");
  }

  @Override
  protected void appendClass(final StringWriter writer, final AttributePlan plan, final Plan<?> parent) {
    if (plan.isRef())
      return;

    writeQualifiedName(writer, plan);
    writer.write("public static class " + plan.getClassSimpleName() + " extends " + plan.getSuperClassNameWithGenericType() + " implements " + Attribute.class.getName() + " {\n");
    writer.write("private static final " + QName.class.getName() + " NAME = new " + QName.class.getName() + "(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\", \"" + plan.getName().getPrefix() + "\");\n");

    // ID LOOKUP
    writeIdLookup(writer, plan, parent);

    // ENUMERATIONS CONSTRUCTOR
    getRestrictions(writer, plan);

    // COPY CONSTRUCTOR
    writer.write(plan.getDocumentation());
    writer.write("protected " + plan.getClassSimpleName() + "(final " + (plan.hasEnumerations() ? plan.getClassName(parent) : plan.getCopyClassName(parent)) + " copy) {\n");
    writer.write("super(copy);\n");
    writer.write("}\n");

    // NATIVE CONSTRUCTORS
    getNativeConstructors(writer, plan);

    // DEFAULT CONSTRUCTOR
    writer.write(plan.getDocumentation());
    writer.write(plan.hasEnumerations() ? "protected " : "public ");
    writer.write(plan.getClassSimpleName() + "() {\n");
    writer.write("super();\n");
    writer.write("}\n");

    // DECODE & ENCODE
    getEncodeDecode(writer, plan);

    // INHERITS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + plan.getCopyClassName(parent) + " inherits() {\n");
    writer.write("return this;\n");
    writer.write("}\n");

    // ID
    if (plan.getId() != null) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + String.class.getName() + " id() {\n");
      writer.write("return \"" + plan.getId() + "\";\n");
      writer.write("}\n");
    }

    // NAME
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + QName.class.getName() + " name() {\n");
    writer.write("return NAME;\n");
    writer.write("}\n");

    // PATTERN
    appendPattern(writer, plan.getPatterns());

    // GETVALUE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + plan.getNativeItemClassNameInterface() + " text() {\n");
    if (plan.isRestriction())
      writer.write("return super.text();\n");
    else if (!Object.class.getName().equals(plan.getNativeItemClassNameInterface()))
      writer.write("return (" + plan.getNativeItemClassNameInterface() + ")super.text();\n");
    else
      writer.write("return super.text();\n");
    writer.write("}\n");

    if (plan.isList()) {
      writer.write("public " + plan.getNativeItemClassName() + " text(final int index) {\n");
      writer.write("final " + plan.getNativeItemClassNameInterface() + " values = text();\n");
      writer.write("return values != null && -1 < index && index < values.size() ? values.get(index) : null;\n");
      writer.write("}\n");
    }

    // SETVALUE
//    if (!plan.hasEnumerations()) {
//      // FIXME: This misses some @Override(s) for situations that inherit from xs types, cause the type of the parameter to the text() method is not known here
//      if (parent != null && ((SimpleTypePlan<?>)parent).getNativeItemClassNameInterface().equals(plan.getNativeItemClassNameInterface()))
//        writer.write("@" + Override.class.getName() + "\n");
//
//      if (plan.isList()) {
//        writer.write("public void text(final " + plan.getNativeNonEnumItemClassNameInterface() + " text)\n");
//        writer.write("{\n");
//        writer.write("super.text(text);\n");
//        writer.write("}\n");
//      }
//      else {
//        writer.write("public void text(final " + plan.getNativeItemClassNameInterface() + " text)\n");
//        writer.write("{\n");
//        writer.write("super.text(text);\n");
//        writer.write("}\n");
//      }
//    }

    // CLONE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + plan.getCopyClassName(parent) + " clone() {\n");
    writer.write("return (" + plan.getClassName(parent) + ")super.clone();\n");
    writer.write("}\n");

    // EQUALS
//    writer.write("@" + Override.class.getName() + "\n");
//    writer.write("public boolean equals(final " + Object.class.getName() + " obj)\n");
//    writer.write("{\n");
//    // NOTE: This is not checking whether getValue() is equal between this and obj
//    // NOTE: because this final class does not contain the value field.
//    writer.write("return super.equals(obj);\n");
//    writer.write("}\n");

    // HASHCODE
//    writer.write("@" + Override.class.getName() + "\n");
//    writer.write("public int hashCode()\n");
//    writer.write("{\n");
//    writer.write("int hashCode = super.hashCode();\n");
//    // NOTE: This is not checking whether getValue() is equal between this and obj
//    // NOTE: because this final class does not contain the value field.
//    writer.write("return hashCode;\n");
//    writer.write("}\n");

    writer.write("}\n");
  }
}