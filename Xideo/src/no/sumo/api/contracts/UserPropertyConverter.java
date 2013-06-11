package no.sumo.api.contracts;

import java.util.List;

import no.sumo.api.vo.profile.RestUserProperty;

public class UserPropertyConverter {

	public List<IUserProperty> transfer( List<RestUserProperty> properties ) {
		return null;
	}

	public List<RestUserProperty> transfer( List<? extends IUserProperty> properties, Class<RestUserProperty> class1 ) {
		return null;
	}

}
