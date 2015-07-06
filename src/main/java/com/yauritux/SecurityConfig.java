package com.yauritux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.yauritux.controller.filter.AuthenticationTokenProcessingFilter;
import com.yauritux.controller.filter.UnauthorizedEntryPoint;
import com.yauritux.repository.dao.UserRepository;
import com.yauritux.service.query.impl.UserQueryServiceImpl;

/**
 * @author yauritux@gmail.com
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;
	
	@Bean
	public UnauthorizedEntryPoint unauthorizedEntryPoint() {
		return new UnauthorizedEntryPoint();
	}
		
	@Bean
	public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter() {
		return new AuthenticationTokenProcessingFilter(new UserQueryServiceImpl(userRepository));
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		http
			.authorizeRequests()
			.antMatchers("/contacts/**").hasRole("USER")
			.anyRequest().anonymous().and()
			.httpBasic().and()
			.csrf().disable();
		*/
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/contacts/**").hasRole("USER")
			.anyRequest().anonymous()
			.and()
			.addFilterBefore(authenticationTokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class) //UsernamePasswordAuthenticationFilter = FORM_LOGIN_FILTER
			.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(authenticationConfiguration.userDetailsService());
		auth.authenticationProvider(authenticationConfiguration.authenticationProvider());
	}
	
	@Bean(name = "myAuthenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
