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

package org.libx4j.xsb.generator.processor.write;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.lib4j.formatter.SourceFormat;
import org.libx4j.xsb.compiler.lang.NamespaceBinding;
import org.libx4j.xsb.runtime.Schema;

public class ClassFile {
  private static final StringBuffer license = new StringBuffer();

  static {
    license.append("/* .-------------------------------------------------------------------. */\n");
    license.append("/* | GENERATED CODE - Xml Shema Binding [xsb.libx4j.org] - DO NOT EDIT | */\n");
    license.append("/* '-------------------------------------------------------------------' */\n\n");
  }

  private final File file;
  private final NamespaceBinding namespaceBinding;
  private List<String> registrationTexts = new ArrayList<>();
  private List<String> classTexts = new ArrayList<>();

  public ClassFile(final File file, final NamespaceBinding namespaceBinding) {
    this.file = file;
    this.namespaceBinding = namespaceBinding;
  }

  public void addRegistrationText(final String registrationText) {
    this.registrationTexts.add(registrationText);
  }

  public void addClassText(final String classText) {
    this.classTexts.add(classText);
  }

  public void close() throws IOException {
    final StringBuilder buffer = new StringBuilder();
    buffer.append("package " + namespaceBinding.getPackageName() + ";\n\n");
    buffer.append("@" + SuppressWarnings.class.getName() + "(\"all\")\n");
    buffer.append("public class " + namespaceBinding.getSimpleClassName() + " extends " + Schema.class.getName() + " {\n\n");
    buffer.append("protected static void _$$register() {");
    for (final String registrationText : registrationTexts)
      buffer.append("\n" + registrationText);
    buffer.append('}');

    for (final String classText : classTexts)
      buffer.append("\n" + classText);

    buffer.append("\n}");

    final String text = SourceFormat.getDefaultFormat().format(buffer.toString());
    file.getParentFile().mkdirs();
    try (final OutputStreamWriter out = new FileWriter(file)) {
      out.write(license.toString());
      out.write(text);
    }
  }
}