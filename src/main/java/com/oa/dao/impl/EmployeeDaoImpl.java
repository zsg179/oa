package com.oa.dao.impl;

import org.springframework.stereotype.Repository;

import com.oa.dao.EmployeeDao;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.Employee;
import com.oa.util.OAResult;
@Repository(value = "employeeDao")
public class EmployeeDaoImpl implements EmployeeDao {

	@Override
	public OAResult create(Employee emp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult delete(String ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult edit(Employee emp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EasyUIDataGridResult getEmpInfoById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
