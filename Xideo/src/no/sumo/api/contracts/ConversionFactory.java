package no.sumo.api.contracts;

public final class ConversionFactory {

	private ConversionFactory() {
	}

	public static PlaybackQueueItemConverter getPlaybackQueueItemConverter() {
		return new PlaybackQueueItemConverter();
	}

	public static ProgramConverter getProgramConverter() {
		return new ProgramConverter();
	}

	public static ProgramPublishedConverter getProgramPublishedConverter() {
		return new ProgramPublishedConverter();
	}

	public static ArticleConverter getArticleConverter() {
		return new ArticleConverter();
	}

	public static ProgramTypeTreeConverter getProgramTypeTreeConverter() {
		return new ProgramTypeTreeConverter();
	}

	public static PlaybackItemConverter getPlaybackItemConverter() {
		return new PlaybackItemConverter();
	}

	public static PlatformConverter getPlatformConverter() {
		return new PlatformConverter();
	}

	public static UserPropertyConverter getUserPropertyConverter() {
		return new UserPropertyConverter();
	}
}