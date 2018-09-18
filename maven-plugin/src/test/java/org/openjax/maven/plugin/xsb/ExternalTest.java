/* Copyright (c) 2008 OpenJAX
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

package org.openjax.maven.plugin.xsb;

import java.io.File;

import org.fastjax.exec.Processes;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openjax.xsb.XSBMojo;
import org.fastjax.io.Files;

public class ExternalTest {
  private static final String[] DEBUG_VM_ARGS = null;
  // private static final String[] DEBUG_VM_ARGS = new String[] {"-Xdebug", "-Xnoagent", "-Djava.compiler=NONE",
  // "-Xrunjdwp:transport=dt_socket,address=8000,server=y"};
  private static final String POM_PATH = "src/test/resources/xml/";

  @Test
  @Ignore
  public void testExternal() throws Exception {
    final Process process = Processes.forkSync(System.in, System.out, System.err, false, DEBUG_VM_ARGS, XSBMojo.class, POM_PATH + "pom-external.xml");
    if (process.exitValue() != 0)
      Assert.fail();

    Files.deleteAllOnExit(new File(POM_PATH + "target").toPath());
  }
}