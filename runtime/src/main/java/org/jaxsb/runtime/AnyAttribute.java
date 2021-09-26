package org.jaxsb.runtime;

import static org.libj.lang.Assertions.*;

import javax.xml.namespace.QName;

import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public class AnyAttribute<T> extends $AnySimpleType<T> {
  private final QName name;

  public AnyAttribute(final QName name) {
    this.name = assertNotNull(name);
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
  public AnyAttribute<T> clone() {
    return (AnyAttribute<T>)super.clone();
  }
}