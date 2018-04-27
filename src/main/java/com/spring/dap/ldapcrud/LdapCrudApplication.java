package com.spring.dap.ldapcrud;

import com.spring.dap.ldapcrud.domain.Person;
import com.spring.dap.ldapcrud.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class LdapCrudApplication {

    private static Logger log = LoggerFactory.getLogger(LdapCrudApplication.class);

    @Autowired
    private PersonRepository personRepository;

    public static void main(String[] args) {
        SpringApplication.run(LdapCrudApplication.class, args);
    }

    @PostConstruct
    public void setup() {
        log.info("Spring LDAP CRUD Operations Binding and Unbinding Example");

        List<Person> persons = personRepository.findAll();
        persons.stream().forEach(System.out::println);

        Person p = new Person();
        p.setUserName("akem");
        p.setPassword("slones");
        p.setInitials("EEA");
        p.setFullName("Eric Akolly");
        p.setLastName("Akolly");
        p.setDisplayName("AKEEEM");

        personRepository.create(p);

    }
}
