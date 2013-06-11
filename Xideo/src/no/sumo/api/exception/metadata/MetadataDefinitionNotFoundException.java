package no.sumo.api.exception.metadata;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.NotFoundException;

public class MetadataDefinitionNotFoundException extends NotFoundException{
    public MetadataDefinitionNotFoundException() {
        super(Errorcode.METADATA_DEFINITION_NOT_FOUND);
    }
}
