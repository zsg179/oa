package com.oa.dao.impl;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
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

	@Override
	public OAResult create(Label label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult addMember(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	  /*public OAResult delete(String id) {
			List<Label> list = ldapTemplate.search(
				      query().where("objectclass").is("groupOfNames")
				             .and("description").is(id),
				      new LabelAttributeMapper());
			Label label=list.get(0);
			String name=label.getCn();
			//修改人员中标签属性的名称
			List<String> members=label.getMembers();//获取标签中的人员路径
			for (int i = 0; i < members.size(); i++) {
				Name dn=buildDn(members.get(i));//构建DN
				Employee emp=ldapTemplate.lookup(dn, new PersonAttributeMapper());//找到人员,找不到会报错
				emp.setLabel(emp.getLabel().replace(name+",", ""));
				DirContextOperations context = ldapTemplate.lookupContext(emp.getDn());
				context.setAttributeValue("employeeType", emp.getLabel());
				ldapTemplate.modifyAttributes(context);
			}
			ldapTemplate.unbind("cn="+name+",ou=标签", true);
			
			return OAResult.ok();
		}*/

	@Override
	public OAResult deleteMember(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult update(String id, String text) {
		List<Label> list = ldapTemplate.search(
			      query().where("objectclass").is("groupOfNames")
			             .and("description").is(id),
			      new LabelAttributeMapper());
		Label old=list.get(0);
		String oldname=old.getCn();
		//修改人员中标签属性的名称
		List<String> members=old.getMembers();//获取标签中的人员路径
		for (int i = 0; i < members.size(); i++) {
			Name dn=buildDn(members.get(i));//构建DN
			Employee emp=ldapTemplate.lookup(dn, new PersonAttributeMapper());//找到人员,找不到会报错
			emp.setLabel(emp.getLabel().replace(oldname, text));//用text取代字符串中的oldname
			DirContextOperations context = ldapTemplate.lookupContext(emp.getDn());
			context.setAttributeValue("employeeType", emp.getLabel());
			ldapTemplate.modifyAttributes(context);
		}
		
		
		ldapTemplate.rename("cn="+oldname+",ou=标签","cn="+text+",ou=标签");
		
		return OAResult.ok();
	}
    private Name buildDn(String DN) {//按照DN构建路径
    	
    	//LdapNameBuilder  ldapNameBuilder = LdapNameBuilder.newInstance("dc=poke_domain,dc=com");
    	LdapNameBuilder  ldapNameBuilder = LdapNameBuilder.newInstance( );
    	String regex = ",";
    	String[] array = DN.split(regex); 
    	
    	for(int i =array.length-1;i>=0 ; i--){
    		ldapNameBuilder.add(array[i]);
        }
        return ldapNameBuilder.build();
	}

  
}
