/* Copyright (c) 2008 OpenJAX
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

package org.openjax.xsb.generator.processor.plan;

import org.openjax.xsb.compiler.processor.model.AliasModel;
import org.openjax.xsb.compiler.processor.model.Model;
import org.openjax.xsb.compiler.processor.model.element.SchemaModel;
import org.openjax.xsb.runtime.JavaBinding;
import org.openjax.xsb.runtime.XSTypeDirectory;
import org.openjax.xsb.generator.processor.plan.element.DocumentationPlan;

public abstract class AliasPlan<T extends AliasModel> extends NamedPlan<T> implements DocumentablePlan {
  protected static String getClassName(final AliasModel model, Plan<?> parent) {
    if (model == null || model.getName() == null)
      return null;

    if (model.getParent() instanceof SchemaModel || XSTypeDirectory.parseType(model.getName()) != null)
      return model.getName().getNamespaceURI().getNamespaceBinding().getClassName() + "." + JavaBinding.getClassSimpleName(model);

    if (parent != null)
      do
        if (parent instanceof AliasPlan && ((AliasPlan<?>)parent).getName() != null)
          return ((AliasPlan<?>)parent).getClassName(null) + "." + JavaBinding.getClassSimpleName(model);
      while((parent = parent.getParent()) != null);

    Model check = model;
    while ((check = check.getParent()) != null)
      if (check instanceof AliasModel && ((AliasModel)check).getName() != null)
        return getClassName((AliasModel)check, null) + "." + JavaBinding.getClassSimpleName(model);

    return model.getName().getNamespaceURI().getNamespaceBinding().getClassName() + "." + JavaBinding.getClassSimpleName(model);
  }

  private DocumentationPlan documentation = null;

//  private String packageName = null;
  private String instanceName = null;

  private String className = null;
  private String classInconvertibleName = null;
  private String classSimpleName = null;
  private String methodName = null;
//  private String schemaReference = null;
  private String xsdLocation = null;

  public AliasPlan(final T model, final Plan<?> parent) {
    super(model, parent);
    if (getModel() != null)
      documentation = Plan.<DocumentationPlan>analyze(getModel().getDocumentation(), this);

//    schemaReference = model.getSchema().getURL().toString();
    xsdLocation = model.getSchema().getTargetNamespace().getNamespaceBinding().getPackageName() + ".xsd";
  }

  public final String getXsdLocation() {
    return xsdLocation;
  }

  @Override
  public final String getDocumentation() {
    return documentation != null ? documentation.getDocumentation() : "";
  }

  public final String getInstanceName() {
    return instanceName == null ? instanceName = JavaBinding.getInstanceName(getModel()) : instanceName;
  }

  public final String getClassName(final Plan<?> parent) {
    if (className != null)
      return className;

    if (parent != null)
      return className = AliasPlan.getClassName(getModel(), parent);

    return className = AliasPlan.getClassName(getModel(), null);
  }

  public final String getClassWithInconvertible(final Plan<?> parent) {
    if (classInconvertibleName != null)
      return classInconvertibleName;

    if (parent != null)
      return classInconvertibleName = AliasPlan.getClassName(getModel(), parent);

    return classInconvertibleName = AliasPlan.getClassName(getModel(), null);
  }

  public final String getClassSimpleName() {
    return classSimpleName == null ? classSimpleName = JavaBinding.getClassSimpleName(getModel()) : classSimpleName;
  }

  public final String getMethodName() {
    return methodName == null ? methodName = JavaBinding.getMethodName(getModel()) : methodName;
  }
}