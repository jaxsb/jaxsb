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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.model.AnyableModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.NamedModel;
import org.jaxsb.compiler.processor.model.element.ComplexTypeModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.jaxsb.compiler.processor.model.element.UnionModel;
import org.jaxsb.generator.processor.plan.AliasPlan;
import org.jaxsb.generator.processor.plan.EnumerablePlan;
import org.jaxsb.generator.processor.plan.ExtensiblePlan;
import org.jaxsb.generator.processor.plan.NamedPlan;
import org.jaxsb.generator.processor.plan.NativeablePlan;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.runtime.CompilerFailureException;
import org.jaxsb.runtime.JavaBinding;
import org.jaxsb.runtime.XSTypeDirectory;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;

public class SimpleTypePlan<T extends SimpleTypeModel<?>> extends AliasPlan<T> implements EnumerablePlan, ExtensiblePlan, NativeablePlan {
  private static SimpleTypeModel<?> getGreatestCommonType(final Collection<SimpleTypeModel<?>> types, final boolean includeEnums) {
    if (types == null || types.size() == 0)
      return null;

    if (types.size() == 1)
      return types.iterator().next();

    final Iterator<SimpleTypeModel<?>> iterator = types.iterator();
    SimpleTypeModel<?> gct = getGreatestCommonType(iterator.next(), iterator.next(), includeEnums);
    while (iterator.hasNext() && gct != null)
      gct = getGreatestCommonType(gct, iterator.next(), includeEnums);

    return gct;
  }

  @SuppressWarnings("null")
  private static SimpleTypeModel<?> getGreatestCommonType(final SimpleTypeModel<?> model1, final SimpleTypeModel<?> model2, final boolean includeEnums) {
    if (model1 == null || model2 == null)
      return null;

    if (!includeEnums) {
      // First we ignore enumeration types because their native types don't
      // get exposed to the user
      if (model1.getEnumerations().size() != 0) {
        if (model2.getEnumerations().size() != 0)
          return null;

        return model2;
      }

      if (model2.getEnumerations().size() != 0)
        return model1;
    }

    UniqueQName name1;
    UniqueQName name2;

    // Second try to see if we can match using the knowledge of type inheritance
    SimpleTypeModel<?> type1 = model1;
    do {
      name1 = type1.getName();
      if (name1 == null)
        name1 = NamedModel.getNameOfRestrictionBase(type1);

      SimpleTypeModel<?> type2 = model2;
      do {
        name2 = type2.getName();
        if (name2 == null)
          name2 = NamedModel.getNameOfRestrictionBase(type2);

        if (name1.equals(name2))
          return type1;
      }
      while ((type2 = type2.getSuperType()) != null || (type2 = ComplexTypeModel.Undefined.parseComplexType(XSTypeDirectory.lookupSuperType(name2))) != null);
    }
    while ((type1 = type1.getSuperType()) != null || (type1 = ComplexTypeModel.Undefined.parseComplexType(XSTypeDirectory.lookupSuperType(name1))) != null);

    return null;
  }

  private static boolean digList(final SimpleTypeModel<?> model) {
    boolean list = model.isList();
    if (!list) {
      SimpleTypeModel<?> type = model;
      while ((type = type.getSuperType()) != null)
        if (list = type.isList())
          break;
    }

    return list;
  }

  private static SimpleTypeModel<?> digBaseXSItemTypeName(final SimpleTypeModel<?> model, final boolean includeEnums) {
    SimpleTypeModel<?> itemType;
    SimpleTypeModel<?> type = model;
    do {
      itemType = getGreatestCommonType(type.getItemTypes(), includeEnums);
      if (itemType == null)
        continue;

      UniqueQName itemName = itemType.getName();
      if (itemName == null)
        itemName = NamedModel.getNameOfRestrictionBase(itemType);

      if (XSTypeDirectory.parseType(itemName) != null)
        return itemType;
    }
    while ((type = type.getSuperType()) != null);

    return null;
  }

  private static SimpleTypeModel<?> digBaseNonXSType(final SimpleTypeModel<?> model) {
    SimpleTypeModel<?> type = model;
    SimpleTypeModel<?> retval = null;
    do {
      if (type.getName() != null && XSTypeDirectory.parseType(type.getName()) != null)
        break;

      retval = type;
    }
    while ((type = type.getSuperType()) != null);

    return retval;
  }

  private final String superClassNameWithGenericType;
  private final String superClassNameWithoutGenericType;

