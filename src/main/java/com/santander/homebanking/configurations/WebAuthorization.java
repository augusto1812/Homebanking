package com.santander.homebanking.configurations;

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
class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//              .antMatchers("/web/index.html","/web/css/**","/web/js/index.js","/web/img/**").permitAll()
//                .antMatchers("/web/index.html","/web/css/**","/web/js/index.js","/web/img/**","/h2-console/**").permitAll()
//                .antMatchers("/**").hasAuthority("CLIENT");

//        http.authorizeRequests()
//                .antMatchers("/admin/").hasAuthority("ADMIN")
//                .antMatchers(HttpMethod.POST,"/api/clients/**").permitAll()
//                .antMatchers("/api/login","/web/index.html","/web/js/index.js","/web/css/","/web/img/").permitAll()
//                .antMatchers("/web/account.html", "/web/accounts.html", "/web/cards.html", "/web/loan-application.html", "/web/js/", "/api/","/rest/").hasAuthority("CLIENT");

        http.authorizeRequests()
                .antMatchers("/web/index.html","/web/css/**","/web/img/**","/web/js/**","/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                .antMatchers("/**").hasAuthority("CLIENT");


        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
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




