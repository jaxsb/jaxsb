/* Copyright (c) 2006 JAX-SB
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

package org.jaxsb.runtime;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.MultiplicableModel;
import org.jaxsb.compiler.processor.model.RedefineableModel;
import org.jaxsb.compiler.processor.model.element.ElementModel;

@SuppressWarnings("rawtypes")
public final class ElementWrapper extends Model implements Nameable {
  public static LinkedHashSet<ElementWrapper> asSet(final Collection<? extends MultiplicableModel> multiplicableModels) {
    final LinkedHashMap<ElementWrapper,ElementWrapper> elementWrappers = new LinkedHashMap<>();
    asSet(multiplicableModels, elementWrappers, 1, 1, new HashSet<>());
    return new LinkedHashSet<>(elementWrappers.values());
  }

  private static void asSet(final Collection<? extends MultiplicableModel> multiplicableModels, final LinkedHashMap<? super ElementWrapper,ElementWrapper> elementWrappers, final int min, final int max, final Collection<? super UniqueQName> redefines) {
    for (MultiplicableModel multiplicableModel : multiplicableModels) {
      // FIXME: the list used to track redefines seems BAD!!!
      if (multiplicableModel instanceof RedefineableModel && ((RedefineableModel<?>)multiplicableModel).getRedefine() != null && !redefines.contains(((Nameable<?>)multiplicableModel).getName())) {
        multiplicableModel = (MultiplicableModel)((RedefineableModel<?>)multiplicableModel).getRedefine();
        redefines.add(((Nameable<?>)multiplicableModel).getName());
      }

      int maxOccurs = multiplicableModel.getMaxOccurs().getValue();
      if (maxOccurs != Integer.MAX_VALUE)
        maxOccurs *= max;

      int minOccurs = multiplicableModel.getMinOccurs().getValue();
      minOccurs *= min;

      if (multiplicableModel instanceof ElementModel) {
        final ElementWrapper elementWrapper = new ElementWrapper((ElementModel)multiplicableModel, minOccurs, maxOccurs);
        final ElementWrapper exists = elementWrappers.get(elementWrapper);
        if (exists != null) {
          exists.setMinOccurs(exists.getMinOccurs() + minOccurs);
          exists.setMaxOccurs(exists.getMaxOccurs() + maxOccurs);
        }
        else {
          elementWrappers.put(elementWrapper, elementWrapper);
        }
      }
      else {
        asSet(multiplicableModel.getMultiplicableModels(), elementWrappers, minOccurs, maxOccurs, redefines);
      }
    }
  }

  private final ElementModel elementModel;
  private int minOccurs;
  private int maxOccurs;

  public ElementWrapper(final ElementModel elementModel, final int minOccurs, final int maxOccurs) {
    super(null, elementModel.getParent());
    this.elementModel = elementModel;
    this.minOccurs = minOccurs;
    this.maxOccurs = maxOccurs;
  }

  public final ElementModel getElementModel() {
    return elementModel;
  }

  public final int getMinOccurs() {
    return minOccurs;
  }

  public void setMinOccurs(final int minOccurs) {
    this.minOccurs = minOccurs;
  }

  public final int getMaxOccurs() {
    return maxOccurs;
  }

  public void setMaxOccurs(final int maxOccurs) {
    this.maxOccurs = maxOccurs;
  }

  @Override
  public UniqueQName getName() {
    return elementModel.getName();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof ElementWrapper))
      return false;

    return elementModel.equals(((ElementWrapper)obj).elementModel);
  }

  @Override
  public int hashCode() {
    return 31 + elementModel.hashCode();
  }
}