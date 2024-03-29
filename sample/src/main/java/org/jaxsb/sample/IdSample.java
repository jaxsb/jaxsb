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

package org.jaxsb.sample;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jaxsb.runtime.Bindings;
import org.jaxsb.www.sample.id.xAA.$BookType;
import org.jaxsb.www.sample.id.xAA.Directory;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;
import org.w3.www._2001.XMLSchema.yAA.$IDREFS;
import org.xml.sax.SAXException;

public class IdSample {
  public static void main(final String[] args) throws Exception {
    new IdSample().runSample();
  }

  public $AnyType<?> runSample() throws IOException, SAXException {
    final URL url = getClass().getResource("/id.xml");
    final Directory directory = (Directory)Bindings.parse(url);
    final List<$BookType> books = directory.getBook();
    for (final $BookType book : books) { // [L]
      final String shortName = book.getAuthor().text();
      final Directory.Author.Id$ authorId = Directory.Author.Id$.lookupId(shortName);
      final Directory.Author author = (Directory.Author)authorId.owner();
      System.out.print(author.getName().text() + " is the author of " + book.getTitle().text() + ".");
      if (book.getCo$_authors() != null) {
        final $IDREFS coAuthors = book.getCo$_authors();
        if (coAuthors.text() != null) {
          final StringBuilder builder = new StringBuilder();
          for (final Object coAuthorIdString : coAuthors.text()) { // [L]
            final Directory.Author.Id$ coAuthorId = Directory.Author.Id$.lookupId((String)coAuthorIdString);
            final Directory.Author coAuthor = (Directory.Author)coAuthorId.owner();
            builder.append(", ").append(coAuthor.getName().text());
          }

          System.out.print(" " + builder.substring(2));
          System.out.print(coAuthors.text().size() == 1 ? " is the co-author." : " are co-authors.");
        }
      }

      System.out.print('\n');
    }

    return directory;
  }
}