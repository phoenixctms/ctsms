package org.phoenixctms.ctsms.web.jersey.resource;

import java.io.Serializable;
import java.util.Collection;

import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.jersey.wrapper.PSFVOWrapper;

import com.google.gson.annotations.SerializedName;

public class Page<T extends Serializable> implements Serializable {

	// https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/annotations/Expose.html
	// serializedname is not read by resourcelistingresource
	private transient PSFVO psf;
	@SerializedName("psf")
	private PSFVOWrapper jsPsf;
	// @SerializedName("items")
	private Collection<T> rows;

	public Page(Collection<T> rows, PSFVO psf) {
		setPsf(psf);
		setRows(rows);
	}

	public PSFVO getPsf() {
		return psf;
	}

	public Collection<T> getRows() {
		return rows;
	}

	public void setPsf(PSFVO psf) {
		if (psf != null) {
			this.jsPsf = new PSFVOWrapper(psf);
		} else {
			this.jsPsf = null;
		}
		this.psf = psf;
	}

	public void setRows(Collection<T> rows) {
		this.rows = rows;
	}
}
