/**
 * 
 */
package com.uimirror.websocket.stomp.counter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author pradhj
 *
 */
public class QueManager implements Serializable{

	private static final long serialVersionUID = 7839814888195287179L;
	private Set<Ticket> tickets;
	private Set<Counter> counters;
	private static QueManager queManager = new QueManager();  
	
	private QueManager(){
		this.tickets = new HashSet<Ticket>(10);
		this.counters = new HashSet<Counter>(10);
	}
	
	public static QueManager getInstance(){
		if(queManager == null){
			queManager = new QueManager();
		}
		return queManager;
	}
	
	/**
	 * <p>as soon as a Person logs in he will be addedd to the counter and shared counter set will 
	 * be updated</p>
	 * @param userId
	 * @param type
	 * @return
	 */
	public Set<Counter> addCounter(String userId, CounterType type){
		Counter c = new Counter(this.counters.size()+1, userId, type);
		this.counters.add(c);
		return this.counters;
	}
	
	/**
	 * <p>This will update the break status of the Counter</p>
	 * @param counterId
	 * @return
	 */
	public Set<Counter> takeABreak(int counterId){
		if(counters.isEmpty() || counterId < 0){
			throw new IllegalArgumentException("There is no Counter To take a break");
		}
		Iterator<Counter> counterIt = counters.iterator();
		while(counterIt.hasNext()){
			Counter c = counterIt.next();
			if(c.getCounterId() == counterId){
				counterIt.remove();
				counters.add(c.updatebreakStatus(Boolean.TRUE));
				break;
			}
		}
		return counters;
	}
	
	/**
	 * <p>This will be when a counter is back from the break he needs to update the back status</p>
	 * @param counterId
	 * @return
	 */
	public Set<Counter> backFromBreak(int counterId){
		if(counters.isEmpty() || counterId < 0){
			throw new IllegalArgumentException("There is no Counter To be back from break");
		}
		Iterator<Counter> counterIt = counters.iterator();
		while(counterIt.hasNext()){
			Counter c = counterIt.next();
			if(c.getCounterId() == counterId){
				counterIt.remove();
				counters.add(c.updatebreakStatus(Boolean.FALSE));
				break;
			}
		}
		return counters;
	}
	
	/**
	 * <p>This will update the counter as closed by the counter ID</p>
	 * @param counterId
	 * @return
	 */
	public Set<Counter> closeCounter(int counterId){
		if(counters.isEmpty() || counterId < 0){
			throw new IllegalArgumentException("There is no Counter To be closed");
		}
		Iterator<Counter> counterIt = counters.iterator();
		while(counterIt.hasNext()){
			Counter c = counterIt.next();
			if(c.getCounterId() == counterId){
				counterIt.remove();
				counters.add(c.close());
				break;
			}
		}
		return counters;
	}
	
	/**
	 * <p>This will create a ticket from the helpdesk and updates the shared ticket set.
	 * @param type
	 * @param details
	 * @return
	 */
	public Ticket createTicket(TicketType type, String details){
		Ticket ticket = new Ticket(this.tickets.size()+1, details, type);
		this.tickets.add(ticket);
		return ticket;
	}
	
	/**
	 * <p>This will handel the counter picking the ticket </p>
	 * @param ticketId
	 * @param counterId
	 * @return
	 */
	public Set<Ticket> serveTicket(int ticketId, int counterId){
		if(ticketId < 0 || counterId < 0 || tickets.isEmpty() || counters.isEmpty()){
			throw new IllegalArgumentException("Ticket can't be served via a counter might be parameters are incorrect or there is no enough ticket or no enoguh counter.");
		}
		
		Iterator<Ticket> ticketIterator = tickets.iterator();
		
		while(ticketIterator.hasNext()){
			Ticket ticket = ticketIterator.next();
			if(ticket.getId() == ticketId){
				ticketIterator.remove();
				tickets.add(ticket.pick());
				updateCounterServeStatus(counterId, ticket);
				break;
			}
		}
		return tickets;
	}
	
	/**
	 * <p>Updates the counter status of the counter who is picking the ticket</p>
	 * @param counterId
	 * @param ticket
	 */
	private void updateCounterServeStatus(int counterId, Ticket ticket){
		Iterator<Counter> counterIterator = counters.iterator();
		while(counterIterator.hasNext()){
			Counter counter = counterIterator.next();
			if(counter.getCounterId() == counterId){
				counterIterator.remove();
				counters.add(counter.pickTicket(ticket));
				break;
			}
		}
	}
	
	
	/**
	 * <p>This will close the ticket</p>
	 * @param ticketId
	 * @param counterId
	 * @return
	 */
	public Set<Ticket> closeTicket(int ticketId, int counterId){
		if(ticketId < 0 || counterId < 0 || counters.isEmpty()){
			throw new IllegalArgumentException("Ticket can't be closed might be parameters are incorrect or there not enoguh counter.");
		}
		
		Iterator<Ticket> ticketIterator = tickets.iterator();
		
		while(ticketIterator.hasNext()){
			Ticket ticket = ticketIterator.next();
			if(ticket.getId() == ticketId){
				ticketIterator.remove();
				closeTicketFromCounter(counterId);
				break;
			}
		}
		return tickets;
	}
	
	/**
	 * <p>This will close the ticket and make the counter free </p>
	 * @param counterId
	 */
	private void closeTicketFromCounter(int counterId){
		Iterator<Counter> counterIterator = counters.iterator();
		while(counterIterator.hasNext()){
			Counter counter = counterIterator.next();
			if(counter.getCounterId() == counterId){
				counterIterator.remove();
				counters.add(counter.closeTicket());
				break;
			}
		}
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public Set<Counter> getCounters() {
		return counters;
	}
	
}
