package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.service.util.UserPropertyValueLoginParameters;
import no.sumo.api.vo.profile.RestLoginResult;

import org.jboss.resteasy.annotations.Form;

/**
 * This login service authenticates users based on
 * {@link no.sumo.api.vo.profile.UserProperty#getValue()}
 */
@Consumes( MediaType.WILDCARD )
@Path( "/authentication/silent" )
public interface IUserPropertyValueLoginService {

	@POST
	@Formatted
	@Path( "/login" )
	public RestLoginResult login( @Form UserPropertyValueLoginParameters parameters );

}
