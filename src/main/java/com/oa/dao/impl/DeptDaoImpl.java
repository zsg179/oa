package com.oa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import com.oa.dao.DeptDao;
import com.oa.mapper.DepartmentAttributeMapper;
import com.oa.mapper.PersonAttributeMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * 部门dao的实现类
 * 
 * @author 朱树广
 *
 */

@Repository(value = "deptDao")
public class DeptDaoImpl implements DeptDao {
	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public List<EasyUITreeNote> getDeptList(String parentId) {
		List<Department> list = ldapTemplate.search(query().where("businessCategory").is(parentId),
				new DepartmentAttributeMapper());
		List<EasyUITreeNote> result = new ArrayList<>();
		for (Department department : list) {
			
			// 取id
			String id = department.getId();
			if (department.getDeptName() == null) {
				// 查询到的是员工
				List<Employee> personList = ldapTemplate.search(query().where("businessCategory").is(department.getParentId()),
						new PersonAttributeMapper());
				for (Employee emp : personList) {
					EasyUITreeNote note = new EasyUITreeNote();
					// 补全pojo
					note.setId(Long.parseLong(emp.getId()));
					note.setText(emp.getFullName());
					note.setState("open");
					result.add(note);
				}
				break;
			} else {
				EasyUITreeNote note = new EasyUITreeNote();
				// 部门
				note.setId(Long.parseLong(id));
				note.setText(department.getDeptName());
				note.setState(department.getIsParent().equals("1") ? "closed" : "open");
				result.add(note);
			}		
		}
		return result;
	}

}
