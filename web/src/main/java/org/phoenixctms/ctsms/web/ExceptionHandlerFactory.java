package org.phoenixctms.ctsms.web;

public class ExceptionHandlerFactory extends javax.faces.context.ExceptionHandlerFactory {

	private javax.faces.context.ExceptionHandlerFactory parent;

	public ExceptionHandlerFactory(javax.faces.context.ExceptionHandlerFactory parent) {
		super();
		this.parent = parent;
	}

	@Override
	public javax.faces.context.ExceptionHandler getExceptionHandler() {
		javax.faces.context.ExceptionHandler result = parent.getExceptionHandler();
		result = new ExceptionHandler(result);
		return result;
	}
}
