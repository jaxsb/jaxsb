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

package org.jaxsb.generator.processor.write;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.jaxsb.compiler.lang.NamespaceBinding;
import org.jaxsb.runtime.Schema;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class ClassFile implements AutoCloseable {
  private static final String license;
  private static final Formatter formatter = new Formatter();

  static {
    final StringBuilder notice = new StringBuilder();
    notice.append("/* .------------------------------------------------------------------. */\n");
    notice.append("/* | GENERATED CODE - Xml Shema Binding [www.jaxsb.org] - DO NOT EDIT | */\n");
    notice.append("/* '------------------------------------------------------------------' */\n\n");
    license = notice.toString();
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

  @Override
  public void close() throws FormatterException, IOException {
    final StringBuilder builder = new StringBuilder();
    builder.append("package ").append(namespaceBinding.getPackageName()).append(";\n\n");
    builder.append('@').append(SuppressWarnings.class.getName()).append("(\"all\")\n");
    builder.append("public class ").append(namespaceBinding.getSimpleClassName()).append(" extends ").append(Schema.class.getName()).append(" {\n\n");
    builder.append("static {");
    for (final String registrationText : registrationTexts)
      builder.append('\n').append(registrationText);
    builder.append('}');

    for (final String classText : classTexts)
      builder.append('\n').append(classText);

    builder.append("\n}");

    final String text = formatter.formatSource(builder.toString());
    file.getParentFile().mkdirs();
    try (final OutputStreamWriter out = new FileWriter(file)) {
      out.write(license);
      out.write(text);
    }
  }
}