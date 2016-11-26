/* Copyright (c) 2008 Seva Safris
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

package org.safris.xsb.generator.processor.plan.element;

import org.safris.xsb.compiler.processor.model.element.WhiteSpaceModel;
import org.safris.xsb.generator.processor.plan.Plan;

public final class WhiteSpacePlan extends Plan<WhiteSpaceModel> {
  public WhiteSpacePlan(final WhiteSpaceModel model, final Plan<?> parent) {
    super(model, parent);
  }
}