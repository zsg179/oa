package com.oa.pojo;

public class Employee {
	private String employeeNumber;
	private String fullName;// cn
	private String lastName;// sn
	private String title;
	private String email;
	private String phone;
	private String department;// ou
	private String company;// o
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Employee [employeeNumber=" + employeeNumber + ", fullName=" + fullName + ", lastName=" + lastName
				+ ", title=" + title + ", email=" + email + ", phone=" + phone + ", department=" + department
				+ ", company=" + company + ", description=" + description + "]";
	}

	

}
