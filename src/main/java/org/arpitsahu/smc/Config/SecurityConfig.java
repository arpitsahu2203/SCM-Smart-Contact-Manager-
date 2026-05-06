package org.arpitsahu.smc.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //user create and login using java code with in memory services
    

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

    //CONFIGURATIION OF AUTHENTICATOR PROVIDER
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(securityCustomUserDetailsService); // ✅ pass in constructor
        provider.setPasswordEncoder(passwordEncoder()); // ✅ this stays as setter
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //HERE WE ARE GOING TO DECIDE WHICH URLS GETS SECURITY AND WHICH DONT
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //httpSedcurity will help us configure that which page will have the security and also what type of security like login page or oAuth etc

        //Configuration of Urls
        httpSecurity.authorizeHttpRequests(authorize->{
            authorize.requestMatchers("/SMC/user/**").authenticated();//thses will get protected 
            authorize.anyRequest().permitAll();//all other files can be accessed by any body
        }); 
        

        //here we added the most default login form to the authenticated page
        //agar kuch bhi change kiya jayega to sab yahin par change honge:form login se related
        //here we can use formLogin to make a lembda that acts as a FormLoginConfigurer and we can use that to configure the login page and also
        //the logout page and also the success and failure handlers
        httpSecurity.formLogin(formLogin->{

            //HERE WE ARE GONNA GIVE THE URL OF THE LOGIN PAGE
            formLogin.loginPage("/login")
            //this mean the login processing will be done on login processing
            .loginProcessingUrl("/authenticate")
            .defaultSuccessUrl("/SMC/user/dashboard")
            .failureUrl("/login?error=true");

            //here we wirte all the pasrameter that we are going to use in the login form
            formLogin.usernameParameter("email")
            .passwordParameter("password");
        });
        
        //here we are going to configure logout page
        //if CSRF is enabled that is by default enabled then the logout request must be a POST request and it must contain the CSRF token in the request body
        //therefore we will disable CSRF for the logout URL
        //this will secifically ingore the CSRF token for the logout URL and allow the logout request to be processed without the CSRF token
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.logout(logout->{
            logout.logoutUrl("/do-logout")
            .logoutSuccessUrl("/login?logout=true");
        });


        return httpSecurity.build();
        //httpSecurity.Build() will return the default security filter chain with our custom configuration
        //Defaul httpSecurity configuration is the normal Security Filter chain 
    }

    
}
