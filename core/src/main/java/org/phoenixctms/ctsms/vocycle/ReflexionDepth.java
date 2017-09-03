package org.phoenixctms.ctsms.vocycle;

public class ReflexionDepth {

	private int parentDepth;
	private int childrenDepth;

	public ReflexionDepth(int parentDepth, int childrenDepth) {
		this.parentDepth = parentDepth;
		this.childrenDepth = childrenDepth;
	}

	public int getChildrenDepth() {
		return childrenDepth;
	}

	public int getParentDepth() {
		return parentDepth;
	}

	public ReflexionDepth towardsChildren() {
		return new ReflexionDepth(this.parentDepth + 1, this.childrenDepth - 1);
	}

	public ReflexionDepth towardsParent() {
		return new ReflexionDepth(this.parentDepth - 1, this.childrenDepth + 1);
	}
}