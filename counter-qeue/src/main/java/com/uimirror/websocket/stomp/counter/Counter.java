package com.uimirror.websocket.stomp.counter;

import java.io.Serializable;

/**
 * <p>A Counter Can be a help desk, banker, cashier</p>
 * <p>As soon as a user logs in to the system, he will be registered with any of the types of above</p>
 * <p>A person who is representing the counter can go for break.</p>
 * @author Jayaram
 */
public class Counter implements Serializable{

	private static final long serialVersionUID = -5780065767640440354L;

	private final int counterId;
	private final String counterOwnerUserId;
	private final CounterType counterType;
	private final long counterOpenTime;
	private final long counterCloseTime;
	private final boolean isOnBreak;
	private final boolean isClosed;
	private final boolean isServing;
	private final Ticket ticket;
	
	/**
	 * <p>This will populate initial counter, when a user loggining in
	 * @param counterId
	 * @param userId
	 * @param counterType
	 */
	public Counter(int counterId, String userId, CounterType counterType){
		this.counterId = counterId;
		this.counterOwnerUserId = userId;
		this.counterOpenTime = System.currentTimeMillis();
		this.isOnBreak = Boolean.FALSE;
		this.counterType = counterType;
		this.isClosed = Boolean.FALSE;
		this.counterCloseTime = 0l;
		this.isServing = Boolean.FALSE;
		this.ticket = null;
	}

	/**
	 * @param counterId
	 * @param counterOwnerUserId
	 * @param counterType
	 * @param counterOpenTime
	 * @param isOnBreak
	 */
	public Counter(int counterId, String counterOwnerUserId,
			CounterType counterType, long counterOpenTime, boolean isOnBreak, 
			boolean isClosed, long closedOn, boolean isServing, Ticket ticket) {
		super();
		this.counterId = counterId;
		this.counterOwnerUserId = counterOwnerUserId;
		this.counterType = counterType;
		this.counterOpenTime = counterOpenTime;
		this.isOnBreak = isOnBreak;
		this.isClosed = isClosed;
		this.counterCloseTime = closedOn;
		this.isServing = isServing;
		this.ticket = ticket;
	}
	
	/**
	 * <p>This will update the Counter with the counter break status.</p>
	 * @param status
	 * @return
	 */
	public Counter updatebreakStatus(boolean status){
		if(this.isServing){
			throw new IllegalArgumentException("Counter Can't take break as he is curentlt serving");
		}
		return new Counter(this.counterId, this.counterOwnerUserId, this.counterType, this.counterOpenTime, status, this.isClosed, this.counterCloseTime, Boolean.FALSE, null);
	}
	
	/**
	 * <p>This will mark the counter as closed</p>
	 * @return
	 */
	public Counter close(){
		if(this.isClosed){
			throw new IllegalArgumentException("Counter Is Already Closed");
		}else if(this.isServing){
			throw new IllegalArgumentException("Counter can't be closed as Its currently Serving.");
		}
		return new Counter(this.counterId, this.counterOwnerUserId, this.counterType, 
				this.counterOpenTime, this.isOnBreak, Boolean.TRUE, System.currentTimeMillis(), Boolean.FALSE, null);
	}
	
	/**
	 * <p>This will pick the ticket for the serving purpouse.</p>
	 * @param ticket
	 * @return
	 */
	public Counter pickTicket(Ticket ticket){
		if(this.isClosed || this.isOnBreak){
			throw new IllegalArgumentException("Counter Can't pick the ticket as its either closed or on break");
		}
		return new Counter(this.counterId, this.counterOwnerUserId, this.counterType, this.counterOpenTime, this.isOnBreak, this.isClosed, this.counterCloseTime, Boolean.TRUE, ticket);
	}
	
	/**
	 * <p>This will close the ticket</p>
	 * @return
	 */
	public Counter closeTicket(){
		if(!this.isServing){
			throw new IllegalArgumentException("Ticket can't be closed as Its not seving");
		}
		return new Counter(this.counterId, this.counterOwnerUserId, this.counterType, this.counterOpenTime, this.isOnBreak, this.isClosed, this.counterCloseTime, Boolean.FALSE, null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + counterId;
		result = prime * result
				+ (int) (counterOpenTime ^ (counterOpenTime >>> 32));
		result = prime
				* result
				+ ((counterOwnerUserId == null) ? 0 : counterOwnerUserId
						.hashCode());
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
		Counter other = (Counter) obj;
		if (counterId != other.counterId)
			return false;
		if (counterOpenTime != other.counterOpenTime)
			return false;
		if (counterOwnerUserId == null) {
			if (other.counterOwnerUserId != null)
				return false;
		} else if (!counterOwnerUserId.equals(other.counterOwnerUserId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Counter [counterId=" + counterId + ", counterOwnerUserId="
				+ counterOwnerUserId + ", counterType=" + counterType
				+ ", counterOpenTime=" + counterOpenTime
				+ ", counterCloseTime=" + counterCloseTime + ", isOnBreak="
				+ isOnBreak + ", isClosed=" + isClosed + "]";
	}

	public int getCounterId() {
		return counterId;
	}

	public String getCounterOwnerUserId() {
		return counterOwnerUserId;
	}

	public CounterType getCounterType() {
		return counterType;
	}

	public long getCounterOpenTime() {
		return counterOpenTime;
	}

	public boolean isOnBreak() {
		return isOnBreak;
	}

	public long getCounterCloseTime() {
		return counterCloseTime;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public boolean isServing() {
		return isServing;
	}

	public Ticket getTicket() {
		return ticket;
	}
	
}
