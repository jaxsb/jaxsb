/* Copyright (c) 2008 JAX-SB
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
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jaxsb.compiler.lang.NamespaceBinding;
import org.jaxsb.compiler.pipeline.PipelineDirectory;
import org.jaxsb.compiler.pipeline.PipelineEntity;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.Nameable;
import org.jaxsb.generator.processor.plan.AliasPlan;
import org.jaxsb.generator.processor.plan.NestablePlan;
import org.jaxsb.generator.processor.plan.Plan;
import org.jaxsb.runtime.CompilerFailureException;
import org.libj.io.FileUtil;
import org.libj.net.URLs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.googlejavaformat.java.FormatterException;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Writer<T extends Plan<?>> implements PipelineEntity {
  private static final Logger logger = LoggerFactory.getLogger(Writer.class);

  private final Collection<String> messages = new HashSet<>();

  private static final Map<File,ClassFile> fileToClassFile = new HashMap<>();

  private File getFile(final T plan, final File destDir) {
    final URL url = plan.getModel().getSchema().getURL();
    final String display = URLs.isLocal(url) ? FileUtil.getCwd().toPath().relativize(new File(url.getFile()).getAbsoluteFile().toPath()).toString() : url.toExternalForm();
    final String message = "Compiling {" + plan.getModel().getTargetNamespace() + "} from " + display;

    if (!messages.contains(message)) {
      messages.add(message);
      logger.info(message);
    }

    final Nameable<?> nameable = (Nameable<?>)plan;
    try {
      if (nameable.getName().getNamespaceURI().getNamespaceBinding() == null)
        throw new CompilerFailureException("The binding configuration does not specify a NamespaceBinding for " + ((Nameable<?>)plan).getName().getNamespaceURI());

      final NamespaceBinding namespaceBinding = nameable.getName().getNamespaceURI().getNamespaceBinding();
      return new File(new File(destDir, namespaceBinding.getPackageName().replace('.', '/')), namespaceBinding.getSimpleClassName() + ".java");
    }
    catch (final CompilerFailureException e) {
      throw e;
    }
    catch (final RuntimeException e) {
      throw new CompilerFailureException(e);
    }
  }

  protected void closeFile(final T plan, final File destDir) {
    if (!(plan instanceof AliasPlan) || (plan instanceof NestablePlan && ((NestablePlan)plan).isNested()))
      return;

    final File file = getFile(plan, destDir);
    try (final ClassFile classFile = fileToClassFile.remove(file)) {
      if (logger.isDebugEnabled())
        logger.debug("Closing: " + classFile.getFile().getAbsolutePath());
    }
    catch (final IOException e) {
      logger.info(e.getMessage(), e);
    }
    catch (final FormatterException e) {
      throw new CompilerFailureException(e);
    }
  }

  protected void writeFile(final Writer<? super T> writer, final T plan, final File destDir) {
    if (!(plan instanceof AliasPlan) || (plan instanceof NestablePlan && ((NestablePlan)plan).isNested()))
      return;

    final URL url = plan.getModel().getSchema().getURL();
    final String display = URLs.isLocal(url) ? FileUtil.getCwd().toPath().relativize(new File(url.getFile()).getAbsoluteFile().toPath()).toString() : url.toExternalForm();
    final String message = "Compiling {" + plan.getModel().getTargetNamespace() + "} from " + display;

    if (!messages.contains(message)) {
      messages.add(message);
      logger.info(message);
    }

    final Nameable<?> nameable = (Nameable<?>)plan;
    if (nameable.getName().getNamespaceURI().getNamespaceBinding() == null)
      throw new CompilerFailureException("The binding configuration does not specify a NamespaceBinding for " + ((Nameable<?>)plan).getName().getNamespaceURI());

    final NamespaceBinding namespaceBinding = nameable.getName().getNamespaceURI().getNamespaceBinding();
    final File file = new File(new File(destDir, namespaceBinding.getPackageName().replace('.', '/')), namespaceBinding.getSimpleClassName() + ".java");
    ClassFile classFile = fileToClassFile.get(file);
    if (classFile == null)
      fileToClassFile.put(file, classFile = new ClassFile(file, namespaceBinding));

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

  public static void writeCopy(final StringWriter writer, final Plan<?> plan, final Plan<?> parent, final String variable) {
    ((Writer)directory.getEntity(plan, null)).appendCopy(writer, plan, parent, variable);
  }

  public static void writeEquals(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendEquals(writer, plan, parent);
  }

  public static void writeHashCode(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendHashCode(writer, plan, parent);
  }

  public static void writeClone(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendClone(writer, plan, parent);
  }

  public static void writeClass(final StringWriter writer, final Plan<?> plan, final Plan<?> parent) {
    ((Writer)directory.getEntity(plan, null)).appendClass(writer, plan, parent);
  }

  protected static PipelineDirectory<GeneratorContext,? super Plan<?>,Writer<?>> directory;

  protected abstract void appendRegistration(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendDeclaration(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendGetMethod(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendSetMethod(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendMarshal(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendParse(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendCopy(StringWriter writer, T plan, Plan<?> parent, String variable);
  protected abstract void appendEquals(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendHashCode(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendClone(StringWriter writer, T plan, Plan<?> parent);
  protected abstract void appendClass(StringWriter writer, T plan, Plan<?> parent);
}