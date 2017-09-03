package org.phoenixctms.ctsms.security;

/*
 * Copyright (C) 1996 Santeri Paavolainen, Helsinki Finland
 *
 * Copyright (C) 2002-2010 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Java+Utilities
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See LICENSE.txt for details.
 *
 * The original work by Santeri Paavolainen can be found a
 * http://santtu.iki.fi/md5/
 */

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;

/**
 * Implements MD5 functionality on a stream.
 * More information about this class is available from <a target="_top" href=
 * "http://ostermiller.org/utils/MD5.html">ostermiller.org</a>.
 * <p>
 * This class produces a 128-bit "fingerprint" or "message digest" for
 * all data read from this stream.
 * It is conjectured that it is computationally infeasible to produce
 * two messages having the same message digest, or to produce any
 * message having a given pre-specified target message digest. The MD5
 * algorithm is intended for digital signature applications, where a
 * large file must be "compressed" in a secure manner before being
 * encrypted with a private (secret) key under a public-key cryptosystem
 * such as RSA.
 * <p>
 * For more information see RFC1321.
 *
 * @see MD5
 * @see MD5OutputStream
 *
 * @author Santeri Paavolainen http://santtu.iki.fi/md5/
 * @author Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 * @since ostermillerutils 1.00.00
 */
public class VerifyMD5InputStream extends FilterInputStream {
	/**
	 * MD5 context
	 */
	private MessageDigest md5;

	private String md5Hash;

	private boolean on = true;

	/**
	 * Creates a MD5InputStream
	 * @param in the underlying input stream
	 * @throws NoSuchAlgorithmException
	 */
	public VerifyMD5InputStream (InputStream in, String md5) throws NoSuchAlgorithmException {
		super(in);

		this.md5 = MessageDigest.getInstance("MD5");
		this.md5Hash = md5;
	}

	public boolean isOn() {
		return on;
	}

	/**
	 * Reads the next byte of data from this input stream. The value byte
	 * is returned as an int in the range 0 to 255. If no byte is available
	 * because the end of the stream has been reached, the value -1 is returned.
	 * This method blocks until input data is available, the end of the stream is
	 * detected, or an exception is thrown.
	 * <p>
	 * This method simply performs in.read() and returns the result.
	 *
	 * @return the next byte of data, or -1 if the end of the stream is reached.
	 * @throws IOException if an I/O error occurs.
	 *
	 * @since ostermillerutils 1.00.00
	 */
	@Override public int read() throws IOException {
		int c = in.read();
		if (c == -1) {
			return -1;
		}
		if (on) {
			md5.update((byte)(c & 0xff));
		}
		return c;
	}



	/**
	 * Reads up to length bytes of data from this input stream into an
	 * array of bytes. This method blocks until some input is available.
	 *
	 * @param bytes the buffer into which the data is read.
	 * @param offset the start offset of the data.
	 * @param length the maximum number of bytes read.
	 * @throws IOException if an I/O error occurs.
	 *
	 * @since ostermillerutils 1.00.00
	 */
	@Override public int read(byte[] bytes, int offset, int length) throws IOException {
		int r;
		if ((r = in.read(bytes, offset, length)) == -1) {
			if (on && !CommonUtil.getHex(md5.digest()).equalsIgnoreCase(md5Hash)) {
				//due to PF3.0.1 bug...
				//throw new RuntimeException(new GeneralSecurityException(L10nUtil.getMessage(MessageCodes.MD5_CHECK_FAILED,DefaultMessages.MD5_CHECK_FAILED)));
				throw new IOException(L10nUtil.getMessage(MessageCodes.MD5_CHECK_FAILED,DefaultMessages.MD5_CHECK_FAILED));
			}
			return r;
		}
		if (on) {
			md5.update(bytes, offset, r);
		}
		return r;
	}

	@Override public void reset() throws IOException {
		super.reset();
		md5.reset();
	}

	public void setOn(boolean on) {
		this.on = on;
	}




}

