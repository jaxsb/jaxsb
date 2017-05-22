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

import java.util.HashMap;
import java.util.Map;

import org.safris.xsb.compiler.lang.UniqueQName;
import org.safris.xsb.runtime.MarshalException;
import org.safris.xsb.runtime.ParseException;
import org.w3c.dom.Element;

public abstract class $xs_ID extends $xs_NCName {
  protected static final Map<String,Map<Object,$xs_ID>> namespaceIds = new HashMap<String,Map<Object,$xs_ID>>();

  private static void persist(final String namespace, final Object value, final $xs_ID id) {
    Map<Object,$xs_ID> idMap = namespaceIds.get(namespace);
    if (idMap == null)
      namespaceIds.put(namespace, idMap = new HashMap<Object,$xs_ID>());

    idMap.put(value, id);
  }

  private static void remove(final String namespace, final Object value) {
    final Map<Object,$xs_ID> ids = namespaceIds.get(namespace);
    if (ids == null)
      return;

    ids.remove(value);
  }

  public static $xs_ID lookupId(final Object id) {
    final Map<Object,$xs_ID> ids = namespaceIds.get(UniqueQName.XS.getNamespaceURI().toString());
    if (ids == null)
      return null;

    return ids.get(id);
  }

  public $xs_ID(final $xs_ID binding) {
    super(binding);
  }

  public $xs_ID(final String value) {
    super(value);
    persist(name().getNamespaceURI(), value, this);
  }

  protected $xs_ID() {
    super();
  }

  @Override
  public String text() {
    return super.text();
  }

  @Override
  public void text(final String text) {
    final Object old = text();
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
  public $xs_ID clone() {
    return new $xs_ID(this) {
      @Override
      protected $xs_ID inherits() {
        return this;
      }
    };
  }
}