package com.uimirror.websocket.stomp.counter;

import org.springframework.stereotype.Service;

@Service
public class QueFactory {

	private final QueManager queManager;
	
	public QueFactory(){
		this.queManager = QueManager.getInstance();
	}
	
	public QueManager getQueManager(){
		return this.queManager;
	}
}
