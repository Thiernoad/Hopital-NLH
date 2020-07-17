package org.sid.springmvc.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	//definir les utilisateur comment chercher les users
	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//super.configure(auth);
		//memory authentification
		//auth.inMemoryAuthentication().withUser("user1").password("{noop}1234").roles("USER");
		//auth.inMemoryAuthentication().withUser("user2").password("{noop}1234").roles("USER");
		//auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("USER","ADMIN");
		PasswordEncoder passwordEncoder=passwordEncoder();
		System.out.println("*******************************");
		System.out.println(passwordEncoder.encode("1234"));
		System.out.println("*********************************");
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select username as principal, password as credentials, active from users where username =?")
		.authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username=?")
		.passwordEncoder(passwordEncoder)
		.rolePrefix("ROLE_");
		//password encoder par defaut
		/*auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder.encode("1234")).roles("USER");
		auth.inMemoryAuthentication().withUser("user2").password(passwordEncoder.encode("1234")).roles("USER");
		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN");
		*/
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// pour desactiver le mode securité
		//super.configure(http);
		
		//pour definir votre propre formulaire authentification qui permet de returner une vue login.html
		//http.formLogin().loginPage("/login");
		
		//cad il va utiliser le meme formulaire par defaut
		http.formLogin().loginPage("/login");
		
		//ici c'est formulaire basic authentification du protocol http
		//http.httpBasic();
		
		//pour authoriser les actions 
		//http.authorizeRequests().anyRequest().authenticated();
		
		http.authorizeRequests().antMatchers("/admin/**","/save**/**","/delete**/**","/form**/**").hasRole("ADMIN");
		http.authorizeRequests().antMatchers("/patients**/**").hasRole("USER");
		
		//toute les requêtes de http necessite de passer par une authentification
		//http.authorizeRequests().anyRequest().authenticated();
		//pour desactiver le csrf syncroniser token
		//http.csrf().disable();
		
		//pour l'activer
		//http.csrf();
		
		http.authorizeRequests().antMatchers("/user**","/login", "/webjars/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated();
		
		//chauqe fois qu,un user demande une ressource qu'il ne peu pas acceder affiche la page notAuthorized
		http.exceptionHandling().accessDeniedPage("/notAuthorized");
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
