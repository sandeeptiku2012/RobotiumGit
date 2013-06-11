package no.sumo.api.exception.category.rating;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.ValidationException;

/**
 * Created with IntelliJ IDEA.
 * User: ogi
 * Date: 26.06.12
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
//TODO rename
public class CategoryRatingFoundException extends ValidationException {

    public CategoryRatingFoundException(Long categoryId, Long memberId) {
        this(String.format("Rating for category '%1$s' with memberId '%2$s' already exists", categoryId, memberId));
    }

    public CategoryRatingFoundException(String message) {
        super(Errorcode.PERMISSION_DENIED, message);
    }
}
