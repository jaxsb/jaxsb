/* Copyright (c) 2006 Seva Safris
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

package org.safris.xsb.generator.compiler.lang;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.safris.xsb.generator.lexer.lang.UniqueQName;
import org.safris.xsb.generator.lexer.processor.Nameable;
import org.safris.xsb.generator.lexer.processor.model.Model;
import org.safris.xsb.generator.lexer.processor.model.MultiplicableModel;
import org.safris.xsb.generator.lexer.processor.model.RedefineableModel;
import org.safris.xsb.generator.lexer.processor.model.element.ElementModel;

@SuppressWarnings("rawtypes")
public final class ElementWrapper extends Model implements Nameable {
  public static LinkedHashSet<ElementWrapper> asSet(final LinkedHashSet<MultiplicableModel> multiplicableModels) {
    final LinkedHashSet<ElementWrapper> elementWrappers = new LinkedHashSet<ElementWrapper>();
    asSet(multiplicableModels, elementWrappers, 1, 1, new HashSet<UniqueQName>());
    return elementWrappers;
  }

  private static void asSet(final LinkedHashSet<MultiplicableModel> multiplicableModels, final LinkedHashSet<ElementWrapper> elementWrappers, int min, final int max, final Collection<UniqueQName> redefines) {
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

      if (multiplicableModel instanceof ElementModel)
        elementWrappers.add(new ElementWrapper((ElementModel)multiplicableModel, minOccurs, maxOccurs));
      else
        asSet(multiplicableModel.getMultiplicableModels(), elementWrappers, minOccurs, maxOccurs, redefines);
    }
  }

  private final ElementModel elementModel;
  private final int minOccurs;
  private final int maxOccurs;

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

  public final int getMaxOccurs() {
    return maxOccurs;
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
    return elementModel.hashCode();
  }
}