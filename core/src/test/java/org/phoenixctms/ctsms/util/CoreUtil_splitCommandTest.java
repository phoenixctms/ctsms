// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2026.
//
package org.phoenixctms.ctsms.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Job commands are one string. Quoted arguments must survive the split into a process command.
 */
public class CoreUtil_splitCommandTest {

	@Test
	public void testUnquotedCommandSplitsOnWhitespace() {
		Assert.assertEquals(CoreUtil.splitCommand("perl process.pl --task=cleanup -id=5"),
				new String[] { "perl", "process.pl", "--task=cleanup", "-id=5" });
	}

	@Test
	public void testQuotedQueryLabelStaysOneArgument() {
		Assert.assertEquals(CoreUtil.splitCommand("--query-category=\"HCMreg\" --query=\"eligible subjects\""),
				new String[] { "--query-category=HCMreg", "--query=eligible subjects" });
	}

	@Test
	public void testQuotedSeparateArgumentDropsQuotes() {
		Assert.assertEquals(CoreUtil.splitCommand("--query \"eligible subjects\""),
				new String[] { "--query", "eligible subjects" });
	}

	@Test
	public void testUnclosedQuoteFails() {
		try {
			CoreUtil.splitCommand("--query=\"eligible");
			Assert.fail("unclosed quote must fail");
		} catch (IllegalArgumentException e) {
			Assert.assertEquals(e.getMessage(), "Unclosed quote in command");
		}
	}

}
