package com.oa.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import com.oa.dao.EmployeeDao;
import com.oa.mapper.DepartmentAttributeMapper;
import com.oa.mapper.PersonAttributeMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUIComboboxResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.util.OAResult;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository(value = "employeeDao")
public class EmployeeDaoImpl implements EmployeeDao {
	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public OAResult create(Employee emp) {
		System.out.println(emp);
		emp.setIsParent("0");
		Name dn = buildDn(emp);
		ldapTemplate.bind(dn, null, buildAttributes(emp));
		return OAResult.ok();
	}

	private Attributes buildAttributes(Employee emp) {
		Attributes attrs = new BasicAttributes();
		BasicAttribute ocattr = new BasicAttribute("objectclass");
		ocattr.add("InetOrgPerson");
		ocattr.add("organizationalPerson");
		ocattr.add("person");
		ocattr.add("top");
		attrs.put(ocattr);
		attrs.put("cn", emp.getFullName());
		attrs.put("sn", emp.getLastName());
		// attrs.put("businessCategory", emp.getParentId());
		attrs.put("description", emp.getId());
		attrs.put("employeeType", emp.getLabel());
		attrs.put("mail", emp.getEmail());
		attrs.put("o", emp.getO());
		attrs.put("ou", emp.getOu());
		attrs.put("st", emp.getIsParent());
		attrs.put("telephoneNumber", emp.getPhone());
		attrs.put("title", emp.getTitle());
		return attrs;
	}

