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

package org.safris.xml.generator.lexer.processor.normalize.element;

import java.util.ArrayList;
import java.util.Collection;

import org.safris.xml.generator.lexer.lang.LexerError;
import org.safris.xml.generator.lexer.lang.UniqueQName;
import org.safris.xml.generator.lexer.processor.model.Model;
import org.safris.xml.generator.lexer.processor.model.element.ListModel;
import org.safris.xml.generator.lexer.processor.model.element.SimpleTypeModel;
import org.safris.xml.generator.lexer.processor.model.element.UnionModel;
import org.safris.xml.generator.lexer.processor.normalize.Normalizer;
import org.safris.xml.generator.lexer.processor.normalize.NormalizerDirectory;

public final class UnionNormalizer extends Normalizer<UnionModel> {
  private final SimpleTypeNormalizer simpleTypeNormalizer = (SimpleTypeNormalizer)getDirectory().lookup(SimpleTypeModel.class);

  public UnionNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final UnionModel model) {
  }

  @Override
  protected void stage2(final UnionModel model) {
    final Collection<SimpleTypeModel<?>> memberTypes = model.getMemberTypes();
    if (memberTypes != null) {
      final Collection<SimpleTypeModel<?>> resolvedMemberTypes = new ArrayList<SimpleTypeModel<?>>(memberTypes.size());
      SimpleTypeModel<?> resolvedMemberType;
      for (final SimpleTypeModel<?> memberType : memberTypes) {
        if (memberType instanceof SimpleTypeModel.Reference) {
          resolvedMemberType = simpleTypeNormalizer.parseSimpleType(memberType.getName());
          if (resolvedMemberType == null) {
            if (!UniqueQName.XS.getNamespaceURI().equals(memberType.getName().getNamespaceURI()))
              throw new LexerError("type == null for " + memberType.getName());

            resolvedMemberType = SimpleTypeModel.Undefined.parseSimpleType(memberType.getName());
          }
        }
        else
          resolvedMemberType = memberType;

        resolvedMemberTypes.add(resolvedMemberType);
      }

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
      throw new LexerError("I dont think this can happen.");

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      // Either there is a higher level union that we want to combine into this union
      if (parent instanceof UnionModel) {
        ((UnionModel)parent).addUnion(model);
        break;
      }
      // Or there is a list that of a union
      else if (parent instanceof ListModel) {
        if (((ListModel)parent).getItemType() != null)
          throw new LexerError("Dont know how this happened, but the <list> has a memberType already and it has a union under it also.");

        ((ListModel)parent).setItemType(model);
        break;
      }
    }
  }

  @Override
  protected void stage6(final UnionModel model) {
    if (model.getMemberTypes() == null || model.getMemberTypes().size() == 0)
      throw new LexerError("I dont think this can happen.");

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
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