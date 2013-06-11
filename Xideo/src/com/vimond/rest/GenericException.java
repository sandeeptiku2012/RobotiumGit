package com.vimond.rest;

import java.io.IOException;

import no.sumo.api.exception.Errorcode;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "error" )
public class GenericException extends IOException {
	private static final long serialVersionUID = 343272684964480972L;

	@Element
	public Errorcode code;

	@Element
	public String description;

	@Element( required = false )
	public String endUserMessage;

	@Element
	public String reference;

	@Override
	public String toString() {
		return code + "\n" + description;
	}
}
