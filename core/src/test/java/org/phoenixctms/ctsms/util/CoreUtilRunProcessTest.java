// This file is part of the Phoenix CTMS project (www.phoenixctms.org),
// distributed under LGPL v2.1. Copyright (C) 2011 - 2017.
//
package org.phoenixctms.ctsms.util;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Background jobs must not block when the child writes more than a pipe buffer
 * to stdout or stderr. {@link CoreUtil#runProcess(boolean, String...)} used to
 * leave those pipes unread.
 */
@Test(groups = { "util", "CoreUtil" })
public class CoreUtilRunProcessTest {

	public static void main(String[] args) throws Exception {
		String mode = args.length > 0 ? args[0] : "";
		byte[] chunk = new byte[256 * 1024];
		java.util.Arrays.fill(chunk, (byte) 'x');
		if ("fail".equals(mode)) {
			System.err.println("boom-marker");
			System.err.flush();
			System.out.write(chunk);
			System.out.flush();
			System.exit(3);
		}
		System.out.write(chunk);
		System.out.flush();
		System.err.write(chunk);
		System.err.flush();
		System.exit(0);
	}

	private static String javaBin() {
		File exe = new File(System.getProperty("java.home"), "bin/java.exe");
		if (exe.isFile()) {
			return exe.getPath();
		}
		return new File(System.getProperty("java.home"), "bin/java").getPath();
	}

	private static String[] javaCommand(String mode) {
		return new String[] {
				javaBin(),
				"-cp",
				System.getProperty("java.class.path"),
				CoreUtilRunProcessTest.class.getName(),
				mode
		};
	}

	@Test(timeOut = 20000)
	public void backgroundJobPastPipeBufferDoesNotBlock() throws Exception {
		Process process = null;
		try {
			process = CoreUtil.runProcess(false, javaCommand("flood"));
			Assert.assertFalse(process.isAlive(), "256KB of output should not leave the child blocked");
			Assert.assertEquals(process.exitValue(), 0);
		} finally {
			if (process != null) {
				process.destroyForcibly();
			}
		}
	}

	@Test(timeOut = 20000)
	public void backgroundJobFailureStillReturnsStderr() throws Exception {
		try {
			CoreUtil.runProcess(false, javaCommand("fail"));
			Assert.fail("expected non-zero exit");
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage() != null && e.getMessage().contains("boom-marker"), e.getMessage());
		}
	}

	@Test(timeOut = 20000)
	public void blockingJobPastPipeBufferCompletes() throws Exception {
		Process process = CoreUtil.runProcess(true, javaCommand("ok"));
		Assert.assertEquals(process.exitValue(), 0);
	}
}
