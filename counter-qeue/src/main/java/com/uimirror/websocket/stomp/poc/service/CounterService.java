package com.uimirror.websocket.stomp.poc.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.uimirror.websocket.stomp.counter.CounterType;
import com.uimirror.websocket.stomp.counter.QueFactory;
import com.uimirror.websocket.stomp.counter.QueManager;
import com.uimirror.websocket.stomp.counter.TicketType;

@Service
public class CounterService {

	private static final Log logger = LogFactory.getLog(CounterService.class);

	private final SimpMessageSendingOperations messagingTemplate;

	private final QueManager queManager;

	@Autowired
	public CounterService(SimpMessageSendingOperations messagingTemplate, QueFactory queFactory) {
		this.messagingTemplate = messagingTemplate;
		this.queManager = queFactory.getQueManager();
	}

	/**
	 * In real application a ticket is probably executed in an external system, i.e. asynchronously.
	 */
	public void createTicket(String ticketDetails, TicketType ticketType, String userId) {

		try{
			
			queManager.createTicket(ticketType, ticketDetails);
		}catch(IllegalArgumentException e){
			logger.error("Ticket can't be opened now {}",e);
			String payload = "Tikcet creation rejected "+e.getMessage();
			this.messagingTemplate.convertAndSendToUser(userId, "/queue/ticket/errors", payload);
		}
	}
	

	/**
	 * In real application a ticket is probably executed in an external system, i.e. asynchronously.
	 */
	public void closeTicket(int ticketId, int counterId, String userId) {

		try{
			
			queManager.closeTicket(ticketId, counterId);
		}catch(IllegalArgumentException e){
			logger.error("Ticket can't be closed now {}",e);
			String payload = "Tikcet Close rejected "+e.getMessage();
			this.messagingTemplate.convertAndSendToUser(userId, "/queue/ticket/errors", payload);
		}
	}
	
	/**
	 * In real application a counter is probably executed in an external system, i.e. asynchronously.
	 */
	public void closeCounter(int counterId, String userId) {

		try{
			
			queManager.closeCounter(counterId);
		}catch(IllegalArgumentException e){
			logger.error("Counter can't be closed now {}",e);
			String payload = "Counter Close rejected "+e.getMessage();
			this.messagingTemplate.convertAndSendToUser(userId, "/queue/counter/errors", payload);
		}
	}
	
	/**
	 * In real application a counter is probably executed in an external system, i.e. asynchronously.
	 */
	public void takeABreakAtCounter(int counterId, String userId) {

		try{
			
			queManager.takeABreak(counterId);
		}catch(IllegalArgumentException e){
			logger.error("Counter can't take break now {}",e);
			String payload = "Counter To Take Break is rejected "+e.getMessage();
			this.messagingTemplate.convertAndSendToUser(userId, "/queue/counter/errors", payload);
		}
	}
	
	/**
	 * In real application a counter is probably executed in an external system, i.e. asynchronously.
	 */
	public void backFromBreakAtCounter(int counterId, String userId) {

		try{
			
			queManager.backFromBreak(counterId);
		}catch(IllegalArgumentException e){
			logger.error("Counter can't be back from break now {}",e);
			String payload = "Counter To back from break is rejected "+e.getMessage();
			this.messagingTemplate.convertAndSendToUser(userId, "/queue/counter/errors", payload);
		}
	}
	
	/**
	 * In real application a counter is probably executed in an external system, i.e. asynchronously.
	 */
	public void OpenCounter(CounterType type, String userId) {

		try{
			
			queManager.addCounter(userId, type);
		}catch(IllegalArgumentException e){
			logger.error("Counter can't be opened now {}",e);
			String payload = "Counter To open is rejected "+e.getMessage();
			this.messagingTemplate.convertAndSendToUser(userId, "/queue/counter/errors", payload);
		}
	}
}
