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

package org.safris.xsb.compiler.processor.plan;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.safris.commons.pipeline.PipelineEntity;
import org.safris.xsb.compiler.lang.CompilerFailureException;
import org.safris.xsb.compiler.lang.ElementWrapper;
import org.safris.xsb.compiler.processor.plan.element.ElementPlan;
import org.safris.xsb.lexer.lang.UniqueQName;
import org.safris.xsb.lexer.processor.model.EnumerableModel;
import org.safris.xsb.lexer.processor.model.Model;
import org.safris.xsb.lexer.processor.model.element.EnumerationModel;
import org.safris.xsb.lexer.processor.model.element.SimpleTypeModel;

public abstract class Plan<T extends Model> implements PipelineEntity {
  public ElementPlan elementRefExistsInParent(final UniqueQName name) {
    return null;
  }

  public static <A extends Plan<?>>LinkedHashSet<A> analyze(Collection<? extends Model> models, final Plan<?> owner) {
    final LinkedHashSet<A> plans;
    if (models != null && models.size() != 0) {
      plans = new LinkedHashSet<A>(models.size());
      for (final Object model : models) {
        // If there is a name conflict with a parent type, then skip
        // adding this in duplicate. Otherwise there will be conflicts
        // with method names.
//              if (owner.getSuperType() != null && model instanceof Nameable && owner.getSuperType().elementRefExistsInParent(((Nameable)model).getName()))
//                  continue;

        if (model instanceof ElementWrapper) {
          final ElementWrapper element = (ElementWrapper)model;
          final A plan = Plan.<A>analyze(element.getElementModel(), owner);
          ((ElementPlan)plan).setMinOccurs(element.getMinOccurs());
          ((ElementPlan)plan).setMaxOccurs(element.getMaxOccurs());
          plans.add(plan);
        }
        else if (model instanceof Model) {
          plans.add(Plan.<A>analyze((Model)model, owner));
        }
      }
    }
    else {
      plans = new LinkedHashSet<A>(0);
    }

    return plans;
  }

  // FIXME: Forgot this section here!!!
  @SuppressWarnings("unchecked")
  public static <A extends Plan<?>>A analyze(final Model model, final Plan<?> owner) {
    if (model == null)
      return null;

    final String planName = Plan.class.getPackage().getName() + ".element." + model.getClass().getSimpleName().substring(0, model.getClass().getSimpleName().indexOf("Model")) + "Plan";
    A plan = null;
    try {
      final Constructor<?> constructor = Class.forName(planName).getConstructor(model.getClass(), Plan.class);
      plan = (A)constructor.newInstance(model, owner);
    }
    catch (final ClassNotFoundException e) {
      throw new CompilerFailureException("Class<?> not found for element [" + model.getClass().getSimpleName() + "]");
    }
    catch (final Exception e) {
      throw new CompilerFailureException(e);
    }

    return plan;
  }

  protected static boolean hasEnumerations(final EnumerableModel model) {
    final Collection<EnumerationModel> enumerations = model.getEnumerations();
    final boolean hasEnumerations = enumerations != null && enumerations.size() != 0;
    if (hasEnumerations)
      return hasEnumerations;

    if (!(model instanceof SimpleTypeModel))
      return false;

    SimpleTypeModel<?> restrictionType = (SimpleTypeModel<?>)model;
    while ((restrictionType = restrictionType.getSuperType()) != null)
      return hasEnumerations(restrictionType);

    return hasEnumerations;
  }

  private final T model;
  private final Plan<?> parent;

  public Plan(final T model, final Plan<?> parent) {
    this.model = model;
    this.parent = parent;
  }

  public T getModel() {
    return model;
  }

  public final Plan<?> getParent() {
    return parent;
  }

  public Plan<?> getSuperType() {
    return null;
  }
}