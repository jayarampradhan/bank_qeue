package com.uimirror.websocket.stomp.counter;

import java.io.Serializable;

public class TicketFormBean implements Serializable{
	
	private static final long serialVersionUID = -6996847193755003257L;

	private String details;
	private TicketType type;

	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public TicketType getType() {
		return type;
	}
	public void setType(TicketType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "TicketFormBean [details=" + details + ", type=" + type + "]";
	}
	
}
