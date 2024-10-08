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

package org.jaxsb.compiler.lang;

import java.io.File;
import java.net.URI;
import java.util.Base64;
import java.util.function.Function;

import org.libj.lang.Identifiers;
import org.libj.net.Service;
import org.libj.net.Services;
import org.libj.util.Diff;

/**
 * This class models the binding between an XML namespace URI and a Java package name. This model asserts that a unique package
 * exists for every different XML namespace.
 *
 * @see NamespaceBinding#parseNamespace(URI)
 * @see NamespaceBinding#parseClassName(String)
 */
public final class NamespaceBinding {
  private static final Function<Character,String> substitutes = (final Character c) -> Character.isJavaIdentifierPart(c) ? "_" + c : "_";

  /**
   * Utility class for optimization of diff compression.
   */
  private static class Rule {
    private final String start;
    private final String end;
    private final char prefix1;
    private final char prefix2;

    protected Rule(final String start, final String end, final char prefix1, final char prefix2) {
      this.start = start;
      this.end = end;
      this.prefix1 = prefix1;
      this.prefix2 = prefix2;
    }

    /**
     * If the start of {@code builder} does not match {@code start}, then there is no match.
     * <p>
     * If the end of {@code builder} does not match, then encode the start, and return {@code prefix1}.
     * <p>
     * If the end of {@code builder} matches, then encode the start and end, and return {@code prefix2}.
     *
     * @param builder The builder.
     * @return '\0' if there is no match. {@code prefix1} if only the start matches. {@code prefix2} if the start and end match.
     */
    protected char encode(final StringBuilder builder) {
      if (start == null) {
        int index = builder.indexOf(":");
        if (index > -1 && index + 1 < builder.length() && builder.charAt(index + 1) == '/') {
          final String scheme = builder.substring(0, index);
          while (builder.charAt(++index) == '/');
          final Service service = Services.getService(scheme);
          if (service != null) {
            builder.delete(0, index);
            builder.insert(0, service.getPort());
          }
        }
      }
      else {
        if (builder.length() < start.length() || !start.equals(builder.substring(0, start.length())))
          return '\0';

        builder.delete(0, start.length());
      }

      if (!end.equals(builder.substring(builder.length() - end.length())))
        return prefix1;

      builder.delete(builder.length() - end.length(), builder.length());
      return prefix2;
    }

    /**
     * If {@code prefix == prefix1}, then prepend {@code start}, and return the string.
     * <p>
     * If {@code prefix == prefix2}, then prepend {@code start} and append {@code end}, and return the string.
     * <p>
     * Otherwise, return null.
     *
     * @param prefix The prefix to the encoding.
     * @param builder The string to decode.
     * @return The string translated with respect to {@code prefix}.
     */
    protected StringBuilder decode(final char prefix, final StringBuilder builder) {
      return prefix == prefix1 ? (start == null ? builder : builder.insert(0, start)) : prefix == prefix2 ? (start == null ? builder : builder.insert(0, start)).append(end) : null;
    }
  }

  private static final Rule[] rules = {
    new Rule("http://", ".xsd", 'y', 'x'),
    new Rule("https://", ".xsd", 'z', 's'),
    new Rule("data://", ".xsd", 'b', 'd'),
    new Rule("file://", ".xsd", 'j', 'f'),
    new Rule("urn://", ".xsd", 'w', 'm'),
    new Rule("urn:", ".xsd", 'u', 'n'),
    new Rule(null, ".xsd", 'p', 'q'),
  };

  private static StringBuilder buildHost(final StringBuilder builder, final String host) {
    if (host == null)
      return builder;

    int end = host.length();
    int start = host.lastIndexOf('.');
    do {
      final String word = host.substring(start + 1, end);
      builder.append('.').append(Identifiers.toIdentifier(word, '_', substitutes));
      end = start;
      start = host.lastIndexOf('.', start - 1);
    }
    while (end > -1);
    return builder;
  }

  private static String formatFileName(final String name) {
    final int dot = name.lastIndexOf('.');
    return dot == -1 ? name : name.substring(0, dot);
  }

