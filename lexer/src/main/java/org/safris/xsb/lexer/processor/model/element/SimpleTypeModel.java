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

package org.safris.xsb.generator.lexer.processor.model.element;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.safris.xsb.generator.lexer.lang.UniqueQName;
import org.safris.xsb.generator.lexer.processor.Referenceable;
import org.safris.xsb.generator.lexer.processor.Undefineable;
import org.safris.xsb.generator.lexer.processor.model.AliasModel;
import org.safris.xsb.generator.lexer.processor.model.DocumentableModel;
import org.safris.xsb.generator.lexer.processor.model.EnumerableModel;
import org.safris.xsb.generator.lexer.processor.model.Model;
import org.safris.xsb.generator.lexer.processor.model.PatternableModel;
import org.safris.xsb.generator.lexer.processor.model.RedefineableModel;
import org.safris.xsb.generator.lexer.processor.model.TypeableModel;
import org.safris.xsb.generator.lexer.schema.attribute.Final;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SimpleTypeModel<T extends SimpleTypeModel<?>> extends AliasModel implements DocumentableModel, EnumerableModel, PatternableModel, RedefineableModel<T>, TypeableModel<T> {
  private final LinkedHashSet<EnumerationModel> enumerations = new LinkedHashSet<EnumerationModel>();
  private final LinkedHashSet<PatternModel> patterns = new LinkedHashSet<PatternModel>();

  private T redefine = null;
  private SimpleTypeModel<?> superType = null;
  private Collection<SimpleTypeModel<?>> itemType = null;

  private boolean restriction = false;
  private boolean list = false;

  private Final _final = null;

  protected SimpleTypeModel(final Node node, final Model parent) {
    super(node, parent);
    if (node == null)
      return;

    final NamedNodeMap attributes = node.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      if ("final".equals(attribute.getLocalName()))
        _final = Final.parseFinal(attribute.getNodeValue());
    }
  }

  @Override
  public final void setRedefine(final T redefine) {
    this.redefine = redefine;
  }

  @Override
  public final T getRedefine() {
    return redefine;
  }

  public final void setSuperType(final SimpleTypeModel<?> superType) {
    if (!this.equals(superType))
      this.superType = superType;
  }

  @Override
  public SimpleTypeModel<?> getSuperType() {
    return superType;
  }

  public final void setItemTypes(final Collection<SimpleTypeModel<?>> itemType) {
    this.itemType = itemType;
  }

  public final Collection<SimpleTypeModel<?>> getItemTypes() {
    return itemType;
  }

  public final void setRestriction(final boolean isRestriction) {
    this.restriction = isRestriction;
  }

  public final boolean isRestriction() {
    return restriction;
  }

  public final void setList(final boolean list) {
    this.list = list;
  }

  public final boolean isList() {
    return list;
  }

  public final Final getFinal() {
    return _final;
  }

  @Override
  public final void addEnumeration(final EnumerationModel enumeration) {
    if (!enumerations.contains(enumeration))
      enumerations.add(enumeration);
  }

  @Override
  public final LinkedHashSet<EnumerationModel> getEnumerations() {
    return enumerations;
  }

  @Override
  public final void addPattern(final PatternModel pattern) {
    this.patterns.add(pattern);
  }

  @Override
  public final LinkedHashSet<PatternModel> getPatterns() {
    return patterns;
  }

  public static final class Reference extends SimpleTypeModel<SimpleTypeModel<?>> implements Referenceable {
    private static final Map<UniqueQName,Reference> all = new HashMap<UniqueQName,Reference>();

    public static Reference parseSimpleType(final UniqueQName name) {
      Reference type = all.get(name);
      if (type != null)
        return type;

      type = new Reference(null);
      type.setName(name);
      Reference.all.put(name, type);
      return type;
    }

    protected Reference(final Model parent) {
      super(null, parent);
    }
  }

  public static final class Undefined extends SimpleTypeModel<SimpleTypeModel<?>> implements Undefineable {
    private static final Map<UniqueQName,Undefined> all = new HashMap<UniqueQName,Undefined>();

    public static Undefined parseSimpleType(final UniqueQName name) {
      Undefined type = all.get(name);
      if (type != null)
        return type;

      type = new Undefined(null);
      type.setName(name);
      Undefined.all.put(name, type);
      return type;
    }

    protected Undefined(final Model parent) {
      super(null, parent);
    }
  }
}