package no.sumo.api.exception.asset.relations;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class AssetRelationNotFoundException extends NotFoundException {

    public AssetRelationNotFoundException() {
        super(Errorcode.ASSET_RELATION_NOT_FOUND);
    }
}
