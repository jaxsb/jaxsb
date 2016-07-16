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

package org.safris.xsb.runtime.processor.plan.element;

import java.lang.reflect.InvocationTargetException;

import javax.xml.namespace.QName;

import org.safris.xsb.compiler.lang.UniqueQName;
import org.safris.xsb.compiler.processor.Formable;
import org.safris.xsb.compiler.processor.model.AliasModel;
import org.safris.xsb.compiler.processor.model.AnyableModel;
import org.safris.xsb.compiler.processor.model.Model;
import org.safris.xsb.compiler.processor.model.RestrictableModel;
import org.safris.xsb.compiler.processor.model.element.ComplexTypeModel;
import org.safris.xsb.compiler.processor.model.element.ElementModel;
import org.safris.xsb.compiler.processor.model.element.SchemaModel;
import org.safris.xsb.compiler.processor.model.element.SimpleTypeModel;
import org.safris.xsb.compiler.schema.attribute.Form;
import org.safris.xsb.runtime.CompilerFailureException;
import org.safris.xsb.runtime.JavaBinding;
import org.safris.xsb.runtime.XSTypeDirectory;
import org.safris.xsb.runtime.processor.plan.AliasPlan;
import org.safris.xsb.runtime.processor.plan.EnumerablePlan;
import org.safris.xsb.runtime.processor.plan.ExtensiblePlan;
import org.safris.xsb.runtime.processor.plan.NativeablePlan;
import org.safris.xsb.runtime.processor.plan.NestablePlan;
import org.safris.xsb.runtime.processor.plan.Plan;
import org.safris.xsb.runtime.processor.plan.RestrictablePlan;

public class ElementPlan extends ComplexTypePlan<ElementModel> implements EnumerablePlan, ExtensiblePlan, Formable<Plan<?>>, NativeablePlan, NestablePlan, RestrictablePlan {
  private final ElementModel element;
  private final boolean ref;
  private final boolean _abstract;
  private final boolean restriction;
  private final boolean fixed;
  private final QName _default;
  private final boolean nillable;
  private final UniqueQName substitutionGroup;

  private UniqueQName typeName;

  private String superClassNameWithType;
  private String superClassNameWithoutType;
  private String declarationGeneric;
  private String declarationRestrictionGeneric = null;

  private boolean nested;
  private boolean isComplexType = false;
  private boolean repeatedExtensionRun = false;
  private ElementPlan repeatedExtension = null;
  private Form formDefault;

  private int minOccurs = 1;
  private int maxOccurs = 1;

  public ElementPlan(final ElementModel model, final Plan<?> parent) {
    super(model, parent);
    ref = (element = getModel()) != model;
    _abstract = model.getAbstract();
    restriction = model.getRestrictionOwner() != null;
    fixed = model.getFixed() != null;
    _default = fixed ? model.getFixed() : model.getDefault();
    nillable = getModel().getNillable() != null && getModel().getNillable();
    substitutionGroup = model.getSubstitutionGroup();
    if (model instanceof AnyableModel)
      return;

    if (element.getSuperType() == null)
      throw new CompilerFailureException("element with no type?");

    typeName = element.getSuperType().getName();
    if (isRestriction())
      superClassNameWithoutType = AliasPlan.getClassName(element.getRestrictionOwner(), null) + "." + JavaBinding.getClassSimpleName(getModel().getRestriction());
    else
      superClassNameWithoutType = AliasPlan.getClassName(element.getSuperType(), element.getSuperType().getParent());

    superClassNameWithType = superClassNameWithoutType;
    // If we are directly inheriting from another element via the substitutionGroup, then dont add the type
    if (substitutionGroup == null || !substitutionGroup.equals(element.getSuperType().getName())) {
      isComplexType = isComplexType(element.getSuperType());
    }
    else if (substitutionGroup != null && isComplexType(element.getSuperType()))
      isComplexType = true;
    else
      isComplexType = false;

    if (!ref && element.getParent() instanceof SchemaModel)
      declarationGeneric = superClassNameWithType;
    else
      declarationGeneric = null;

    nested = ref || !(element.getParent() instanceof SchemaModel);

    formDefault = !ref ? element.getFormDefault() : Form.QUALIFIED;
  }

  private boolean isComplexType(final SimpleTypeModel<?> simpleType) {
    return XSTypeDirectory.ANYTYPE.getNativeBinding().getName().equals(simpleType.getName()) || (simpleType instanceof ComplexTypeModel && !isRestriction());
  }