  private static StringBuilder buildPath(final StringBuilder builder, final String path) {
    if (path == null)
      return builder;

    int start = path.charAt(0) == '/' ? 0 : -1;
    int end = path.indexOf('/', start == 0 ? 1 : 0);
    final int len = path.length();
    do {
      final String word = end == -1 ? (start == len - 1 ? null : path.substring(start + 1)) : start != end ? path.substring(start + 1, end) : null;
      if (word != null)
        builder.append('.').append(Identifiers.toIdentifier(end == -1 ? formatFileName(word) : word, '_', substitutes));

      start = end;
      end = path.indexOf('/', start + 1);
    }
    while (start > -1);
    return builder;
  }

  private static StringBuilder buildUrn(final StringBuilder builder, final String urn) {
    int start = urn.charAt(0) == ':' ? 0 : -1;
    int end = urn.indexOf(':', start == 0 ? 1 : 0);
    final int len = urn.length();
    do {
      if (end != -1 || start != len - 1) {
        final String word = Identifiers.toIdentifier(end == -1 ? urn.substring(start + 1) : urn.substring(start + 1, end), '\0', substitutes);
        if (start > 0 || !"urn".equals(word)) {
          if (!word.startsWith("_"))
            builder.append('_');

          builder.append(word);
        }
      }

      start = end;
      end = urn.indexOf(':', start + 1);
    }
    while (start > -1);
    return builder;
  }

  private static final Base64.Encoder base64Encoder = Base64.getEncoder().withoutPadding();

  private static String flipNamespaceURI(final String uri, final boolean replaceServicePort) {
    final int colon = uri.indexOf(":/");
    if (colon == -1)
      return uri;

    int start = colon;
    while (uri.charAt(++start) == '/');
    final int end = uri.indexOf('/', start + 1);
    final StringBuilder builder = new StringBuilder();
    buildHost(builder, uri.substring(start, end));
    builder.delete(0, 1);
    builder.insert(0, uri.substring(0, start));
    builder.append(uri.substring(end));
    if (replaceServicePort)
      addPrefixForDigits(builder, end);
    else
      removePrefixForDigits(builder);

    return builder.toString();
  }

  private static String getDiff(final String packageName, final String namespaceURI) {
    char prefix = '\0';
    final StringBuilder namespaceForDiff = new StringBuilder(namespaceURI);
    for (final Rule rule : rules) { // [A]
      final char ch = rule.encode(namespaceForDiff);
      if (ch != '\0') {
        prefix = ch;
        break;
      }
    }

    if (prefix == '\0')
      throw new UnsupportedOperationException("Unsupported namespace format: " + namespaceURI);

    final StringBuilder packageNameForDiff = new StringBuilder(packageName);
    for (int i = 0, i$ = packageName.length(), count = 0; i < i$; ++i) { // [N]
      if (packageNameForDiff.charAt(i) == '_' && i > 0 && packageNameForDiff.charAt(i - 1) != '/' && packageNameForDiff.charAt(i - 1) != '.')
        packageNameForDiff.setCharAt(i, ':');
      else if (packageNameForDiff.charAt(i) == '.' && ++count > 2)
        packageNameForDiff.setCharAt(i, '/');
    }

    final Diff diff = new Diff(packageNameForDiff.toString(), namespaceForDiff.toString());
    return prefix + base64Encoder.encodeToString(diff.toBytes()).replace('+', '$').replace('/', '_');
  }

  private static StringBuilder addPrefixForDigits(final StringBuilder builder, final int index) {
    boolean match = false;
    for (int i = builder.length() - 1; i >= index; --i) { // [N]
      final char ch = builder.charAt(i);
      if (match && (ch == '/' || ch == '_'))
        builder.insert(i + 1, '_');

      match = Character.isDigit(ch);
    }

    return builder;
  }

