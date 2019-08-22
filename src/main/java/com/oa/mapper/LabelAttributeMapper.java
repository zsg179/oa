package com.oa.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.oa.pojo.Label;

/**
 * @author 朱树广
 * @date 2019年8月21日 上午10:38:54
 * @version 1.0
 */
public class LabelAttributeMapper implements AttributesMapper<Label> {

	@Override
	public Label mapFromAttributes(Attributes attrs) throws NamingException {
		Label label = new Label();
		List<String> members = new ArrayList<String>();
		if (attrs.get("cn") != null) {
			label.setCn((String) attrs.get("cn").get());
		}
		for (int i = 0; i < attrs.get("member").size(); i++) {
			if (attrs.get("member") != null) {
				members.add((String) attrs.get("member").get(i));
				label.setMembers(members);
			}
		}
		if (attrs.get("description") != null) {
			label.setId((String) attrs.get("description").get());
		}
		if (attrs.get("businessCategory") != null) {
			label.setParentId((String) attrs.get("businessCategory").get());
		}
		if (attrs.get("o") != null) {
			label.setIsParent((String) attrs.get("o").get());
		}
		return label;
	}

}
