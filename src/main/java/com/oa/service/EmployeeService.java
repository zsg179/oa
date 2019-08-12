package com.oa.service;

import java.util.List;

import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.util.OAResult;

public interface EmployeeService {
	/**
	 * 新增员工
	 * @param emp
	 * @return
	 */
	OAResult create(Employee emp);
	
	/**
	 * 删除员工
	 * @param ids
	 * @return
	 */
	OAResult delete(String ids);
	/**
	 * 编辑员工
	 * @param emp
	 * @return
	 */
	OAResult edit(Employee emp);
	/**
	 * 通过id获取员工信息
	 * @param id
	 * @return
	 */
	EasyUIDataGridResult getEmpInfoById(String id);
	/**
	 * 获取员工列表
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNote> getEmpList(String parentId);
}
