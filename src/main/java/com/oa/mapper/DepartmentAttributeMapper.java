package com.oa.mapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.oa.pojo.Department;

public class DepartmentAttributeMapper implements AttributesMapper<Department> {

	@Override
	public Department mapFromAttributes(Attributes attrs) throws NamingException {
		Department department = new Department();
		if(attrs.get("o")!=null){
			department.setDeptName((String) attrs.get("o").get());
		}
		if(attrs.get("ou")!=null){
			department.setDeptName((String) attrs.get("ou").get());
		}
		if(attrs.get("description")!=null){
			department.setId((String)attrs.get("description").get());
		}
		if(attrs.get("businessCategory")!=null){
			department.setParentId((String) attrs.get("businessCategory").get());
		}
		if(attrs.get("st")!=null){
			department.setIsParent((String) attrs.get("st").get());
		}
		if(attrs.get("l")!=null){
			department.setO((String) attrs.get("l").get());
		}
		if(attrs.get("postalCode")!=null){
			department.setPosition((String) attrs.get("postalCode").get());
		}
		if(attrs.get("facsimileTelephoneNumber")!=null){
			department.setIsLastDept((String) attrs.get("facsimileTelephoneNumber").get());
		}
		return department;
	}

}
