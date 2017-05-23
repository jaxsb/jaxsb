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

package org.w3.x2001.xmlschema.xe;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.libx4j.xsb.runtime.Binding;

public abstract class $xs_anyType extends $xs_anySimpleType {
  private final List<Binding> anys = new ArrayList<Binding>(7);
  private final List<Binding> anys$ = new ArrayList<Binding>(7);

  public $xs_anyType(final $xs_anyType binding) {
    super(binding);
  }

  public $xs_anyType(final Object text) {
    super(text);
  }

  protected $xs_anyType(final String text) {
    super();
  }

  protected $xs_anyType() {
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
  public $xs_anyType clone() {
    return new $xs_anyType(this) {
      @Override
      public QName name() {
        return this.name();
      }

      @Override
      protected $xs_anyType inherits() {
        return this;
      }
    };
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof $xs_anyType))
      return false;

    final $xs_anyType that = ($xs_anyType)obj;
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