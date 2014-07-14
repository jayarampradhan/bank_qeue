package com.uimirror.websocket.stomp.poc.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;


/**
 * ServletContext initializer for Spring Security specific configuration such as
 * the chain of Spring Security filters.
 * <p>
 * The Spring Security configuration is customized with
 * {@link com.uimirror.websocket.stomp.poc.config.WebSecurityConfig}.
 *
 */
public class WebSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
