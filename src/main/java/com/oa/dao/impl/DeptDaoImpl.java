package com.oa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;

import org.springframework.ldap.core.DirContextOperations;

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

	protected Name buildDn(Department dept) {
		return LdapNameBuilder.newInstance().add("o", dept.getO()).add("ou", dept.getDeptName()).build();
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
		//补全pojo
		dept.setIsParent("1");
		Name dn = buildDn(dept);
		ldapTemplate.bind(dn, null, buildAttributes(dept));
		return OAResult.ok();
	}
	
	private Attributes buildAttributes(Department dept) {
	      Attributes attrs = new BasicAttributes();
	      BasicAttribute ocattr = new BasicAttribute("objectclass");
	      ocattr.add("top");
	      ocattr.add("organizationalUnit");
	      attrs.put(ocattr);
	      attrs.put("businessCategory", dept.getParentId());
	      attrs.put("description", dept.getId());
	      attrs.put("l", dept.getO());
	      attrs.put("st", dept.getIsParent());
	      return attrs;
	   }

	@Override
	public OAResult delete(String ids) {
		
		//获取前台ids
		String[] idList = ids.split(",");
		String description=null;
		for(String id : idList){
			description=id;//获取到前台的部门编号
		}
		//找出了部门
		 LdapQuery query = query()
		         .base("")
		         .attributes("ou", "description")
		         .where("objectclass").is("organizationalUnit")
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
		//找出o
		LdapQuery query2 = query()
		         .base("")
		         .attributes("l", "description")
		         .where("objectclass").is("organizationalUnit")
		         .and("description").is(description);
		 List<String> list2 = ldapTemplate.search(
				 query2, new AttributesMapper<String>() {
				public String mapFromAttributes(Attributes attrs) throws NamingException {
					return (String) attrs.get("l").get();
				}
			});
		 String o=null;
		  for(String str:list2){
			  o=str;
		  }
		
		

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
			return OAResult.ok();
		} else {
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


	

	

	
	
    private Name buildDn(String DN) {//按照DN构建路径
    	
    	//LdapNameBuilder  ldapNameBuilder = LdapNameBuilder.newInstance("dc=poke_domain,dc=com");
    	LdapNameBuilder  ldapNameBuilder = LdapNameBuilder.newInstance( );
    	String regex = ",";
    	String[] array = DN.split(regex); 
    	
    	for(int i =array.length-1;i>=0 ; i--){
    		//String[] AandV = array[i].split("=");
    		//ldapNameBuilder.add(AandV[0].trim(),AandV[1].trim());//添加属性和它的值
    		ldapNameBuilder.add(array[i]);
        }
    	
    	//ldapNameBuilder.add(DN.trim());
    	
        return ldapNameBuilder.build();
    }
	@Override
	public OAResult edit(Department dept) {
		Name dn = buildDn(dept);
		ldapTemplate.rebind(dn, null, buildAttributes(dept));
		return OAResult.ok();

	}
    
    @Override
    public OAResult update(String DN,Department OU) {//根据提供的条目和OU更新组织
    	
    	
    	Name dn = buildDn(DN);
    	
    	DirContextOperations context = ldapTemplate.lookupContext(dn);
    	
    	mapToContext(OU, context);
        
        ldapTemplate.modifyAttributes(context);
        
        return OAResult.ok();
    }
    
   
	//ou
    protected void mapToContext (Department OU, DirContextOperations context) {
    	
    	//将修改后的属性值赋给context的属性
    	//context.setAttributeValue("ou", OU.getDeptName());会报错
    	
    	context.setAttributeValue("l", OU.getO());
    	context.setAttributeValue("st", OU.getIsParent());
    	context.setAttributeValue("description", OU.getId());
    	context.setAttributeValue("businessCategory", OU.getParentId());
    	
    	
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

	@Override
	public OAResult getMaxId() {
		List<Department> list1 = ldapTemplate.search(query().where("st").is("1"), new DepartmentAttributeMapper());
		long maxId1 = -1;
		for (Department department : list1) {
			long id1 = Long.parseLong(department.getId());
			long id2 = Long.parseLong(department.getParentId());
			if (maxId1 < id1) {
				maxId1 = id1;
			}
			if (maxId1 < id2) {
				maxId1 = id2;
			}
		}
		List<Employee> list2 = ldapTemplate.search(query().where("st").is("0"), new PersonAttributeMapper());
		long maxId2 = -1;
		for (Employee employee : list2) {
			long id1 = Long.parseLong(employee.getId());
			long id2 = Long.parseLong(employee.getParentId());
			if (maxId2 < id1) {
				maxId2 = id1;
			}
			if (maxId2 < id2) {
				maxId2 = id2;
			}
		}
		if (maxId1 > maxId2) {
			return OAResult.ok(maxId1 + 1 + "");
		} else {
			return OAResult.ok(maxId2 + 1 + "");
		}
	}

	


}
