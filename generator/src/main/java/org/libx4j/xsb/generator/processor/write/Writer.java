/* Copyright (c) 2008 lib4j
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
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.lib4j.io.Files;
import org.lib4j.net.URLs;
import org.lib4j.pipeline.PipelineDirectory;
import org.lib4j.pipeline.PipelineEntity;
import org.libx4j.xsb.compiler.processor.GeneratorContext;
import org.libx4j.xsb.compiler.processor.Nameable;
import org.libx4j.xsb.generator.processor.plan.AliasPlan;
import org.libx4j.xsb.generator.processor.plan.NestablePlan;
import org.libx4j.xsb.generator.processor.plan.Plan;
import org.libx4j.xsb.runtime.CompilerFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Writer<T extends Plan<?>> implements PipelineEntity {
  private static final Logger logger = LoggerFactory.getLogger(Writer.class);

  private final Collection<String> messages = new HashSet<String>();

  private static final Map<File,ClassFile> fileToClassFile = new HashMap<File,ClassFile>();

  private File getFile(final Writer<T> writer, final T plan, final File destDir) {
    final URL url = plan.getModel().getSchema().getURL();
    final String display = URLs.isLocal(url) ? Files.relativePath(Files.getCwd().getAbsoluteFile(), new File(url.getFile()).getAbsoluteFile()) : url.toExternalForm();
    final String message = "Compiling {" + plan.getModel().getTargetNamespace() + "} from " + display;

    if (!messages.contains(message)) {
      messages.add(message);
      logger.info(message);
    }

    final Nameable<?> nameable = (Nameable<?>)plan;
    try {
      if (nameable.getName().getNamespaceURI().getPackage() == null)
        throw new CompilerFailureException("The binding configuration does not specify a class name for " + ((Nameable<?>)plan).getName().getNamespaceURI());

      final String packageName = nameable.getName().getNamespaceURI().getPackage();
      return new File(new File(destDir, packageName.replace('.', '/')), "xe.java");
    }
    catch (final Exception e) {
      throw new CompilerFailureException(e);
    }
  }

  protected void closeFile(final Writer<T> writer, final T plan, final File destDir) {
    if (!(plan instanceof AliasPlan) || (plan instanceof NestablePlan && ((NestablePlan)plan).isNested()))
      return;

    final File file = getFile(writer, plan, destDir);
    final ClassFile classFile = fileToClassFile.get(file);
    if (classFile == null)
      return;

    try {
      classFile.close();
    }
    catch (final IOException e) {
      throw new CompilerFailureException(e);
    }

    fileToClassFile.remove(file);
  }

  protected void writeFile(final Writer<T> writer, final T plan, final File destDir) {
    if (!(plan instanceof AliasPlan) || (plan instanceof NestablePlan && ((NestablePlan)plan).isNested()))
      return;

    final URL url = plan.getModel().getSchema().getURL();
    final String display = URLs.isLocal(url) ? Files.relativePath(Files.getCwd().getAbsoluteFile(), new File(url.getFile()).getAbsoluteFile()) : url.toExternalForm();
    final String message = "Compiling {" + plan.getModel().getTargetNamespace() + "} from " + display;

    if (!messages.contains(message)) {
      messages.add(message);
      logger.info(message);
    }

    final Nameable<?> nameable = (Nameable<?>)plan;
    if (nameable.getName().getNamespaceURI().getPackage() == null)
      throw new CompilerFailureException("The binding configuration does not specify a class name for " + ((Nameable<?>)plan).getName().getNamespaceURI());

    final String packageName = nameable.getName().getNamespaceURI().getPackage();

    final File file = new File(new File(destDir, packageName.replace('.', '/')), "xe.java");
    ClassFile classFile = fileToClassFile.get(file);
    if (classFile == null)
      fileToClassFile.put(file, classFile = new ClassFile(file, packageName));

    StringWriter stringWriter = new StringWriter();
    writer.appendRegistration(stringWriter, plan, null);
    classFile.addRegistrationText(stringWriter.toString());

    stringWriter = new StringWriter();
    writer.appendClass(stringWriter, plan, null);
    classFile.addClassText(stringWriter.toString());
  }

  public static void writeDeclaration(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendDeclaration(writer, plan, parent);
  }

  public static void writeGetMethod(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendGetMethod(writer, plan, parent);
  }

  public static void writeSetMethod(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendSetMethod(writer, plan, parent);
  }

  public static void writeMarshal(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendMarshal(writer, plan, parent);
  }

  public static void writeParse(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendParse(writer, plan, parent);
  }

  public static void writeCopy(final StringWriter writer, final Plan<?> plan, Plan<?> parent, final String variable) {
    ((Writer)directory.getEntity(plan, null)).appendCopy(writer, plan, parent, variable);
  }

  public static void writeEquals(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendEquals(writer, plan, parent);
  }

  public static void writeHashCode(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendHashCode(writer, plan, parent);
  }

  public static void writeClass(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendClass(writer, plan, parent);
  }

  protected static PipelineDirectory<GeneratorContext,Plan<?>,Writer<?>> directory = null;

  protected abstract void appendRegistration(final StringWriter writer, final T plan, final Plan<?> parent);
  protected abstract void appendDeclaration(final StringWriter writer, final T plan, final Plan<?> parent);
  protected abstract void appendGetMethod(final StringWriter writer, final T plan, final Plan<?> parent);
  protected abstract void appendSetMethod(final StringWriter writer, final T plan, final Plan<?> parent);
  protected abstract void appendMarshal(final StringWriter writer, final T plan, final Plan<?> parent);
  protected abstract void appendParse(final StringWriter writer, final T plan, final Plan<?> parent);
  protected abstract void appendCopy(final StringWriter writer, final T plan, final Plan<?> parent, final String variable);
  protected abstract void appendEquals(final StringWriter writer, final T plan, final Plan<?> parent);
  protected abstract void appendHashCode(final StringWriter writer, final T plan, final Plan<?> parent);
  protected abstract void appendClass(final StringWriter writer, final T plan, final Plan<?> parent);
}