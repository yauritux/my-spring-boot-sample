package com.yauritux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.yauritux.model.entity.Role;
import com.yauritux.repository.dao.UserRepository;

/**
 * @author yauritux@gmail.com
 */
@Configuration
@ComponentScan("com.yauritux")
public class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
	
	@Autowired
	private UserRepository userRepository;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}		

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setUserDetailsService(userDetailsService());
		return authProvider;
	}
	
	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username)
					throws UsernameNotFoundException {
				com.yauritux.model.entity.User user = userRepository.findByUsername(username);
				if (user != null) {
					String[] roles = new String[(user.getRoles() != null ? user.getRoles().size() : 0)];
					int i = 0;
					for (Role role : user.getRoles()) {
						roles[i] = role.getRoleName();
						i++;
					}
					return new User(user.getUsername(), user.getPassword(), true, true, true, true,
							AuthorityUtils.createAuthorityList(roles));
							//AuthorityUtils.createAuthorityList("ROLE_USER"));
				} else {
					throw new UsernameNotFoundException("could not find the user '" + username + "'");
				}
			}
		};
	}	
}