  private LinkedHashSet<PatternPlan> patterns;
  private LinkedHashSet<EnumerationPlan> enumerations;
  private Boolean hasEnumerations;
  private Boolean hasSuperEnumerations;

  private final String nativeItemClassName;
  private String nativeNonEnumItemClassName;
  private String nativeInterface;
  private String nativeNonEnumInterface;
  private String nativeImplementation;
  private String nativeNonEnumImplementation;
  private String nativeFactory;
  private String nonEnumNativeFactory;
  private boolean list;
  private String baseNonXSTypeClassName;

  private UniqueQName superTypeName;
//  private UniqueQName baseNonXSTypeName = null;
  private final UniqueQName baseXSItemTypeName;
  private final UniqueQName baseXSNonEnumItemTypeName;

  private boolean parsedSuperType;
  private NamedPlan<?> superType;

  private boolean isUnion;
  private boolean isUnionWithNonEnumeration;

  private static UniqueQName getItemName(final SimpleTypeModel<?> model) {
    UniqueQName name = model.getName();
    if (name == null)
      name = NamedModel.getNameOfRestrictionBase(model);

    return name;
  }

  @SuppressWarnings("unchecked")
  public SimpleTypePlan(final T model, final Plan<?> parent) {
    super(model.getRedefine() != null ? (T)model.getRedefine() : model, parent);
    if (model instanceof AnyableModel) {
      nativeItemClassName = null;
      baseXSItemTypeName = null;
      baseXSNonEnumItemTypeName = null;
      superClassNameWithGenericType = null;
      superClassNameWithoutGenericType = null;
      return;
    }

    // Gets the XS pre-simpleType name of the type
    final SimpleTypeModel<?> baseNonXSType = digBaseNonXSType(model);
//    if (baseNonXSType != null)
//      baseNonXSTypeName = baseNonXSType.getName();

    // Gets the XS simpleType name of the itemType
    final SimpleTypeModel<?> baseXSItemType = digBaseXSItemTypeName(getModel(), true);
    final SimpleTypeModel<?> baseXSNonEnumItemType = digBaseXSItemTypeName(getModel(), false);

    if (baseXSItemType != null)
      baseXSItemTypeName = getItemName(baseXSItemType);
    else if (baseNonXSType != null && baseNonXSType.getSuperType() != null)
      baseXSItemTypeName = getItemName(baseNonXSType.getSuperType());
    else if (XSTypeDirectory.parseType(getModel().getSuperType().getName()) != null)
      baseXSItemTypeName = getModel().getSuperType().getName();
    else
      baseXSItemTypeName = null;

    if (baseXSNonEnumItemType != null)
      baseXSNonEnumItemTypeName = getItemName(baseXSNonEnumItemType);
    else if (baseNonXSType != null && baseNonXSType.getSuperType() != null)
      baseXSNonEnumItemTypeName = getItemName(baseNonXSType.getSuperType());
    else if (XSTypeDirectory.parseType(getModel().getSuperType().getName()) != null)
      baseXSNonEnumItemTypeName = getModel().getSuperType().getName();
    else
      baseXSNonEnumItemTypeName = null;

    baseNonXSTypeClassName = JavaBinding.getClassName(baseNonXSType);

    final XSTypeDirectory baseXSItemTypeDirectory = XSTypeDirectory.parseType(baseXSItemTypeName);
    final XSTypeDirectory baseXSNonEnumItemTypeDirectory = XSTypeDirectory.parseType(baseXSNonEnumItemTypeName);
    if (baseXSItemTypeDirectory == null)
      throw new CompilerFailureException("Should always be able to resolve the type for name: " + getName());

    superTypeName = model.getSuperType().getName();
    superClassNameWithoutGenericType = AliasPlan.getClassName(model.getSuperType(), null);

    if (this.list = baseXSItemTypeDirectory.getNativeBinding().isList()) {
      nativeItemClassName = baseXSItemTypeDirectory.getNativeBinding().getNativeClass().getType().getCanonicalName();
    }
    else {
      this.list = digList(getModel());
      nativeItemClassName = baseXSItemTypeDirectory.getNativeBinding().getNativeClass().getCls().getCanonicalName();
    }

    if (this.list = baseXSNonEnumItemTypeDirectory.getNativeBinding().isList()) {
      nativeNonEnumItemClassName = baseXSNonEnumItemTypeDirectory.getNativeBinding().getNativeClass().getType().getCanonicalName();
    }
    else {
      this.list = digList(getModel());
      nativeNonEnumItemClassName = baseXSNonEnumItemTypeDirectory.getNativeBinding().getNativeClass().getCls().getCanonicalName();
    }

    nativeFactory = baseXSItemTypeDirectory.getNativeFactory();
    nonEnumNativeFactory = baseXSNonEnumItemTypeDirectory.getNativeFactory();
    if (isList()) {
      nativeInterface = List.class.getName() + "<" + nativeItemClassName + ">";
      nativeImplementation = ArrayList.class.getName() + "<" + nativeItemClassName + ">";
      nativeNonEnumInterface = List.class.getName() + "<" + nativeNonEnumItemClassName + ">";
      nativeNonEnumImplementation = ArrayList.class.getName() + "<" + nativeNonEnumItemClassName + ">";
    }
    else {
      nativeInterface = nativeItemClassName;
      nativeImplementation = nativeItemClassName;
      nativeNonEnumInterface = nativeNonEnumItemClassName;
      nativeNonEnumImplementation = nativeNonEnumItemClassName;
    }

    if ($AnyType.class.getCanonicalName().equals(superClassNameWithoutGenericType) || $AnySimpleType.class.getCanonicalName().equals(superClassNameWithoutGenericType))
      superClassNameWithGenericType = superClassNameWithoutGenericType + "<" + nativeInterface + ">";
    else
      superClassNameWithGenericType = superClassNameWithoutGenericType;

    isUnionWithNonEnumeration = getSuperType() != null && ((SimpleTypePlan<?>)getSuperType()).isUnionWithNonEnumeration();
    analyzeUnion(model);
  }

