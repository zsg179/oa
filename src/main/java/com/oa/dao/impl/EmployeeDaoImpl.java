package com.oa.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;


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
		//获取前台ids
		String[] idList = ids.split(",");
		String description=null;
		for(String id : idList){
			description=id;//获取到前台的部门编号
		}
		System.out.println(description+"找到了ids");
				
		//找出了部门
		LdapQuery query = query()
		.base("")
		.attributes("ou", "description")
		.where("objectclass").is("person")
		.and("description").is(description);
				 
		List<String> list = ldapTemplate.search(
		query, new AttributesMapper<String>() {
		public String mapFromAttributes(Attributes attrs) throws NamingException {
			return (String) attrs.get("ou").get();
		}
		});
		String deptName=null;
		for(String str:list){
			deptName=str;	 
		}
		System.out.println(deptName+"是部门ou");
		//找出o
		LdapQuery query2 = query()
		.base("")
		.attributes("o", "description")
		.where("objectclass").is("person")
		.and("description").is(description);
		List<String> list2 = ldapTemplate.search(
		query2, new AttributesMapper<String>() {
		public String mapFromAttributes(Attributes attrs) throws NamingException {
			return (String) attrs.get("o").get();
			}
		});
		String o=null;
		for(String str:list2){
			o=str;
		}
		System.out.println(o+"是公司");
		//找员工cn
		LdapQuery query3 = query()
		.base("")
		.attributes("cn", "description")
		.where("objectclass").is("person")
		.and("description").is(description);
		List<String> list3 = ldapTemplate.search(
		query3, new AttributesMapper<String>() {
		public String mapFromAttributes(Attributes attrs) throws NamingException {
		return (String) attrs.get("cn").get();
		}
		});
		String cn=null;
		for(String str:list3){
			cn=str;
		}
		System.out.println(cn+"是名字");
					  
		Employee p=new Employee();
		p.setO(o);
		p.setFullName(cn);
		p.setOu(deptName);;
		Name dn = buildDnE(p);//
		System.out.println(dn);
		ldapTemplate.unbind(dn);
					
		return OAResult.ok();
		}
	    
        protected Name buildDnE(Employee person) {//员工的buildDn
		  return buildDnE(person.getFullName(), person.getOu(), person.getO());
		}

	    protected Name buildDnE(String fullname, String department, String company) {
		  return LdapNameBuilder.newInstance()
		  .add("o", company)
		  .add("ou", department)
		  .add("cn", fullname)
		  .build();
	   }

	protected Name buildDn(Employee emp) {
		return LdapNameBuilder.newInstance().add("o",emp.getO() ).add("ou",emp.getOu()).add("cn", emp.getFullName()).build();
	}
	@Override
	public OAResult edit(Employee emp) {
		Name dn = buildDn(emp);
		DirContextOperations context = ldapTemplate.lookupContext(dn);
        mapToContext(emp, context);
        
        ldapTemplate.modifyAttributes(context);
		return OAResult.ok();
	}
	
    protected void mapToContext (Employee emp, DirContextOperations context) {
    	
    	//context.setAttributeValue("cn", emp.getFullName());
    	context.setAttributeValue("sn",emp.getLastName());
    	context.setAttributeValue("businessCategory",emp.getParentId());
    	context.setAttributeValue("description",emp.getId());
    	context.setAttributeValue("telephoneNumber",emp.getPhone());
    	context.setAttributeValue("mail",emp.getEmail());
    	context.setAttributeValue("employeeType",emp.getLabel());
    	context.setAttributeValue("title",emp.getTitle());
    	
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
