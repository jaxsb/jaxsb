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

package org.safris.xsb.runtime;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.safris.commons.test.LoggableTest;
import org.safris.xsb.runtime.WeakClassLoader;

public class ClassLoaderTest extends LoggableTest {
  public static void main(final String[] args) throws Exception {
    if (args == null || args.length == 0) {
      new ClassLoaderTest().testClassLoaders();
      return;
    }

    final ClassLoaderTest test = new ClassLoaderTest();
    if ("weak".equals(args[0]))
      test.testClassLoader(new WeakClassLoader());
    else if ("system".equals(args[0]))
      test.testClassLoader(ClassLoader.getSystemClassLoader());
    else
      throw new Error("Unknown classLoader option");

    Runtime.getRuntime().gc();
  }

  private float testClassLoader(final java.lang.ClassLoader classLoader) throws Exception {
    long freeMemoryBeforeLoad = Runtime.getRuntime().freeMemory();
//      PackageLoader.getSystemPackageLoader().loadPackage("org.safris.xml.toolkit.component.runtime");
    long freeMemoryAfterLoad = Runtime.getRuntime().freeMemory();

    Runtime.getRuntime().gc();

    long freeMemoryAfterUnload = Runtime.getRuntime().freeMemory();
    float ratio = (float)freeMemoryAfterUnload / (float)freeMemoryAfterLoad;
    log(classLoader.getClass().getName());
    log("{");
    log("\tFree Memory Before Load: " + freeMemoryBeforeLoad);
    log("\tFree Memory After Load: " + freeMemoryAfterLoad);
    log("\tFree Memory After Unload: " + freeMemoryAfterUnload);
    log("\tratio: " + ratio);
    log("}");
    return ratio;
  }

  @Test
  // FIXME
  @Ignore("FIXME")
  @SuppressWarnings("unused")
  public void testClassLoaders() {
    Runtime.getRuntime().gc();
    // FIXME: The scenario that is tested here is the more performant than the other.
    float system = fork("system");
    float weak = fork("weak");

    weak = fork("weak");
    system = fork("system");
    //assertTrue("Custom classLoader is NOT more efficient than System classLoader!", weak < system);

    system = fork("system");
    weak = fork("weak");
    //assertTrue("Custom classLoader is NOT more efficient than System classLoader!", weak < system);
  }

  private static float fork(final String loader) {
    String classpath = "";
    String userDir = System.getProperty("user.dir");
    String localRepository = System.getProperty("localRepository");
    if (userDir != null) {
      classpath += File.pathSeparator + userDir + "/target/classes";
      classpath += File.pathSeparator + userDir + "/target/test-classes";
    }

    if (localRepository != null)
      classpath += File.pathSeparator + localRepository + "/junit/junit/4.3/junit-4.3.jar";

    if (classpath.length() != 0)
      System.setProperty("java.class.path", System.getProperty("java.class.path") + classpath);

    /*      Process process = Forks.fork(ClassLoaderTest.class, new String[] {loader});
     InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
     InputStreamReader errorStreamReader = new InputStreamReader(process.getErrorStream());
     BufferedReader bufferedInputReader = new BufferedReader(inputStreamReader);
     BufferedReader bufferedErrorReader = new BufferedReader(errorStreamReader);
     String line = null;
     float ratio = 0;
     while((line = bufferedInputReader.readLine()) != null) {
     if (line.contains("ratio: "))
     ratio = Float.parseFloat(line.substring(line.indexOf(": ") + 2));

     log(line);
     }

     while((line = bufferedErrorReader.readLine()) != null)
     log(line);

     try {
     process.waitFor();
     }
     catch(final InterruptedException e)
     {
     throw new Error(e);
     }

     return ratio;*/ return 0;
  }
}