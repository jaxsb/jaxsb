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
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.safris.commons.xml.validator.ValidationException;
import org.safris.commons.xml.validator.Validator;
import org.safris.xsb.compiler.lang.CompilerFailureException;
import org.safris.xsb.compiler.lang.XSTypeDirectory;
import org.safris.xsb.compiler.processor.plan.Plan;
import org.safris.xsb.compiler.processor.plan.element.AnyAttributePlan;
import org.safris.xsb.compiler.processor.plan.element.AnyPlan;
import org.safris.xsb.compiler.processor.plan.element.AttributePlan;
import org.safris.xsb.compiler.processor.plan.element.ComplexTypePlan;
import org.safris.xsb.compiler.processor.plan.element.ElementPlan;
import org.safris.xsb.compiler.processor.write.Writer;
import org.safris.xsb.compiler.runtime.Binding;
import org.safris.xsb.compiler.runtime.BindingList;
import org.safris.xsb.compiler.runtime.BindingRuntimeException;
import org.safris.xsb.compiler.runtime.ComplexType;
import org.safris.xsb.compiler.runtime.MarshalException;
import org.safris.xsb.compiler.runtime.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ComplexTypeWriter<T extends ComplexTypePlan<?>> extends SimpleTypeWriter<T> {
  @Override
  protected void appendRegistration(final StringWriter writer, final T plan, final Plan<?> parent) {
    writer.write("_$$registerType(" + plan.getClassName(parent) + ".NAME, " + plan.getClassName(parent) + ".class);\n");
    writer.write("_$$registerSchemaLocation(" + plan.getClassName(parent) + ".NAME.getNamespaceURI(), " + plan.getClassName(null) + ".class, \"" + plan.getXsdLocation() + "\");\n");
  }

  @Override
  protected void appendDeclaration(final StringWriter writer, final T plan, final Plan<?> parent) {
    throw new CompilerFailureException("complexType cannot have a declaration");
  }

  @Override
  protected void appendGetMethod(final StringWriter writer, final T plan, final Plan<?> parent) {
    throw new CompilerFailureException("complexType cannot have a get method");
  }

  @Override
  protected void appendSetMethod(final StringWriter writer, final T plan, final Plan<?> parent) {
    throw new CompilerFailureException("complexType cannot have a set method");
  }

  @Override
  protected void appendMarshal(final StringWriter writer, final T plan, final Plan<?> parent) {
    throw new CompilerFailureException("complexType cannot have a marshal method");
  }

  @Override
  protected void appendParse(final StringWriter writer, final T plan, final Plan<?> parent) {
    throw new CompilerFailureException("complexType cannot have a parse method");
  }

  @Override
  public void appendCopy(final StringWriter writer, final T plan, Plan<?> parent, final String variable) {
    throw new CompilerFailureException("complexType cannot have a copy statement");
  }

  @Override
  protected void appendEquals(final StringWriter writer, final T plan, final Plan<?> parent) {
    throw new CompilerFailureException("complexType cannot have a equals statement");
  }

  @Override
  protected void appendHashCode(final StringWriter writer, final T plan, final Plan<?> parent) {
    throw new CompilerFailureException("complexType cannot have a hashCode statement");
  }

  @Override
  protected void appendClass(final StringWriter writer, final T plan, final Plan<?> parent) {
    writeQualifiedName(writer, plan);
    writer.write("public static abstract class " + plan.getClassSimpleName() + " extends " + plan.getSuperClassNameWithType() + " implements " + ComplexType.class.getName() + "\n");
    writer.write("{\n");
    writer.write("private static final " + QName.class.getName() + " NAME = getClassQName(" + plan.getClassName(parent) + ".class);\n");

    // FACTORY METHOD
    writer.write("protected static " + plan.getClassSimpleName() + " newInstance(final " + plan.getBaseNonXSTypeClassName() + " inherits)\n");
    writer.write("{\n");
    writer.write("return new " + plan.getClassName(parent) + "()\n");
    writer.write("{\n");
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + plan.getBaseNonXSTypeClassName() + " inherits()\n");
    writer.write("{\n");
    writer.write("return inherits;\n");
    writer.write("}\n");
    writer.write("};\n");
    writer.write("}\n");

    // ID LOOKUP
    writeIdLookup(writer, plan, parent);

    // MIXED
    if ((!plan.hasSimpleContent() || plan.getNativeItemClassNameInterface() == null) && plan.getMixed() != null && plan.getMixed())
      writer.write("private " + String.class.getName() + " text = null;\n");

    for (final Object attribute : plan.getAttributes())
      Writer.writeDeclaration(writer, (AttributePlan)attribute, plan);

    for (final Object element : plan.getElements())
      Writer.writeDeclaration(writer, (ElementPlan)element, plan);

    // ENUMERATIONS CONSTRUCTOR
    getRestrictions(writer, plan, parent);

    // COPY CONSTRUCTOR
    writer.write(plan.getDocumentation());
    writer.write("public " + plan.getClassSimpleName() + "(final " + plan.getClassName(null) + " copy)\n");
    writer.write("{\n");
    writer.write("super(copy);\n");
    if (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed())
      writer.write("this.text = copy.text;\n");
    for (final Object attribute : plan.getAttributes())
      Writer.writeCopy(writer, (AttributePlan)attribute, plan, "copy");
    for (final Object element : plan.getElements())
      Writer.writeCopy(writer, (ElementPlan)element, plan, "copy");
    writer.write("}\n");

    // MIXED CONSTRUCTOR
    if (plan.getNativeItemClassNameInterface() != null) {
      if (!plan.hasEnumerations()) {
        writer.write(plan.getDocumentation());
        writer.write("public ");
      }
      else
        writer.write("protected ");
      writer.write(plan.getClassSimpleName() + "(final " + plan.getNativeItemClassNameInterface() + " text)\n");
      writer.write("{\n");
      writer.write("super(text);\n");
      writer.write("}\n");
    }
    else if (plan.getMixedType()) {
      writer.write(plan.getDocumentation());
      writer.write("public " + plan.getClassSimpleName() + "(final " + String.class.getName() + " text)\n");
      writer.write("{\n");
      writer.write("super(text);\n");
      writer.write("}\n");
    }
    else if (plan.getMixed() != null && plan.getMixed()) {
      writer.write(plan.getDocumentation());
      writer.write("public " + plan.getClassSimpleName() + "(final " + String.class.getName() + " text)\n");
      writer.write("{\n");
      writer.write("this.text = text;\n");
      writer.write("}\n");
    }

    // DEFAULT CONSTRUCTOR
    if (plan.hasEnumerations())
      writer.write("protected ");
    else {
      writer.write(plan.getDocumentation());
      writer.write("public ");
    }
    writer.write(plan.getClassSimpleName() + "()\n");
    writer.write("{\n");
    writer.write("super();\n");
    for (final Object attribute : plan.getAttributes())
      writer.write(((AttributePlan)attribute).getFixedInstanceCall(plan));
    writer.write("}\n");

    if (plan.hasSimpleContent() && plan.getNativeItemClassNameInterface() != null) {
      if (plan.getNativeItemClassName() == null && XSTypeDirectory.ANYSIMPLETYPE.getNativeBinding().getName().equals(plan.getBaseXSItemTypeName())) {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public " + BindingList.class.getName() + "<" + plan.getNativeItemClassNameInterface() + "> text()\n");
        writer.write("{\n");
        writer.write("return (" + BindingList.class.getName() + "<" + plan.getNativeItemClassNameInterface() + ">)super.text();\n");
        writer.write("}\n");

        writer.write("public " + plan.getNativeItemClassNameInterface() + " text(final int index)\n");
        writer.write("{\n");
        writer.write("final " + List.class.getName() + "<" + plan.getNativeItemClassNameInterface() + "> values = text();\n");
        writer.write("return values != null && -1 < index && index < values.size() ? values.get(index) : null;\n");
        writer.write("}\n");

        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public void text(final " + List.class.getName() + "<" + plan.getNativeItemClassNameInterface() + "> text)\n");
        writer.write("{\n");
        writer.write("super.text(text);\n");
        writer.write("}\n");
      }
      else {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public " + plan.getNativeItemClassNameInterface() + " text()\n");
        writer.write("{\n");
        if (!Object.class.getName().equals(plan.getNativeItemClassNameInterface()))
          writer.write("return (" + plan.getNativeItemClassNameInterface() + ")super.text();\n");
        else
          writer.write("return super.text();\n");
        writer.write("}\n");

        if (plan.hasEnumerations()) {
          writer.write("public void text(final Enum enm)\n");
          writer.write("{\n");
          writer.write("super.text(enm.text());\n");
          writer.write("}\n");
        }
        else {
          writer.write("@" + Override.class.getName() + "\n");
          writer.write("public void text(final " + plan.getNativeItemClassNameInterface() + " text)\n");
          writer.write("{\n");
          writer.write("super.text(text);\n");
          writer.write("}\n");
        }
      }
    }
    else if (plan.getMixed() != null && plan.getMixed()) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + String.class.getName() + " text()\n");
      writer.write("{\n");
      writer.write("return text;\n");
      writer.write("}\n");
      writer.write("public void text(final " + String.class.getName() + " text)\n");
      writer.write("{\n");
      writer.write("if (isNull())\n");
      writer.write("throw new " + BindingRuntimeException.class.getName() + "(\"NULL Object is immutable.\");\n");
      writer.write("this.text = text;\n");
      writer.write("}\n");
    }
    else if (plan.getMixedType()) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + String.class.getName() + " text()\n");
      writer.write("{\n");
      writer.write("return (" + String.class.getName() + ")super.text();\n");
      writer.write("}\n");
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public void text(" + String.class.getName() + " text)\n");
      writer.write("{\n");
      writer.write("super.text(text);\n");
      writer.write("}\n");
    }

    for (final Object attribute : plan.getAttributes()) {
      Writer.writeSetMethod(writer, (AttributePlan)attribute, plan);
      Writer.writeGetMethod(writer, (AttributePlan)attribute, plan);
    }

    for (final Object element : plan.getElements()) {
      Writer.writeSetMethod(writer, (ElementPlan)element, plan);
      Writer.writeGetMethod(writer, (ElementPlan)element, plan);
    }

    // INHERITS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected abstract " + plan.getBaseNonXSTypeClassName() + " inherits();\n");

    // GETNAME
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + QName.class.getName() + " name()\n");
    writer.write("{\n");
    writer.write("return name(_$$inheritsInstance());\n");
    writer.write("}\n");

    // GETTYPE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + QName.class.getName() + " typeName()\n");
    writer.write("{\n");
    writer.write("return NAME;\n");
    writer.write("}\n");

    // ELEMENT ITERATORS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + Iterator.class.getName() + "<" + Binding.class.getName() + "> elementIterator()\n");
    writer.write("{\n");
    writer.write("return super.elementIterator();\n");
    writer.write("}\n");

    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + BindingList.class.getName() + "<? extends " + Binding.class.getName() + "> fetchChild(final " + QName.class.getName() + " name)\n");
    writer.write("{\n");
    writer.write("return super.fetchChild(name);\n");
    writer.write("}\n");

