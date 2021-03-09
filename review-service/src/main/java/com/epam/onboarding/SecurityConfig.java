package com.epam.onboarding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api-header-name}")
    private String headerName;

    @Value("${api-header-value}")
    private String headerValue;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ApiFilter filter = new ApiFilter();
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            if (!headerValue.equals(principal)) {
                throw new BadCredentialsException("API key not found or un-expected value");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity.
                antMatcher("**").
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }

    private class ApiFilter extends AbstractPreAuthenticatedProcessingFilter {
        @Override
        protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
            return request.getHeader(headerName);
        }

        @Override
        protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
            return "N/A";
        }
    }
}
