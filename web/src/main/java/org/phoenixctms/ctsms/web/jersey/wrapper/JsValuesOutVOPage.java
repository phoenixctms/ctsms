package org.phoenixctms.ctsms.web.jersey.wrapper;

import java.io.Serializable;
import java.util.Collection;

import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.web.jersey.resource.Page;

import com.google.gson.annotations.SerializedName;

public class JsValuesOutVOPage<OUTVO extends Serializable, JsonVO extends Serializable> extends Page<OUTVO> implements Serializable {

	@SerializedName("js_rows")
	private Collection<JsonVO> jsRows;

	public JsValuesOutVOPage(Collection<OUTVO> rows, Collection<JsonVO> jsRows, PSFVO psf) {
		super(rows, psf);
		setJsRows(jsRows);
	}

	public Collection<JsonVO> getJsRows() {
		return jsRows;
	}

	public void setJsRows(Collection<JsonVO> jsRows) {
		this.jsRows = jsRows;
	}
}
