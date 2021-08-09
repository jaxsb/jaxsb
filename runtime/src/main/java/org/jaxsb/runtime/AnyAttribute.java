package org.jaxsb.runtime;

import javax.xml.namespace.QName;

import org.libj.lang.Assertions;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class AnyAttribute extends $AnySimpleType {
  private static final long serialVersionUID = -3821161497069887361L;
  private final QName name;

  public AnyAttribute(final QName name) {
    this.name = Assertions.assertNotNull(name);
  }

  @Override
  public QName name() {
    return name;
  }

  @Override
  protected Binding inherits() {
    return null;
  }

  @Override
  public AnyAttribute clone() {
    return (AnyAttribute)super.clone();
  }
}