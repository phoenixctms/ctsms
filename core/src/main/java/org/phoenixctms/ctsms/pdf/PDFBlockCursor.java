package org.phoenixctms.ctsms.pdf;

public abstract class PDFBlockCursor {

	protected float blockX;
	protected float blockY;
	protected float blockWidth;

	public float getBlockCenterX() {
		return blockX + blockWidth / 2.0f;
	}

	public float getBlockWidth() {
		return blockWidth;
	}

	public float getBlockX() {
		return blockX;
	}

	public float getBlockY() {
		return blockY;
	}

	public void setBlockWidth(float blockWidth) {
		this.blockWidth = blockWidth;
	}

	public void setBlockX(float blockX) {
		this.blockX = blockX;
	}

	public void setBlockY(float blockY) {
		this.blockY = blockY;
	}
}
