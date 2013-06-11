package no.sumo.api.exception;



public class NoValidChannelListException extends ThirdPartyErrorException {

	private static final long serialVersionUID = -1971339713675222377L;

	public NoValidChannelListException(String message) {
		super(message);
	}
}
