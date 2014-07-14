package com.uimirror.websocket.stomp.poc.service;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.uimirror.websocket.stomp.counter.Counter;
import com.uimirror.websocket.stomp.counter.QueFactory;
import com.uimirror.websocket.stomp.counter.QueManager;
import com.uimirror.websocket.stomp.counter.Ticket;

@Service
public class QueService implements ApplicationListener<BrokerAvailabilityEvent>{

	private static Log logger = LogFactory.getLog(QueService.class);

	private final MessageSendingOperations<String> messagingTemplate;
	private final QueManager queManager; 

	private AtomicBoolean brokerAvailable = new AtomicBoolean();

	@Autowired
	public QueService(MessageSendingOperations<String> messagingTemplate, QueFactory queFactory) {
		this.messagingTemplate = messagingTemplate;
		this.queManager = queFactory.getQueManager();
	}

	@Override
	public void onApplicationEvent(BrokerAvailabilityEvent event) {
		this.brokerAvailable.set(event.isBrokerAvailable());
	}
	
	
	@Scheduled(fixedDelay=2000)
	public void sendCounterDetails() {
		for (Counter counter : this.queManager.getCounters()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Sending counter details " + counter);
			}
			if (this.brokerAvailable.get()) {
				this.messagingTemplate.convertAndSend("/topic/bank.counter." + counter.getCounterId(), counter);
			}
		}
	}
	
	@Scheduled(fixedDelay=2000)
	public void sendTicketDetails() {
		for (Ticket ticket : this.queManager.getTickets()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Sending ticket details " + ticket);
			}
			if (this.brokerAvailable.get()) {
				this.messagingTemplate.convertAndSend("/topic/bank.tickets." + ticket.getId(), ticket);
			}
		}
	}

}
