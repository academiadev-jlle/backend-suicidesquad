package br.com.academiadev.suicidesquad.config;

import br.com.academiadev.suicidesquad.security.JwtTokenProvider;
import br.com.academiadev.suicidesquad.security.RestAuthenticationEntryPoint;
import br.com.academiadev.suicidesquad.service.CustomUserDetailsService;
import br.com.academiadev.suicidesquad.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .httpBasic()
                .disable()
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers(
                    "/auth/facebook/authorization",
                    "/auth/facebook/callback",
                    "/auth/email"
                ).permitAll()
                .antMatchers(
                    "/swagger-ui.html",
                    "/v2/api-docs",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                .antMatchers(
                    HttpMethod.GET,
        "/pets",
                    "/pets/{id}"
                ).permitAll()
                .antMatchers(
                    HttpMethod.POST,
        "/usuarios"
                ).permitAll()
                .antMatchers(
                    HttpMethod.GET,
        "/usuarios",
                    "/usuarios/{id}"
                ).permitAll()
                .antMatchers(
                        HttpMethod.PUT,
                        "/senhas/requisitar_redefinicao",
                        "/senhas/redefinir"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
            .apply(new JwtConfigurer(jwtTokenProvider));
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(PasswordService.encoder());
    }
}
