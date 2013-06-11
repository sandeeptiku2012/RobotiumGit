package no.sumo.api.exception.asset.comment;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class AssetCommentNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -2082220236189704258L;

	public AssetCommentNotFoundException(Long commentId) {
		this(commentId, String.format("Asset comment with id '%s' was not found", commentId));
	}
	
	public AssetCommentNotFoundException(Errorcode code, String message) {
		super(code, message);
	}

	public AssetCommentNotFoundException(Long commentId, String message) {
		super(Errorcode.ASSET_COMMENT_NOT_FOUND, message, commentId);
	}
}
