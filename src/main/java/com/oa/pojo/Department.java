package com.oa.pojo;

public class Department {
	private String deptName;//ou
	private String id;//description
	private String parentId;//businessCategory
	private String isParent;//st
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
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
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	@Override
	public String toString() {
		return "Department [deptName=" + deptName + ", id=" + id + ", parentId=" + parentId + ", isParent=" + isParent
				+ "]";
	}
	
}
