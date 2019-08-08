package com.oa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
	private DepartmentAttributeMapper contentMapper;
 


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
	public OAResult delete(String ids) {
		
		//获取前台ids
		String[] idList = ids.split(",");
		String description=null;
		for(String id : idList){
			description=id;//获取到前台的部门编号
		}
		//返回结果
		System.out.println(description);
		//找出了部门
		 LdapQuery query = query()
		         .base("")
		         .attributes("ou", "description")
		         .where("objectclass").is("organizationalUnit")
		         .and("description").is(description);
		 List<String> list = ldapTemplate.search(
				 query, new AttributesMapper<String>() {
				public String mapFromAttributes(Attributes attrs) throws NamingException {
					//System.out.println("o是"+(String) attrs.get("l").get());
					return (String) attrs.get("ou").get();
				}
				//l
			});
		 String deptName=null;
		  for(String str:list){
			  deptName=str;
			 
		  }
		System.out.println(deptName);
		//找出o
		LdapQuery query2 = query()
		         .base("")
		         .attributes("l", "description")
		         .where("objectclass").is("organizationalUnit")
		         .and("description").is(description);
		 List<String> list2 = ldapTemplate.search(
				 query2, new AttributesMapper<String>() {
				public String mapFromAttributes(Attributes attrs) throws NamingException {
					System.out.println("o是"+(String) attrs.get("l").get());
					return (String) attrs.get("l").get();
				}
				//l
			});
		 String o=null;
		  for(String str:list2){
			  o=str;
			 
		  }
		System.out.println(o);
		
		
		
		Department dept =new Department();
		dept.setDeptName(deptName);
		dept.setId(description);
		dept.setO(o);
		Name dn =buildDnDept(dept);
		
		/**
		 * 来一个判断，如果部门里面还有人则不删除部门，如果部门人数为0则删除该空部门。
		 */
		LdapQuery query3 = query().attributes("cn", "o","ou").where("objectclass").is("person").and("o").is(o).and("ou").is(deptName);
		List<String> list3 = ldapTemplate.search(query3, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {

				return (String) attrs.get("cn").get();
			}
		});
		int employNumbur=list3.size();	
		if(employNumbur==0){
			ldapTemplate.unbind(dn);
			System.out.println("成功");
			return OAResult.ok();
		}
		else{
			System.out.println("失败");
			return OAResult.unOk();
		}
		
		

		
	}
	protected Name buildDnDept(Department dept) {//为删除方法服务
	    return buildDnDept(dept.getO(), dept.getDeptName());
	 }

	 protected Name buildDnDept(String company, String department) {//为删除方法服务
	    return LdapNameBuilder.newInstance()
	      .add("o", company)
	      .add("ou", department)
	      .build();
	    //"ou=市场部,o=总部"
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
