package com.uimirror.websocket.stomp.poc.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

@Configuration
public class JmxConfig {

	@Autowired
	private WebSocketHandler subProtocolWebSocketHandler;

	@Autowired
	private ThreadPoolTaskExecutor clientInboundChannelExecutor;

	@Autowired
	private ThreadPoolTaskExecutor clientOutboundChannelExecutor;

	@Autowired
	private ThreadPoolTaskScheduler messageBrokerSockJsTaskScheduler;

	@Bean
	public MBeanExporter exporter() {
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("counter-qeue:name=webSocketStats", monitor());
		MBeanExporter exporter = new MBeanExporter();
		exporter.setBeans(beans);
		return exporter;
	}

	@Bean
	public WebSocketStats monitor() {
		return new WebSocketStats((SubProtocolWebSocketHandler) this.subProtocolWebSocketHandler,
				this.clientInboundChannelExecutor, this.clientOutboundChannelExecutor,
				this.messageBrokerSockJsTaskScheduler);
	}

	@Scheduled(fixedDelay=10000)
	public void logMonitorStats() {
		monitor().logStats();
	}

}
