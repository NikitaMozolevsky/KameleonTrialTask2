package com.example.kameleontrialtask2.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_creation")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private Date dateOfCreation;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Person(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
