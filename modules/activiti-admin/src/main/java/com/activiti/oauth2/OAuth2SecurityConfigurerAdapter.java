package com.activiti.oauth2;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springsecurity.AdapterDeploymentContextFactoryBean;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakCsrfRequestMatcher;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

public abstract class OAuth2SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> {

    @Autowired(required = false)
    private KeycloakConfigResolver keycloakConfigResolver;

    @Bean
    protected AdapterDeploymentContext adapterDeploymentContext() throws Exception {
        AdapterDeploymentContextFactoryBean factoryBean;
        factoryBean = new AdapterDeploymentContextFactoryBean(keycloakConfigResolver);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    protected AuthenticationEntryPoint authenticationEntryPoint() throws Exception {
        return new KeycloakAuthenticationEntryPoint(adapterDeploymentContext());
    }

    protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        return new KeycloakAuthenticationProvider();
    }

    @Bean
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(authenticationManagerBean());
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        return filter;
    }

    @Bean
    protected KeycloakPreAuthActionsFilter keycloakPreAuthActionsFilter() {
        return new KeycloakPreAuthActionsFilter(httpSessionManager());
    }

    protected KeycloakCsrfRequestMatcher keycloakCsrfRequestMatcher() {
        return new KeycloakCsrfRequestMatcher();
    }

    @Bean
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    protected KeycloakLogoutHandler keycloakLogoutHandler() throws Exception {
        return new KeycloakLogoutHandler(adapterDeploymentContext());
    }

    protected abstract SessionAuthenticationStrategy sessionAuthenticationStrategy();

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().requireCsrfProtectionMatcher(keycloakCsrfRequestMatcher())
            .and()
            .sessionManagement()
            .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
            .and()
            .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
            .addFilterBefore(keycloakAuthenticationProcessingFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class)
            .addFilterAfter(keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class)
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
            .and()
            .logout()
            .addLogoutHandler(keycloakLogoutHandler())
            .logoutUrl("/sso/logout").permitAll()
            .logoutSuccessUrl("/");
    }

    @Bean
    protected KeycloakSecurityContextRequestFilter keycloakSecurityContextRequestFilter() {
        return new KeycloakSecurityContextRequestFilter();
    }

    @Bean
    protected KeycloakAuthenticatedActionsFilter keycloakAuthenticatedActionsRequestFilter() {
        return new KeycloakAuthenticatedActionsFilter();
    }
}
