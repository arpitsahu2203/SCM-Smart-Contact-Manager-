package org.arpitsahu.smc.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.arpitsahu.smc.ServiceImpl.SecurityCustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    //user create and login using Java code with in memory services
    

    // @Bean-
    // public UserDetailsService userDetailsService(){
    //     UserDetails user1 = User
    //     .withUsername("Arpit")
    //     .password("Arpit@123")
    //     .roles("ADMIN","USER")
    //     .build();

    //     var inMemoryUserDetailManager=new InMemoryUserDetailsManager(user1);
    //     return new InMemoryUserDetailsManager();
    // }

    @Autowired
    private SecurityCustomUserDetailsService securityCustomUserDetailsService;


    //here we implemented OauthenticationSuccessHandler that we made so that we can pass it later in
    //httpSecurity.oauthlogin
    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

    //CONFIGURATION OF AUTHENTICATOR PROVIDER
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(securityCustomUserDetailsService); // ✅ pass in constructor
        provider.setPasswordEncoder(passwordEncoder()); // ✅ this stays as setter
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




    //HERE WE ARE GOING TO DECIDE WHICH URLS GETS SECURITY AND WHICH DON'T
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        //httpSecurity will help us configure that which page will have the security and also what type of security like login page or oAuth etc.
        //Configuration of Urls
        httpSecurity.authorizeHttpRequests(authorize->{
            authorize.requestMatchers("/SMC/user/**").authenticated();//theses will get protected
            authorize.anyRequest().permitAll();//all other files can be accessed by anybody
        }); 
        


        //here we added the most default login form to the authenticated page
        //agar kuch bhi change kiya jayega to sab yahin par change honge:form login se related
        //here we can use formLogin to make a lambda that acts as a FormLoginConfigurer and we can use that to configure the login page and also
        //the logout page and also the success and failure handlers
        httpSecurity.formLogin(formLogin->{

            //HERE WE ARE GONNA GIVE THE URL OF THE LOGIN PAGE
            formLogin.loginPage("/login")
            //this mean the login processing will be done on login processing
            .loginProcessingUrl("/authenticate")
            .defaultSuccessUrl("/SMC/user/profile")
            .failureUrl("/login?error=true");

            //here we write all the parameter that we are going to use in the login form
            formLogin.usernameParameter("email")
            .passwordParameter("password");
        });



        
        //here we are going to configure logout page
        //CSRF is enabled that is by default enabled then the logout request must be a POST request, and it must contain the CSRF token in the request body
        //therefore we will disable CSRF for the logout URL
        //this will secifically ingore the CSRF token for the logout URL and allow the logout request to be processed without the CSRF token
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.logout(logout->{
            logout.logoutUrl("/do-logout")
            .logoutSuccessUrl("/login?logout=true");
        });


        //Here we are going to perform OAuth google login
        httpSecurity.oauth2Login(oauth->{
                oauth.loginPage("/login");
                oauth.successHandler(oAuthAuthenticationSuccessHandler);
        });



        return httpSecurity.build();
        //httpSecurity.Build() will return the default security filter chain with our custom configuration
        //Default httpSecurity configuration is the normal Security Filter chain
    }

    
}
