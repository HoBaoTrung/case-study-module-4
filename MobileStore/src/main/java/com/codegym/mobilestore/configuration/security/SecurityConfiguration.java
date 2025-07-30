package com.codegym.mobilestore.configuration.security;

import com.codegym.mobilestore.service.user.CustomOAuth2UserService;
import com.codegym.mobilestore.service.user.CustomOidcUserService;
import com.codegym.mobilestore.service.user.UserService;
import com.codegym.mobilestore.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@PropertySource("classpath:secret.properties")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public CustomSuccessHandle customSuccessHandle() {
        return new CustomSuccessHandle();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder(10));
    }

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private CustomOidcUserService customOidcUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic();
        http.formLogin(form ->
                form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customSuccessHandle())
        );

        http.authorizeHttpRequests(author -> author
                // ADMIN: ưu tiên trước
                .requestMatchers(HttpMethod.GET, "/products/add", "/products/*/edit").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/products/add", "/products/*/edit").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/products").hasRole("ADMIN")
                // Các route yêu cầu login
                .requestMatchers("/checkout/**").authenticated()

                .anyRequest().permitAll()
        ).exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler()));
//        http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(csrf -> csrf
                // Disable crsf cho vài đường dẫn /api/**
                .ignoringRequestMatchers("/products/add", "/products/*/edit", "/api/users/**")
        ).oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/welcome")
                .failureHandler(customOAuth2FailureHandler)
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                        .oidcUserService(customOidcUserService)
                )
        );

        //http.cors();
        http
                // ... other configurations
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Redirect after successful logout
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL))) // Clears site data including cookies
                );


    }

    @Bean
    public ClientRegistration googleClientRegistration(
            @Value("${google.client-id}") String clientId,
            @Value("${google.client-secret}") String clientSecret,
            @Value("${google.redirect-uri}") String redirectUri
    ) {
        return buildClientRegistration(
                "google",
                clientId,
                clientSecret,
                redirectUri,
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                "https://accounts.google.com/o/oauth2/v2/auth",
                "https://oauth2.googleapis.com/token",
                "https://www.googleapis.com/oauth2/v3/certs",
                "https://openidconnect.googleapis.com/v1/userinfo",
                "sub",
                "Google",
                "openid", "profile", "email"
        );
    }

    @Bean
    public ClientRegistration facebookClientRegistration(
            @Value("${facebook.client-id}") String clientId,
            @Value("${facebook.client-secret}") String clientSecret,
            @Value("${facebook.redirect-uri}") String redirectUri
    ) {
        return buildClientRegistration(
                "facebook",
                clientId,
                clientSecret,
                redirectUri,
                ClientAuthenticationMethod.CLIENT_SECRET_POST,
                "https://www.facebook.com/v12.0/dialog/oauth",
                "https://graph.facebook.com/v12.0/oauth/access_token",
                null,
                "https://graph.facebook.com/me?fields=id,name,email",
                "id",
                "Facebook",
                "public_profile", "email"
        );
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            ClientRegistration googleClientRegistration,
            ClientRegistration facebookClientRegistration
    ) {
        return new InMemoryClientRegistrationRepository(googleClientRegistration, facebookClientRegistration);
    }

    private ClientRegistration buildClientRegistration(
            String registrationId,
            String clientId,
            String clientSecret,
            String redirectUri,
            ClientAuthenticationMethod authMethod,
            String authorizationUri,
            String tokenUri,
            String jwkSetUri,
            String userInfoUri,
            String userNameAttribute,
            String clientName,
            String... scopes
    ) {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(authMethod)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(redirectUri)
                .scope(scopes)
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .userInfoUri(userInfoUri)
                .userNameAttributeName(userNameAttribute)
                .clientName(clientName);

        if (jwkSetUri != null) {
            builder.jwkSetUri(jwkSetUri);
        }

        return builder.build();
    }
}
