package com.oa.dao.impl;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import com.oa.dao.LabelDao;
import com.oa.mapper.DepartmentAttributeMapper;
import com.oa.mapper.LabelAttributeMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
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
		ldapTemplate.rename("cn="+oldname+",ou=标签","cn="+text+",ou=标签");
		
		return OAResult.ok();
	}

}