  private static StringBuilder removePrefixForDigits(final StringBuilder builder) {
    char ch;
    for (int i = builder.length() - 1, match = 0; i >= 0; --i) { // [N]
      ch = builder.charAt(i);
      if (match > 1 && (ch == '/' || ch == '.'))
        builder.delete(i + 1, i + 2);

      if (match > 0 && ch == '_') {
        ++match;
      }
      else if (Character.isDigit(ch)) {
        match = 1;
      }
      else {
        match = 0;
      }
    }

    return builder;
  }

  /**
   * Create a {@link NamespaceBinding} from a fully qualified class name. This method is intended for class names created by
   * {@link NamespaceBinding#getClassName()}, which contain a simple class name as a Base64-encoded diff between the package name and
   * the original namespace URI. If such a class name is inputted to this method, the resulting {@link NamespaceBinding} is guaranteed
   * to the unique binding between that class name and the namespace URI from which it originated.
   *
   * @param className The fully qualified class name previously encoded by {{@link NamespaceBinding#getClassName()}.
   * @return A guaranteed unique {@link NamespaceBinding} for the package name.
   * @see NamespaceBinding#getClassName()
   * @see NamespaceBinding#parseNamespace(URI)
   */
  public static NamespaceBinding parseClassName(final String className) {
    final int index = className.lastIndexOf('.');
    final char prefix = className.charAt(index + 1);

    final String simpleClassName = className.substring(index + 2);
    final byte[] diffBytes = Base64.getDecoder().decode(simpleClassName.replace('$', '+').replace('_', '/'));
    final Diff diff = Diff.decode(diffBytes);
    final String source = className.substring(0, index).replace('.', '/');

    final StringBuilder preparedSource = new StringBuilder(source);
    for (int i = 0, i$ = source.length(), count = 0; i < i$; ++i) { // [N]
      if (preparedSource.charAt(i) == '_' && i > 0 && preparedSource.charAt(i - 1) != '/' && preparedSource.charAt(i - 1) != '.')
        preparedSource.setCharAt(i, ':');
      else if (preparedSource.charAt(i) == '/' && ++count < 3)
        preparedSource.setCharAt(i, '.');
    }

    final StringBuilder namespaceUri = new StringBuilder(diff.patch(preparedSource.toString()));
    for (final Rule rule : rules) { // [A]
      final StringBuilder decode = rule.decode(prefix, namespaceUri);
      if (decode != null)
        break;
    }

    if (!Character.isDigit(namespaceUri.charAt(0)))
      return new NamespaceBinding(URI.create(flipNamespaceURI(namespaceUri.toString(), false)), className, simpleClassName);

    final StringBuilder port = new StringBuilder().append(namespaceUri.charAt(0));
    int i = 1;
    while (Character.isDigit(namespaceUri.charAt(i)))
      port.append(namespaceUri.charAt(i++));

    final Service service = Services.getService(Integer.parseInt(port.toString()));
    return new NamespaceBinding(URI.create(service == null ? namespaceUri.toString() : flipNamespaceURI(service.getName() + "://" + namespaceUri.substring(i), false)), className, simpleClassName);
  }

  /**
   * Create a {@link NamespaceBinding} from a {@link URI}. This method guarantees that a unique package name will be created for each
   * unique {@link URI}. Examples of namespaces that would otherwise seem to result in the same package name are:
   * <p>
   * {@code http://www.foo.com/bar.xsd}
   * <p>
   * {@code http://www.foo.com/bar}
   * <p>
   * {@code https://www.foo.com/bar.xsd}
   * <p>
   * {@code https://www.foo.com/bar}
   * <p>
   * {@code file://www.foo.com/bar.xsd}
   * <p>
   * The resulting package name for each of these namespaces is:
   * <p>
   * {@code com.foo.www.bar}
   * <p>
   * The simple class name is based on a Base64 string representation of a diff between the original namespace URI and the resulting
   * package name. The package name carries enough information within itself to be able to translate directly back to the unique
   * namespace URI from which it was generated.
   *
   * @param uri The namespace URI.
   * @return A guaranteed unique {@link NamespaceBinding} to the uri.
   */
  public static NamespaceBinding parseNamespace(final URI uri) {
    if (uri == null)
      return null;

    final StringBuilder builder = new StringBuilder(uri.toString().length());
    final String packageName;
    if (uri.getHost() == null) {
      buildUrn(builder, uri.toString());
      packageName = builder.length() == 0 ? "" : builder.charAt(0) == '_' ? builder.substring(1) : builder.toString();
    }
    else {
      buildHost(builder, uri.getHost());
      buildPath(builder, uri.getPath());
      packageName = builder.length() == 0 ? "" : builder.charAt(0) == '.' ? builder.substring(1) : builder.toString();
    }

    return new NamespaceBinding(uri, packageName);
  }

