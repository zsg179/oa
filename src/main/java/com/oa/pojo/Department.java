package com.oa.pojo;


public class Department {
	private String deptName;//ou
	private String id;//description:部门编号
	private String parentId;//businessCategory
	private String isParent;//st
	private String o;//l:上级部门
	private String position;//postalCode:职位
	private String isLastDept;//facsimileTelephoneNumber:是否为最后一级部门，1:是，0:不是
	
	
	public String getIsLastDept() {
		return isLastDept;
	}
	public void setIsLastDept(String isLastDept) {
		this.isLastDept = isLastDept;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getO() {
		return o;
	}
	public void setO(String o) {
		this.o = o;
	}
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
				+ ", o=" + o + ", position=" + position + ", isLastDept=" + isLastDept + "]";
	}
	public String getDn() {
		return "ou="+deptName+","+o;
	}
	
	
	
}
/*@Entry(objectClasses = { "organizationalUnit","organization", "top" }, base="dc=poke_domain,dc=com")
public class Department {
	@Id
	private Name dn;
	@Attribute(name="ou")
	@DnAttribute(value="ou",index=1)
	private String deptName;//ou,o
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
*/