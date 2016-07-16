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

import java.lang.reflect.AccessibleObject;
import java.util.List;

import org.safris.xsb.generator.lexer.lang.UniqueQName;

public final class NativeBinding {
  private final UniqueQName name;
  private final GenericClass baseClass;
  private final GenericClass nativeClass;
  private final AccessibleObject factoryMethod;
  private final boolean list;

  public NativeBinding(final UniqueQName name, final GenericClass baseClass, GenericClass nativeClass, final AccessibleObject factoryMethod) {
    if (name == null)
      throw new NullPointerException("name == null");

    if (baseClass == null)
      throw new NullPointerException("baseClass<?> == null");

    this.name = name;
    this.baseClass = baseClass;
    this.nativeClass = nativeClass;
    this.factoryMethod = factoryMethod;
    this.list = nativeClass != null ? nativeClass.isList() : false;
  }

  public NativeBinding(final UniqueQName name, final GenericClass baseClass, final GenericClass nativeClass) {
    this(name, baseClass, nativeClass, null);
  }

  public NativeBinding(final UniqueQName name, final GenericClass baseClass) {
    this(name, baseClass, null, null);
  }

  public boolean isList() {
    return list;
  }

  public UniqueQName getName() {
    return name;
  }

  public GenericClass getBaseClass() {
    return baseClass;
  }

  public GenericClass getNativeClass() {
    return nativeClass;
  }

  public AccessibleObject getFactoryMethod() {
    return factoryMethod;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof NativeBinding))
      return false;

    final NativeBinding nativeBinding = (NativeBinding)obj;
    return name.equals(nativeBinding.name) && baseClass.equals(nativeBinding.baseClass) && nativeClass.equals(nativeBinding.nativeClass);
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public String toString() {
    return name.toString() + "\n" + baseClass.toString() + "\n" + nativeClass.toString();
  }

  public static final class GenericClass {
    private final Class<?> cls;
    private final Class<?> type;
    private Boolean list = null;

    public GenericClass(final Class<?> cls, final Class<?> type) {
      if (cls == null)
        throw new NullPointerException("cls == null");

      this.cls = cls;
      this.type = type;
    }

    public GenericClass(final Class<?> cls) {
      this(cls, null);
    }

    public final Class<?> getCls() {
      return cls;
    }

    public final Class<?> getType() {
      return type;
    }

    protected boolean isList() {
      return list == null ? list = List.class.isAssignableFrom(cls) : list;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof GenericClass))
        return false;

      final GenericClass genericClass = (GenericClass)obj;
      return cls.equals(genericClass.cls) && (type == null && genericClass.type == null || type != null && type.equals(genericClass.type));
    }

    @Override
    public int hashCode() {
      return toString().hashCode();
    }

    @Override
    public String toString() {
      return type != null ? cls.getName() + "<" + type.getName() + ">" : cls.getName();
    }
  }
}