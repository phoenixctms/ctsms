package org.phoenixctms.ctsms.web.jersey.resource;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.In;

/**
 * Declares the authentication schemes the REST API accepts (see AuthenticationProvider)
 * in swagger.json, so clients like Swagger UI offer an "Authorize" dialog.
 * Picked up by the BeanConfig scan of this package because of the @SwaggerDefinition annotation.
 */
@SwaggerDefinition
public class SwaggerSecurity implements ReaderListener {

	public final static String BASIC_AUTH = "basicAuth";
	public final static String BEARER_AUTH = "bearerAuth";

	@Override
	public void beforeScan(Reader reader, Swagger swagger) {
	}

	@Override
	public void afterScan(Reader reader, Swagger swagger) {
		BasicAuthDefinition basicAuth = new BasicAuthDefinition();
		basicAuth.setDescription("username and password of a user");
		swagger.addSecurityDefinition(BASIC_AUTH, basicAuth);
		// swagger 2.0 has no bearer scheme, so declare the header:
		ApiKeyAuthDefinition bearerAuth = new ApiKeyAuthDefinition("Authorization", In.HEADER);
		bearerAuth.setDescription("JWT, entered as: Bearer <token>");
		swagger.addSecurityDefinition(BEARER_AUTH, bearerAuth);
		// either one applies to all operations:
		swagger.addSecurity(new SecurityRequirement().requirement(BASIC_AUTH));
		swagger.addSecurity(new SecurityRequirement().requirement(BEARER_AUTH));
	}
}
