package com.example.tinderappmaven.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    private CustomUserDetailsService userDetailsService;

    @Autowired
    public SpringSecurityConfiguration(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().ignoringRequestMatchers("/register/**")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/login/**", "/register/**", "/images/**","/css/**", "/js/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST,"/register/**")
                .hasAnyAuthority("ROLE_ADMIN").anyRequest().authenticated()
                .and()
                .formLogin(form -> form
                        .loginPage("/login").usernameParameter("email")
                        .defaultSuccessUrl("/main-page")
                        .loginProcessingUrl("/login").usernameParameter("email")
                        .failureUrl("/login?error=true")
                        .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                );
                http.headers().frameOptions().disable();
        return http.build();
    }
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }



//        private final AppUserServiceImpl appUserService;
//
//    public SpringSecurityConfiguration(@Lazy AppUserServiceImpl appUserService) {
//        this.appUserService = appUserService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().ignoringRequestMatchers(PathRequest.toH2Console())
//                .and()
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers(PathRequest.toH2Console()).permitAll()
//                                .requestMatchers("/registration").permitAll()
//                                .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
//                                .requestMatchers(HttpMethod.GET,"/login/**")
//                                .hasAnyAuthority(RoleName.ROLE_USER.value(),RoleName.ROLE_ADMIN.value()).anyRequest().authenticated()
//                ).csrf().disable().userDetailsService(appUserService)
//                .headers(headers ->
//                        headers
//                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
//                                .contentSecurityPolicy("frame-ancestors 'self'")
//                                .and().contentTypeOptions(HeadersConfigurer.ContentTypeOptionsConfig::disable)
//                                .cacheControl().disable()
//                )
//                .formLogin(formLogin ->
//                        {
//                            try {
//                                formLogin
////                                      .loginPage("/login").usernameParameter("email")
//                                        .defaultSuccessUrl("/main-page",true)
//                                        .permitAll()
//                                        .and().logout().permitAll();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                );
//        return http.build();
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
