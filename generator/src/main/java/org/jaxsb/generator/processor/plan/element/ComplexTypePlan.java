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
import java.util.LinkedHashSet;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.model.MixableModel;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.TypeableModel;
import org.jaxsb.compiler.processor.model.element.ComplexTypeModel;
import org.jaxsb.compiler.processor.model.element.SimpleContentModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.jaxsb.generator.processor.plan.AttributablePlan;
import org.jaxsb.generator.processor.plan.ElementablePlan;
import org.jaxsb.generator.processor.plan.MixablePlan;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.runtime.ElementWrapper;
import org.jaxsb.runtime.XSTypeDirectory;

public class ComplexTypePlan<T extends ComplexTypeModel<?>> extends SimpleTypePlan<T> implements AttributablePlan, ElementablePlan, MixablePlan {
  private final Boolean mixed;

  private Boolean mixedType;
  private boolean simpleContent;

  private LinkedHashSet<AttributePlan> attributes;
  private LinkedHashSet<ElementPlan> elements;

  @Override
  public ElementPlan elementRefExistsInParent(final UniqueQName name) {
    // FIXME: This is slow!
    if (getElements() != null)
      for (final ElementPlan element : getElements()) // [S]
        if (element.getName() != null && element.getName().equals(name))
          return element;

    if (getSuperType() == null)
      return null;

    return getSuperType().elementRefExistsInParent(name);
  }

  @SuppressWarnings("unchecked")
  public ComplexTypePlan(final T model, final Plan<?> parent) {
    super(model.getRedefine() != null ? (T)model.getRedefine() : model, parent);
    mixed = getModel().getMixed();
    final ArrayList<Model> children = model.getChildren();
    for (int i = 0, i$ = children.size(); i < i$; ++i) { // [RA]
      final Model child = children.get(i);
      if (child instanceof SimpleContentModel) {
        simpleContent = true;
        break;
      }
    }
  }

  public final boolean hasSimpleContent() {
    return simpleContent;
  }

  @Override
  public final LinkedHashSet<AttributePlan> getAttributes() {
    return attributes == null ? attributes = Plan.analyze(getModel().getAttributes(), this) : attributes;
  }

  @Override
  public final LinkedHashSet<ElementPlan> getElements() {
    return elements == null ? elements = Plan.analyze(ElementWrapper.asSet(getModel().getMultiplicableModels()), this) : elements;
  }

  @Override
  public final Boolean getMixed() {
    return mixed;
  }

  @Override
  public final Boolean getMixedType() {
    if (mixedType != null)
      return mixedType;

    // this flag may be a HACK. I am using it to see if I have a restriction in the chain of
    // dependencies. If I do, then mixed="true" has to be stated explicitly.
    boolean isEverRestriction = false;
    TypeableModel<?> parent = getModel();
    boolean restriction = getModel().isRestriction();
    while ((parent = parent.getSuperType()) != null) {
      if (parent instanceof MixableModel && !restriction && ((MixableModel)parent).getMixed() != null && ((MixableModel)parent).getMixed())
        return mixedType = true;

      restriction = ((SimpleTypeModel<?>)parent).isRestriction();
      isEverRestriction = isEverRestriction || restriction;
    }

    parent = getModel();
    while ((parent = parent.getSuperType()) != null)
      if (parent instanceof MixableModel && ((MixableModel)parent).getMixed() != null)
        return mixedType = ((MixableModel)parent).getMixed();

    if (!isEverRestriction && XSTypeDirectory.ANYTYPE.getNativeBinding().getName().equals(getBaseXSItemTypeName()))
      return mixedType = true;

    return mixedType = false;
  }
}