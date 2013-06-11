package no.sumo.api.exception.category.rating;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: ogi
 * Date: 22.06.12
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class CategoryRatingNotFoundException extends NotFoundException {
    public CategoryRatingNotFoundException(Long ratingId, Long categoryId) {
        this(ratingId, String.format("No rating with id '%1$s' for category '%2$s' was found", ratingId, categoryId));
    }

    public CategoryRatingNotFoundException(Long ratingId, String message) {
        super(Errorcode.CATEGORY_RATING_NOT_FOUND, message, ratingId);
    }
}
