package org.jaxsb.runtime;

import java.util.Objects;

import javax.xml.namespace.QName;

import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class AnyAttribute extends $AnySimpleType {
  private static final long serialVersionUID = -3821161497069887361L;
  private final QName name;

  public AnyAttribute(final QName name) {
    this.name = Objects.requireNonNull(name);
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