package com.prudhvi.FoodieExpress.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		boolean isRestaurant = authentication.getAuthorities().stream()
				.anyMatch(grantAuthority -> grantAuthority.getAuthority().equals("ROLE_RESTAURANT"));
		
		boolean isCustomer = authentication.getAuthorities().stream()
				.anyMatch(grantAuthority -> grantAuthority.getAuthority().equals("ROLE_CUSTOMER"));
		
		
		if (isRestaurant) {
			response.sendRedirect("/restaurants/dashboard");
		}
		if (isCustomer) {
			response.sendRedirect("/customers/dashboard");
		}
	}

}
