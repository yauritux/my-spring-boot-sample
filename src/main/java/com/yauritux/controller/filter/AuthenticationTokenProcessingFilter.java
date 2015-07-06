package com.yauritux.controller.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import com.yauritux.common.util.AuthTokenUtils;
import com.yauritux.model.entity.User;

/**
 * Filter to verify whether supplied authentication token is valid or not.
 * 
 * @author yauritux@gmail.com
 *
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {
	
	/**
	 * Contains UserDetailsService that is injected from security configuration.
	 */
	private final UserDetailsService userService;
	
	/**
	 * Constructor.
	 * @param userService UserDetailsService
	 */
	public AuthenticationTokenProcessingFilter(UserDetailsService userService) {
		this.userService = userService;
	}
	
	/**
	 * Method that will be doing actual filter process.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param chain FilterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		System.out.println("System.out-AuthenticationTokenProcessingFilter::doFilter::BEGIN");
		
		// cast request into an HttpServletRequest
		HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		
		// Extract authToken and username 
		String authToken = this.extractAuthTokenFromRequest(httpRequest);
		String username = httpRequest.getParameter("username");
		
		System.out.println("System.out-AuthenticationTokenProcessingFilter::doFilter::authToken=" + authToken);
		
		System.out.println("AuthenticationTokenProcessingFilter::doFilter::username=" + username);
		
		try {
			// Halt if either username or authToken is not available (null) 
			if (username == null || authToken == null) {
				System.out.println("AuthenticationTokenProcessingFilter::doFilter::missing either username or authToken");
				return;
			}
			
			System.out.println("System.out-AuthenticationTokenProcessingFilter::doFilter::load user with username '" + username + "'");
			// load user with supplied username
			User user = (User) this.userService.loadUserByUsername(username);
			
			// Challenge supplied token with actual token in userDetails. Halt if it isn't valid
			if (!AuthTokenUtils.validateToken(authToken, user)) {
				System.out.println("System.out-AuthenticationTokenProcessingFilter::doFilter::token no longer valid.");
				return;
			}
			
			System.out.println("System.out-AuthenticationTokenProcessingFilter::doFilter::all valid");
			
			// set authentication if all is valid
			UsernamePasswordAuthenticationToken authentication 
				= new UsernamePasswordAuthenticationToken(user, null, ((UserDetails) user).getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (UsernameNotFoundException e) {
			System.out.println("AuthenticationTokenProcessingFilter::doFilter::UsernameNotFoundException");
		} finally {
			chain.doFilter(request, response);
		}
		
		System.out.println("AuthenticationTokenProcessingFilter::doFilter::END");
	}
	
	/**
	 * Cast request into HttpServletRequest.
	 * 
	 * @param request ServletRequest
	 * @return HttpServletRequest
	 */
	private HttpServletRequest getAsHttpRequest(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP Request.");
		}
		
		return (HttpServletRequest) request;
	}
	
	/**
	 * Extract authToken from supplied request.
	 * 
	 * @param httpRequest HttpServletRequest
	 * @return String authToken extracted from httpRequest
	 */
	private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
		// Get token from Header
		String authToken = httpRequest.getHeader("X-Auth-Token");
		
		// If token can't be found than get it from the request parameter
		if (authToken == null) {
			authToken = httpRequest.getParameter("auth_token");
		}
		
		return authToken;
	}
}