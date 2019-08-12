package com.oa.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.List;

import com.oa.dao.EmployeeDao;
import com.oa.mapper.DepartmentAttributeMapper;
import com.oa.mapper.PersonAttributeMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.util.OAResult;

@Repository(value = "employeeDao")
public class EmployeeDaoImpl implements EmployeeDao {
	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public OAResult create(Employee emp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult delete(String ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult edit(Employee emp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EasyUIDataGridResult getEmpInfoById(String id) {
		List<Employee> list = ldapTemplate.search(query().where("description").is(id), new PersonAttributeMapper());
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(list.size());
		result.setRows(list);
		return result;
	}

	@Override
	public List<EasyUITreeNote> getEmpList(String parentId) {
		List<Department> list = ldapTemplate.search(query().where("businessCategory").is(parentId),
				new DepartmentAttributeMapper());
		List<EasyUITreeNote> result = new ArrayList<>();
		for (Department department : list) {
			// 取id
			String id = department.getId();
			if (department.getIsParent().equals("0")) {
				// 查询到的是员工
				List<Employee> personList = ldapTemplate.search(
						query().where("businessCategory").is(department.getParentId()), new PersonAttributeMapper());
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
