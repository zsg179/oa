package com.oa.pojo;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 朱树广
 * @date 2019年8月20日 下午3:21:15
 * @version 1.0
 */
public class Label {
	private String cn;//标签名
	private List<String> members=new ArrayList<String>();//内含成员
	private String id;//description
	private String parentId;//businessCategory
	private String isParent;//o

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public void addMember(String newMember) {
		members.add(newMember);
	}

	public void removeMember(String member) {
		members.remove(member);
	}

	@Override
	public String toString() {
		return "Label [cn=" + cn + ", members=" + members + ", id=" + id + ", parentId=" + parentId + ", isParent="
				+ isParent + "]";
	}

	

	
	

}
