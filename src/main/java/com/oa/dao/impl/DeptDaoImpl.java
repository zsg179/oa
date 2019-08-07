package com.oa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;

import com.oa.dao.DeptDao;
import com.oa.mapper.DepartmentAttributeMapper;
import com.oa.mapper.PersonAttributeMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.util.OAResult;

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

	public static final String BASE_DN = "dc=poke_domain,dc=com";

	protected Name buildDn(Department dept) {
		return LdapNameBuilder.newInstance(BASE_DN).add("o", dept.getO()).add("ou", dept.getDeptName()).build();
	}

	protected Department buildDept(Name dn, Attributes attrs) {
		Department dept = new Department();
		// dept.setO(LdapUtils.getStringValue(dn, "o"));
		dept.setDeptName(LdapUtils.getStringValue(dn, "ou"));
		try {
			dept.setO((String) attrs.get("l").get());
			dept.setId((String) attrs.get("description").get());
			dept.setParentId((String) attrs.get("businessCategory").get());
			dept.setIsParent((String) attrs.get("st").get());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dept;
	}

	@Override
	public List<EasyUITreeNote> getDeptList(String parentId) {
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

	@Override
	public OAResult create(Department dept) {
		Name dn = buildDn(dept);
		return null;
	}

	@Override
	public OAResult delete(Department dept) {
		
		dept.setDeptName("运维部");
		dept.setO("总部");
		Name dn =buildDn(dept);
		/**
		 * 来一个判断，如果部门里面还有人则不删除部门，如果部门人数为0则删除该空部门。
		 */
		LdapQuery query = query().attributes("cn", "o","ou").where("objectclass").is("person").and("o").is("总部").and("ou").is("运维部");
		List<String> list = ldapTemplate.search(query, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {

				return (String) attrs.get("cn").get();
			}
		});
		int employNumbur=list.size();	
		if(employNumbur==0){
			ldapTemplate.unbind(dn);
			System.out.println("成功");
			return OAResult.ok();
		}
		else{
			System.out.println("失败");
			return OAResult.ok();
		}
	}

	@Override
	public OAResult edit(Department dept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EasyUIDataGridResult geteDeptInfoById(String id) {
		List<Department> list = ldapTemplate.search(query().where("description").is(id),
				new DepartmentAttributeMapper());
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(list.size());
		return result;
	}

}
