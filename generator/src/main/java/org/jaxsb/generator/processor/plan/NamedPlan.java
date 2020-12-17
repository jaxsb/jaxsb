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

package org.jaxsb.generator.processor.plan;

import java.util.HashMap;

import org.jaxsb.compiler.lang.Prefix;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.NamedModel;
import org.jaxsb.generator.processor.plan.element.AttributeGroupPlan;
import org.jaxsb.generator.processor.plan.element.ComplexTypePlan;
import org.jaxsb.generator.processor.plan.element.GroupPlan;
import org.jaxsb.generator.processor.plan.element.SimpleTypePlan;
import org.jaxsb.runtime.CompilerFailureException;

@SuppressWarnings("rawtypes")
public abstract class NamedPlan<T extends NamedModel> extends Plan<T> implements Nameable<Plan<T>> {
  protected static NamedPlan<?> parseNamedPlan(final Class<? extends NamedPlan> cls, final UniqueQName name) {
    return getNamedPlanMap(cls).get(name);
  }

  private static HashMap<UniqueQName,NamedPlan<?>> getNamedPlanMap(final Class<? extends NamedPlan> cls) {
    if (SimpleTypePlan.class.isAssignableFrom(cls))
      return simpleTypes;

    if (ComplexTypePlan.class.isAssignableFrom(cls))
      return complexTypes;

    if (AttributeGroupPlan.class.isAssignableFrom(cls))
      return attributeGroups;

    if (GroupPlan.class.isAssignableFrom(cls))
      return groups;

    return null;
  }

  private static final HashMap<UniqueQName,NamedPlan<?>> simpleTypes = new HashMap<>();
  private static final HashMap<UniqueQName,NamedPlan<?>> complexTypes = new HashMap<>();
  private static final HashMap<UniqueQName,NamedPlan<?>> attributeGroups = new HashMap<>();
  private static final HashMap<UniqueQName,NamedPlan<?>> groups = new HashMap<>();

  private final UniqueQName name;

  public NamedPlan(final T model, final Plan<?> parent) {
    super(model, parent);
    final UniqueQName modelName = model.getName();
    if (modelName != null) {
      final Prefix prefix = modelName.getPrefix();
      if (prefix == null)
        throw new CompilerFailureException("[ERROR] No prefix exists for namespace {" + modelName.getNamespaceURI() + "}. Is the binding for this namespace defined in the bindings configuration?");

      name = UniqueQName.getInstance(modelName.getNamespaceURI(), modelName.getLocalPart(), prefix.toString());
      final HashMap<UniqueQName,NamedPlan<?>> map = getNamedPlanMap(getClass());
      if (map != null)
        map.put(name, this);
    }
    else {
      name = null;
    }

    if (this instanceof AnyablePlan)
      return;

    if (name == null)
      throw new IllegalArgumentException(getClass().getSimpleName() + " with no name? what's going on?");
  }

  @Override
  public final UniqueQName getName() {
    return name;
  }
}