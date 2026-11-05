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

	// the tie names of an expression are the symbols only
	private static final String[] TIE_NAMES = new String[] { "∧", "∨", "∪", "∩", "∖", "(", ")" };
	private static final CriterionTie[] TIES = new CriterionTie[] { CriterionTie.AND, CriterionTie.OR, CriterionTie.UNION, CriterionTie.INTERSECT, CriterionTie.EXCEPT,
			CriterionTie.LEFT_PARENTHESIS, CriterionTie.RIGHT_PARENTHESIS };

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
	public void testTieSymbolEndsValue() {
		Assert.assertEquals(CriterionText.scanValueEnd("status ∧ next", 0, TIE_NAMES, TIES), "status ".length());
	}

	@Test
	public void testTieWordStaysInsideValue() {
		String value = "NN1679-8505 SDF training visit 3 and postponed clamp Version 2";
		Assert.assertEquals(CriterionText.scanValueEnd(value, 0, TIE_NAMES, TIES), value.length());
	}
}
