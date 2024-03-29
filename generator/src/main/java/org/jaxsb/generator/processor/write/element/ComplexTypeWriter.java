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
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.generator.processor.plan.element.AnyAttributePlan;
import org.jaxsb.generator.processor.plan.element.AnyPlan;
import org.jaxsb.generator.processor.plan.element.AttributePlan;
import org.jaxsb.generator.processor.plan.element.ComplexTypePlan;
import org.jaxsb.generator.processor.plan.element.ElementPlan;
import org.jaxsb.generator.processor.write.Writer;
import org.jaxsb.runtime.BindingList;
import org.jaxsb.runtime.CompilerFailureException;
import org.jaxsb.runtime.ComplexType;
import org.jaxsb.runtime.MarshalException;
import org.jaxsb.runtime.XSTypeDirectory;
import org.openjax.xml.api.ValidationException;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
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
  public void appendCopy(final StringWriter writer, final T plan, final Plan<?> parent, final String variable) {
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
  protected void appendClone(final StringWriter writer, final T plan, final Plan<?> parent) {
    throw new CompilerFailureException("complexType cannot have a clone statement");
  }

  @Override
  protected void appendClass(final StringWriter writer, final T plan, final Plan<?> parent) {
    writeQualifiedName(writer, plan);
    writer.write("public abstract static class " + plan.getClassSimpleName() + " extends " + plan.getSuperClassNameWithGenericType() + " implements " + ComplexType.class.getName() + " {\n");
    writer.write("private static final " + QName.class.getName() + " NAME = new " + QName.class.getName() + "(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\", \"" + plan.getName().getPrefix() + "\");\n");

    // FACTORY METHOD
    writer.write("protected static " + plan.getClassSimpleName() + " newInstance(final " + plan.getBaseNonXSTypeClassName() + " inherits) {\n");
    writer.write("return new " + plan.getClassName(parent) + "() {\n");
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + plan.getBaseNonXSTypeClassName() + " inherits() {\n");
    writer.write("return inherits;\n");
    writer.write("}\n");
    writer.write("};\n");
    writer.write("}\n");

    // ID LOOKUP
    writeIdLookup(writer, plan, parent);

    for (final Object attribute : plan.getAttributes()) // [S]
      Writer.writeDeclaration(writer, (AttributePlan)attribute, plan);

    for (final Object element : plan.getElements()) // [S]
      Writer.writeDeclaration(writer, (ElementPlan)element, plan);

    // ENUMERATIONS CONSTRUCTOR
    getRestrictions(writer, plan);

    // COPY CONSTRUCTOR
    writer.write(plan.getDocumentation());
    writer.write("protected " + plan.getClassSimpleName() + "(final " + plan.getClassName(null) + " copy) {\n");
    writer.write("super(copy);\n");
    for (final Object attribute : plan.getAttributes()) // [S]
      Writer.writeCopy(writer, (AttributePlan)attribute, plan, "copy");
    for (final Object element : plan.getElements()) // [S]
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
      writer.write(plan.getClassSimpleName() + "(final " + plan.getNativeItemClassNameInterface() + " text) {\n");
      writer.write("super(text);\n");
      writer.write("}\n");
    }
    else if (plan.getMixedType() || plan.getMixed() != null && plan.getMixed()) {
      writer.write(plan.getDocumentation());
      writer.write("public " + plan.getClassSimpleName() + "(final " + String.class.getName() + " text) {\n");
      writer.write("super(text);\n");
      writer.write("}\n");
    }

    // DEFAULT CONSTRUCTOR
    if (plan.hasEnumerations())
      writer.write("protected ");
    else {
      writer.write(plan.getDocumentation());
      writer.write("public ");
    }
    writer.write(plan.getClassSimpleName() + "() {\n");
    writer.write("super();\n");
    for (final Object attribute : plan.getAttributes()) // [S]
      writer.write(((AttributePlan)attribute).getFixedInstanceCall(plan));
    writer.write("}\n");

    if (plan.hasSimpleContent() && plan.getNativeItemClassNameInterface() != null) {
      if (plan.getNativeItemClassName() == null && XSTypeDirectory.ANYSIMPLETYPE.getNativeBinding().getName().equals(plan.getBaseXSItemTypeName())) {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public " + List.class.getName() + "<" + plan.getNativeItemClassNameInterface() + "> text() {\n");
        writer.write("return (" + List.class.getName() + "<" + plan.getNativeItemClassNameInterface() + ">)super.text();\n");
        writer.write("}\n");

        writer.write("public " + plan.getNativeItemClassNameInterface() + " text(final int index) {\n");
        writer.write("final " + List.class.getName() + "<" + plan.getNativeItemClassNameInterface() + "> values = text();\n");
        writer.write("return values != null && -1 < index && index < values.size() ? values.get(index) : null;\n");
        writer.write("}\n");

        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public void text(final " + plan.getNativeItemClassNameInterface() + " text) {\n");
        writer.write("super.text(text);\n");
        writer.write("}\n");
      }
      else {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public " + plan.getNativeItemClassNameInterface() + " text() {\n");
        if (!Object.class.getName().equals(plan.getNativeItemClassNameInterface()))
          writer.write("return (" + plan.getNativeItemClassNameInterface() + ")super.text();\n");
        else
          writer.write("return super.text();\n");
        writer.write("}\n");

        if (plan.hasEnumerations()) {
          writer.write("public void text(final Enum enm) {\n");
          writer.write("super.text(enm.text());\n");
          writer.write("}\n");
        }
        else {
          writer.write("@" + Override.class.getName() + "\n");
          writer.write("public void text(final " + plan.getNativeItemClassNameInterface() + " text) {\n");
          writer.write("super.text(text);\n");
          writer.write("}\n");
        }
      }
    }
    else if (plan.getMixedType() || plan.getMixed() != null && plan.getMixed()) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + String.class.getName() + " text() {\n");
      writer.write("return super.text();\n");
      writer.write("}\n");
      writer.write("public void text(final " + String.class.getName() + " text) {\n");
      writer.write("super.text(text);\n");
      writer.write("}\n");
    }

    for (final Object attribute : plan.getAttributes()) { // [S]
      Writer.writeSetMethod(writer, (AttributePlan)attribute, plan);
      Writer.writeGetMethod(writer, (AttributePlan)attribute, plan);
    }

    for (final Object element : plan.getElements()) { // [S]
      Writer.writeSetMethod(writer, (ElementPlan)element, plan);
      Writer.writeGetMethod(writer, (ElementPlan)element, plan);
    }

    // INHERITS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected abstract " + plan.getBaseNonXSTypeClassName() + " inherits();\n");

    // ID
    if (plan.getId() != null) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + String.class.getName() + " id() {\n");
      writer.write("return \"" + plan.getId() + "\";\n");
      writer.write("}\n");
    }

    // TYPE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + QName.class.getName() + " type() {\n");
    writer.write("return NAME;\n");
    writer.write("}\n");

    // ATTRIBUTE & ELEMENT ITERATORS
    if ($AnyType.class.getCanonicalName().equals(plan.getSuperClassNameWithoutGenericType()) || $AnySimpleType.class.getCanonicalName().equals(plan.getSuperClassNameWithoutGenericType())) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + Iterator.class.getName() + "<" + $AnySimpleType.class.getCanonicalName() + "<?>> attributeIterator() {\n");
      writer.write("return super.attributeIterator();\n");
      writer.write("}\n");

      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + Iterator.class.getName() + "<" + $AnyType.class.getCanonicalName() + "<?>> elementIterator() {\n");
      writer.write("return super.elementIterator();\n");
      writer.write("}\n");

      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + BindingList.class.getName() + "<" + $AnyType.class.getCanonicalName() + "<?>> fetchChild(final " + QName.class.getName() + " name) {\n");
      writer.write("return super.fetchChild(name);\n");
      writer.write("}\n");
    }

    // writer.write("public " + ListIterator.class.getName() + "<" + $AnyType.class.getCanonicalName() + "<?>>
    // elementListIterator()\n");
    // writer.write("{\n");
    // writer.write("return super.elementListIterator();\n");
    // writer.write("}\n");

    // writer.write("public " + ListIterator.class.getName() + "<" + $AnyType.class.getCanonicalName() + "<?>> elementListIterator(final
    // int index)\n");
    // writer.write("{\n");
    // writer.write("return super.elementListIterator(index);\n");
    // writer.write("}\n");

    // MARSHAL
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + Element.class.getName() + " marshal() throws " + MarshalException.class.getName() + " {\n");
    writer.write("final " + Element.class.getName() + " node = marshal(createElementNS(name().getNamespaceURI(), name().getLocalPart()), name(), type(_$$inheritsInstance()));\n");
    if (plan.getElements().size() != 0)
      writer.write("_$$marshalElements(node);\n");
    writer.write("return node;\n");
    writer.write("}\n");

    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + Element.class.getName() + " marshal(final " + Element.class.getName() + " parent, final " + QName.class.getName() + " name, final " + QName.class.getName() + " type) throws " + MarshalException.class.getName() + " {\n");
    if (plan.getElements() != null || plan.getAttributes() != null || plan.getMixed()) {
      writer.write(Element.class.getName() + " node = super.marshal(parent, name, type);\n");
      if (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed()) {
        writer.write("if (text != null)\n");
        writer.write("node.appendChild(node.getOwnerDocument().createTextNode(text.toString()));\n");
      }
      for (final Object attribute : plan.getAttributes()) // [S]
        Writer.writeMarshal(writer, (AttributePlan)attribute, plan);

      writer.write("return node;\n");
    }
    else {
      writer.write("return super.marshal(parent, name, type);\n");
    }

    writer.write("}\n");

    // PARSE ATTRIBUTE
    if (plan.getAttributes() != null) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected boolean parseAttribute(final " + Attr.class.getName() + " attribute) {\n");
      writer.write("if (attribute == null || XMLNS.getLocalPart().equals(attribute.getPrefix())) {\n");
      writer.write("return true;\n");
      writer.write("}\n");
      AttributePlan any = null;
      for (final Object attribute : plan.getAttributes()) { // [S]
        if (attribute instanceof AnyAttributePlan)
          any = (AttributePlan)attribute;
        else
          Writer.writeParse(writer, (AttributePlan)attribute, plan);
      }

      writer.write("return super.parseAttribute(attribute);\n");
      writer.write("}\n");

      if (any != null) {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("protected void parseAnyAttribute(final " + Attr.class.getName() + " attribute) {\n");
        Writer.writeParse(writer, any, plan);
        writer.write("}\n");
      }
    }

    // PARSE ELEMENT
    if (plan.getElements() != null || (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed())) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected boolean parseElement(final " + Element.class.getName() + " element) throws " + ValidationException.class.getName() + " {\n");
      if (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed()) {
        writer.write("if (element.getNodeType() == " + Node.class.getName() + ".TEXT_NODE) {\n");
        writer.write("if (element.getNodeValue().length() != 0) {\n");
        writer.write(String.class.getName() + " value = element.getNodeValue().trim();\n");
        writer.write("if (text == null && value.length() != 0)\n");
        writer.write("text = new " + String.class.getName() + "(value);\n");
        writer.write("}\n");
        writer.write("return true;\n");
        writer.write("}\n");
      }

      ElementPlan any = null;
      for (final Object element : plan.getElements()) { // [S]
        if (element instanceof AnyPlan)
          any = (ElementPlan)element;
        else
          Writer.writeParse(writer, (ElementPlan)element, plan);
      }

      writer.write("return super.parseElement(element);\n");
      writer.write("}\n");

      if (any != null) {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("protected void parseAny(final " + Element.class.getName() + " element) throws " + ValidationException.class.getName() + " {\n");
        Writer.writeParse(writer, any, plan);
        writer.write("}\n");
      }
    }

    // CLONE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + plan.getClassName(parent) + " clone() {\n");
    writer.write("final " + plan.getClassName(parent) + " clone = (" + plan.getClassName(parent) + ")super.clone();\n");
    if (plan.getNativeItemClassNameInterface() == null && plan.getMixed() != null && plan.getMixed())
      writer.write("clone.text = text;");
    for (final AttributePlan attribute : plan.getAttributes()) // [S]
      Writer.writeClone(writer, attribute, plan);
    for (final ElementPlan element : plan.getElements()) // [S]
      Writer.writeClone(writer, element, plan);
    writer.write("return clone;\n");
    writer.write("}\n");

    // EQUALS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public boolean equals(final " + Object.class.getName() + " obj) {\n");
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
      for (final AttributePlan attribute : plan.getAttributes()) // [S]
        Writer.writeEquals(writer, attribute, plan);
      for (final ElementPlan element : plan.getElements()) // [S]
        Writer.writeEquals(writer, element, plan);
    }
    writer.write("return super.equals(obj);\n");
    writer.write("}\n");

    // HASHCODE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public int hashCode() {\n");
    writer.write("int hashCode = super.hashCode();\n");
    for (final Object attribute : plan.getAttributes()) // [S]
      Writer.writeHashCode(writer, (AttributePlan)attribute, plan);
    for (final Object element : plan.getElements()) // [S]
      Writer.writeHashCode(writer, (ElementPlan)element, plan);
    writer.write("return hashCode;\n");
    writer.write("}\n");

    // ATTRIBUTES
    for (final Object attribute : plan.getAttributes()) // [S]
      Writer.writeClass(writer, (AttributePlan)attribute, plan);

    // ELEMENTS
    for (final Object element : plan.getElements()) // [S]
      Writer.writeClass(writer, (ElementPlan)element, plan);

    writer.write("}\n");
  }
}