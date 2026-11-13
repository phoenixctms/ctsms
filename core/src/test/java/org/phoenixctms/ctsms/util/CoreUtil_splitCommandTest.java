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
	public void testWindowsPathKeepsBackslashes() {
		Assert.assertEquals(CoreUtil.splitCommand("C:\\ctsms\\dbtool.bat -task=cleanup -id=5"),
				new String[] { "C:\\ctsms\\dbtool.bat", "-task=cleanup", "-id=5" });
	}

	@Test
	public void testQuotedWindowsPathKeepsBackslashes() {
		Assert.assertEquals(CoreUtil.splitCommand("perl \"D:\\config default\\EcrfExporter\\process.pl\" --config=config.job.cfg"),
				new String[] { "perl", "D:\\config default\\EcrfExporter\\process.pl", "--config=config.job.cfg" });
		Assert.assertEquals(CoreUtil.splitCommand("-dir=\"C:\\ctsms\\\" -next"),
				new String[] { "-dir=C:\\ctsms\\", "-next" });
	}

	@Test
	public void testUncPathKeepsBackslashes() {
		Assert.assertEquals(CoreUtil.splitCommand("\\\\server\\share\\dbtool.bat -id=5"),
				new String[] { "\\\\server\\share\\dbtool.bat", "-id=5" });
	}

	@Test
	public void testPasswordIsInsertedAfterSplit() {
		String command = "C:\\ctsms\\dbtool.bat -u=user -p=\"" + CoreUtil.COMMAND_PASSWORD_PLACEHOLDER + "\" --query=\"eligible subjects\"";
		Assert.assertEquals(CoreUtil.splitCommand(command, "a\"b c\\"),
				new String[] { "C:\\ctsms\\dbtool.bat", "-u=user", "-p=a\"b c\\", "--query=eligible subjects" });
	}

	@Test
	public void testUnquotedPasswordStaysOneArgument() {
		Assert.assertEquals(CoreUtil.splitCommand("dbtool -p=" + CoreUtil.COMMAND_PASSWORD_PLACEHOLDER + " -next", "a b\"c"),
				new String[] { "dbtool", "-p=a b\"c", "-next" });
		Assert.assertEquals(CoreUtil.splitCommand("dbtool -p=" + CoreUtil.COMMAND_PASSWORD_PLACEHOLDER + " -next", ""),
				new String[] { "dbtool", "-p=", "-next" });
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
