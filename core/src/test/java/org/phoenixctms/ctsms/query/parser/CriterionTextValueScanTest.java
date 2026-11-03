// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2026.
//
package org.phoenixctms.ctsms.query.parser;

import org.phoenixctms.ctsms.enumeration.CriterionTie;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Value scanning must not treat a tie name embedded in a word as a tie.
 */
public class CriterionTextValueScanTest {

	private static final String[] TIE_NAMES = new String[] { "and", "(", ")" };
	private static final CriterionTie[] TIES = new CriterionTie[] { CriterionTie.AND, CriterionTie.LEFT_PARENTHESIS, CriterionTie.RIGHT_PARENTHESIS };

	@Test
	public void testBrandNewStaysIntact() {
		String value = "Brand new";
		Assert.assertEquals(CriterionText.scanValueEnd(value, 0, TIE_NAMES, TIES), value.length());
	}

	@Test
	public void testParenthesesStayInsideValue() {
		String value = "Smith (Jr)";
		Assert.assertEquals(CriterionText.scanValueEnd(value, 0, TIE_NAMES, TIES), value.length());
	}

	@Test
	public void testTieAfterBoundaryStillEndsValue() {
		Assert.assertEquals(CriterionText.scanValueEnd("status and next", 0, TIE_NAMES, TIES), "status ".length());
	}
}
