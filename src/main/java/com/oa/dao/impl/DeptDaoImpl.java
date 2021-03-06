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
import com.oa.mapper.LabelAttributeMapper;
import com.oa.mapper.PersonAttributeMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.pojo.Label;
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
		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();
		String sDN = dept.getO();
		String regex = ",";
		String[] array = sDN.split(regex);
		for (int i = array.length - 1; i >= 0; i--) {
			ldapNameBuilder.add(array[i]);
		}
		ldapNameBuilder.add("ou", dept.getDeptName());
		return ldapNameBuilder.build();
	}

	protected Department buildDept(Name dn, Attributes attrs) {
		Department dept = new Department();
		// dept.setO(LdapUtils.getStringValue(dn, "o"));
		dept.setDeptName(LdapUtils.getStringValue(dn, "ou"));
		try {
			dept.setO((String) attrs.get("l").get());
			dept.setId((String) attrs.get("description").get());
			dept.setIsLastDept((String) attrs.get("facsimileTelephoneNumber").get());
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
		// 补全pojo
		dept.setIsParent("1");
		dept.setIsLastDept("1");
		
		//判断是否有同级人员
		List<Employee> tjemp = ldapTemplate.search(
				query().where("objectclass").is("person")
				.and("businessCategory").is(dept.getParentId()),
				new PersonAttributeMapper());
		if(tjemp.size()>0){
			return OAResult.build(500, "创建失败，人员和部门不能同级");
		}
		// 修改上级部门为非最后一级部门
		List<Department> list = ldapTemplate.search(query().where("description").is(dept.getParentId()),
				new DepartmentAttributeMapper());
		Department parentDept = list.get(0);
		if (parentDept.getIsLastDept().equals("1")) {
			parentDept.setIsLastDept("0");
			// 持久化更新
			Name parentDn = buildDn(parentDept);
			ldapTemplate.rebind(parentDn, null, buildAttributes(parentDept));
		}
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
		attrs.put("facsimileTelephoneNumber", dept.getIsLastDept());
		attrs.put("description", dept.getId());
		attrs.put("l", dept.getO());
		attrs.put("st", dept.getIsParent());
		attrs.put("postalCode", dept.getPosition());
		return attrs;
	}

	@Override
	public OAResult delete(String ids) {

		// 获取前台ids
		String[] idList = ids.split(",");
		String description = null;
		for (String id : idList) {
			description = id;// 获取到前台的部门编号
		}
		// 找出了部门,市场部
		LdapQuery query = query().base("").attributes("ou", "description").where("objectclass").is("organizationalUnit")
				.and("description").is(description);

		List<String> list = ldapTemplate.search(query, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("ou").get();
			}
		});
		String deptName = null;
		for (String str : list) {
			deptName = str; // ou
		}
		// 找出o，判断是否还有分支部门会使用到list2
		LdapQuery query2 = query().base("").attributes("l", "description").where("objectclass").is("organizationalUnit")
				.and("description").is(description);
		List<String> list2 = ldapTemplate.search(query2, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("l").get();
			}
		});
		String o = null;//使用
		for (String str : list2) {
			o = str;
		}

		Department dept = new Department();
		dept.setDeptName(deptName);
		dept.setId(description);
		dept.setO(o);
		Name dn = buildDn(dept);
		String emplyo = dn.toString();
		/**
		 * 来一个判断，如果部门里面还有人则不删除部门，如果部门人数为0则删除该空部门。
		 */
		LdapQuery query3 = query().attributes("cn", "o", "ou").where("objectclass").is("person").and("o").is(emplyo)
				.and("ou").is(deptName);
		List<String> list3 = ldapTemplate.search(query3, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("cn").get();
			}
		});
		int employNumbur = list3.size();
		//判断他的旁支是否还有，上级部门是否变成最后一级部门
		
		//Name dnSJ = buildDn(dept);
		LdapQuery query4 = query().attributes("ou", "l").where("objectclass").is("organizationalUnit").and("l").is(o);
		List<String> list4 = ldapTemplate.search(query4, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				//System.out.println("查找符合条件的部门"+(String) attrs.get("ou").get());
				return (String) attrs.get("ou").get();
			}
		});
		//System.out.println("list4的长度="+list4.size());
		//进行修改属性操作
		
		
		
		
		if (employNumbur == 0) {
			//进行修改属性操作
			if(list4.size()-1==0){
				//进入上层目录
				Name scDN=buildCDn(o);//使用o
				//System.out.println("上级的路径"+scDN.toString());
				//Name groupDn = buildGDn(employoutype);
				Attribute attr = new BasicAttribute("facsimileTelephoneNumber", "1");
				ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
				ldapTemplate.modifyAttributes(scDN, new ModificationItem[] { item });
				
			}
			else{
				
			}
			ldapTemplate.unbind(dn);
			return OAResult.ok();
		} else {
			return OAResult.unOk();
		}
	}
	protected Name buildCDn(String DN) {
		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();
		String regex = ",";
		String[] array = DN.split(regex);
		for (int i = array.length - 1; i >= 0; i--) {
			ldapNameBuilder.add(array[i]);
		}
		//ldapNameBuilder.add("ou", dept.getDeptName());
		return ldapNameBuilder.build();
	}

	@Override
	public OAResult edit(Department dept) {

		String description = dept.getId();
		List<Department> list = ldapTemplate.search(
				query().where("objectclass").is("organizationalUnit").and("description").is(description),
				new DepartmentAttributeMapper());
		Department olddept = (Department) list.get(0);

		dept.setIsParent("1");
		dept.setIsLastDept(olddept.getIsLastDept());

		Name oldDn = buildDn(olddept);
		Name newDn = buildDn(dept);

		String Pid = ldapTemplate.lookup(dept.getO(), new DepartmentAttributeMapper()).getId();
		dept.setParentId(Pid);

		DirContextOperations context = ldapTemplate.lookupContext(oldDn);
		
	   //判断是否有人员同级
		List<Employee> tjemp = ldapTemplate.search(
				query().where("objectclass").is("person")
				.and("businessCategory").is(Pid),
				new PersonAttributeMapper());
		if(tjemp.size()>0){
			return OAResult.build(500, "不能移动到有人员的部门，人员和部门不能同级");
		}
		
		// 修改部门属性
		mapToContext(dept, context);
		newDn = buildDn(dept);
		ldapTemplate.modifyAttributes(context);// 修改除条目外的其他属性
		ldapTemplate.rename(oldDn, newDn);
				

		//修改上级部门
		List<Department> osjdept = ldapTemplate.search(
				query().where("objectclass").is("organizationalUnit")
				.and("businessCategory").is(olddept.getParentId()),
				new DepartmentAttributeMapper());
		if(osjdept.size()<1){
			Department nextdept = ldapTemplate.lookup(olddept.getO(), new DepartmentAttributeMapper());
			DirContextOperations context1 = ldapTemplate.lookupContext(nextdept.getDn());
			context1.setAttributeValue("facsimileTelephoneNumber", "1");
			ldapTemplate.modifyAttributes(context1);
		}
		Department nsjdept=ldapTemplate.lookup(dept.getO(), new DepartmentAttributeMapper());
		DirContextOperations context1 = ldapTemplate.lookupContext(nsjdept.getDn());
		context1.setAttributeValue("facsimileTelephoneNumber", "0");
		ldapTemplate.modifyAttributes(context1);
		
		// 修改这个部门下部门人员的上级部门
		// 部门
		List<Department> listdept = ldapTemplate.search(
				query().base(dept.getDn()).where("objectclass").is("organizationalUnit"),
				new DepartmentAttributeMapper());
		
		for (int i = 0; i < listdept.size(); i++) {
			Department nextdept = listdept.get(i);
			nextdept.setO(nextdept.getO().replace(olddept.getDn(), dept.getDn()));
			DirContextOperations context2 = ldapTemplate.lookupContext(nextdept.getDn());
			context2.setAttributeValue("l", nextdept.getO());
			ldapTemplate.modifyAttributes(context2);
		}
		// 人员
		List<Employee> listemp = ldapTemplate.search(query().base(dept.getDn()).where("objectclass").is("person"),
				new PersonAttributeMapper());
		
		for (int i = 0; i < listemp.size(); i++) {
			Employee nextemp = listemp.get(i);
			String OldEmpDn=nextemp.getDn();
			nextemp.setO(nextemp.getO().replace(olddept.getDn(), dept.getDn()));
			DirContextOperations context2 = ldapTemplate.lookupContext(nextemp.getDn());
			context2.setAttributeValue("o", nextemp.getO());
			ldapTemplate.modifyAttributes(context2);
			
			String NewEmpDn=nextemp.getDn();
			// 处理标签，改变标签里的人员DN
			String regex = ","; 
			String label=nextemp.getLabel(); 
			String[] array1 =label.split(regex);
			//System.out.println(OldEmpDn+"    "+NewEmpDn);
			for(int j=0;j<array1.length; j++){
				if(array1[j].equals("")==false){
					//System.out.println(array1[j]);
					Name groupDn = buildGroupDn(array1[j]);
					DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
					ctx.addAttributeValue("member", NewEmpDn);
					ctx.removeAttributeValue("member", OldEmpDn);
					ldapTemplate.modifyAttributes(ctx);
				}	 
			}
		}
		return OAResult.ok();

	}
	private Name buildGroupDn(String groupName) {
		return LdapNameBuilder.newInstance().add("ou=标签").add("cn", groupName).build();
	}

	// ou
	protected void mapToContext(Department OU, DirContextOperations context) {
		// 将修改后的属性值赋给context的属性
		// context.setAttributeValue("ou", OU.getDeptName());会报错
		context.setAttributeValue("l", OU.getO());
		context.setAttributeValue("st", OU.getIsParent());
		context.setAttributeValue("description", OU.getId());
		context.setAttributeValue("businessCategory", OU.getParentId());
		context.setAttributeValue("postalCode", OU.getPosition());
		context.setAttributeValue("facsimileTelephoneNumber", OU.getIsLastDept());

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
		List<Label> list3 = ldapTemplate.search(query().where("objectclass").is("groupOfNames").and("o").is("0"),
				new LabelAttributeMapper());
		long maxId3 = -1;
		for (Label label : list3) {
			long id1 = Long.parseLong(label.getId());
			long id2 = Long.parseLong(label.getParentId());
			if (maxId3 < id1) {
				maxId3 = id1;
			}
			if (maxId3 < id2) {
				maxId3 = id2;
			}
		}
		if (maxId1 > maxId2 && maxId1 > maxId3) {
			return OAResult.ok(maxId1 + 1 + "");
		} else if (maxId2 > maxId1 && maxId2 > maxId3) {
			return OAResult.ok(maxId2 + 1 + "");
		} else {
			return OAResult.ok(maxId3 + 1 + "");
		}
	}

}