	@Override
	public OAResult delete(String ids) {
		// TODO Auto-generated method stub
		// 获取前台ids
		String[] idList = ids.split(",");
		String description = null;
		for (String id : idList) {
			description = id;// 获取到前台的部门编号
		}
		// 找出了部门
		LdapQuery query = query().base("").attributes("ou", "description").where("objectclass").is("person")
				.and("description").is(description);

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
		LdapQuery query2 = query().base("").attributes("o", "description").where("objectclass").is("person")
				.and("description").is(description);
		List<String> list2 = ldapTemplate.search(query2, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("o").get();
			}
		});
		String o = null;
		for (String str : list2) {
			o = str;
		}
		// 找员工cn
		LdapQuery query3 = query().base("").attributes("cn", "description").where("objectclass").is("person")
				.and("description").is(description);
		List<String> list3 = ldapTemplate.search(query3, new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws NamingException {
				return (String) attrs.get("cn").get();
			}
		});
		String cn = null;
		for (String str : list3) {
			cn = str;
		}
		Employee p = new Employee();
		p.setO(o);
		p.setFullName(cn);
		p.setOu(deptName);

		Name dn = buildDnEm(p);//
		ldapTemplate.unbind(dn);

		return OAResult.ok();
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

	protected Name buildDn(Employee emp) {

		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();

		String sDN = emp.getO();

		String regex = ",";
		String[] array = sDN.split(regex);

		for (int i = array.length - 1; i >= 0; i--) {
			ldapNameBuilder.add(array[i]);
		}

		ldapNameBuilder.add("cn", emp.getFullName());

		return ldapNameBuilder.build();
	}

	protected Name buildDn(String DN) {// 按照DN构建路径

		LdapNameBuilder ldapNameBuilder = LdapNameBuilder.newInstance();
		// LdapNameBuilder ldapNameBuilder =
		// LdapNameBuilder.newInstance("dc=poke_domain,dc=com");
		String regex = ",";
		String[] array = DN.split(regex);

		for (int i = array.length - 1; i >= 0; i--) {
			ldapNameBuilder.add(array[i]);
		}
		return ldapNameBuilder.build();
	}

	@Override
	public OAResult edit(Employee emp) {
		//根据前端传的ID号设置要修改的emp
		String number=emp.getOu();
		List<Department> list = ldapTemplate.search(
			      query().where("objectclass").is("organizationalUnit")
			             .and("description").is(number),
			      new DepartmentAttributeMapper());
		Department dept=(Department)list.get(0);
		emp.setO(dept.getDn());//设置o
		emp.setOu(dept.getDeptName());//设置ou
		emp.setIsParent("0");//设置st
		emp.setParentId(number);//设置父节点ID
		
		//获取修改前的节点
		String description=emp.getId();
		List<Employee> listemp = ldapTemplate.search(
			      query().where("objectclass").is("person")
			             .and("description").is(description),
			             new PersonAttributeMapper());
		Employee oldemp=(Employee)listemp.get(0);

		Name olddn = buildDn(oldemp);
    	Name newdn = buildDn(emp);
    	/*
    	String Pid=ldapTemplate.lookup(emp.getO(), new DepartmentAttributeMapper()).getId();
    	emp.setParentId(Pid);
		*/

		DirContextOperations context = ldapTemplate.lookupContext(olddn);
		mapToContext(emp, context);

		// 处理标签
		String regex = ","; 
		String label=oldemp.getLabel(); 
		String[] array1 =label.split(regex); 
		for(int i =0;i<array1.length-1; i++){
			 removeMemberFromGroup(array1[i],oldemp); 
		} 
		label=emp.getLabel();
		String[] array2 = label.split(regex); 
		for(int i =0;i<array2.length-1;i++){ 
			addMemberToGroup(array2[i],emp); 
		}
		 
		ldapTemplate.modifyAttributes(context);
		ldapTemplate.rename(olddn, newdn);

		return OAResult.ok();
	}
	
	private Name buildGroupDn(String groupName) {
		return LdapNameBuilder.newInstance().add("ou=标签").add("cn", groupName).build();
	}

	// 添加人员到标签
	public void addMemberToGroup(String groupName, Employee emp) {
		Name groupDn = buildGroupDn(groupName);
		Name userDn = buildDn(emp);
		DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
		ctx.addAttributeValue("member", userDn);
		ldapTemplate.update(ctx);
	}

	// 从标签移除人员
	public void removeMemberFromGroup(String groupName, Employee emp) {
		Name groupDn = buildGroupDn(groupName);
		Name userDn = buildDn(emp);
		DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
		ctx.removeAttributeValue("member", userDn);
		ldapTemplate.update(ctx);
	}

	
	protected void mapToContext(Employee emp, DirContextOperations context) {

		// context.setAttributeValue("cn", emp.getFullName());
		context.setAttributeValue("sn", emp.getLastName());
		context.setAttributeValue("businessCategory", emp.getParentId());
		context.setAttributeValue("description", emp.getId());
		context.setAttributeValue("telephoneNumber", emp.getPhone());
		context.setAttributeValue("mail", emp.getEmail());
		context.setAttributeValue("employeeType", emp.getLabel());
		context.setAttributeValue("title", emp.getTitle());

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

	@Override
	public List<EasyUIComboboxResult> getCompany() {
		List<Department> list = ldapTemplate.search(query().where("businessCategory").is("0"),
				new DepartmentAttributeMapper());
		List<EasyUIComboboxResult> result = new ArrayList<EasyUIComboboxResult>();
		for (Department dept : list) {
			EasyUIComboboxResult comboboxResult = new EasyUIComboboxResult();
			comboboxResult.setId(dept.getId());
			comboboxResult.setText(dept.getDeptName());
			result.add(comboboxResult);
		}
		return result;
	}

	@Override
	public List<EasyUIComboboxResult> getDept(String parentId) {
		List<EasyUIComboboxResult> result = new ArrayList<>();
		List<EasyUIComboboxResult> lastDept = getLastDept(result, parentId);
		return lastDept;

	}

	public List<EasyUIComboboxResult> getLastDept(List<EasyUIComboboxResult> result, String id) {
		List<Department> list = ldapTemplate.search(query().where("businessCategory").is(id),
				new DepartmentAttributeMapper());
		if (list.size() > 0) {
			for (Department dept : list) {
				if (dept.getIsLastDept().equals("1")) {
					EasyUIComboboxResult comboboxResult = new EasyUIComboboxResult();
					comboboxResult.setId(dept.getId());
					comboboxResult.setText(dept.getDeptName());
					List<Department> list2 = ldapTemplate.search(query().where("description").is(dept.getParentId()),
							new DepartmentAttributeMapper());
					Department parentDept = list2.get(0);
					comboboxResult.setGroup(parentDept.getDeptName());
					result.add(comboboxResult);
				} else if (dept.getIsLastDept().equals("0")) {
					getLastDept(result, dept.getId());
				}
			}
		}
		return result;

	}

	@Override
	public List<EasyUIComboboxResult> getPosition(String id) {
		// 根据id查询部门
		List<Department> list = ldapTemplate.search(query().where("description").is(id),
				new DepartmentAttributeMapper());
		Department dept = list.get(0);
		String positions = dept.getPosition();
		String[] splitPositions = positions.split(";");
		List<EasyUIComboboxResult> result = new ArrayList<>();
		for (String position : splitPositions) {
			EasyUIComboboxResult comboboxResult = new EasyUIComboboxResult();
			// 给每一个职位设置一个随机id，此id仅用于职位下拉列表选择时能选中职位，不加id或相同id无法选中，原因未知。
			comboboxResult.setId(System.currentTimeMillis() + String.format("%02d", new Random().nextInt(99)));
			// 设置职位内容
			comboboxResult.setText(position);
			result.add(comboboxResult);
		}
		return result;

		/*
		 * **********通过读取配置文件获取职位 //根据id查询部门 List<Department> list =
		 * ldapTemplate.search(query().where("description").is(id), new
		 * DepartmentAttributeMapper()); Department dept = list.get(0); String
		 * parentName = dept.getO(); //分解出中文 String[] split =
		 * parentName.split("[^\u4e00-\u9fa5]+"); String key="";
		 * //拼装成properties文件中key的格式 for(int i =split.length-1;i>=1 ; i--){
		 * key=key+split[i]+"_"; } String deptName = dept.getDeptName();
		 * //加上部门名字 key = key+deptName; List<EasyUIComboboxResult> result = new
		 * ArrayList<>(); try { //加载配置文件 Properties properties = new
		 * Properties(); InputStream in =
		 * this.getClass().getClassLoader().getResourceAsStream(
		 * "position.properties"); //properties文件含有中文，字节流无法读取，需转成字符流。
		 * BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		 * properties.load(bf); //根据key取值 String value =
		 * properties.getProperty(key); //#为分隔符分解出每一个职位 String[] positions =
		 * value.split("#"); for (String position : positions) {
		 * EasyUIComboboxResult comboboxResult = new EasyUIComboboxResult();
		 * //给每一个职位设置一个随机id，此id仅用于职位下拉列表选择时能选中职位，不加id或相同id无法选中，原因未知。
		 * comboboxResult.setId(System.currentTimeMillis()+String.format("%02d",
		 * new Random().nextInt(99))); //设置职位内容
		 * comboboxResult.setText(position); result.add(comboboxResult); } }
		 * catch (IOException e) { e.printStackTrace(); } return result;
		 */
	}

	@Override
	public List<EasyUIComboboxResult> getLabel() {
		List<String> list = ldapTemplate.search(query().attributes("cn").where("objectclass").is("groupOfNames"),
				new AttributesMapper<String>() {
					@Override
					public String mapFromAttributes(Attributes attrs) throws NamingException {
						return (String) attrs.get("cn").get();
					}
				});
		List<EasyUIComboboxResult> result = new ArrayList<>();
		for (String label : list) {
			EasyUIComboboxResult comboboxResult = new EasyUIComboboxResult();
			comboboxResult.setId(System.currentTimeMillis() + String.format("%02d", new Random().nextInt(99)));
			comboboxResult.setText(label);
			result.add(comboboxResult);
		}
		return result;
	}

}
