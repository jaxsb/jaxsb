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

package org.jaxsb.generator.processor.plan.element;

import javax.xml.namespace.QName;

import org.jaxsb.compiler.processor.Formable;
import org.jaxsb.compiler.processor.model.AnyableModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.RestrictableModel;
import org.jaxsb.compiler.processor.model.element.AttributeModel;
import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.jaxsb.compiler.schema.attribute.Form;
import org.jaxsb.compiler.schema.attribute.Use;
import org.jaxsb.generator.processor.plan.AliasPlan;
import org.jaxsb.generator.processor.plan.NestablePlan;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.generator.processor.plan.RestrictablePlan;
import org.jaxsb.runtime.JavaBinding;
import org.jaxsb.runtime.XSTypeDirectory;

public class AttributePlan extends SimpleTypePlan<AttributeModel> implements Formable<Plan<?>>, NestablePlan, RestrictablePlan {
  private final boolean ref;
  private final boolean restriction;
  private final boolean fixed;
  private final QName _default;
  private final Use use;

  private String thisClassNameWithType;
  private String declarationRestrictionGeneric;
  private String declarationRestrictionSimpleName;

  private String superClassNameWithType;
  private String superClassNameWithoutType;

  private boolean nested;
  private Form formDefault;

  public AttributePlan(final AttributeModel model, final Plan<?> parent) {
    super(model, parent);
    final AttributeModel attribute = model.getRef() != null ? model.getRef() : model;
    this.ref = attribute != model;
    restriction = model.getRestrictionOwner() != null;
    fixed = model.getFixed() != null;
    _default = fixed ? model.getFixed() : model.getDefault();
    use = model.getUse();
    if (model instanceof AnyableModel)
      return;

    if (isRestriction())
      superClassNameWithoutType = AliasPlan.getClassName(attribute.getRestrictionOwner(), null) + "." + JavaBinding.getClassSimpleName(getModel().getRestriction());
    else
      superClassNameWithoutType = AliasPlan.getClassName(attribute.getSuperType(), null);

    superClassNameWithType = superClassNameWithoutType;

    thisClassNameWithType = !ref && attribute.getParent() instanceof SchemaModel ? superClassNameWithType : null;

    nested = ref || !(attribute.getParent() instanceof SchemaModel);
    formDefault = ref ? Form.QUALIFIED : model.getForm() != null ? model.getForm() : attribute.getFormDefault();
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
      defaultInstance += _default + ")";

    return defaultInstance;
  }

  public final String getFixedInstanceCall(final Plan<?> parent) {
    if (!fixed)
      return "";

    final String defaultInstance = getDefaultInstance(parent);
    if (isRestriction())
      return "super.set" + getDeclarationRestrictionSimpleName() + "(" + defaultInstance + ");\n";

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
  public final String getSuperClassNameWithoutGenericType() {
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

    return AliasPlan.getClassName(getModel(), parent);
  }

  public final String getThisClassNameWithTypeWithInconvertible(final Plan<?> parent) {
    if (thisClassNameWithType != null)
      return thisClassNameWithType;

    //if (!UniqueQName.XS.getNamespaceURI().equals(getModel().getSuperType().getName().getNamespaceURI()))
    //final AliasModel model = !getModel().isRestriction() ? getModel().getSuperType() : getModel();

    return AliasPlan.getClassName(getModel(), parent);
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

    if (first == null)
      throw new IllegalStateException("Should not get here");

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
  public final String getSuperClassNameWithGenericType() {
    return superClassNameWithType;
  }

  public final String getCopyClassName(final Plan<?> parent) {
    if (!getModel().getSuperType().getName().equals(XSTypeDirectory.ANYSIMPLETYPE.getNativeBinding().getName()))
      return AliasPlan.getClassName(getModel().getSuperType(), null);

    if (getModel().getRef() != null && getModel().getRef().getName() != null)
      return AliasPlan.getClassName(getModel().getRef(), parent);

    return AliasPlan.getClassName(getModel(), parent);
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