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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.jaxsb.compiler.schema.attribute.Form;
import org.jaxsb.generator.processor.plan.EnumerablePlan;
import org.jaxsb.generator.processor.plan.ExtensiblePlan;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.generator.processor.plan.element.AnyAttributePlan;
import org.jaxsb.generator.processor.plan.element.AnyPlan;
import org.jaxsb.generator.processor.plan.element.AttributePlan;
import org.jaxsb.generator.processor.plan.element.ElementPlan;
import org.jaxsb.generator.processor.plan.element.SimpleTypePlan;
import org.jaxsb.generator.processor.write.Writer;
import org.jaxsb.runtime.Binding;
import org.jaxsb.runtime.BindingList;
import org.jaxsb.runtime.ComplexType;
import org.jaxsb.runtime.ElementAudit;
import org.jaxsb.runtime.ElementSpec;
import org.jaxsb.runtime.MarshalException;
import org.jaxsb.runtime.SimpleType;
import org.openjax.xml.api.ValidationException;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.w3.www._2001.XMLSchema.yAA.$Boolean;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ElementWriter<T extends ElementPlan> extends ComplexTypeWriter<T> {
  protected static void writeNilMarshal(final StringWriter writer) {
    writer.write("if (nil != null && !node.hasAttributeNS(XSI_NIL.getNamespaceURI(), XSI_NIL.getLocalPart())) {\n");
    writer.write("node.setAttributeNS(XSI_NIL.getNamespaceURI(), XSI_NIL.getPrefix() + \":\" + XSI_NIL.getLocalPart(), " + String.class.getName() + ".valueOf(nil));\n");
    writer.write("if (!parent.getOwnerDocument().getDocumentElement().hasAttributeNS(XMLNS.getNamespaceURI(), XSI_NIL.getPrefix()))\n");
    writer.write("parent.getOwnerDocument().getDocumentElement().setAttributeNS(XMLNS.getNamespaceURI(), XMLNS.getLocalPart() + \":\" + XSI_NIL.getPrefix(), XSI_NIL.getNamespaceURI());\n");
    writer.write("}\n");
  }

  @Override
  protected void appendRegistration(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (!plan.isNested()) {
      writer.write("_$$registerElement(" + plan.getClassName(parent) + ".NAME, " + plan.getClassName(parent) + ".class);\n");
      writer.write("_$$registerSchemaLocation(" + plan.getClassName(parent) + ".NAME.getNamespaceURI(), " + plan.getClassName(null) + ".class, \"" + plan.getXsdLocation() + "\");\n");
    }
  }

  @Override
  protected void appendDeclaration(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (plan.isRestriction() || plan.getRepeatedExtension() != null)
      return;

    final String className = plan.getDeclarationGenericWithInconvertible(parent);
    writer.write("private " + ElementAudit.class.getName() + "<" + className + "> " + plan.getInstanceName() + " = new " + ElementAudit.class.getName() + "<>(" + className + ".class, this, " + plan.getDefaultInstance(parent) + ", new " + QName.class.getName() + "(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\", \"" + plan.getName().getPrefix() + "\"), new " + QName.class.getName() + "(\"" + plan.getTypeName().getNamespaceURI() + "\", \"" + plan.getTypeName().getLocalPart() + "\", \"" + plan.getName().getPrefix() + "\"), " + (!plan.isNested() || Form.QUALIFIED.equals(plan.getFormDefault())) + ", " + plan.isNillable() + ", " + plan.getMinOccurs() + ", " + plan.getMaxOccurs() + ");\n");
  }

  @Override
  protected void appendGetMethod(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (plan.getRepeatedExtension() != null)
      return;

    final String methodName = plan.getMethodName();
    if (plan.getMaxOccurs() == 1) {
      writer.write("public " + plan.getDeclarationRestrictionGeneric(parent) + " get" + methodName + "()\n{\n");
      if (plan.isRestriction())
        writer.write("return super.get" + plan.getClassSimpleName() + "();\n");
      else
        writer.write("return " + plan.getInstanceName() + ".getElement();\n");
      writer.write("}\n");
    }
    else {
      writer.write("public " + BindingList.class.getName() + "<" + plan.getDeclarationRestrictionGeneric(parent) + "> get" + methodName + "()\n{\n");
      if (plan.isRestriction())
        writer.write("return super.get" + plan.getClassSimpleName() + "();\n");
      else
        writer.write("return " + plan.getInstanceName() + ".getElements();\n");
      writer.write("}\n");

      writer.write("public " + plan.getDeclarationRestrictionGeneric(parent) + " get" + methodName + "(final int index)\n{\n");
      writer.write("final " + BindingList.class.getName() + "<" + plan.getDeclarationRestrictionGeneric(parent) + "> values = get" + plan.getMethodName() + "();\n");
      writer.write("return values == null || index < 0 || values.size() <= index ? null : values.get(index);\n");
      writer.write("}\n");
    }
  }

  @Override
  protected void appendSetMethod(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (plan.getRepeatedExtension() != null)
      return;

    writer.write("@" + ElementSpec.class.getName() + "(minOccurs=" + plan.getMinOccurs() + ", maxOccurs=" + plan.getMaxOccurs() + ")\n");
    final String type = plan.getDeclarationGeneric(parent);
    final String methodName = plan.getMethodName();
    final String setAdd = plan.getMaxOccurs() > 1 ? "add" : "set";
    writer.write("public " + type + " " + setAdd + methodName + "(final " + type + " " + plan.getInstanceName() + ") {\n");
    if (plan.isRestriction())
      writer.write("super." + setAdd + methodName + "(" + plan.getInstanceName() + ");\n");
    else
      writer.write("_$$addElement(this." + plan.getInstanceName() + ", " + plan.getInstanceName() + ");\n");
    writer.write("return " + plan.getInstanceName() + ";\n");
    writer.write("}\n");
  }

  @Override
  protected void appendMarshal(final StringWriter writer, final T plan, final Plan<?> parent) {
  }

  @Override
  protected void appendParse(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (plan.isRestriction() || plan.getRepeatedExtension() != null)
      return;

    if (!plan.isNested() || Form.QUALIFIED.equals(plan.getFormDefault())) {
      if (plan.getName().getNamespaceURI().toString().isEmpty())
        writer.write("if (element.getNamespaceURI() == null && \"" + plan.getName().getLocalPart() + "\".equals(element.getLocalName()))\n");
      else
        writer.write("if (\"" + plan.getName().getNamespaceURI() + "\".equals(element.getNamespaceURI()) && \"" + plan.getName().getLocalPart() + "\".equals(element.getLocalName()))\n");

      writer.write("{\n");
      writer.write("return _$$addElement(this." + plan.getInstanceName() + ", (" + plan.getDeclarationGeneric(parent) + ")" + Binding.class.getName() + ".parse(element, " + plan.getClassName(parent) + ".class));\n");
      writer.write("}\n");
      writer.write("if (" + Binding.class.getName() + "._$$iSsubstitutionGroup(new " + QName.class.getName() + "(element.getNamespaceURI(), element.getLocalName()), \"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\")) {\n");
      writer.write("return _$$addElement(this." + plan.getInstanceName() + ", (" + plan.getDeclarationGeneric(parent) + ")" + Binding.class.getName() + ".parse(element));\n");
    }
    else {
      writer.write("if (\"" + plan.getName().getLocalPart() + "\".equals(element.getLocalName())) {\n");
      writer.write("return _$$addElement(this." + plan.getInstanceName() + ", (" + plan.getDeclarationGeneric(parent) + ")" + Binding.class.getName() + ".parse(element, " + plan.getClassName(parent) + ".class));\n");
    }
    writer.write("}\n");
  }

  @Override
  public void appendCopy(final StringWriter writer, final T plan, final Plan<?> parent, final String variable) {
    if (plan.isRestriction() || plan.getRepeatedExtension() != null)
      return;

    writer.write("this." + plan.getInstanceName() + " = " + variable + "." + plan.getInstanceName() + ";\n");
  }

  @Override
  protected void appendEquals(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (plan.isRestriction() || plan.getRepeatedExtension() != null)
      return;

    writer.write("if (" + plan.getInstanceName() + " != null ? !" + plan.getInstanceName() + ".equals(that." + plan.getInstanceName() + ") : that." + plan.getInstanceName() + " != null)\n");
    writer.write("return _$$failEquals();\n");
  }

  @Override
  protected void appendHashCode(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (plan.isRestriction() || plan.getRepeatedExtension() != null)
      return;

    writer.write("if (" + plan.getInstanceName() + " != null)\n");
    writer.write("hashCode = 31 * hashCode + " + plan.getInstanceName() + ".hashCode();\n");
  }

  @Override
  protected void appendClone(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (plan.isRestriction() || plan.getRepeatedExtension() != null)
      return;

    writer.write("clone." + plan.getInstanceName() + " = " + plan.getInstanceName() + " == null ? null : clone.getAudit(" + plan.getInstanceName() + ");\n");
  }

  @Override
  protected void appendClass(final StringWriter writer, final T plan, final Plan<?> parent) {
    if (plan.isRef() || plan.getRepeatedExtension() != null)
      return;

    writeQualifiedName(writer, plan);
    writer.write("public static ");
    if (plan.isAbstract())
      writer.write("abstract ");

    writer.write("class " + plan.getClassSimpleName() + " extends " + plan.getSuperClassNameWithGenericType() + " implements " + (plan.isComplexType() ? ComplexType.class.getName() : SimpleType.class.getName()) + ", " + org.jaxsb.runtime.Element.class.getName() + " {\n");

    writer.write("private static final " + QName.class.getName() + " NAME = new " + QName.class.getName() + "(\"" + plan.getName().getNamespaceURI() + "\", \"" + plan.getName().getLocalPart() + "\", \"" + plan.getName().getPrefix() + "\");\n");

    // SUBSTITUTION GROUP
    if (plan.getSubstitutionGroup() != null)
      writer.write("private static final " + QName.class.getName() + " SUBSTITUTION_GROUP = new " + QName.class.getName() + "(\"" + plan.getSubstitutionGroup().getNamespaceURI() + "\", \"" + plan.getSubstitutionGroup().getLocalPart() + "\");\n");

    // ID LOOKUP
    writeIdLookup(writer, plan, parent);

    if (plan.getMixed() != null && plan.getMixed())
      writer.write("private " + String.class.getName() + " text = null;\n");

    if (plan.isNillable())
      writer.write("private " + Boolean.class.getName() + " nil = null;\n");

    for (final AttributePlan attribute : plan.getAttributes()) // [S]
      Writer.writeDeclaration(writer, attribute, plan);

    for (final ElementPlan element : plan.getElements()) // [S]
      Writer.writeDeclaration(writer, element, plan);

    // ENUMERATIONS CONSTRUCTOR
    getRestrictions(writer, plan);

    // COPY CONSTRUCTOR
    writer.write(plan.getDocumentation());
    writer.write("protected " + plan.getClassSimpleName() + "(final " + (plan.hasEnumerations() ? plan.getClassName(parent) : plan.getCopyClassName(parent)) + " copy) {\n");
    writer.write("super(copy);\n");
    if ((plan.getMixed() != null && plan.getMixed()) || plan.getAttributes().size() != 0 || plan.getElements().size() != 0) {
      if (!plan.getCopyClassName(parent).equals(plan.getClassName(parent))) {
        writer.write("if (!(copy instanceof " + plan.getClassName(parent) + "))\n");
        writer.write("return;\n");
        writer.write(plan.getClassName(parent) + " binding = (" + plan.getClassName(parent) + ")copy;\n");
      }
      else {
        writer.write(plan.getClassName(parent) + " binding = copy;\n");
      }

      if (plan.getMixed() != null && plan.getMixed())
        writer.write("this.text = binding.text;\n");
      for (final AttributePlan attribute : plan.getAttributes()) // [S]
        Writer.writeCopy(writer, attribute, plan, "binding");
      for (final ElementPlan element : plan.getElements()) // [S]
        Writer.writeCopy(writer, element, plan, "binding");
    }
    writer.write("}\n");

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

    // MIXED CONSTRUCTOR
    if (!plan.isComplexType() || (plan.getMixed() == null && plan.getMixedType()) || (plan.getMixed() != null && plan.getMixed())) {
      if (plan.getMixedType() || plan.getMixed() != null && plan.getMixed()) {
        if (!String.class.getName().equals(plan.getNativeNonEnumItemClassNameInterface())) {
          writer.write("public " + plan.getClassSimpleName() + "(final " + String.class.getName() + " text) {\n");
          writer.write("super(text);\n");
          writer.write("}\n");
        }

        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public " + String.class.getName() + " text() {\n");
        writer.write("return (" + String.class.getName() + ")super.text();\n");
        writer.write("}\n");

        if (parent != null && ((SimpleTypePlan<?>)parent).getNativeItemClassNameInterface().equals(plan.getNativeItemClassNameInterface()))
          writer.write("@" + Override.class.getName() + "\n");

        writer.write("public void text(final " + String.class.getName() + " text) {\n");
        writer.write("super.text(text);\n");
        writer.write("}\n");
      }
      else if (plan.getNativeItemClassNameInterface() != null) {
        if (plan.hasEnumerations()) {
          final boolean hasSuperEnumerations = ((EnumerablePlan)plan).hasSuperEnumerations();
          final String enumClassName;
          if (hasSuperEnumerations)
            enumClassName = ((ExtensiblePlan)plan).getSuperClassNameWithoutGenericType() + ".Enum";
          else
            enumClassName = "Enum";

          if (plan.isList()) {
            writer.write("public <E extends " + List.class.getName() + "<" + enumClassName + ">>void text(final E enm) {\n");
            writer.write("super.text(new " + plan.getNativeItemClassNameImplementation() + "());\n");
            writer.write("if (enm instanceof " + RandomAccess.class.getName() + ") {\n");
            writer.write("for (int i = 0, i$ = enm.size(); i < i$; ++i) { // [RA]\n");
            writer.write("final " + enumClassName + " member = enm.get(i);\n");
            writer.write("if (member != null)\n");
            writer.write("((" + List.class.getName() + "<" + plan.getNativeItemClassName() + ">)super.text()).add(member.text);\n");
            writer.write("}\n");
            writer.write("}\n");
            writer.write("else {\n");
            writer.write("for (final " + enumClassName + " member : enm) // [L]\n");
            writer.write("if (member != null)\n");
            writer.write("((" + List.class.getName() + "<" + plan.getNativeItemClassName() + ">)super.text()).add(member.text);\n");
            writer.write("}\n");
            writer.write("}\n");

            writer.write("public void text(final " + enumClassName + " ... enm) {\n");
            writer.write("super.text(new " + plan.getNativeItemClassNameImplementation() + "());\n");
            writer.write("for (final " + enumClassName + " member : enm) // [A]\n");
            writer.write("if (member != null)\n");
            writer.write("((" + List.class.getName() + "<" + plan.getNativeItemClassName() + ">)super.text()).add(member.text);\n");
            writer.write("}\n");

            if (plan.isUnionWithNonEnumeration()) {
              writer.write("public void text(final " + plan.getNativeNonEnumItemClassNameInterface() + " text) {\n");
              writer.write("super.text(new " + plan.getNativeNonEnumItemClassNameImplementation() + "());\n");
              writer.write("if (text instanceof " + RandomAccess.class.getName() + ") {\n");
              writer.write("for (int i = 0, i$ = text.size(); i < i$; ++i) { // [RA]\n");
              writer.write("final " + enumClassName + " member = text.get(i);\n");
              writer.write("if (member != null)\n");
              writer.write("((" + List.class.getName() + "<" + plan.getNativeItemClassName() + ">)super.text()).add(member.text);\n");
              writer.write("}\n");
              writer.write("}\n");
              writer.write("else {\n");
              writer.write("for (final " + enumClassName + " member : text) // [L]\n");
              writer.write("if (member != null)\n");
              writer.write("((" + List.class.getName() + "<" + plan.getNativeNonEnumItemClassNameInterface() + ">)super.text()).add(member.text);\n");
              writer.write("}\n");
              writer.write("}\n");
            }
          }
          else {
            writer.write("public void text(final " + enumClassName + " enm) {\n");
            writer.write("super.text(enm.text());\n");
            writer.write("}\n");

            if (plan.isUnionWithNonEnumeration()) {
              writer.write("@" + Override.class.getName() + "\n");
              writer.write("public void text(final " + plan.getNativeNonEnumItemClassNameInterface() + " text) {\n");
              writer.write("super.text(text);\n");
              writer.write("}\n");
            }
          }
        }
        else {
          // FIXME: This misses some @Override(s) for situations that inherit from xs types,
          // FIXME: cause the type of the parameter to the text() method is not known here
          if (parent != null && ((SimpleTypePlan<?>)parent).getNativeItemClassNameInterface().equals(plan.getNativeItemClassNameInterface()))
            writer.write("@" + Override.class.getName() + "\n");

          if (plan.isList()) {
            writer.write("public void text(final " + plan.getNativeNonEnumItemClassNameInterface() + " text) {\n");
            writer.write("super.text(text);\n");
            writer.write("}\n");

            writer.write("public <T extends " + plan.getNativeNonEnumItemClassNameInterface() + ">void text(final " + plan.getNativeItemClassName() + " ... text) {\n");
            writer.write("super.text((T)" + Arrays.class.getName() + ".asList(text));\n");
            writer.write("}\n");
          }
          else {
            writer.write("public void text(final " + plan.getNativeNonEnumItemClassNameInterface() + " text) {\n");
            writer.write("super.text(text);\n");
            writer.write("}\n");
          }
        }

        // if (plan.getNativeItemClassName() == null &&
        // XSTypeDirectory.ANYSIMPLETYPE.getNativeBinding().getName().equals(plan.getBaseXSItemTypeName()))
        // {
        // writer.write("public void text(final " + plan.getNativeItemClassNameInterface() + " text)\n");
        // writer.write("{\n");
        // writer.write("super.text(text);\n");
        // writer.write("}\n");
        // }

        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public " + plan.getNativeItemClassNameInterface() + " text() {\n");
        if (!Object.class.getName().equals(plan.getNativeItemClassNameInterface()))
          writer.write("return (" + plan.getNativeItemClassNameInterface() + ")super.text();\n");
        else
          writer.write("return super.text();\n");
        writer.write("}\n");

        if (plan.isList()) {
          writer.write("public " + plan.getNativeItemClassName() + " text(final int index) {\n");
          writer.write("final " + List.class.getName() + "<" + plan.getNativeNonEnumItemClassName() + "> values = text();\n");
          writer.write("return values != null && -1 < index && index < values.size() ? values.get(index) : null;\n");
          writer.write("}\n");
        }
      }
      else if (plan.hasEnumerations()) {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("public " + String.class.getName() + " text() {\n");
        writer.write("return (" + String.class.getName() + ")super.text();\n");
        writer.write("}\n");
      }
    }

    // NATIVE CONSTRUCTORS
    if (plan.writeNativeConstructor())
      getNativeConstructors(writer, plan);

    for (final AttributePlan attribute : plan.getAttributes()) { // [S]
      Writer.writeSetMethod(writer, attribute, plan);
      Writer.writeGetMethod(writer, attribute, plan);
    }

    for (final ElementPlan element : plan.getElements()) { // [S]
      Writer.writeSetMethod(writer, element, plan);
      Writer.writeGetMethod(writer, element, plan);
    }

    // INHERITS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + plan.getCopyClassName(parent) + " inherits() {\n");
    writer.write("return this;\n");
    writer.write("}\n");

    // SUBSTITUTION GROUP
    if (plan.getSubstitutionGroup() != null) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected boolean _$$isSubstitutionGroup(" + QName.class.getName() + " name) {\n");
      writer.write("return name != null && SUBSTITUTION_GROUP.getNamespaceURI().equals(name.getNamespaceURI()) && SUBSTITUTION_GROUP.getLocalPart().equals(name.getLocalPart());\n");
      writer.write("}\n");
    }

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

    // QUALIFIED
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public boolean qualified() {\n");
    writer.write("return " + (plan.getFormDefault() == Form.QUALIFIED) + ";\n");
    writer.write("}\n");

    // NILABLE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public boolean nilable() {\n");
    writer.write("return " + plan.isNillable() + ";\n");
    writer.write("}\n");

    // OWNER
    if (plan.getOwnerClassName() != null) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("public " + plan.getOwnerClassName() + " owner() {\n");
      writer.write("return (" + plan.getOwnerClassName() + ")super.owner();\n");
      writer.write("}\n");
    }

    // PATTERN
    appendPattern(writer, plan.getPatterns());

    // ATTRIBUTE ITERATORS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + Iterator.class.getName() + "<" + $AnySimpleType.class.getCanonicalName() + "<?>> attributeIterator() {\n");
    writer.write("return super.attributeIterator();\n");
    writer.write("}\n");

    // ELEMENT ITERATORS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + Iterator.class.getName() + "<" + $AnyType.class.getCanonicalName() + "<?>> elementIterator() {\n");
    writer.write("return super.elementIterator();\n");
    writer.write("}\n");

    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + BindingList.class.getName() + "<" + $AnyType.class.getCanonicalName() + "<?>> fetchChild(final " + QName.class.getName() + " name) {\n");
    writer.write("return super.fetchChild(name);\n");
    writer.write("}\n");

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
    if (plan.isNested())
      writer.write("protected ");
    else
      writer.write("public ");

    // FIXME: This is equivalent to $AnyType.marshal()
    writer.write(Element.class.getName() + " marshal() throws " + MarshalException.class.getName() + " {\n");
    writer.write("final " + Element.class.getName() + " node = marshal(createElementNS(name().getNamespaceURI(), name().getLocalPart()), name(), type(_$$inheritsInstance()));\n");
    writer.write("_$$marshalElements(node);\n");
    writer.write("return node;\n");
    writer.write("}\n\n");

    writer.write("@" + Override.class.getName() + "\n");
    writer.write("protected " + Element.class.getName() + " marshal(final " + Element.class.getName() + " parent, final " + QName.class.getName() + " name, final " + QName.class.getName() + " type) throws " + MarshalException.class.getName() + " {\n");
    writer.write("final " + Element.class.getName() + " node = super.marshal(parent, name, type);\n");
    if (plan.isNillable())
      writeNilMarshal(writer);

    if (plan.getElements().size() != 0 || plan.getAttributes().size() != 0) {
      if (plan.getMixed() != null && plan.getMixed()) {
        writer.write("if (text != null)\n");
        writer.write("node.appendChild(node.getOwnerDocument().createTextNode(text));\n");
      }

      for (final AttributePlan attribute : plan.getAttributes()) // [S]
        Writer.writeMarshal(writer, attribute, plan);
    }

    writer.write("return node;\n");
    writer.write("}\n");

    // PARSE ATTRIBUTE
    if (plan.getAttributes() != null && plan.getAttributes().size() != 0 || plan.isNillable()) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected boolean parseAttribute(final " + Attr.class.getName() + " attribute) {\n");
      writer.write("if (attribute == null || XMLNS.getLocalPart().equals(attribute.getPrefix())) {\n");
      writer.write("return true;\n");
      writer.write("}\n");
      AttributePlan any = null;
      for (final AttributePlan attribute : plan.getAttributes()) { // [S]
        if (attribute instanceof AnyAttributePlan)
          any = attribute;
        else
          Writer.writeParse(writer, attribute, plan);
      }

      if (plan.isNillable()) {
        writer.write("else if (XSI_NIL.getNamespaceURI().equals(attribute.getNamespaceURI()) && XSI_NIL.getLocalPart().equals(attribute.getLocalName())) {\n");
        writer.write("this.nil = " + $Boolean.class.getCanonicalName() + ".parse(attribute.getNodeValue());\n");
        writer.write("return true;\n");
        writer.write("}\n");
      }

      writer.write("return super.parseAttribute(attribute);\n");
      writer.write("}\n");

      if (any != null) {
        writer.write("@" + Override.class.getName() + "\n");
        writer.write("protected void parseAnyAttribute(final " + Attr.class.getName() + " attribute) throws " + ValidationException.class.getName() + " {\n");
        Writer.writeParse(writer, any, plan);
        writer.write("}\n");
      }
    }

    // PARSE ELEMENT
    if (plan.getElements() != null && plan.getElements().size() != 0 || plan.getMixed() != null && plan.getMixed()) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected boolean parseElement(final " + Element.class.getName() + " element) throws " + ValidationException.class.getName() + " {\n");
      if (plan.getMixed() != null && plan.getMixed()) {
        writer.write("if (" + Node.class.getName() + ".TEXT_NODE == element.getNodeType()) {\n");
        writer.write("if (element.getNodeValue().length() != 0) {\n");
        writer.write("if (text == null)\n");
        writer.write("text = element.getNodeValue();\n");
        writer.write("else\n");
        writer.write("text += element.getNodeValue();\n");
        writer.write("}\n");
        writer.write("return true;\n");
        writer.write("}\n");
      }

      ElementPlan any = null;
      for (final ElementPlan element : plan.getElements()) { // [S]
        if (element instanceof AnyPlan)
          any = element;
        else
          Writer.writeParse(writer, element, plan);
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

    if (plan.isList() && plan.getSuperType() == null) {
      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected void _$$decode(final " + Element.class.getName() + " parent, " + String.class.getName() + " value) {\n");
      writer.write("if (value == null || value.length() == 0)\n");
      writer.write("return;\n");

      writer.write("super.text(new " + plan.getNativeItemClassNameImplementation() + "());\n");
      writer.write("final " + StringTokenizer.class.getName() + " tokenizer = new " + StringTokenizer.class.getName() + "(value);\n");
      writer.write("while (tokenizer.hasMoreTokens())\n");
      if (plan.getNativeFactory() != null)
        writer.write("((" + plan.getNativeItemClassNameInterface() + ")super.text()).add(" + plan.getNativeFactory() + "(tokenizer.nextToken()));\n");
      else
        writer.write("((" + plan.getNativeItemClassNameInterface() + ")super.text()).add(tokenizer.nextToken());\n");
      writer.write("}\n");

      writer.write("@" + Override.class.getName() + "\n");
      writer.write("protected " + String.class.getName() + " _$$encode(final " + Element.class.getName() + " parent) throws " + MarshalException.class.getName() + " {\n");
      writer.write("if (super.text() == null || ((" + List.class.getName() + "<" + plan.getNativeItemClassName() + ">)super.text()).size() == 0)\n");
      writer.write("return null;\n");
      writer.write("final " + StringBuilder.class.getName() + " builder = new " + StringBuilder.class.getName() + "();\n");
      writer.write("final " + List.class.getName() + "<" + plan.getNativeItemClassName() + "> list = (" + List.class.getName() + "<" + plan.getNativeItemClassName() + ">)super.text();\n");
      writer.write("if (list instanceof " + RandomAccess.class.getName() + ") {\n");
      writer.write("for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]\n");
      writer.write("final " + plan.getNativeItemClassName() + " member = list.get(i);\n");
      writer.write("if (member != null)\n");
      writer.write("builder.append(member).append(' ');\n");
      writer.write("}\n");
      writer.write("}\n");
      writer.write("else {\n");
      writer.write("for (final " + plan.getNativeItemClassName() + " member : list) // [L]\n");
      writer.write("if (member != null)\n");
      writer.write("builder.append(member).append(' ');\n");
      writer.write("}\n");
      writer.write("return builder.substring(0, builder.length() - 1);\n");
      writer.write("}\n");
    }

    // CLONE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public " + plan.getClassName(parent) + " clone() {\n");
    writer.write("return (" + plan.getClassName(parent) + ")super.clone();\n");
    writer.write("}\n");

    // EQUALS
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public boolean equals(final " + Object.class.getName() + " obj) {\n");
    writer.write("if (this == obj)\n");
    writer.write("return true;\n");
    writer.write("if (!(obj instanceof " + plan.getCopyClassName(parent) + "))\n");
    writer.write("return _$$failEquals();\n");
    if (plan.getAttributes() != null && plan.getAttributes().size() != 0 || plan.getNativeItemClassNameInterface() == null && plan.getElements() != null && plan.getElements().size() != 0 || plan.getMixed() != null && plan.getMixed()) {
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

    if (plan.getCopyClassName(parent).equals(plan.getClassName(parent)))
      writer.write("return true;\n");
    else
      writer.write("return super.equals(obj);\n");
    writer.write("}\n");

    // HASHCODE
    writer.write("@" + Override.class.getName() + "\n");
    writer.write("public int hashCode() {\n");
    writer.write("int hashCode = super.hashCode();\n");
    if (plan.getMixed() != null && plan.getMixed()) {
      writer.write("if (text != null)\n");
      writer.write("hashCode = 31 * hashCode + text.hashCode();\n");
    }
    for (final AttributePlan attribute : plan.getAttributes()) // [S]
      Writer.writeHashCode(writer, attribute, plan);
    for (final ElementPlan element : plan.getElements()) // [S]
      Writer.writeHashCode(writer, element, plan);
    writer.write("return hashCode;\n");
    writer.write("}\n");

    // ATTRIBUTES
    for (final AttributePlan attribute : plan.getAttributes()) // [S]
      Writer.writeClass(writer, attribute, plan);

    // ELEMENTS
    for (final ElementPlan element : plan.getElements()) // [S]
      Writer.writeClass(writer, element, plan);

    writer.write("}\n");
  }
}