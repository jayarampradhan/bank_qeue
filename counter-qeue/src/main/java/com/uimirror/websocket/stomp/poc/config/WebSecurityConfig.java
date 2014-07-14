package com.uimirror.websocket.stomp.poc.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 * Customizes Spring Security configuration.
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()  // Refactor login form

			// See https://jira.springsource.org/browse/SPR-11496
			.headers().addHeaderWriter(
				new XFrameOptionsHeaderWriter(
						XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)).and()

			.formLogin()
				.defaultSuccessUrl("/destination")
				.loginPage("/login.html")
				.failureUrl("/login.html?error")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login.html?logout")
				.logoutUrl("/logout.html")
				.permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/assets/**").permitAll()
				.anyRequest().authenticated()
				.and();
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("recaption").password("123").roles("USER").and()
				.withUser("staff1").password("123").roles("BANKER").and()
				.withUser("staff2").password("123").roles("BANKER").and()
				.withUser("staff3").password("123").roles("BANKER").and()
				.withUser("staff4").password("123").roles("BANKER").and()
				.withUser("staff5").password("123").roles("BANKER").and();
	}
}