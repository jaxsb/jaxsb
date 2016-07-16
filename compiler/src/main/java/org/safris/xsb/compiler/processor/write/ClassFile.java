package org.safris.xsb.compiler.processor.write;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.safris.commons.formatter.SourceFormat;
import org.safris.xsb.compiler.runtime.Schema;

public class ClassFile {
  private static final StringBuffer license = new StringBuffer();

  static {
    license.append("/* .-------------------------------------------------------------------. */\n");
    license.append("/* | GENERATED CODE - Xml Shema Binding [xsb.safris.org] - DO NOT EDIT | */\n");
    license.append("/* '-------------------------------------------------------------------' */\n\n");
  }

  private final File file;
  private final String packageName;
  private List<String> registrationTexts = new ArrayList<String>();
  private List<String> classTexts = new ArrayList<String>();

  public ClassFile(final File file, final String packageName) {
    this.file = file;
    this.packageName = packageName;
  }

  public void addRegistrationText(final String registrationText) {
    this.registrationTexts.add(registrationText);
  }

  public void addClassText(final String classText) {
    this.classTexts.add(classText);
  }

  public void close() throws IOException {
    final StringBuilder buffer = new StringBuilder();
    buffer.append("package " + packageName + ";\n\n");
    buffer.append("@" + SuppressWarnings.class.getName() + "(\"all\")\n");
    buffer.append("public class xe extends " + Schema.class.getName() + " {\n\n");
    buffer.append("protected static void _$$register() {");
    for (final String registrationText : registrationTexts)
      buffer.append("\n" + registrationText);
    buffer.append("}");

    for (final String classText : classTexts)
      buffer.append("\n" + classText);

    buffer.append("\n}");

    final String text = SourceFormat.getDefaultFormat().format(buffer.toString());
    file.getParentFile().mkdirs();
    try (final FileOutputStream out = new FileOutputStream(file)) {
      out.write(license.toString().getBytes());
      out.write(text.getBytes());
    }
  }
}