//  writer.write("public " + ListIterator.class.getName() + "<" + Binding.class.getName() + "> elementListIterator()\n");
//  writer.write("{\n");
//  writer.write("return super.elementListIterator();\n");
//  writer.write("}\n");

//  writer.write("public " + ListIterator.class.getName() + "<" + Binding.class.getName() + "> elementListIterator(final int index)\n");
//  writer.write("{\n");
//  writer.write("return super.elementListIterator(index);\n");
//  writer.write("}\n");

    // MARSHAL
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + Element.class.getName() + " marshal() throws " + MarshalException.class.getName() + ", " + ValidationException.class.getName() + "\n");
    writer.write("{\n");
    writer.write(Element.class.getName() + " root = createElementNS(name().getNamespaceURI(), name().getLocalPart());\n");
    writer.write(Element.class.getName() + " node = marshal(root, name(), typeName(_$$inheritsInstance()));\n");
    if (plan.getElements().size() != 0)
      writer.write("_$$marshalElements(node);\n");
    writer.write("if (" + Validator.class.getName() + ".getSystemValidator() != null)\n");
    writer.write(Validator.class.getName() + ".getSystemValidator().validateMarshal(node);\n");
    writer.write("return node;\n");
    writer.write("}\n");

    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + Element.class.getName() + " marshal(" + Element.class.getName() + " parent, " + QName.class.getName() + " name, " + QName.class.getName() + " typeName) throws " + MarshalException.class.getName() + "\n");
    writer.write("{\n");
    if (plan.getElements() != null || plan.getAttributes() != null || plan.getMixed()) {
      writer.write(Element.class.getName() + " node = super.marshal(parent, name, typeName);\n");
      if (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed()) {
        writer.write("if (text != null)\n");
        writer.write("node.appendChild(node.getOwnerDocument().createTextNode(text.toString()));\n");
      }
      for (final Object attribute : plan.getAttributes())
        Writer.writeMarshal(writer, (AttributePlan)attribute, plan);

      writer.write("return node;\n");
    }
    else
      writer.write("return super.marshal(parent, name, typeName);\n");

    writer.write("}\n");

    // PARSE ATTRIBUTE
    if (plan.getAttributes() != null) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected boolean parseAttribute(" + Attr.class.getName() + " attribute) throws " + ParseException.class.getName() + ", " + ValidationException.class.getName() + "\n");
      writer.write("{\n");
      writer.write("if (attribute == null || XMLNS.getLocalPart().equals(attribute.getPrefix()))\n");
      writer.write("{\n");
      writer.write("return true;\n");
      writer.write("}\n");
      AttributePlan any = null;
      for (final Object attribute : plan.getAttributes()) {
        if (attribute instanceof AnyAttributePlan)
          any = (AttributePlan)attribute;
        else
          Writer.writeParse(writer, (AttributePlan)attribute, plan);
      }

      writer.write("return super.parseAttribute(attribute);\n");
      writer.write("}\n");

      if (any != null) {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("protected void parseAnyAttribute(" + Attr.class.getName() + " attribute) throws " + ParseException.class.getName() + ", " + ValidationException.class.getName() + "\n");
        writer.write("{\n");
        Writer.writeParse(writer, any, plan);
        writer.write("}\n");
      }
    }

    // PARSE ELEMENT
    if (plan.getElements() != null || (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed())) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected boolean parseElement(" + Element.class.getName() + " element) throws " + ParseException.class.getName() + ", " + ValidationException.class.getName() + "\n");
      writer.write("{\n");
      if (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed()) {
        writer.write("if (element.getNodeType() == " + Node.class.getName() + ".TEXT_NODE)\n");
        writer.write("{\n");
        writer.write("if (element.getNodeValue().length() != 0)\n");
        writer.write("{\n");
        writer.write(String.class.getName() + " value = element.getNodeValue().trim();\n");
        writer.write("if (text == null && value.length() != 0)\n");
        writer.write("text = new " + String.class.getName() + "(value);\n");
        writer.write("}\n");
        writer.write("return true;\n");
        writer.write("}\n");
      }

      ElementPlan any = null;
      for (final Object element : plan.getElements()) {
        if (element instanceof AnyPlan)
          any = (ElementPlan)element;
        else
          Writer.writeParse(writer, (ElementPlan)element, plan);
      }

      writer.write("return super.parseElement(element);\n");
      writer.write("}\n");

      if (any != null) {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("protected void parseAny(" + Element.class.getName() + " element) throws " + ParseException.class.getName() + ", " + ValidationException.class.getName() + "\n");
        writer.write("{\n");
        Writer.writeParse(writer, any, plan);
        writer.write("}\n");
      }
    }

    // IS_NULL
    //writeIsNull(writer, plan);

    // CLONE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + plan.getClassName(parent) + " clone()\n");
    writer.write("{\n");
    writer.write("return " + plan.getClassName(parent) + ".newInstance(this);\n");
    writer.write("}\n");

    // EQUALS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public boolean equals(" + Object.class.getName() + " obj)\n");
    writer.write("{\n");
    writer.write("if (this == obj)\n");
    writer.write("return true;\n");
    writer.write("if (!(obj instanceof " + plan.getClassName(parent) + "))\n");
    writer.write("return _$$failEquals();\n");
    if ((plan.getAttributes() != null && plan.getAttributes().size() != 0) || (plan.getNativeItemClassNameInterface() == null && plan.getElements() != null && plan.getElements().size() != 0) || (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed())) {
      writer.write("final " + plan.getClassName(parent) + " that = (" + plan.getClassName(parent) + ")obj;\n");
      if (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed()) {
        writer.write("if (text != null ? !text.equals(that.text) : that.text != null)\n");
        writer.write("return _$$failEquals();\n");
      }
      for (final Object attribute : plan.getAttributes())
        Writer.writeEquals(writer, (AttributePlan)attribute, plan);
      for (final Object element : plan.getElements())
        Writer.writeEquals(writer, (ElementPlan)element, plan);
    }
    writer.write("return super.equals(obj);\n");
    writer.write("}\n");

    // HASHCODE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public int hashCode()\n");
    writer.write("{\n");
    writer.write("int hashCode = super.hashCode();\n");
    writer.write("hashCode += NAME.hashCode();\n");
    for (final Object attribute : plan.getAttributes())
      Writer.writeHashCode(writer, (AttributePlan)attribute, plan);
    for (final Object element : plan.getElements())
      Writer.writeHashCode(writer, (ElementPlan)element, plan);
    writer.write("return hashCode;\n");
    writer.write("}\n");

    // ATTRIBUTES
    for (final Object attribute : plan.getAttributes())
      Writer.writeClass(writer, (AttributePlan)attribute, plan);

    // ELEMENTS
    for (final Object element : plan.getElements())
      Writer.writeClass(writer, (ElementPlan)element, plan);

    writer.write("}\n");
  }
}