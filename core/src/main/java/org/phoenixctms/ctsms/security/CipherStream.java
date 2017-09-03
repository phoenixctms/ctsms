package org.phoenixctms.ctsms.security;

import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

public class CipherStream extends CipherOutputStream {

	private byte[] iv;

	public CipherStream(OutputStream os, Cipher c) {
		super(os, c);
		iv = c.getIV();
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}
}
