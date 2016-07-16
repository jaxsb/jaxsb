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

package org.safris.xsb.generator.lexer.processor.normalize.element;

import java.util.Collection;

import org.safris.xsb.generator.lexer.lang.LexerFailureException;
import org.safris.xsb.generator.lexer.lang.UniqueQName;
import org.safris.xsb.generator.lexer.processor.model.Model;
import org.safris.xsb.generator.lexer.processor.model.element.ListModel;
import org.safris.xsb.generator.lexer.processor.model.element.SimpleTypeModel;
import org.safris.xsb.generator.lexer.processor.model.element.UnionModel;
import org.safris.xsb.generator.lexer.processor.normalize.Normalizer;
import org.safris.xsb.generator.lexer.processor.normalize.NormalizerDirectory;

public final class ListNormalizer extends Normalizer<ListModel> {
  private final SimpleTypeNormalizer simpleTypeNormalizer = (SimpleTypeNormalizer)getDirectory().lookup(SimpleTypeModel.class);

  public ListNormalizer(final NormalizerDirectory directory) {
    super(directory);
  }

  @Override
  protected void stage1(final ListModel model) {
  }

  @Override
  protected void stage2(final ListModel model) {
    final Collection<SimpleTypeModel<?>> itemTypes = model.getItemType();
    if (itemTypes == null || itemTypes.size() != 1)
      return;
    // throw new LexerError("This should not happen, right?!"); // This happens in XMLSchema.xsd .. returning may not be a good idea, as UnionModel and
    // ListModel have intricate relationship wrt the stages in the normalizers

    final SimpleTypeModel<?> itemType = itemTypes.iterator().next();
    SimpleTypeModel<?> type = itemType;
    if (type instanceof SimpleTypeModel.Reference) {
      type = simpleTypeNormalizer.parseSimpleType(type.getName());
      if (type == null) {
        if (!UniqueQName.XS.getNamespaceURI().equals(itemType.getName().getNamespaceURI()))
          throw new LexerFailureException("type == null for ");

        type = SimpleTypeModel.Undefined.parseSimpleType(itemType.getName());
      }

      model.setItemType(type);
    }
  }

  @Override
  protected void stage3(final ListModel model) {
    // FIXME: This is done here because XMLSchema has a construct that does not comply with other situations I've seen
    stage2(model);
  }

  @Override
  protected void stage4(final ListModel model) {
    if (model.getItemType() == null)
      return;
    // throw new LexerError("This can't happen."); // This happens in XMLSchema.xsd .. returning may not be a good idea, as UnionModel and ListModel have
    // intricate relationship wrt the stages in the normalizers

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      // If there is a higher level union, then this list is one of the memberTypes
      if (parent instanceof UnionModel) {
        ((UnionModel)parent).getMemberTypes().addAll(model.getItemType());
        break;
      }
    }
  }

  @Override
  protected void stage5(final ListModel model) {
    // FIXME: This is done here because XMLSchema has a construct that does not comply with other situations I've seen
    stage4(model);
  }

  @Override
  protected void stage6(final ListModel model) {
    if (model.getItemType() == null)
      throw new LexerFailureException("This can't happen.");

    Model parent = model;
    while ((parent = parent.getParent()) != null) {
      // If this list defines a named simpleType
      if (parent instanceof SimpleTypeModel && ((SimpleTypeModel<?>)parent).getName() != null) {
        final SimpleTypeModel<?> simpleTypeModel = (SimpleTypeModel<?>)parent;
        simpleTypeModel.setSuperType(SimpleTypeModel.Undefined.parseSimpleType(UniqueQName.getInstance(UniqueQName.XS.getNamespaceURI(), "anySimpleType")));
        simpleTypeModel.setItemTypes(model.getItemType());
        simpleTypeModel.setList(true);
        break;
      }
    }
  }
}