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

package org.jaxsb.compiler.processor.normalize.element;

import java.util.ArrayList;
import java.util.Collection;

import org.jaxsb.compiler.lang.LexerFailureException;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.element.ListModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.jaxsb.compiler.processor.model.element.UnionModel;
import org.jaxsb.compiler.processor.normalize.Normalizer;
import org.jaxsb.compiler.processor.normalize.NormalizerDirectory;

public final class UnionNormalizer extends Normalizer<UnionModel> {
  private final SimpleTypeNormalizer simpleTypeNormalizer = (SimpleTypeNormalizer)getDirectory().lookup(SimpleTypeModel.class);

  public UnionNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final UnionModel model) {
  }

  private SimpleTypeModel<?> resolve(final SimpleTypeModel<?> memberType) {
    if (!(memberType instanceof SimpleTypeModel.Reference))
      return memberType;

    final SimpleTypeModel<?> resolvedMemberType = simpleTypeNormalizer.parseSimpleType(memberType.getName());
    if (resolvedMemberType != null)
      return resolvedMemberType;

    if (!UniqueQName.XS.getNamespaceURI().equals(memberType.getName().getNamespaceURI()))
      throw new LexerFailureException("type == null for " + memberType.getName());

    return SimpleTypeModel.Undefined.parseSimpleType(memberType.getName());
  }

  @Override
  protected void stage2(final UnionModel model) {
    final Collection<SimpleTypeModel<?>> memberTypes = model.getMemberTypes();
    if (memberTypes != null) {
      final Collection<SimpleTypeModel<?>> resolvedMemberTypes = new ArrayList<>(memberTypes.size());
      for (final SimpleTypeModel<?> memberType : memberTypes) // [C]
        resolvedMemberTypes.add(resolve(memberType));

      final ArrayList<Model> children = model.getChildren();
      for (int i = 0, i$ = children.size(); i < i$; ++i) { // [RA]
        final Model child = children.get(i);
        if (child instanceof SimpleTypeModel)
          resolvedMemberTypes.add(resolve((SimpleTypeModel<?>)child));
      }

      if (resolvedMemberTypes.isEmpty())
        throw new LexerFailureException("I dont think this can happen");

      model.getMemberTypes().clear();
      model.getMemberTypes().addAll(resolvedMemberTypes);
    }
  }

  @Override
  protected void stage3(final UnionModel model) {
  }

  @Override
  protected void stage4(final UnionModel model) {
  }

  @Override
  protected void stage5(final UnionModel model) {
    if (model.getMemberTypes() == null || model.getMemberTypes().size() == 0)
      throw new LexerFailureException("I dont think this can happen");

    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
      // Either there is a higher level union that we want to combine into this union
      if (parent instanceof UnionModel) {
        ((UnionModel)parent).addUnion(model);
        break;
      }
      // Or there is a list that of a union
      else if (parent instanceof ListModel) {
        if (((ListModel)parent).getItemType() != null)
          throw new LexerFailureException("Don't know how this happened, but the <list> has a memberType already and it has a union under it also.");

        ((ListModel)parent).setItemType(model);
        break;
      }
    }
  }

  @Override
  protected void stage6(final UnionModel model) {
    if (model.getMemberTypes() == null || model.getMemberTypes().size() == 0)
      throw new LexerFailureException("I dont think this can happen");

    for (Model parent = model; (parent = parent.getParent()) != null;) { // [X]
      // If this union defines a named simpleType
      if (parent instanceof SimpleTypeModel && ((SimpleTypeModel<?>)parent).getName() != null) {
        final SimpleTypeModel<?> simpleTypeModel = (SimpleTypeModel<?>)parent;
        simpleTypeModel.setSuperType(SimpleTypeModel.Undefined.parseSimpleType(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anySimpleType")));
        simpleTypeModel.setItemTypes(model.getNormalizedMemberTypes());
        break;
      }
    }
  }
}