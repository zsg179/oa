package com.oa.mapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.oa.pojo.Employee;

public class PersonAttributeMapper implements AttributesMapper<Employee> {

	@Override
	public Employee mapFromAttributes(Attributes attrs) throws NamingException {
		Employee person = new Employee();
		person.setFullName((String) attrs.get("cn").get());
		person.setLastName((String) attrs.get("sn").get());
		person.setEmployeeNumber((String) attrs.get("employeeNumber").get());
		person.setTitle((String) attrs.get("title").get());
		person.setEmail((String) attrs.get("mail").get());
		person.setPhone((String) attrs.get("telephoneNumber").get());
		person.setDescription((String) attrs.get("description").get());
		person.setDepartment((String) attrs.get("ou").get());
		person.setCompany((String) attrs.get("o").get());
		return person;
	}

}
