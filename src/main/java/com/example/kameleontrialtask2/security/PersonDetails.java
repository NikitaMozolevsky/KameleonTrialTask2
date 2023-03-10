package com.example.kameleontrialtask2.security;

import com.example.kameleontrialtask2.entity.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

//Обертка над сущностью
//Реализует стандартизированный интерфейс
public class PersonDetails implements UserDetails {

    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    //для получения данных аутентифицированного пользователя
    public Person getPerson() {
        return this.person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //возвращение листа из одного элемента обозначающего роль
        //можно в лист передавать либо роль, либо позволительные действия
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
