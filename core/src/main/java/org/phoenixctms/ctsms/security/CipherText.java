package org.phoenixctms.ctsms.security;

public class CipherText {

	private byte[] iv;
	private byte[] cipherText;

	public CipherText() {
	}

	public CipherText(byte[] iv, byte[] cipherText) {
		super();
		this.iv = iv;
		this.cipherText = cipherText;
	}

	public byte[] getCipherText() {
		return cipherText;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setCipherText(byte[] cipherText) {
		this.cipherText = cipherText;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}
}
