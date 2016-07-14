/* Copyright (c) 2008 Seva Safris
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

package org.safris.xml.toolkit.test.binding;

import java.io.File;
import java.io.FileInputStream;
import org.junit.Assert;
import org.junit.Test;
import org.safris.commons.xml.dom.DOMs;
import org.safris.xml.generator.compiler.runtime.Bindings;
import org.safris.xml.schema.binding.test.unit.name.$na_name;
import org.safris.xml.schema.binding.test.unit.name.na_Name;
import org.safris.xml.schema.binding.test.unit.name.na_Name$;
import org.safris.xml.schema.binding.test.unit.name.na_name;
import org.safris.xml.schema.binding.test.unit.name.na_name$;
import org.safris.xml.schema.binding.test.unit.name.na_root;
import org.xml.sax.InputSource;

public class NameTest extends AbstractTest {
  public static void main(String[] args) throws Exception {
    new NameTest().testExample();
  }

  @Test
  public void testExample() throws Exception {
    na_name name_name_name_name_name_name = new na_name();
    name_name_name_name_name_name.set_Name$(new $na_name._Name$("more"));
    name_name_name_name_name_name.set_name$(new $na_name._name$("stuff"));

    na_name name_name_name_name_name1 = new na_name();
    name_name_name_name_name1.addna_name(name_name_name_name_name_name);

    na_name name_name_name_name1 = new na_name();
    name_name_name_name1.addna_name(name_name_name_name_name1);
    name_name_name_name1.setna_Name$(new na_Name$(true));
    name_name_name_name1.setna_name$(new na_name$(false));

    na_name name_name_name1 = new na_name();
    name_name_name1.addna_name(name_name_name_name1);

    na_name name_name1 = new na_name();
    name_name1.addna_name(name_name_name1);
    name_name1.setna_Name$(new na_Name$(false));
    name_name1.setna_name$(new na_name$(true));

    na_name name1 = new na_name();
    name1.addna_name(name_name1);

    na_name name_name_name2 = new na_name();
    name_name_name2.setna_Name$(new na_Name$(true));
    name_name_name2.setna_name$(new na_name$(false));
    name_name_name2.set_Name$(new $na_name._Name$("when"));
    name_name_name2.set_name$(new $na_name._name$("what"));
    name_name_name2.addna_name(new na_name());

    na_name name_name2 = new na_name();
    name_name2.addna_name(name_name_name2);

    na_name name2 = new na_name();
    name2.setna_Name$(new na_Name$(false));
    name2.setna_name$(new na_name$(true));
    name2.addna_name(name_name2);

    na_name name_name3 = new na_name();
    name_name3.set_Name$(new $na_name._Name$("here"));
    name_name3.set_name$(new $na_name._name$("where"));
    name_name3.setna_Name$(new na_Name$(false));
    name_name3.setna_name$(new na_name$(true));
    name_name3.addna_name(new na_name());

    na_name name3 = new na_name();
    name3.addna_name(name_name3);

    na_name name_name_name_name_name4 = new na_name();
    name_name_name_name_name4.set_Name$(new $na_name._Name$("yup"));
    name_name_name_name_name4.set_name$(new $na_name._name$("nill"));

    na_name name_name_name_name4 = new na_name();
    name_name_name_name4.addna_name(name_name_name_name_name4);

    na_name name_name_name4 = new na_name();
    name_name_name4.addna_name(name_name_name_name4);
    name_name_name4.setna_Name$(new na_Name$(false));
    name_name_name4.setna_name$(new na_name$(true));

    na_name name_name4 = new na_name();
    name_name4.addna_name(name_name_name4);

    na_name name_name_name4_2 = new na_name();
    name_name_name4_2.set_Name$(new $na_name._Name$("ever"));
    name_name_name4_2.set_name$(new $na_name._name$("who"));
    name_name_name4_2.setna_Name$(new na_Name$(true));
    name_name_name4_2.setna_name$(new na_name$(false));

    na_name name_name4_2 = new na_name();
    name_name4_2.addna_name(name_name_name4_2);

    na_Name name4 = new na_Name();
    name4.set_Name$(new $na_name._Name$("hello"));
    name4.set_name$(new $na_name._name$("hi"));
    name4.setna_Name$(new na_Name$(true));
    name4.setna_name$(new na_name$(false));
    name4.addna_name(name_name4);
    name4.addna_name(name_name4_2);

    na_name name_name_name5 = new na_name();
    name_name_name5.addna_name(new na_name());
    name_name_name5.set_Name$(new $na_name._Name$("world"));
    name_name_name5.set_name$(new $na_name._name$("my"));
    name_name_name5.setna_Name$(new na_Name$(true));
    name_name_name5.setna_name$(new na_name$(true));

    na_name name_name5 = new na_name();
    name_name5.addna_name(name_name_name5);

    na_Name name5 = new na_Name();
    name5.set_Name$(new $na_name._Name$("yup"));
    name5.set_name$(new $na_name._name$("really"));
    name5.setna_Name$(new na_Name$(true));
    name5.setna_name$(new na_name$(true));
    name5.addna_name(name_name5);
    name5.addna_name(new na_name());

    na_root root = new na_root();
    root.addna_name(name1);
    root.addna_name(name2);
    root.addna_name(name3);
    root.addna_Name(name4);
    root.addna_Name(name5);

    assertTrue(verifyBinding(root));

    // Write the xml we've constructed in the code to a string
    String xmlFromObjects = DOMs.domToString(root.marshal());

    final File file = new File("src/test/resources/xml/unit/name.xml");
    if (!file.exists())
      throw new Error("File " + file.getAbsolutePath() + " does not exist.");

    if (!file.canRead())
      throw new Error("File " + file.getAbsolutePath() + " is not readable.");

    na_root root2 = (na_root)Bindings.parse(new InputSource(new FileInputStream(file)));

    assertTrue(verifyBinding(root2));

    // Write the xml we parsed from the file to a string
    String xmlFromFile = DOMs.domToString(root2.marshal());

    // The xml from the file has a xsi:nil="false", which must be preserved
    // from the file but cannot be expressed from the code (because
    // xsi:nil="false" is default and doesnt need to be specified
    // explicitly.
    assertTrue(xmlFromFile.contains(" xsi:nil=\"false\""));

    // Therefore, we need to remove the attribute to equate the strings of
    // the xml from file and from code to each other.
    xmlFromFile = xmlFromFile.replace(" xsi:nil=\"false\"", "");

    // The two xml strings should be equal
    Assert.assertEquals(xmlFromObjects, xmlFromFile);
  }
}