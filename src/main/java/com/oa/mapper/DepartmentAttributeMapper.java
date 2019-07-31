package com.oa.mapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.oa.pojo.Department;

public class DepartmentAttributeMapper implements AttributesMapper<Department> {

	@Override
	public Department mapFromAttributes(Attributes attrs) throws NamingException {
		Department department = new Department();
		department.setDeptName((String) attrs.get("ou").get());
		return department;
	}

}
