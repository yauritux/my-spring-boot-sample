package com.yauritux.controller.rest.resource;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yauritux.SecurityConfig;
import com.yauritux.common.util.AuthTokenUtils;
import com.yauritux.model.constant.Status;
import com.yauritux.model.dto.StatusResponse;
import com.yauritux.model.dto.UserSessionRequest;
import com.yauritux.model.entity.User;
import com.yauritux.repository.dao.UserRepository;

/**
 * @author yauritux@gmail.com
 */
@RestController
@RequestMapping("/login")
public class LoginController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private SecurityConfig securityConfig;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public StatusResponse login(@RequestBody UserSessionRequest request) throws Exception {
		System.out.println("LoginController::login::BEGIN");
		LOG.debug("LoginController::login::BEGIN");
		//String password = new BCryptPasswordEncoder().encode(request.getPassword());
		String password = request.getPassword();
		System.out.println("password = " + password);
		UsernamePasswordAuthenticationToken authenticationToken
			= new UsernamePasswordAuthenticationToken(request.getUsername(), password);
		
		System.out.println("LoginController::login::authenticationToken=" + authenticationToken);
		LOG.debug("LoginController::login::authenticationToken=" + authenticationToken);
		
		Authentication authentication = securityConfig.authenticationManagerBean().authenticate(authenticationToken);
		
		System.out.println("LoginController::login::authentication=" + authentication);
		LOG.debug("LoginController::login::authentication=" + authentication);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// construct and saving token
		System.out.println("LoginController::login::casting authentication principal to User");
		LOG.debug("LoginController::login::casting authentication principal to User");
		Object principal = authentication.getPrincipal();
		String username = null;
		if (principal instanceof User) { 
			username = ((User) principal).getUsername();
		} else if (principal instanceof org.springframework.security.core.userdetails.User) {
			username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
		} else {
			return new StatusResponse(Status.FAIL, null);
		}
		System.out.println("LoginController::login::user=" + username);
		LOG.debug("LoginController::login::user=" + username);
		User user = userRepository.findByUsername(username);
		user.setAuthToken(AuthTokenUtils.createToken(user));
		user.setAuthTokenValidThru(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 3)));
		
		System.out.println("LoginController::login::update user token = " + user.getAuthToken());
		LOG.debug("LoginController::login::update user token = " + user.getAuthToken());
		
		userRepository.save(user);
		
		return new StatusResponse(Status.SUCCESS, user.getAuthToken());
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public StatusResponse badCredentialsExceptionHandler(BadCredentialsException e) {
		LOG.debug("Authentication failed", e);
		return new StatusResponse(Status.FAIL, "Authentication failed: " + e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public StatusResponse exceptionHandler(Exception e) {
		LOG.debug("Authentication error", e);
		return new StatusResponse(Status.FAIL, "Authentication error: " + e.getMessage());
	}
}
