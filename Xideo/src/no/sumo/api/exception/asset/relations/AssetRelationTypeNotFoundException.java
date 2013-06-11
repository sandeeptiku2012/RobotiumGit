package no.sumo.api.exception.asset.relations;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class AssetRelationTypeNotFoundException extends NotFoundException{

    public AssetRelationTypeNotFoundException() {
        super(Errorcode.ASSET_RELATION_TYPE_NOT_FOUND);
    }
}
