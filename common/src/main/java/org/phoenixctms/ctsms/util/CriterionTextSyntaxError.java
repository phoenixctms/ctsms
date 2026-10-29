package org.phoenixctms.ctsms.util;

import java.io.Serializable;

/**
 * Data of the ServiceException that SearchService.parseCriterionText throws when the parsed query text fails the syntax check of the query builder:
 * the position of the criterion where the check failed, and the line of the query text it starts on (null for JSON).
 */
public class CriterionTextSyntaxError implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Long position;
	private final Integer line;

	public CriterionTextSyntaxError(Long position, Integer line) {
		this.position = position;
		this.line = line;
	}

	public Integer getLine() {
		return line;
	}

	public Long getPosition() {
		return position;
	}
}
