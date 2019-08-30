package com.oa.dao.impl;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

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
import org.springframework.stereotype.Repository;

import com.oa.dao.LabelDao;
import com.oa.mapper.LabelAttributeMapper;
import com.oa.mapper.PersonAttributeMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.pojo.Label;
import com.oa.pojo.LabelMember;
import com.oa.util.OAResult;

/**
 * @author 朱树广
 * @date 2019年8月21日 上午10:34:36
 * @version 1.0
 */
@Repository(value = "labelDao")
public class LabelDaoImpl implements LabelDao {
	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public List<EasyUITreeNote> getLabelList(String parentId) {
		List<Label> list = ldapTemplate.search(query().where("businessCategory").is(parentId),
				new LabelAttributeMapper());
		List<EasyUITreeNote> result = new ArrayList<EasyUITreeNote>();
		for (Label label : list) {
			EasyUITreeNote easyUITreeNote = new EasyUITreeNote();
			easyUITreeNote.setId(Long.parseLong(label.getId()));
			easyUITreeNote.setText(label.getCn());
			easyUITreeNote.setState("open");
			result.add(easyUITreeNote);
		}
		return result;
	}

	@Override
	public EasyUIDataGridResult getMembers(String id) {
		List<Label> list = ldapTemplate.search(query().where("description").is(id), new LabelAttributeMapper());
		if (list != null && list.size() > 0) {
			Label label = list.get(0);
			List<String> members = label.getMembers();
			List<LabelMember> labelMember = new ArrayList<>();
			for (String mem : members) {
				LabelMember member = new LabelMember();
				member.setMember(mem);
				labelMember.add(member);
			}
			EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
			easyUIDataGridResult.setTotal(members.size());
			easyUIDataGridResult.setRows(labelMember);
			return easyUIDataGridResult;
		}
		return null;
	}

	@Override
	public OAResult create(Label label,String id) {
		List<Employee> listemp = ldapTemplate.search(
			      query().where("objectclass").is("person")
			             .and("description").is(id),
			             new PersonAttributeMapper());
		Employee emp=(Employee)listemp.get(0);
		//Label label1=list.get(0);
		String q=emp.getDn();
		//System.out.println(q);
	    label.addMember(q);
		label.setCn(label.getCn());//设置cn
		label.setId(label.getId());//设置description
		label.setIsParent("0");//将st置为0
		label.setParentId("46");//设置parentId
		Name dn = buildDn(label);
		ldapTemplate.bind(dn, null, buildAttributes(label));
		return OAResult.ok();
		}
	private Attributes buildAttributes(Label label) {
		BasicAttributes attrs = new BasicAttributes();	     
		BasicAttribute ocattr = new BasicAttribute("objectClass");	     
		ocattr.add("groupOfNames");	      
		ocattr.add("top");	      
		attrs.put(ocattr);	     
		attrs.put("cn", label.getCn());//显示标签组
		for(int i=0;i<label.getMembers().size();i++)
		{	attrs.put("member",label.getMembers().get(i));}
		attrs.put("description", label.getId());//显示id          	      
		attrs.put("o",label.getIsParent());      
		attrs.put("businessCategory", label.getParentId());//显示父节点     
		return attrs;
		}
				
	protected Name buildDn(Label label) {
		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();
		ldapNameBuilder.add("ou", "标签").add("cn", label.getCn()).build();
		return ldapNameBuilder.build();
		}


	@Override
	public OAResult addMember(String labelId, String id) {
		List<Label> list = ldapTemplate.search(
				query().where("objectclass").is("groupOfNames").and("description").is(labelId),
				new LabelAttributeMapper());
		Label label = list.get(0);

		List<Employee> listemp = ldapTemplate.search(
				query().where("objectclass").is("person").and("description").is(id), new PersonAttributeMapper());
		Employee emp = (Employee) listemp.get(0);

		// label.addMember(emp.getDn());
		Name groupDn = buildGroupDn(label.getCn());
		DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
		ctx.addAttributeValue("member", emp.getDn());
		ldapTemplate.modifyAttributes(ctx);

		Name userDn = buildDn(emp.getDn());
		DirContextOperations context2 = ldapTemplate.lookupContext(userDn);
		emp.setLabel(emp.getLabel() + "," + label.getCn());
		context2.setAttributeValue("employeeType", emp.getLabel());
		ldapTemplate.modifyAttributes(context2);

		return OAResult.ok();
	}

