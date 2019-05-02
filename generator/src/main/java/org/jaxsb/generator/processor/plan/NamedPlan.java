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
import java.util.Map;

import org.jaxsb.compiler.lang.Prefix;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.NamedModel;
import org.jaxsb.runtime.CompilerFailureException;

public abstract class NamedPlan<T extends NamedModel> extends Plan<T> implements Nameable<Plan<T>> {
  protected static NamedPlan<?> parseNamedPlan(final UniqueQName name) {
    return all.get(name);
  }

  private static final Map<UniqueQName,NamedPlan<?>> all = new HashMap<>();

  private final UniqueQName name;

  public NamedPlan(final T model, final Plan<?> parent) {
    super(model, parent);
    if (model.getName() != null) {
      final Prefix prefix = model.getName().getPrefix();
      if (prefix == null)
        throw new CompilerFailureException("[ERROR] No prefix exists for namespace {" + model.getName().getNamespaceURI() + "}. Is the binding for this namespace defined in the bindings configuration?");

      name = UniqueQName.getInstance(model.getName().getNamespaceURI(), model.getName().getLocalPart(), prefix.toString());
      all.put(name, this);
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