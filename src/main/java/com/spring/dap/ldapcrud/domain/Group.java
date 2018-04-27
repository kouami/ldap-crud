package com.spring.dap.ldapcrud.domain;

import lombok.Getter;
import lombok.Setter;
import javax.naming.Name;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Group {

    private String name;
    private Set<Name> members;

    public Group(String name, Set<Name> members) {
        this.name = name;
        this.members = members;
    }

    public Group(Name dn, String name, Set<Name> members) {
        this.name = name;
        this.members = members;
    }

    public void addMember(Name member) {
        if (this.members == null){
            this.members = new HashSet<>();
        }
        members.add(member);
    }

    public void removeMember(Name member) {
        members.remove(member);
    }
}
