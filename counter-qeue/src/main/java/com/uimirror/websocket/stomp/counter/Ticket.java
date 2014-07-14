package com.uimirror.websocket.stomp.counter;

import java.io.Serializable;

public class Ticket implements Serializable{
	
	private static final long serialVersionUID = -6996847193755003257L;

	private final int id;
	private final String details;
	private final TicketType type;
	private final long createdOn;
	private final long servedOn;
	private final long closedOn;
	private final boolean isClosed;
	private final boolean isWaiting;
	
	public Ticket(int id, String details, TicketType type) {
		super();
		this.id = id;
		this.details = details;
		this.type = type;
		this.createdOn = System.currentTimeMillis();
		this.servedOn = 0l;
		this.closedOn = 0l;
		this.isClosed = Boolean.FALSE;
		this.isWaiting = Boolean.TRUE;
	}

	public Ticket(int id, String details, TicketType type, long createdOn,
			long servedOn, long closedOn, boolean isClosed, boolean isWaiting) {
		super();
		this.id = id;
		this.details = details;
		this.type = type;
		this.createdOn = createdOn;
		this.servedOn = servedOn;
		this.closedOn = closedOn;
		this.isClosed = isClosed;
		this.isWaiting = isWaiting;
	}
	
	/**
	 * <p>This will pick the ticket</p>
	 * @return
	 */
	public Ticket pick(){
		if(this.isClosed){
			throw new IllegalArgumentException("Ticket Can't be Picked as Its already closed");
		}
		return new Ticket(this.id, this.details, this.type, this.createdOn
				, System.currentTimeMillis(), 0l, Boolean.FALSE
				, Boolean.FALSE);
	}
	
	/**
	 * <p>This will close the Ticket, changes the from any status to closed</p>
	 * @return
	 */
	public Ticket close(){
		if(this.isClosed){
			throw new IllegalArgumentException("Ticket is Akready closed.");
		}
		return new Ticket(this.id, this.details, this.type, this.createdOn
				, this.servedOn, System.currentTimeMillis(), Boolean.TRUE
				, Boolean.FALSE);
	}

	public int getId() {
		return id;
	}

	public String getDetails() {
		return details;
	}

	public TicketType getType() {
		return type;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public long getServedOn() {
		return servedOn;
	}

	public long getClosedOn() {
		return closedOn;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public boolean isWaiting() {
		return isWaiting;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", details=" + details + ", type=" + type
				+ ", createdOn=" + createdOn + ", servedOn=" + servedOn
				+ ", closedOn=" + closedOn + ", isClosed=" + isClosed
				+ ", isWaiting=" + isWaiting + "]";
	}
	
}
