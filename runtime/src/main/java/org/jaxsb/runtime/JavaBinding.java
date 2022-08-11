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

import java.util.regex.Pattern;

import org.jaxsb.compiler.lang.Prefix;
import org.jaxsb.compiler.lang.UniqueQName;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.compiler.processor.model.Model;
import org.jaxsb.compiler.processor.model.element.AttributeModel;
import org.jaxsb.compiler.processor.model.element.ElementModel;
import org.jaxsb.compiler.processor.model.element.NotationModel;
import org.jaxsb.compiler.processor.model.element.RedefineModel;
import org.jaxsb.compiler.processor.model.element.SchemaModel;
import org.jaxsb.compiler.processor.model.element.SimpleTypeModel;
import org.libj.lang.Strings;

public final class JavaBinding {
  private static final Pattern classPattern = Pattern.compile("Class_*"); // FIXME: I think this regex is wrong!
  private static final Pattern keywordPattern = Pattern.compile("_*(abstract)|(assert)|(boolean)|(break)|(byte)|(case)|(catch)|(char)|(class)|(const)|(continue)|(default)|(do)|(double)|(else)|(enum)|(extends)|(false)|(final)|(finally)|(float)|(for)|(goto)|(if)|(implements)|(import)|(instanceof)|(int)|(interface)|(long)|(native)|(new)|(null)|(package)|(private)|(protected)|(public)|(return)|(short)|(static)|(strictfp)|(super)|(switch)|(synchronized)|(this)|(throw)|(throws)|(transient)|(true)|(try)|(void)|(volatile)|(while)");

  private static final String ATTRIBUTE_SUFFIX = "$";
  private static final String NOTATION_MIDFIX = "$";
  private static final String COMPLEXTYPE_PREFIX = "$";

  private static String toJavaIdentifier(final String ncName) {
    final StringBuilder builder = new StringBuilder(ncName.length());
    for (int i = 0, i$ = ncName.length(); i < i$; ++i) { // [N]
     final char ch = ncName.charAt(i);
     if (ch == '-')
       builder.append('$').append('_');
     else if (ch == '.')
       builder.append('$');
     else
       builder.append(ch);
    }

    return builder.toString();
  }

  public static String getInstanceName(final Model model) {
    if (!(model instanceof Nameable) || ((Nameable<?>)model).getName() == null)
      throw new CompilerFailureException("Method being called on a model with no name");

    final Prefix prefix = getPrefix(model);
    final boolean nested = JavaBinding.isNested(model);
    final String local = nested ? "Local" : "Ref";
    if (model instanceof AttributeModel)
      return "_" + prefix + toJavaIdentifier(((SimpleTypeModel<?>)model).getName().getLocalPart()) + ATTRIBUTE_SUFFIX + local;

    if (model instanceof ElementModel)
      return "_" + prefix + toJavaIdentifier(((SimpleTypeModel<?>)model).getName().getLocalPart()) + local;

    if (model instanceof NotationModel)
      return "_" + prefix + NOTATION_MIDFIX + toJavaIdentifier(((NotationModel)model).getName().getLocalPart()) + local;

    if (model instanceof SimpleTypeModel)
      return "_" + COMPLEXTYPE_PREFIX.toLowerCase() + prefix + toJavaIdentifier(((SimpleTypeModel<?>)model).getName().getLocalPart()) + local;

    throw new CompilerFailureException("model is not instanceof {AttributeModel,ElementModel,NotationModel,SimpleTypeModel}");
  }

  public static String getClassName(final Model model) {
    if (model == null)
      return null;

    if (!(model instanceof Nameable) || ((Nameable<?>)model).getName() == null)
      throw new CompilerFailureException("Method being called on a model with no name");

    final Nameable<?> nameable = ((Nameable<?>)model);
    return nameable.getName().getNamespaceURI().getNamespaceBinding().getClassName() + "." + getClassSimpleName(model);
  }

  private static boolean isNested(final Model model) {
    return !(model.getParent() instanceof SchemaModel || (model.getParent() instanceof RedefineModel && model.getParent().getParent() instanceof SchemaModel) || (model instanceof Nameable && XSTypeDirectory.parseType(((Nameable<?>)model).getName()) != null));
  }

  private static Prefix getPrefix(final Model model) {
    final boolean nested = JavaBinding.isNested(model);
    return !nested || model.isQualified(nested) ? UniqueQName.getPrefix(((Nameable<?>)model).getName().getNamespaceURI()) : Prefix.EMPTY;
  }

  public static String getClassSimpleName(final Model model) {
    return getClassSimpleName(model, false, true);
  }

  public static String getMethodName(final Model model) {
    final String methodName = getClassSimpleName(model, true, false);
    return classPattern.matcher(methodName).matches() ? methodName + "_" : methodName;
  }

  private static boolean isReserved(final String string) {
    return keywordPattern.matcher(string).matches();
  }

  private static String getClassSimpleName(final Model model, final boolean withPrefix, final boolean fixReserved) {
    if (!(model instanceof Nameable) || ((Nameable<?>)model).getName() == null)
      throw new CompilerFailureException("Method being called on a model with no name");

    String simpleName = Strings.flipFirstCap(toJavaIdentifier(((Nameable<?>)model).getName().getLocalPart()));
    if (fixReserved && isReserved(simpleName))
      simpleName = "_" + simpleName;

    if (withPrefix)
      simpleName = Strings.flipFirstCap(getPrefix(model).toString()) + simpleName;

    if (model instanceof AttributeModel)
      return simpleName + ATTRIBUTE_SUFFIX;

    if (model instanceof ElementModel)
      return simpleName;

    if (model instanceof NotationModel)
      return simpleName + "Notation";

    if (model instanceof SimpleTypeModel)
      return COMPLEXTYPE_PREFIX + simpleName;

    throw new CompilerFailureException("model is not instanceof {AttributeModel,ElementModel,NotationModel,SimpleTypeModel}");
  }


  private JavaBinding() {
  }
}