package com.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.dao.EmployeeDao;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.service.EmployeeService;
import com.oa.util.OAResult;
@Service(value="employeeService")
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeDao employeeDao;

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
		return employeeDao.getEmpInfoById(id);
	}

	@Override
	public List<EasyUITreeNote> getEmpList(String parentId) {
		return employeeDao.getEmpList(parentId);
	}

}
