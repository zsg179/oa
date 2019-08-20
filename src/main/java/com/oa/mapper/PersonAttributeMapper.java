package com.oa.mapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.oa.pojo.Employee;

public class PersonAttributeMapper implements AttributesMapper<Employee> {

	@Override
	public Employee mapFromAttributes(Attributes attrs) throws NamingException {
		Employee person = new Employee();
		if(attrs.get("cn")!=null){   
			person.setFullName((String) attrs.get("cn").get());
		}
		if(attrs.get("sn")!=null){
			person.setLastName((String) attrs.get("sn").get());
		}
		if(attrs.get("title")!=null){
			person.setTitle((String) attrs.get("title").get());
		}
		if(attrs.get("mail")!=null){
			person.setEmail((String) attrs.get("mail").get());
		}
		if(attrs.get("telephoneNumber")!=null){
			person.setPhone((String) attrs.get("telephoneNumber").get());
		}
		if(attrs.get("description")!=null){
			person.setId((String) attrs.get("description").get());
		}
		if(attrs.get("st")!=null){
			person.setIsParent((String) attrs.get("st").get());
		}
		if(attrs.get("businessCategory")!=null){
			person.setParentId((String) attrs.get("businessCategory").get());
		}
		if(attrs.get("o")!=null){
			person.setO((String) attrs.get("o").get());
		}
		if(attrs.get("ou")!=null){
			person.setOu((String) attrs.get("ou").get());
		}
		if(attrs.get("employeeType")!=null){
			person.setLabel((String) attrs.get("employeeType").get());
		}
		return person;
	}

}
