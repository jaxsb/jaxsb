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

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import org.junit.Ignore;
import org.safris.commons.io.Files;

@Ignore("Make this a real test!")
public class TestMain {
  private static final FileFilter xsdFileFilter = new FileFilter()
  {
    public boolean accept(File pathname) {
      return pathname.getName().endsWith(".xsd");
    }
  };

  public static void main(String[] args) {
    while (true) {
      try {
        final File debugDir = new File("../../bin/Debug");
        final File targetDir = new File("target/generated-sources/xmlbinding/");
        if (debugDir.exists() && targetDir.exists()) {
          final List<File> files = Files.listAll(targetDir, xsdFileFilter);
          for (File file : files)
            Files.copy(file, new File(debugDir, Files.relativePath(targetDir, file)));
        }

        XencRegressionTest.main(null);
        DsRegressionTest.main(null);
        AcRegressionTest.main(null);
        SamlRegressionTest.main(null);
        SamlpRegressionTest.main(null);
        LibRegressionTest.main(null);
//              IdppRegressionTest.main(null);
        DscRegressionTest.main(null);
        TnsRegressionTest.main(null);
        MdRegressionTest.main(null);
      }
      catch (Throwable t) {
        t.printStackTrace();
      }

      System.out.println("Smallest Tested XML Document:\n" + RegressionTestMetrics.getSmallestXMLDocument());
      System.out.println("Largest Tested XML Document:\n" + RegressionTestMetrics.getLargestXMLDocument());
      System.out.println("Test Count: " + RegressionTestMetrics.getTestCount());
    }
  }
}
