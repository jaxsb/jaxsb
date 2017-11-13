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

import java.util.HashMap;
import java.util.Map;

import org.libx4j.xsb.compiler.lang.UniqueQName;
import org.libx4j.xsb.runtime.MarshalException;
import org.libx4j.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $ID extends $nCName {
  private static final long serialVersionUID = 8671692505211063717L;
  protected static final Map<String,Map<String,$ID>> namespaceIds = new HashMap<String,Map<String,$ID>>();

  private static void persist(final String namespace, final String value, final $ID id) {
    Map<String,$ID> idMap = namespaceIds.get(namespace);
    if (idMap == null)
      namespaceIds.put(namespace, idMap = new HashMap<String,$ID>());

    idMap.put(value, id);
  }

  private static void remove(final String namespace, final Object value) {
    final Map<String,$ID> ids = namespaceIds.get(namespace);
    if (ids == null)
      return;

    ids.remove(value);
  }

  public static $ID lookupId(final Object id) {
    final Map<String,$ID> ids = namespaceIds.get(UniqueQName.XS.getNamespaceURI().toString());
    if (ids == null)
      return null;

    return ids.get(id);
  }

  public $ID(final $ID binding) {
    super(binding);
  }

  public $ID(final String value) {
    super(value);
    persist(name().getNamespaceURI(), value, this);
  }

  protected $ID() {
    super();
  }

  @Override
  public String text() {
    return super.text();
  }

  @Override
  public void text(final String text) {
    final String old = text();
    super.text(text);
    if (old != null)
      remove(name().getNamespaceURI(), old);

    persist(name().getNamespaceURI(), text, this);
  }

  @Override
  protected void _$$decode(final Element parent, final String value) throws ParseException {
    persist(parent.getNamespaceURI(), value, this);
    super.text(value);
  }

  @Override
  protected String _$$encode(final Element parent) throws MarshalException {
    return super.text() != null ? super.text().toString() : "";
  }

  @Override
  public $ID clone() {
    return new $ID(this) {
      private static final long serialVersionUID = -2416845455854858753L;

      @Override
      protected $ID inherits() {
        return this;
      }
    };
  }
}