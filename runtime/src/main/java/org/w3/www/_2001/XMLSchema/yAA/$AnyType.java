/* Copyright (c) 2006 lib4j
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

package org.w3.www._2001.XMLSchema.yAA;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.libx4j.xsb.runtime.Binding;

public abstract class $AnyType extends $AnySimpleType {
  private static final long serialVersionUID = -6510869738097560771L;

  private final List<Binding> anys = new ArrayList<>(7);
  private final List<Binding> anys$ = new ArrayList<>(7);

  public $AnyType(final $AnyType binding) {
    super(binding);
  }

  public $AnyType(final Object text) {
    super(text);
  }

  protected $AnyType(final String text) {
    super();
  }

  protected $AnyType() {
    super();
  }

  public void addAny$(final Binding any) {
    this.anys$.add(any);
  }

  public List<Binding> getAny$() {
    return anys$;
  }

  public void addAny(final Binding any) {
    this.anys.add(any);
  }

  public List<Binding> getAny() {
    return anys;
  }

  @Override
  public $AnyType clone() {
    return new $AnyType(this) {
      private static final long serialVersionUID = 7881269189540416455L;

      @Override
      public QName name() {
        return this.name();
      }

      @Override
      protected $AnyType inherits() {
        return this;
      }
    };
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof $AnyType))
      return false;

    final $AnyType that = ($AnyType)obj;
    if (anys != null) {
      if (that.anys != null && anys.size() == that.anys.size()) {
        for (int i = 0; i < anys.size(); i++)
          if (!anys.get(i).equals(that.anys.get(i)))
            return false;
      }
      else {
        return false;
      }
    }
    else if (that.anys != null) {
      return false;
    }

    if (anys$ != null) {
      if (that.anys$ != null && anys$.size() == that.anys$.size()) {
        for (int i = 0; i < anys$.size(); i++)
          if (!anys$.get(i).equals(that.anys$.get(i)))
            return false;
      }
      else {
        return false;
      }
    }
    else if (that.anys$ != null) {
      return false;
    }

    return true;
  }
}