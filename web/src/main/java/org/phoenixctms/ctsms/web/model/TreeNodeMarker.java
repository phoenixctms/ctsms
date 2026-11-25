package org.phoenixctms.ctsms.web.model;

public class TreeNodeMarker {

	private Long id;
	private String label;

	public TreeNodeMarker(Long id, String label) {
		this.id = id;
		this.label = label;
	}

	public Long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
}
