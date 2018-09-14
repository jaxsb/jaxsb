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

package org.openjax.xsb.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.openjax.xsb.runtime.Binding;
import org.openjax.xsb.runtime.Bindings;
import org.openjax.xsb.sample.id.xAA.$BookType;
import org.openjax.xsb.sample.id.xAA.Directory;
import org.w3.www._2001.XMLSchema.yAA.$IDREFS;
import org.xml.sax.InputSource;

public class IdSample {
  public static void main(final String[] args) throws Exception {
    new IdSample().runSample();
  }

  public Binding runSample() throws Exception {
    final File file = new File("src/main/resources/id.xml");
    if (!file.exists())
      throw new FileNotFoundException("File " + file.getAbsolutePath() + " does not exist.");

    if (!file.canRead())
      throw new IllegalStateException("File " + file.getAbsolutePath() + " is not readable.");

    final Directory directory = (Directory)Bindings.parse(new InputSource(new FileInputStream(file)));
    final List<$BookType> books = directory.getBook();
    for (final $BookType book : books) {
      final String shortName = book.getAuthor().text();
      final Directory.Author.Id$ authorId = Directory.Author.Id$.lookupId(shortName);
      final Directory.Author author = (Directory.Author)authorId.owner();
      System.out.print(author.getName().text() + " is the author of " + book.getTitle().text() + ".");
      if (book.getCo$_authors() != null) {
        final $IDREFS coAuthors = book.getCo$_authors();
        if (coAuthors.text() != null) {
          StringBuffer buffer = new StringBuffer();
          for (final Object coAuthorIdString : coAuthors.text()) {
            final Directory.Author.Id$ coAuthorId = Directory.Author.Id$.lookupId((String)coAuthorIdString);
            final Directory.Author coAuthor = (Directory.Author)coAuthorId.owner();
            buffer.append(", ").append(coAuthor.getName().text());
          }

          System.out.print(" " + buffer.substring(2));
          if (coAuthors.text().size() == 1)
            System.out.print(" is the co-author.");
          else
            System.out.print(" are co-authors.");
        }
      }

      System.out.println();
    }

    return directory;
  }
}