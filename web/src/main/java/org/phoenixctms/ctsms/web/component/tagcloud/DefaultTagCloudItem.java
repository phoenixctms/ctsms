package org.phoenixctms.ctsms.web.component.tagcloud;

public class DefaultTagCloudItem implements TagCloudItem {

	private String label;
	private String onclick;
	private int strength = 1;

	public DefaultTagCloudItem() {
	}

	public DefaultTagCloudItem(String label, String onclick, int strength) {
		this.label = label;
		this.onclick = onclick;
		this.strength = strength;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TagCloudItem other = (TagCloudItem) obj;
		if ((this.label == null) ? (other.getLabel() != null) : !this.label.equals(other.getLabel())) {
			return false;
		}
		if ((this.onclick == null) ? (other.getOnclick() != null) : !this.onclick.equals(other.getOnclick())) {
			return false;
		}
		if (this.strength != other.getStrength()) {
			return false;
		}
		return true;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getOnclick() {
		return onclick;
	}

	@Override
	public int getStrength() {
		return strength;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 73 * hash + (this.label != null ? this.label.hashCode() : 0);
		hash = 73 * hash + (this.onclick != null ? this.onclick.hashCode() : 0);
		hash = 73 * hash + this.strength;
		return hash;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	@Override
	public void setStrength(int strength) {
		this.strength = strength;
	}
}
