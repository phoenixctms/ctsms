package org.phoenixctms.ctsms.web.component.tagcloud;

import java.io.Serializable;

public interface TagCloudItem extends Serializable {

	public String getLabel();

	public String getOnclick();

	public int getStrength();

	public void setStrength(int strength);
}