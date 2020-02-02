package org.phoenixctms.ctsms.js;

import jdk.nashorn.api.scripting.JSObject;

@SuppressWarnings("restriction")
public class ValidationError {

	private final static String ECRF_FIELD_ID = "ecrfFieldId";
	private final static String INQUIRY_ID = "inquiryId";
	private final static String TAG_ID = "tagId";
	private final static String INDEX = "index";
	private final static String SERIES = "series";
	private final static String OUTPUT = "output";

	private static Boolean convertToBoolean(Object o) {
		try {
			return Boolean.valueOf(o.toString());
		} catch (Exception e) {
		}
		return null;
	}

	private static Long convertToLong(Object o) {
		try {
			return Long.parseLong(String.valueOf(o));
		} catch (Exception e1) {
			try {
				return (long) Double.parseDouble(String.valueOf(o));
			} catch (Exception e2) {
			}
		}
		return null;
	}

	private static String convertToString(Object o) {
		try {
			return o.toString();
		} catch (Exception e) {
		}
		return null;
	}

	private Long ecrfFieldId;
	private Long inquiryId;
	private Long tagId;
	private Boolean series;
	private Long index;
	private String output;

	public ValidationError(JSObject msg) {
		ecrfFieldId = convertToLong(msg.getMember(ECRF_FIELD_ID));
		inquiryId = convertToLong(msg.getMember(INQUIRY_ID));
		tagId = convertToLong(msg.getMember(TAG_ID));
		series = convertToBoolean(msg.getMember(SERIES));
		index = convertToLong(msg.getMember(INDEX));
		output = convertToString(msg.getMember(OUTPUT));
	}

	public Long getEcrfFieldId() {
		return ecrfFieldId;
	}

	public Long getIndex() {
		return index;
	}

	public Long getInquiryId() {
		return inquiryId;
	}

	public String getOutput() {
		return output;
	}

	public Boolean getSeries() {
		return series;
	}

	public Long getTagId() {
		return tagId;
	}
}
