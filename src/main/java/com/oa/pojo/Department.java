package com.oa.pojo;

public class Department {
	private String deptName;//ou
	private Long id;//description
	private Long parentId;//businessCategory
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	@Override
	public String toString() {
		return "Department [deptName=" + deptName + ", id=" + id + ", parentId=" + parentId + "]";
	}
	
	
	
}
