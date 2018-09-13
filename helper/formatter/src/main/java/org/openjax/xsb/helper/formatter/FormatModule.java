/* Copyright (c) 2006 OpenJAX
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

package org.openjax.xsb.helper.formatter;

public abstract class FormatModule {
  protected static String TAB = "  ";
  private static int depth = 0;
  private static FormatModule lastModule = null;

  public static void restetDepth() {
    depth = 0;
  }

  protected void increaseDepth() {
    depth++;
  }

  protected void decreaseDepth() {
    depth--;
  }

  protected int getDepth() {
    return depth;
  }

  protected static FormatModule getLastModule() {
    return lastModule;
  }

  protected static void setLastModule(final FormatModule module) {
    FormatModule.lastModule = module;
  }

  abstract String format(final String formated, final String token);
}