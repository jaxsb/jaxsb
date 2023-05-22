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

package org.jaxsb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jaxsb.compiler.lang.NamespaceURI;
import org.jaxsb.compiler.processor.GeneratorContext;
import org.jaxsb.compiler.processor.reference.SchemaReference;
import org.jaxsb.generator.Generator;
import org.libj.net.URLs;
import org.libj.util.CollectionUtil;
import org.openjax.maven.mojo.FilterParameter;
import org.openjax.maven.mojo.FilterType;
import org.openjax.maven.mojo.GeneratorMojo;

@Mojo(name="generate", defaultPhase=LifecyclePhase.GENERATE_SOURCES)
@Execute(goal="generate")
public class JaxSbMojo extends GeneratorMojo {
  @Parameter(property="includes")
  private List<String> includes;

  @Parameter(property="excludes")
  private List<String> excludes;

  @Parameter(property="skipXsd")
  private boolean skipXsd;

  // Contains all source paths for all executions of the generator in the single VM, such
  // that subsequent executions have a reference to the source paths of previous executions
  // so as to allow for bindings of excluded namespaces to be generated in prior executions
  private static final HashSet<File> sourcePath = new HashSet<>();

  private static Set<NamespaceURI> buildNamespaceSet(final List<String> list) {
    final int i$;
    if (list == null || (i$ = list.size()) == 0)
      return null;

    final HashSet<NamespaceURI> set = new HashSet<>(i$);
    if (list instanceof RandomAccess) {
      int i = 0; do // [RA]
        set.add(NamespaceURI.getInstance(list.get(i)));
      while (++i < i$);
    }
    else {
      final Iterator<String> i = list.iterator(); do // [I]
        set.add(NamespaceURI.getInstance(i.next()));
      while (i.hasNext());
    }

    return set;
  }

  @FilterParameter(FilterType.URL)
  @Parameter(property="schemas", required=true)
  private List<String> schemas;

  @Override
  public void execute(final Configuration configuration) throws MojoExecutionException, MojoFailureException {
    final ArrayList<SchemaReference> generatorBindings = new ArrayList<>();
    if (schemas.size() > 0)
      for (final String schema : new LinkedHashSet<>(schemas)) // [S]
        generatorBindings.add(new SchemaReference(URLs.create(schema), false));

    final Set<NamespaceURI> includes = buildNamespaceSet(this.includes);
    final Set<NamespaceURI> excludes = buildNamespaceSet(this.excludes);

    final GeneratorContext ceneratorContext = new GeneratorContext(configuration.getDestDir(), configuration.getOverwrite(), null, false, includes, excludes);
    try {
      Generator.generate(ceneratorContext, generatorBindings, sourcePath, skipXsd);
    }
    catch (final IOException e) {
      throw new MojoExecutionException("Generator " + ceneratorContext + " \"" + CollectionUtil.toString(generatorBindings, ' ') + "\" \"" + CollectionUtil.toString(sourcePath, ' ') + "\" " + skipXsd, e);
    }

    sourcePath.add(configuration.getDestDir());

    final Resource resource = new Resource();
    resource.setDirectory(configuration.getDestDir().getAbsolutePath());
    if (isInTestPhase())
      project.addTestResource(resource);
    else
      project.addResource(resource);
  }
}