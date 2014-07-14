/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uimirror.websocket.stomp.poc.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.uimirror.websocket.stomp.counter.Counter;
import com.uimirror.websocket.stomp.counter.CounterType;
import com.uimirror.websocket.stomp.counter.QueFactory;
import com.uimirror.websocket.stomp.counter.QueManager;
import com.uimirror.websocket.stomp.counter.Ticket;
import com.uimirror.websocket.stomp.counter.TicketFormBean;
import com.uimirror.websocket.stomp.poc.service.CounterService;
import com.uimirror.websocket.stomp.poc.service.QueService;


@Controller
public class ServiceDeskController {

	private static final Log logger = LogFactory.getLog(ServiceDeskController.class);

	private final QueService queService;

	private final CounterService counterService;

	private final QueManager queManager;

	@Autowired
	public ServiceDeskController(QueService queService, CounterService counterService, QueFactory queFactory) {
		this.queService = queService;
		this.counterService = counterService;
		this.queManager = queFactory.getQueManager();
	}
	
	@RequestMapping(value="/destination", method=RequestMethod.GET)
	public ModelAndView redirectToDestination(Principal principal){
		CounterType counterType = CounterType.HELPDESK;
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for(SimpleGrantedAuthority obj : authorities){
			if(obj.getAuthority().equals("ROLE_USER")){
				counterType = CounterType.HELPDESK;
				
			}else if(obj.getAuthority().equals("ROLE_BANKER")){
				counterType = CounterType.BANKER;
			}
		}
		queManager.addCounter(principal.getName(), counterType);
		if(counterType == CounterType.HELPDESK){
			return new ModelAndView("/reception.html");
		}
		return new ModelAndView("/servicedesk.html");
	}

	@SubscribeMapping("/counters")
	public Set<Counter> getCounters(Principal principal) throws Exception {
		logger.debug("Positions for " + principal.getName());
		return queManager.getCounters();
	}
	
	@SubscribeMapping("/tickets")
	public Set<Ticket> getTickets(Principal principal) throws Exception {
		logger.debug("Positions for " + principal.getName());
		return queManager.getTickets();
	}
	
	@MessageMapping("/createticket")
	public void createTicket(TicketFormBean tBean, Principal principal) throws Exception {
		logger.debug("Creating a Ticket By " + principal.getName());
		counterService.createTicket(tBean.getDetails(), tBean.getType(), principal.getName());
	}

}
