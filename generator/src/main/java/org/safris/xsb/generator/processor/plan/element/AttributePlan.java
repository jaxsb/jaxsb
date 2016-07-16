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

package org.safris.xsb.generator.processor.plan.element;

import javax.xml.namespace.QName;

import org.safris.xsb.compiler.processor.Formable;
import org.safris.xsb.compiler.processor.model.AnyableModel;
import org.safris.xsb.compiler.processor.model.Model;
import org.safris.xsb.compiler.processor.model.RestrictableModel;
import org.safris.xsb.compiler.processor.model.element.AttributeModel;
import org.safris.xsb.compiler.processor.model.element.SchemaModel;
import org.safris.xsb.compiler.schema.attribute.Form;
import org.safris.xsb.compiler.schema.attribute.Use;
import org.safris.xsb.generator.processor.plan.AliasPlan;
import org.safris.xsb.generator.processor.plan.EnumerablePlan;
import org.safris.xsb.generator.processor.plan.ExtensiblePlan;
import org.safris.xsb.generator.processor.plan.NativeablePlan;
import org.safris.xsb.generator.processor.plan.NestablePlan;
import org.safris.xsb.generator.processor.plan.Plan;
import org.safris.xsb.generator.processor.plan.RestrictablePlan;
import org.safris.xsb.runtime.JavaBinding;
import org.safris.xsb.runtime.XSTypeDirectory;

public class AttributePlan extends SimpleTypePlan<AttributeModel> implements EnumerablePlan, ExtensiblePlan, Formable<Plan<?>>, NativeablePlan, NestablePlan, RestrictablePlan {
  private final AttributeModel attribute;
  private final boolean ref;
  private final boolean restriction;
  private final boolean fixed;
  private final QName _default;
  private final Use use;

  private String thisClassNameWithType = null;
  private String declarationRestrictionGeneric = null;
  private String declarationRestrictionSimpleName = null;

  private String superClassNameWithType = null;
  private String superClassNameWithoutType = null;

  private boolean nested = false;
  private Form formDefault = null;

  public AttributePlan(final AttributeModel model, final Plan<?> parent) {
    super(model, parent);
    ref = (attribute = getModel()) != model;
    restriction = model.getRestrictionOwner() != null;
    fixed = model.getFixed() != null;
    _default = fixed ? model.getFixed() : model.getDefault();
    use = model.getUse();
    if (model instanceof AnyableModel)
      return;

    if (isRestriction())
      superClassNameWithoutType = AliasPlan.getClassName(attribute.getRestrictionOwner(), null) + "." + JavaBinding.getClassSimpleName(getModel().getRestriction());
    else
      superClassNameWithoutType = AliasPlan.getClassName(attribute.getSuperType(), attribute.getSuperType().getParent());

    superClassNameWithType = superClassNameWithoutType;

    thisClassNameWithType = !ref && attribute.getParent() instanceof SchemaModel ? superClassNameWithType : null;

    nested = ref || !(attribute.getParent() instanceof SchemaModel);
    formDefault = model.getForm() == Form.QUALIFIED || ref ? Form.QUALIFIED : attribute.getFormDefault();
  }

  public final Use getUse() {
    return use;
  }

  public final boolean isFixed() {
    return fixed;
  }

  public final String getDefaultInstance(final Plan<?> parent) {
    if (getDefault() == null)
      return null;

    String _default = XSTypeDirectory.QNAME.getNativeBinding().getName().equals(getBaseXSItemTypeName()) ? getDefault().toString() : getDefault().getLocalPart();
    if (hasEnumerations() && (!(getSuperType() instanceof SimpleTypePlan) || !((SimpleTypePlan<?>)getSuperType()).isUnion()))
      _default = getClassName(parent) + "." + EnumerationPlan.getDeclarationName(getDefault());
    else
      _default = "\"" + _default + "\"";

    String defaultInstance = "new " + getClassName(parent) + "(";
    if (!hasEnumerations() && getNativeFactory() != null)
      defaultInstance += getNativeFactory() + "(" + _default + "))";
    else
      defaultInstance += "" + _default + ")";

    return defaultInstance;
  }

  public final String getFixedInstanceCall(final Plan<?> parent) {
    if (!fixed)
      return "";

    String defaultInstance = getDefaultInstance(parent);
    if (isRestriction())
      return "super." + getDeclarationRestrictionSimpleName() + "(" + defaultInstance + ");\n";

    return "_$$setAttribute(" + getInstanceName() + ", this, " + defaultInstance + ");\n";
  }

  public final QName getDefault() {
    return _default;
  }

  @Override
  public final boolean isRestriction() {
    return restriction;
  }

  @Override
  public final AttributeModel getModel() {
    return super.getModel().getRef() != null ? super.getModel().getRef() : super.getModel();
  }

  @Override
  public final String getSuperClassNameWithoutType() {
    return superClassNameWithoutType;
  }

  public final boolean isRef() {
    return ref;
  }

  public final String getThisClassNameWithType(final Plan<?> parent) {
    if (thisClassNameWithType != null)
      return thisClassNameWithType;

    //if (!UniqueQName.XS.getNamespaceURI().equals(getModel().getSuperType().getName().getNamespaceURI()))
    //final AliasModel model = !getModel().isRestriction() ? getModel().getSuperType() : getModel();

    return AliasPlan.getClassName(getModel(), parent.getModel());
  }

  public final String getThisClassNameWithTypeWithInconvertible(final Plan<?> parent) {
    if (thisClassNameWithType != null)
      return thisClassNameWithType;

    //if (!UniqueQName.XS.getNamespaceURI().equals(getModel().getSuperType().getName().getNamespaceURI()))
    //final AliasModel model = !getModel().isRestriction() ? getModel().getSuperType() : getModel();

    return AliasPlan.getClassName(getModel(), parent.getModel());
  }

  public final String getDeclarationRestrictionGeneric(final Plan<?> parent) {
    if (!isRestriction())
      return getThisClassNameWithTypeWithInconvertible(parent);

    if (declarationRestrictionGeneric != null)
      return declarationRestrictionGeneric;

    RestrictableModel<?> first = null;
    RestrictableModel<?> prior = getModel();
    while (prior.getRestriction() != null) {
      first = prior;
      prior = prior.getRestriction();
    }

    return declarationRestrictionGeneric = AliasPlan.getClassName(first.getRestrictionOwner(), null) + "." + JavaBinding.getClassSimpleName((Model)prior);
  }

  public final String getDeclarationRestrictionSimpleName() {
    if (!isRestriction())
      return getClassSimpleName();

    if (declarationRestrictionSimpleName != null)
      return declarationRestrictionSimpleName;

    RestrictableModel<?> prior = getModel();
    while (prior.getRestriction() != null)
      prior = prior.getRestriction();

    return declarationRestrictionSimpleName = JavaBinding.getClassSimpleName((Model)prior);
  }

  @Override
  public final String getSuperClassNameWithType() {
    return superClassNameWithType;
  }

  public final String getCopyClassName(final Plan<?> parent) {
    if (!getModel().getSuperType().getName().equals(XSTypeDirectory.ANYSIMPLETYPE.getNativeBinding().getName()))
      return AliasPlan.getClassName(getModel().getSuperType(), null);

    if (getModel().getRef() != null && getModel().getRef().getName() != null)
      return AliasPlan.getClassName(getModel().getRef(), parent != null ? parent.getModel() : null);

    return AliasPlan.getClassName(getModel(), parent != null ? parent.getModel() : null);
  }

  @Override
  public final boolean isNested() {
    return nested;
  }

  @Override
  public final Form getFormDefault() {
    return formDefault;
  }
}