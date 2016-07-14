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

package org.safris.xml.toolkit.test.binding.regression;

import com.sun.org.apache.xpath.internal.XPathAPI;
import java.io.OutputStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import liberty_id_sis_pp_2003_08.pp_Query;
import org.junit.Ignore;
import org.junit.Test;
import org.safris.commons.xml.dom.DOMStyle;
import org.safris.commons.xml.dom.DOMs;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public class XPathTest {
  private static String xpath = null;

  protected static boolean isTextNode(Node node) {
    if (node == null)
      return false;

    final short nodeType = node.getNodeType();
    return nodeType == Node.CDATA_SECTION_NODE || nodeType == Node.TEXT_NODE;
  }

  @Ignore("Finish implementing this test!")
  @Test
  public void testXPath() throws Exception {
    pp_Query._QueryItem queryItem = new pp_Query._QueryItem();
//      queryItem.setSelect(new QueryType.QueryItem.Select(xpath));

    pp_Query query = new pp_Query();
    query.add_QueryItem(queryItem);

    Element element = query.marshal();
    System.out.println(DOMs.domToString(element, DOMStyle.INDENT));

    Transformer serializer = TransformerFactory.newInstance().newTransformer();
    serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

    NodeIterator nodeIterator = XPathAPI.selectNodeIterator(element, xpath);
    Node node = null;
    while ((node = nodeIterator.nextNode()) != null) {
      if (isTextNode(node)) {
        // DOM may have more than one node corresponding to a
        // single XPath text node.  Coalesce all contiguous text nodes
        // at this level
        final StringBuffer stringBuffer = new StringBuffer(node.getNodeValue());
        for (Node nextNode = node.getNextSibling(); isTextNode(nextNode); nextNode = nextNode.getNextSibling())
          stringBuffer.append(nextNode.getNodeValue());

        System.out.print(stringBuffer);
      }
      else {
        serializer.transform(new DOMSource(node), new StreamResult(new OutputStreamWriter(System.out)));
      }
    }
  }
}