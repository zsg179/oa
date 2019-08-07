package com.oa.pojo;

public class Employee {
	private String employeeNumber;
	private String fullName;// cn
	private String lastName;// sn
	private String title;
	private String email;
	private String phone;//telephoneNumber
	private String id;//description
	private String isParent;//st
	private String parentId;//businessCategory
	private String o;//o
	private String ou;//ou
	
	public String getO() {
		return o;
	}
	public void setO(String o) {
		this.o = o;
	}
	public String getOu() {
		return ou;
	}
	public void setOu(String ou) {
		this.ou = ou;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Override
	public String toString() {
		return "Employee [employeeNumber=" + employeeNumber + ", fullName=" + fullName + ", lastName=" + lastName
				+ ", title=" + title + ", email=" + email + ", phone=" + phone + ", id=" + id + ", isParent=" + isParent
				+ ", parentId=" + parentId + ", o=" + o + ", ou=" + ou + "]";
	}
	


	

}
