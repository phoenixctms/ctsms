package org.phoenixctms.ctsms.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.application.PrimeResourceHandler;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.Constants;

public class ResourceHandlerWrapper extends PrimeResourceHandler {

	private final static Logger logger = Logger.getLogger(ResourceHandlerWrapper.class.getName());
	private static final HashSet<String> ALLOWED_EXPRESSIONS = new HashSet<String>();
	static {
		ALLOWED_EXPRESSIONS.add("#{sessionScopeBean.getImage()}");
	}

	public ResourceHandlerWrapper(ResourceHandler wrapped) {
		super(wrapped);
	}

	@Override
	public void handleResourceRequest(FacesContext context) throws IOException {
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String library = params.get("ln");
		String dynamicContentId = params.get(Constants.DYNAMIC_CONTENT_PARAM);
		if (dynamicContentId != null && library != null && library.equals("primefaces")) {
			Map<String, Object> session = context.getExternalContext().getSessionMap();
			try {
				String dynamicContentEL = (String) session.get(dynamicContentId);
				if (!ALLOWED_EXPRESSIONS.contains(dynamicContentEL)) {
					throw new Exception("prevented EL " + dynamicContentEL);
				}
				System.out.println(dynamicContentEL);
				ELContext eLContext = context.getELContext();
				ValueExpression ve = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), dynamicContentEL, StreamedContent.class);
				StreamedContent content = (StreamedContent) ve.getValue(eLContext);
				HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
				response.setContentType(content.getContentType());
				byte[] buffer = new byte[2048];
				int length;
				InputStream inputStream = content.getStream();
				while ((length = (inputStream.read(buffer))) >= 0) {
					response.getOutputStream().write(buffer, 0, length);
				}
				response.setStatus(200);
				response.getOutputStream().flush();
				context.responseComplete();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error in streaming dynamic resource: " + e.getMessage());
			} finally {
				session.remove(dynamicContentId);
			}
		}
		else {
			super.handleResourceRequest(context);
		}
	}
}
