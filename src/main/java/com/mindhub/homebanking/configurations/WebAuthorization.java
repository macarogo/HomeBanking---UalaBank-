package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
 class WebAuthorization extends WebSecurityConfigurerAdapter{

 @Override

 protected void configure(HttpSecurity http) throws Exception {

  http.authorizeRequests()

          .antMatchers("/rest/**","/h2-console/**").hasAuthority("ADMIN")
          .antMatchers("/manager.html/**", "/manager.js", "/img/**").hasAuthority("ADMIN")
          .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
          .antMatchers("/api/clients/current", "/api/loans").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.PATCH, "/api/clients/current/accounts/**").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.POST, "/api/clients/current/cards").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.PATCH, "/api/cards/expired/**").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards/**").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.POST, "/api/loans").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.POST, "/api/cards/newPago").permitAll()
          .antMatchers(HttpMethod.POST, "/api/pdf/**").hasAuthority("CLIENT")
          .antMatchers(HttpMethod.PATCH, "/api/clients/modify").hasAuthority("ADMIN")
          .antMatchers(HttpMethod.DELETE, "/api/clients/deleteClient").hasAuthority("ADMIN")
          .antMatchers("/api/accounts/**").hasAnyAuthority("CLIENT")
          .antMatchers("/api/**").hasAuthority("ADMIN")
          .antMatchers("/web/index.html","/styles/**","/JS/index.js", "/img/**").permitAll()
          .antMatchers("/**").hasAuthority("CLIENT");

  http.formLogin()

          .usernameParameter("email")

          .passwordParameter("password")

          .loginPage("/api/login");


  http.logout().logoutUrl("/api/logout");

  http.csrf().disable();

  //disabling frameOptions so h2-console can be accessed

  http.headers().frameOptions().disable();

  // if user is not authenticated, just send an authentication failure response

  http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

  // if login is successful, just clear the flags asking for authentication

  http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

  // if login fails, just send an authentication failure response

  http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

  // if logout is successful, just send a success response

  http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

 }

 private void clearAuthenticationAttributes(HttpServletRequest request) {

  HttpSession session = request.getSession(false);

  if (session != null) {

   session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

  }
 }
}