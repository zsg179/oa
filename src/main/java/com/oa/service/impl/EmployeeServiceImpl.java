package com.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.dao.EmployeeDao;
import com.oa.pojo.EasyUIComboboxResult;
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
		OAResult result = employeeDao.delete(ids);
		return OAResult.ok();
	}

	@Override
	public OAResult edit(Employee emp) {
		
		return employeeDao.edit(emp);
	}

	@Override
	public EasyUIDataGridResult getEmpInfoById(String id) {
		return employeeDao.getEmpInfoById(id);
	}

	@Override
	public List<EasyUITreeNote> getEmpList(String parentId) {
		return employeeDao.getEmpList(parentId);
	}
	
	
	
	@Override
	public OAResult update(Employee DN,Employee emp) {
		
		return employeeDao.update(DN, emp);
	}

	@Override
	public List<EasyUIComboboxResult> getCompany() {
		return employeeDao.getCompany();
	}

	@Override
	public List<EasyUIComboboxResult> getDept(String parentId) {
		return employeeDao.getDept(parentId);
	}

	@Override
	public List<EasyUIComboboxResult> getPosition(String id) {
		return employeeDao.getPosition(id);
	}

	@Override
	public List<EasyUIComboboxResult> getLabel() {
		return employeeDao.getLabel();
	}

}