	private Name buildGroupDn(String groupName) {
		return LdapNameBuilder.newInstance().add("ou=标签").add("cn", groupName).build();
	}

	@Override
	public OAResult delete(String id) {
		String description = id;
		// 找出cn，cn表示标签
		LdapQuery query2 = query().base("").attributes("cn", "description").where("objectclass").is("groupOfNames")
				.and("description").is(description);

		List<String> list2 = ldapTemplate.search(query2, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("cn").get();
			}
		});
		String DN = null;
		for (String str : list2) {
			DN = str;
		}
		// 获取标签的cn
		String regex = ",";
		String[] array = DN.split(regex);
		String deleLabel = array[0];

		Name dn = buildGDn(DN);// DN是一串字符串

		// menber属性
		List<Label> mlist = ldapTemplate.search(
				query().where("objectclass").is("groupOfNames").and("description").is(id), new LabelAttributeMapper());

		Label old = mlist.get(0);

		// 修改人员标签属性的名称
		List<String> menbers = old.getMembers();

		boolean flag = false;
		// 删除前的是否最后一个标签判断，只有一个标签则不删。否则删。
		for (int i = 0; i < menbers.size(); i++) {
			Name dn2 = buildGrDn(menbers.get(i));
			Employee emp = (Employee) ldapTemplate.lookup(dn2, new PersonAttributeMapper());
			String oldLabel = emp.getLabel();
			if (oldLabel.equals(deleLabel)) {
				flag = true;
			}
		}
		if (flag == true)
			return OAResult.unOk();// 不符合删除条件则直接退出，什么也不干。

		// ******正式删除
		for (int i = 0; i < menbers.size(); i++) {
			Name dn2 = buildGrDn(menbers.get(i));
			Employee emp = (Employee) ldapTemplate.lookup(dn2, new PersonAttributeMapper());
			String oldLabel = emp.getLabel();
			if (oldLabel.equals(deleLabel)) {
				// 直接删除
				// String newL="null";
				// emp.setLabel(emp.getLabel().replace(oldLabel, newL));

			} else {
				// 只删除那个，别的保留
				String[] array2 = oldLabel.split(regex);
				String newLabel = "";

				for (int j = 0; j < array2.length; j++) {
					if (array2[j].equals(deleLabel)) {
						array2[j] = "";
					}
					newLabel = newLabel.concat(array2[j]);
					if (array2[j].equals("") || j == array2.length - 1 || j == 0)
						newLabel = newLabel.concat("");
					else
						newLabel = newLabel.concat(",");
				}
				emp.setLabel(emp.getLabel().replace(oldLabel, newLabel));
			}

			DirContextOperations context = ldapTemplate.lookupContext(emp.getDn());
			context.setAttributeValue("employeeType", emp.getLabel());
			ldapTemplate.modifyAttributes(context);

		}

		ldapTemplate.unbind(dn);
		return OAResult.ok();
	}

	protected Name buildGrDn(String DN) {
		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();
		String regex = ",";
		String[] array = DN.split(regex);
		for (int i = array.length - 1; i >= 0; i--) {

			ldapNameBuilder.add(array[i]);
		}
		return ldapNameBuilder.build();
	}

	protected Name buildGDn(String cn) {
		return LdapNameBuilder.newInstance().add("ou=标签").add("cn", cn).build();
	}
	/*
	 * public OAResult delete(String id) { List<Label> list =
	 * ldapTemplate.search( query().where("objectclass").is("groupOfNames")
	 * .and("description").is(id), new LabelAttributeMapper()); Label
	 * label=list.get(0); String name=label.getCn(); //修改人员中标签属性的名称 List<String>
	 * members=label.getMembers();//获取标签中的人员路径 for (int i = 0; i <
	 * members.size(); i++) { Name dn=buildDn(members.get(i));//构建DN Employee
	 * emp=ldapTemplate.lookup(dn, new PersonAttributeMapper());//找到人员,找不到会报错
	 * emp.setLabel(emp.getLabel().replace(name+",", "")); DirContextOperations
	 * context = ldapTemplate.lookupContext(emp.getDn());
	 * context.setAttributeValue("employeeType", emp.getLabel());
	 * ldapTemplate.modifyAttributes(context); }
	 * ldapTemplate.unbind("cn="+name+",ou=标签", true);
	 * 
	 * return OAResult.ok(); }
	 */

	@SuppressWarnings("null")
	@Override
	public OAResult deleteMember(String labelId, String id) {
		// TODO Auto-generated method stub

		// **********************************************************
		// 找出了部门
		LdapQuery query2 = query().base("").attributes("cn", "description").where("objectclass").is("groupOfNames")
				.and("description").is(labelId);

		List<String> list2 = ldapTemplate.search(query2, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("cn").get();
			}
		});
		String employoutype = null;
		for (String str : list2) {
			employoutype = str;
		}
		// System.out.println(employoutype+" 部门标签");
		// **************************************

		// 找出了员工部门
		LdapQuery query = query().base("").attributes("ou", "description").where("objectclass").is("person")
				.and("description").is(id);

		List<String> list = ldapTemplate.search(query, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("ou").get();
			}
		});
		String deptName = null;
		for (String str : list) {
			deptName = str;
		}
		// 找出o
		LdapQuery query3 = query().base("").attributes("o", "description").where("objectclass").is("person")
				.and("description").is(id);
		List<String> list3 = ldapTemplate.search(query3, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("o").get();
			}
		});
		String o = null;
		for (String str : list3) {
			o = str;
		}
		// 找员工cn
		LdapQuery query4 = query().base("").attributes("cn", "description").where("objectclass").is("person")
				.and("description").is(id);
		List<String> list4 = ldapTemplate.search(query4, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("cn").get();
			}
		});
		String cn = null;
		for (String str : list4) {
			cn = str;
		}
		Employee p = new Employee();
		p.setO(o);
		p.setFullName(cn);
		p.setOu(deptName);

		Name DN = buildDnEm(p);//

		// Name DN=buildGmDn(o,cn);//构建DN
		Employee emp = ldapTemplate.lookup(DN, new PersonAttributeMapper());// 找到人员,找不到会报错
		String oldLabel = emp.getLabel();
		// System.out.println(emp+" 员工路径");
		// System.out.println(DN+" ****DN");

		// 找出员工标签然后进行和部门标签进行对比。
		String regex = ",";
		String[] array = oldLabel.split(regex);
		String newLabel = "";

		List<String> arrlist = new ArrayList<String>();
		for (int k = 0; k < array.length; k++) {
			arrlist.add(array[k]);
			// System.out.println("进去arrlist"+array[k]);
		}
		if (arrlist.contains(employoutype)) {
			if (array.length == 1) {// 只有一个标签，但是和部门标签不同则查找错误
				// System.out.println("只有一个待删标签");//150,表示必须保留至少一个员工标签，移除员工失败。
				return OAResult.unOk_3();
			} else// 多个标签，删除
			{
				// System.out.println("多个待删标签");
				for (int j = 0; j < array.length; j++) {
					// System.out.println(array[j]+"数组之前");
					if (array[j].equals(employoutype)) {
						array[j] = "";
						// System.out.println(array[j]+"数组");
					}
					// System.out.println("加入newLabel");
					newLabel = newLabel.concat(array[j]);
					// System.out.println("newLabel="+newLabel);
					if (array[j].equals("") || j == array.length - 1 || j == 0)
						newLabel = newLabel.concat("");
					else
						newLabel = newLabel.concat(",");
				}
				// System.out.println("进入修改标签属性。");
				emp.setLabel(emp.getLabel().replace(oldLabel, newLabel));
				DirContextOperations context = ldapTemplate.lookupContext(emp.getDn());
				context.setAttributeValue("employeeType", emp.getLabel());
				ldapTemplate.modifyAttributes(context);

				// 进入部门标签删除属性
				List<Label> menberlist = ldapTemplate.search(
						query().where("objectclass").is("groupOfNames").and("description").is(labelId),
						new LabelAttributeMapper());// 查询到了部门
				Label label = menberlist.get(0);
				String name = label.getCn();
				// 修改人员中标签属性的名称

				Name empCN = DN;
				// System.out.println("员工DN是"+empCN);
				List<String> members = label.getMembers();// 获取标签中的人员路径
				for (int i = 0; i < members.size(); i++) {
					Name menberdn = buildGrDn(members.get(i));// userDn
					if (menberdn.equals(empCN)) {
						// System.out.println("找到了");
						Name groupDn = buildGDn(employoutype);
						Attribute attr = new BasicAttribute("member", menberdn.toString());
						ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr);
						ldapTemplate.modifyAttributes(groupDn, new ModificationItem[] { item });
						// System.out.println("移除函数调用");

						break;// 结束循环退出
					}

				}

			}
		}

		else {
			// System.out.println("不符合删除条件");
			return OAResult.unOk_2();// 151.该员工不属于标签下，请到正确的标签下删除。
		}

		return OAResult.ok();
	}

	protected Name buildGmDn(String DN, String cn) {
		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();
		String regex = ",";
		String[] array = DN.split(regex);
		for (int i = array.length - 1; i >= 0; i--) {
			ldapNameBuilder.add(array[i]);
		}
		ldapNameBuilder.add(cn);
		return ldapNameBuilder.build();
	}

	protected Name buildDnEm(Employee person) {
		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();
		String sDN = person.getO();
		String regex = ",";
		String[] array = sDN.split(regex);
		for (int i = array.length - 1; i >= 0; i--) {
			ldapNameBuilder.add(array[i]);
		}
		// ldapNameBuilder.add("ou",person.getOu());
		ldapNameBuilder.add("cn", person.getFullName());
		return ldapNameBuilder.build();
	}

	@Override
	public OAResult update(String id, String text) {
		List<Label> list = ldapTemplate.search(
				query().where("objectclass").is("groupOfNames").and("description").is(id), new LabelAttributeMapper());
		Label old = list.get(0);
		String oldname = old.getCn();
		// 修改人员中标签属性的名称
		List<String> members = old.getMembers();// 获取标签中的人员路径
		for (int i = 0; i < members.size(); i++) {
			Name dn = buildDn(members.get(i));// 构建DN
			Employee emp = ldapTemplate.lookup(dn, new PersonAttributeMapper());// 找到人员,找不到会报错
			emp.setLabel(emp.getLabel().replace(oldname, text));// 用text取代字符串中的oldname
			DirContextOperations context = ldapTemplate.lookupContext(emp.getDn());
			context.setAttributeValue("employeeType", emp.getLabel());
			ldapTemplate.modifyAttributes(context);
		}

		ldapTemplate.rename("cn=" + oldname + ",ou=标签", "cn=" + text + ",ou=标签");

		return OAResult.ok();
	}

	private Name buildDn(String DN) {// 按照DN构建路径

		// LdapNameBuilder ldapNameBuilder =
		// LdapNameBuilder.newInstance("dc=poke_domain,dc=com");
		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();
		String regex = ",";
		String[] array = DN.split(regex);

		for (int i = array.length - 1; i >= 0; i--) {

			ldapNameBuilder.add(array[i]);
		}
		return ldapNameBuilder.build();
	}

}
