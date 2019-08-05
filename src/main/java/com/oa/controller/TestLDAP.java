package com.oa.controller;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import com.oa.mapper.DepartmentAttributeMapper;
import com.oa.mapper.PersonAttributeMapper;
import com.oa.mapper.PersonContextMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;

@Controller
public class TestLDAP {
	@Autowired
	private LdapTemplate ldapTemplate;
	
	
	
	protected Name buildDn(Employee p) {
	    return LdapNameBuilder.newInstance("dc=poke_domain,dc=com")
		  .add("o",p.getCompany())
		  .add("ou",p.getDepartment())
	      .add("cn", p.getFullName())
	      .build();
	  }
	protected Employee buildPerson(Name dn, Attributes attrs) {
		  Employee person = new Employee();
		  person.setCompany(LdapUtils.getStringValue(dn, "o"));
		  person.setDepartment(LdapUtils.getStringValue(dn, "ou"));
		  person.setFullName(LdapUtils.getStringValue(dn, "cn"));
		  try {
			// Populate rest of person object using attributes.
			person.setLastName((String) attrs.get("sn").get());
			person.setEmployeeNumber((String) attrs.get("employeeNumber").get());
			person.setTitle((String) attrs.get("title").get());
			person.setEmail((String) attrs.get("mail").get());
			person.setPhone((String) attrs.get("telephoneNumber").get());
			person.setDescription((String) attrs.get("description").get());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return person;
	}
	/**
	 * 查询财务部的所有人员
	 * 
	 * @return
	 */
	@RequestMapping("/findAll")
	public String findemps() {
		/*
		 * ApplicationContext context = new
		 * ClassPathXmlApplicationContext("applicationContext.xml");
		 * LdapTemplate ldapTemplate = context.getBean(LdapTemplate.class);
		 */
		// LdapTemplate ldapTemplate = LdapUtil.getLdapTemplate();
		//AndFilter filter = new AndFilter();
		//filter.and(new EqualsFilter("objectClass", "person"));
		// List<Employee> list = ldapTemplate.search("ou=财务部,ou=所有部门,ou=总部",
		// filter.encode(),new PersonAttributeMapper());
		List<Employee> list = ldapTemplate.search(query().where("objectclass").is("person"),
				new PersonAttributeMapper());
		for (Employee employee : list) {
			System.out.println(employee);
		}
		return "test";
	}
	
	@RequestMapping("/findDepts")
	public String findDepts(){
		//AndFilter filter = new AndFilter();
		//filter.and(new EqualsFilter("objectClass", "organizationalUnit"));
		List<Department> list = ldapTemplate.search(query().where("objectclass").is("organizationalUnit"),
				new DepartmentAttributeMapper());
		for (Department department : list) {
			System.out.println(department);
		}
		return "test";
	}

	/**
	 * 通过员工编号查询员工
	 */
	@RequestMapping("/findUser")
	public String findByEmployeeNumber() {
		// DirContextAdapter employee = (DirContextAdapter)
		// ldapTemplate.lookup("cn=张三,ou=财务部,ou=所有部门,ou=总部");
		Employee employee = ldapTemplate.lookup("cn=李四,ou=市场部,ou=产品市场中心,ou=所有部门,ou=分部", new PersonAttributeMapper());
		System.out.println(employee);
		return "test";
	}

	/**
	 * 查询部门
	 */
	@RequestMapping("/finddept")
	public String findDept() {
		DirContextAdapter dept = (DirContextAdapter) ldapTemplate.lookup("ou=市场部,o=总部");
		System.out.println(dept);
		return "test";
	}

	@RequestMapping("/findperson")
	public String getPersonNamesByLastName() {
		LdapQuery query = query().attributes("cn", "sn").where("objectclass").is("person").and("sn").is("李");
		List<String> list = ldapTemplate.search(query, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {

				return (String) attrs.get("cn").get();
			}
		});
		for (String string : list) {
			System.out.println(string);
		}
		return "test";
	}
	@RequestMapping("/delete")
	public String delete(){
		Employee person = new Employee();
		person.setFullName("李四");
		person.setCompany("分部");
		person.setDepartment("市场部");
		person.setEmail("jane.doe@poke_domain.com");
		person.setEmployeeNumber("2");
		person.setLastName("李");
		person.setPhone("+46 555-123459");
		person.setTitle("市场部经理");
		Name dn = buildDn(person);
		ldapTemplate.unbind(dn);
		return "test";
	}
	/*public Employee findByPrimaryKey(
		      String name, String company, String country) {
		      Name dn = buildDn(name, company, country);
		      return (Employee) ldapTemplate.lookup(dn, new PersonContextMapper());
		   }*/
	@RequestMapping("/getDeptList")
	public String getDeptList() {
		List<Department> list = ldapTemplate.search(query().where("businessCategory").is("0"), new DepartmentAttributeMapper());
		for (Department department : list) {
			System.out.println(department);
		}
		return "test";
	}
	

}