  /**
   * States whether this element is a duplicate of an element in the
   * inheritance hierarchy of the owning parent element or complexType. This
   * information means that since the element is being repeated twice its
   * methods will mask those of the first occurrence in the parent type.
   *
   * @return <code>true</code> if the name of this element exists in the
   * hierarchy of the parent element or complexType.
   */
  public final ElementPlan getRepeatedExtension() {
    if (repeatedExtensionRun)
      return repeatedExtension;

    repeatedExtension = getParent().getSuperType() != null ? getParent().getSuperType().elementRefExistsInParent(getName()) : null;
    repeatedExtensionRun = true;
    return repeatedExtension;
  }

  public final UniqueQName getSubstitutionGroup() {
    return substitutionGroup;
  }

  public final boolean isAbstract() {
    return _abstract;
  }

  public final void setMinOccurs(final int minOccurs) {
    this.minOccurs = minOccurs;
  }

  public final int getMinOccurs() {
    return minOccurs;
  }

  public final boolean isNillable() {
    return nillable;
  }

  public final QName getDefault() {
    return _default;
  }

  public final String getDefaultInstance(final Plan<?> parent) {
    if (getDefault() == null)
      return null;

    String _default = XSTypeDirectory.QNAME.getNativeBinding().getName().equals(getBaseXSItemTypeName()) ? getDefault().toString() : getDefault().getLocalPart();
    if (hasEnumerations()) {
      if (!isUnionWithNonEnumeration() || !testNativeFactory(getNativeFactoryNonEnum(), _default))
        _default = getClassName(parent) + "." + EnumerationPlan.getDeclarationName(getDefault());
    }
    else
      _default = "\"" + _default + "\"";

    String defaultInstance = "new " + getClassName(parent) + "(";
    if (!hasEnumerations() && getNativeFactory() != null)
      defaultInstance += getNativeFactory() + "(" + _default + "))";
    else
      defaultInstance += "" + _default + ")";

    return defaultInstance;
  }

  private static boolean testNativeFactory(final String nativeFactory, final String defaultValue) {
    final int index = nativeFactory.lastIndexOf(".");
    final String nativeFactoryClass = nativeFactory.substring(0, index);
    final String nativeFactoryMethod = nativeFactory.substring(index + 1);
    try {
      Class.forName(nativeFactoryClass).getMethod(nativeFactoryMethod, String.class).invoke(null, defaultValue);
    }
    catch (final InvocationTargetException e) {
      return false;
    }
    catch (final Exception e) {
      throw new CompilerFailureException(e);
    }

    return true;
  }

  public final boolean isComplexType() {
    return isComplexType;
  }

  @Override
  public final boolean isRestriction() {
    return restriction;
  }

  @Override
  public final ElementModel getModel() {
    return super.getModel().getRef() != null ? super.getModel().getRef() : super.getModel();
  }

  public final void getSuperClassNameWithoutType(final String superClassNameWithoutType) {
    this.superClassNameWithoutType = superClassNameWithoutType;
  }

  @Override
  public final String getSuperClassNameWithoutType() {
    return superClassNameWithoutType;
  }

  public final void setMaxOccurs(final int maxOccurs) {
    this.maxOccurs = maxOccurs;
  }

  public final int getMaxOccurs() {
    return maxOccurs;
  }

  public final boolean isRef() {
    return ref;
  }

  public final boolean writeNativeConstructor() {
    return !isComplexType() || (getMixed() != null ? getMixed() : getMixedType());
  }

  public final UniqueQName getTypeName() {
    return typeName;
  }

  public final String getDeclarationGeneric(final Plan<?> parent) {
    if (declarationGeneric != null)
      return declarationGeneric;

    final AliasModel model;
    //if (!UniqueQName.XS.getNamespaceURI().equals(getModel().getSuperType().getName().getNamespaceURI()))
    if (!getModel().isExtension() && !getModel().isRestriction())
      model = getModel().getSuperType();
    else
      model = getModel();

    return AliasPlan.getClassName(model, parent.getModel());
  }

  public final String getDeclarationGenericWithInconvertible(final Plan<?> parent) {
    if (declarationGeneric != null)
      return declarationGeneric;

    final AliasModel model;
    //if (!UniqueQName.XS.getNamespaceURI().equals(getModel().getSuperType().getName().getNamespaceURI()))
    if (!getModel().isExtension() && !getModel().isRestriction())
      model = getModel().getSuperType();
    else
      model = getModel();

    return AliasPlan.getClassName(model, parent.getModel());
  }

  public final String getDeclarationRestrictionGeneric(final Plan<?> parent) {
    if (!isRestriction())
      return getDeclarationGenericWithInconvertible(parent);

    if (declarationRestrictionGeneric != null)
      return declarationRestrictionGeneric;

    RestrictableModel<?> first = null;
    RestrictableModel<?> prior = getModel();
    while (prior.getRestriction() != null) {
      first = prior;
      prior = prior.getRestriction();
    }

    return declarationRestrictionGeneric = AliasPlan.getClassName(first.getRestrictionOwner(), null) + JavaBinding.getClassSimpleName((Model)first);
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