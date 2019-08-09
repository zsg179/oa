package com.oa.mapper;

import javax.naming.NamingException;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

import com.oa.pojo.Employee;

public class PersonContextMapper implements ContextMapper{

	@Override
	public Employee mapFromContext(Object ctx) throws NamingException {
		DirContextAdapter context = (DirContextAdapter)ctx;
		Employee p = new Employee();
        p.setFullName(context.getStringAttribute("cn"));
        p.setLastName(context.getStringAttribute("sn"));
        p.setEmail(context.getStringAttribute("email"));
        p.setEmployeeNumber(context.getStringAttribute("employNumber"));
        p.setPhone(context.getStringAttribute("telephoneNumber"));
        p.setTitle(context.getStringAttribute("title"));
        return p;
	}

}