  /**
   * Create a {@link NamespaceBinding} from a {@link String} uri. This method guarantees that a unique package name will be created
   * for each unique {@link URI}. Examples of namespaces that would otherwise seem to result in the same package name are:
   * <p>
   * {@code http://www.foo.com/bar.xsd}
   * <p>
   * {@code http://www.foo.com/bar}
   * <p>
   * {@code https://www.foo.com/bar.xsd}
   * <p>
   * {@code https://www.foo.com/bar}
   * <p>
   * {@code file://www.foo.com/bar.xsd}
   * <p>
   * The resulting package name for each of these namespaces is:
   * <p>
   * {@code com.foo.www.bar}
   * <p>
   * The simple class name is based on a Base64 string representation of a diff between the original namespace URI and the resulting
   * package name. The package name carries enough information within itself to be able to translate directly back to the unique
   * namespace URI from which it was generated.
   *
   * @param uri The namespace URI.
   * @return A guaranteed unique {@link NamespaceBinding} to the uri.
   * @see NamespaceBinding#parseNamespace(URI)
   */
  public static NamespaceBinding parseNamespace(final String uri) {
    return uri == null || uri.length() == 0 ? null : parseNamespace(URI.create(uri));
  }

  private final URI namespaceUri;
  private final String packageName;
  private final String simpleClassName;
  private final String className;
  private final String javaPath;

  private NamespaceBinding(final URI namespaceUri, final String packageName, final String simpleClassName) {
    this.namespaceUri = namespaceUri;
    this.packageName = packageName;
    this.simpleClassName = simpleClassName;
    this.className = packageName + "." + simpleClassName;
    this.javaPath = className.replace('.', File.separatorChar).concat(".java");
  }

  private NamespaceBinding(final URI namespaceUri, final String packageName) {
    this(namespaceUri, packageName, getDiff(packageName, flipNamespaceURI(namespaceUri.toString(), true)));
  }

  /**
   * Returns the namespace URI for this binding.
   *
   * @return The namespace URI for this binding.
   */
  public URI getNamespaceUri() {
    return namespaceUri;
  }

  /**
   * Returns the package name for this binding.
   *
   * @return The package name for this binding.
   */
  public String getPackageName() {
    return packageName;
  }

  /**
   * Returns the simple class name (i.e. class name without the package name) for this binding.
   *
   * @return The simple class name (i.e. class name without the package name) for this binding.
   */
  public String getSimpleClassName() {
    return simpleClassName;
  }

  /**
   * Returns the fully qualified class name for this binding.
   *
   * @return The fully qualified class name for this binding.
   */
  public String getClassName() {
    return className;
  }

  /**
   * Returns the relative path of the {@code .java} file for this binding.
   *
   * @return The relative path of the {@code .java} file for this binding.
   */
  public String getJavaPath() {
    return javaPath;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof NamespaceBinding))
      return false;

    final NamespaceBinding that = (NamespaceBinding)obj;
    return namespaceUri.equals(that.namespaceUri) && packageName.equals(that.packageName) && simpleClassName.equals(that.simpleClassName);
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + namespaceUri.hashCode();
    hashCode = 31 * hashCode + packageName.hashCode();
    hashCode = 31 * hashCode + simpleClassName.hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    return "{\n  namespaceUri: \"" + namespaceUri + "\",\n  packageName: \"" + packageName + "\",\n  simpleClassName: \"" + simpleClassName + "\"\n}";
  }
}