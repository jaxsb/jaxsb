/* Copyright (c) 2006 JAX-SB
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

package org.jaxsb.tutorial;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class InvoiceUpdaterTest {
  @Test
  public void test() throws Exception {
    // FIXME: Can create a JUnit runner to test these in parallel
    final int tests = 100;
    final CountDownLatch latch = new CountDownLatch(tests);
    final Thread thread = Thread.currentThread();
    final ExecutorService executor = Executors.newFixedThreadPool(tests);
    for (int i = 0; i < tests; ++i) { // [N]
      executor.execute(() -> {
        try {
          InvoiceUpdater.main(new String[] {getClass().getResource("/invoice.xml").getPath(), "Super Booties", "73648", "9", "4.30"});
        }
        catch (final Exception e) {
          e.printStackTrace();
          thread.interrupt();
        }
        finally {
          latch.countDown();
        }
      });
    }

    try {
      latch.await();
    }
    catch (final InterruptedException e) {
      fail();
    }
  }
}