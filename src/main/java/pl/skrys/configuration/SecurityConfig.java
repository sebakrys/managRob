package pl.skrys.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "myUserAppDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN","ROBPROG", "MANAGER");
        auth.inMemoryAuthentication().withUser("skrys").password("{noop}skrys").roles("MANAGER");
        auth.inMemoryAuthentication().withUser("manager").password("{noop}manager").roles("MANAGER");
        auth.inMemoryAuthentication().withUser("robprog").password("{noop}robprog").roles("ROBPROG");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

    }

    protected void configure(HttpSecurity http) throws Exception{

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        http.authorizeRequests()
                .antMatchers("/appUsers*").access("hasRole('ADMIN')")
                .antMatchers("/userAppRole*").access("hasRole('ROLE_ADMIN')")
                //.and().formLogin().permitAll();//default strona logowania
                .and().formLogin().loginPage("/login").permitAll()// custom login page
                .usernameParameter("login").passwordParameter("password")
                .failureForwardUrl("/login.html?error")
                .defaultSuccessUrl("/in_home.html")
                .and().logout().logoutSuccessUrl("/login.html?logout")
                .and().exceptionHandling().accessDeniedPage("/accessDenied");

    }

}