  private void analyzeUnion(final SimpleTypeModel<?> model) {
    for (final Model child : model.getChildren()) {
      if (child instanceof SimpleTypeModel)
        analyzeUnion((SimpleTypeModel<?>)child);

      if (!(child instanceof UnionModel))
        continue;

      isUnion = true;
      for (final SimpleTypeModel<?> memberType : ((UnionModel)child).getMemberTypes()) {
        if (memberType.getEnumerations().size() != 0)
          continue;

        isUnionWithNonEnumeration = true;
        break;
      }
    }
  }

  public boolean isUnion() {
    return isUnion;
  }

  public boolean isUnionWithNonEnumeration() {
    return isUnionWithNonEnumeration;
  }

  public final String getBaseNonXSTypeClassName() {
    return baseNonXSTypeClassName;
  }

  public final UniqueQName getBaseXSItemTypeName() {
    return baseXSItemTypeName;
  }

  public final boolean isList() {
    return list;
  }

  @Override
  public final String getNativeItemClassNameInterface() {
    return nativeInterface;
  }

  public final String getNativeNonEnumItemClassNameInterface() {
    return nativeNonEnumInterface;
  }

  @Override
  public final String getNativeItemClassNameImplementation() {
    return nativeImplementation;
  }

  public final String getNativeNonEnumItemClassNameImplementation() {
    return nativeNonEnumImplementation;
  }

  @Override
  public final String getNativeFactory() {
    return nativeFactory;
  }

  public final String getNativeFactoryNonEnum() {
    return nonEnumNativeFactory;
  }

  @Override
  public String getSuperClassNameWithoutGenericType() {
    return superClassNameWithoutGenericType;
  }

  @Override
  public final LinkedHashSet<EnumerationPlan> getEnumerations() {
    return enumerations == null ? enumerations = Plan.analyze(getModel().getEnumerations(), this) : enumerations;
  }

  public final String getNativeItemClassName() {
    return nativeItemClassName;
  }

  public final String getNativeNonEnumItemClassName() {
    return nativeNonEnumItemClassName;
  }

  @Override
  public final boolean hasEnumerations() {
    return hasEnumerations == null ? hasEnumerations = hasEnumerations(getModel()) : hasEnumerations;
  }

  @Override
  public final boolean hasSuperEnumerations() {
    if (hasSuperEnumerations != null)
      return hasSuperEnumerations;

    return hasSuperEnumerations = hasEnumerations(getModel().getSuperType());
  }

  public final Collection<PatternPlan> getPatterns() {
    return patterns == null ? patterns = Plan.analyze(getModel().getPatterns(), this) : patterns;
  }

  @Override
  public String getSuperClassNameWithGenericType() {
    return superClassNameWithGenericType;
  }

  @Override
  public Plan<?> getSuperType() {
    if (parsedSuperType)
      return superType;

    superType = parseNamedPlan(getClass(), superTypeName);
    if (superType == this)
      return superType = null;

    parsedSuperType = true;
    return superType;
  }
}