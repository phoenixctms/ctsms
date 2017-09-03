package org.phoenixctms.ctsms.query.parser;

public class SyntaxException extends Exception {

	public enum SyntaxErrors {
		MISSING_LEFT_PARENTHESIS,
		INCOMPATIBLE_FIRST_OPERAND_TYPE,
		INCOMPATIBLE_SECOND_OPERAND_TYPE,
		MISSING_OPERAND,
		MISSING_OPERATOR,
		NO_OPERATOR,
		LEFT_PARENTHESIS_AFTER_RIGHT_PARETHESIS,
		LEFT_PARENTHESIS_AFTER_VALUE,
		LEFT_PARENTHESIS_AT_END,
		RIGHT_PARENTHESIS_AT_BEGIN,
		RIGHT_PARENTHESIS_AFTER_LEFT_PARENTHESIS,
		RIGHT_PARENTHESIS_AFTER_OPERATOR,
		OPERATOR_AT_BEGIN,
		OPERATOR_AFTER_LEFT_PARENTHESIS,
		OPERATOR_AFTER_OPERATOR,
		OPERATOR_AT_END,
		VALUE_AFTER_RIGHT_PARENTHESIS,
		VALUE_AFTER_VALUE,
		PARENTHESIS_MISSING
	}

	private static final long serialVersionUID = 1L;
	private Object token;
	private SyntaxErrors error;

	public SyntaxException(SyntaxErrors error) {
		super();
		this.error = error;
	}

	public SyntaxException(SyntaxErrors error, Object token) {
		super();
		this.error = error;
		this.token = token;
	}

	public SyntaxException(SyntaxErrors error, Object token, Throwable cause) {
		super(cause);
		this.error = error;
		this.token = token;
	}

	public SyntaxException(SyntaxErrors error, Throwable cause) {
		super(cause);
		this.error = error;
	}

	public SyntaxErrors getError() {
		return error;
	}

	public Object getToken() {
		return token;
	}

	public void setError(SyntaxErrors error) {
		this.error = error;
	}

	public void setToken(Object token) {
		this.token = token;
	}
}
