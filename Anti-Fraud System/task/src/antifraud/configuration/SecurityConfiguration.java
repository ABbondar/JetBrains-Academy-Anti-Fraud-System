package antifraud.configuration;

import antifraud.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final UserServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/shutdown").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/auth/list").hasAnyAuthority("ADMINISTRATOR", "SUPPORT")
                .antMatchers(HttpMethod.PUT, "/api/auth/role/**").hasAuthority("ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT, "/api/auth/access/**").hasAuthority("ADMINISTRATOR")
                .antMatchers(HttpMethod.DELETE, "/api/auth/**").hasAuthority("ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip/**", "/api/antifraud/stolencard/**").hasAuthority("SUPPORT")
                .antMatchers(HttpMethod.POST, "/api/antifraud/suspicious-ip/**", "/api/antifraud/stolencard/**").hasAuthority("SUPPORT")
                .antMatchers(HttpMethod.DELETE, "/api/antifraud/suspicious-ip/**", "/api/antifraud/stolencard/**").hasAuthority("SUPPORT")
                .antMatchers(HttpMethod.GET, "/api/antifraud/history/**").hasAuthority("SUPPORT")
                .antMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasAuthority("MERCHANT")
                .antMatchers(HttpMethod.PUT, "/api/antifraud/transaction/**").hasAuthority("SUPPORT")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    protected PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }
}