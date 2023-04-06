package com.example.kameleontrialtask2.config;

import com.example.kameleontrialtask2.security.AuthProviderImpl;
import com.example.kameleontrialtask2.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity //указывает на то, что конфигурационный класс SpringSecurity
//it permits use @PreAuthorise, SS will check role before executing method
//usually uses in services
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;
    private final AuthProviderImpl authProvider;

    private final String[] allowedPagesForGuest = new String[]{
            "/guest",
            /*"/logout",*/
            "/registration",
            "/error",
            "/registration-page",
            "/top-10",
            "/flop-10",
            "/last-quote",
            /**Отвечают за CSS и JS, судя по всему исходит от /resources*/
            "/static/js/**",
            "/static/css/**",
            "/static/html/**"
    };

    private final String[] allowedPagesForUser = new String[] {
            "/create-quote-page",
            "/create-quote",
            "/profile",
            "/profile-page",

    };

    private final String[] allowedPagesForAdmin = new String[] {
            "/admin"
    };

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, AuthProviderImpl authProvider) {
        this.personDetailsService = personDetailsService;
        this.authProvider = authProvider;
    }

    //настройка формы для логина
    @Override //переопределяется из WebSecurityConfigurerAdapter
    protected void configure(HttpSecurity http) throws Exception {
        //конфигурация страницы входа, выхода, ошибки и т.д.
        //конфигурация авторизации (доступ по роли к страницам)
        //работает с http
        http
                //попытка отправки злоумышленииком формы, для каких-то злоумышленных
                //дел, доджится токеном на каждой thymeleaf странице
                /*.csrf().disable()*/ //отключение защиты от межсайтовой подделки запросов

                .authorizeHttpRequests()
                //страницы доступные админу
                //"ADMIN_ROLE" понимается как "ADMIN" автоматически SpSec
                //возможна работа не с ролью, а с Authorities
                //list of actions which user can do
                /**можно удалить и настраивать доступ к методам аннотацией
                 * @PreAuthorise*/
                .antMatchers(allowedPagesForAdmin).hasRole("ADMIN")
                .antMatchers(allowedPagesForUser).hasRole("USER")
                //страницы доступные всем
                .antMatchers(allowedPagesForGuest).permitAll()
                //для получения остальных страниц и пользователем и админом
                //can be deleted if will use @PreAuthorise and
                //@EnableGlobalMethodSecurity(prePostEnabled = true)
                //and mb something else
                .anyRequest().hasAnyRole("USER", "ADMIN")
                //остальные запросы недоступны
                /*.anyRequest().authenticated()*/ //при отсутствии admin ограничений
                .and() //and - объединитель разных настроек, настройка авторизации
                .formLogin()
                .loginPage("/guest") //метод захода в систему
                //SpringSecurity ожидает что сюда придут логин и пароль
                //SpringSecurity сам обрабатывает данные
                .loginProcessingUrl("/process-login")
                //не точно, подсмотрел
                .usernameParameter("username")
                .passwordParameter("password")
                //unsuccessful with key error (located in view (th) show message)
                .failureForwardUrl("/bad-credentials")
                //что происходит при успешной аутентификации
                //перенаправление на /hello, true - всегда
                .defaultSuccessUrl("/guest", true)
                .and()
                //удаление пользователя из сессии, удаление кукиз у пользователя
                .logout()
                .logoutUrl("/logout")
                //redirect to this page after logout
                .logoutSuccessUrl("/guest");

    }

    //настраивает логику аутентификации
    //даем понять SpringSecurity что для аутентификации используется
    //именно этот AuthProviderImpl
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /*auth.authenticationProvider(authProvider);*/

        auth.userDetailsService(personDetailsService)//упрощение, есть другая версия
                //прогоняет пароль через BCryptPasswordEncoder
                //при аутентификации
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean //возвращается используемый алгоритм шифрования
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
