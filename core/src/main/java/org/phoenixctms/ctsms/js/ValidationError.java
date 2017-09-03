package org.phoenixctms.ctsms.js;

import sun.org.mozilla.javascript.internal.Scriptable;


public class ValidationError {

	private Long ecrfFieldId;
	private Long inquiryId;
	private Long tagId;
	private Boolean series;
	private Long index;
	private String output;
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

	public ValidationError(Scriptable msg) {
		// TODO Auto-generated constructor stub
		// scriptable.get("output");
		// if (msg != null) {
		// for (Object o : msg.getIds()) {
		// int index = (Integer) o;
		// String x = (String) msg.get(index, null);
		// System.out.println(x);
		// }
		// }
		// Integer i = (Integer) msg.get(ECRF_FIELD_ID, null);
		// ecrfFieldId = (i != null ? new Long(i) : null);
		// i = (Integer) msg.get(INQUIRY_ID, null);
		// inquiryId = (i != null ? new Long(i) : null);
		// i = (Integer) msg.get(TAG_ID, null);
		// tagId = (i != null ? new Long(i) : null);
		// series = (Boolean) msg.get(SERIES, null);
		// i = (Integer) msg.get(INDEX, null);
		// index = (i != null ? new Long(i) : null);
		// output = (String) msg.get(OUTPUT, null);
		ecrfFieldId = convertToLong(msg.get(ECRF_FIELD_ID, null));
		inquiryId = convertToLong(msg.get(INQUIRY_ID, null));
		tagId = convertToLong(msg.get(TAG_ID, null));
		series = convertToBoolean(msg.get(SERIES, null));
		index = convertToLong(msg.get(INDEX, null));
		output = convertToString(msg.get(OUTPUT, null));
		////		try {
		////			ecrfFieldId = Long.pnew Long((Integer) msg.get(ECRF_FIELD_ID, null));
		////		} catch (Exception e) {
		////		}
		//		try {
		//			inquiryId = new Long((Integer) msg.get(INQUIRY_ID, null));
		//		} catch (Exception e) {
		//		}
		//		try {
		//			tagId = new Long((Integer) msg.get(TAG_ID, null));
		//		} catch (Exception e) {
		//		}
		//		series = (Boolean) msg.get(SERIES, null);
		//		try {
		//			index = new Long((Integer) msg.get(INDEX, null));
		//		} catch (Exception e) {
		//		}
		//		output = (String) msg.get(OUTPUT, null);
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
