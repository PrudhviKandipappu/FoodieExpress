package com.prudhvi.FoodieExpress.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.prudhvi.FoodieExpress.service.CustomerRestaurantDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	CustomerRestaurantDetailsService customerAndRestaurantDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/register/**", "/", "/signup/**", "/images/**", "/styles/**", "/scripts/**", "/index", "/error/**", "/login").permitAll()
					.requestMatchers("/customers/**").hasRole("CUSTOMER")
					.requestMatchers("/restaurants/**").hasRole("RESTAURANT")
					.anyRequest().authenticated()
					)
			.formLogin(formLogin -> formLogin
					.loginPage("/login")
					.successHandler(new CustomAuthenticationSuccessHandler())
					.permitAll())
			.logout(logout -> logout
					.logoutSuccessUrl("/")
					.deleteCookies("JSESSIONID"));
		
		return httpSecurity.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return customerAndRestaurantDetailsService;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customerAndRestaurantDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
