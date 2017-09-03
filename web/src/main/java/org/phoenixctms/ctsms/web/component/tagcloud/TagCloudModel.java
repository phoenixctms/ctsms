package org.phoenixctms.ctsms.web.component.tagcloud;

import java.io.Serializable;
import java.util.List;

public interface TagCloudModel extends Serializable {

	public void addTag(TagCloudItem item);

	public void clear();

	public List<TagCloudItem> getTags();

	public void removeTag(TagCloudItem item);
}