package com.spring.dap.ldapcrud.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Person {

    private String userName;
    private String password;
    private String fullName;
    private String lastName;
    private String initials;
    private String displayName;

    public Person(){}

    public Person(String userName, String password, String fullName, String lastName, String initials,
                  String displayName) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.lastName = lastName;
        this.initials = initials;
        this.displayName = displayName;
    }
}
