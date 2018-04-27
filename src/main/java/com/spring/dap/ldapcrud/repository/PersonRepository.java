package com.spring.dap.ldapcrud.repository;

import com.spring.dap.ldapcrud.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import java.util.List;
import java.util.Map;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonRepository implements BaseLdapNameAware {

    @Autowired
    private LdapTemplate ldapTemplate;
    private LdapName baseLdapPath;

    @Override
    public void setBaseLdapPath(LdapName baseLdapPath) {
        this.baseLdapPath = baseLdapPath;
    }

    public List<Person> findAll() {
        EqualsFilter filter = new EqualsFilter("objectClass", "inetOrgPerson");
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), new PersonContextMapper());
    }

    public void create(Person p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    private Name buildDn(Person p) {
        return LdapNameBuilder.newInstance()
                .add("ou", "people")
                .add("uid", p.getUserName())
                .build();
    }

    public void update(Person p) {
        ldapTemplate.rebind(buildDn(p), null, buildAttributes(p));
    }

    public Person findOne(String uid) {
        Name dn = LdapNameBuilder.newInstance(baseLdapPath)
                .add("ou", "people")
                .add("uid", uid)
                .build();
        return ldapTemplate.lookup(dn, new PersonContextMapper());
    }

    public List<Person> findByName(String name) {
        LdapQuery q = query()
                .where("objectClass").is("inetOrgPerson")
                .and("cn").whitespaceWildcardsLike(name);
        return ldapTemplate.search(q, new PersonContextMapper());
    }

    private Attributes buildAttributes(Person p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectClass");
        ocAttr.add("top");
        ocAttr.add("inetOrgPerson");
        attrs.put(ocAttr);
        attrs.put("ou", "people");
        attrs.put("uid", p.getUserName());
        attrs.put("cn", p.getFullName());
        attrs.put("sn", p.getLastName());
        attrs.put("userPassword", p.getPassword());
        attrs.put("initials", p.getInitials());
        attrs.put("displayName", p.getDisplayName());
        return attrs;
    }

    public void updateLastName(Person p) {
        Attribute attr = new BasicAttribute("sn", p.getLastName());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(p), new ModificationItem[]{item});
    }

    public void delete(Person p) {
        ldapTemplate.unbind(buildDn(p));
    }

    private static class PersonContextMapper extends AbstractContextMapper<Person> {
        public Person doMapFromContext(DirContextOperations context) {
            Person person = new Person();
            person.setFullName(context.getStringAttribute("cn"));
            person.setLastName(context.getStringAttribute("sn"));
            person.setUserName(context.getStringAttribute("uid"));
            byte[] bytePass = (byte[]) context.getObjectAttribute("userPassword");
            person.setPassword(new String(bytePass));
            person.setInitials(context.getStringAttribute("initials"));
            person.setDisplayName(context.getStringAttribute("displayName"));
            return person;
        }
    }
}
