package com.oa.dao;

import java.util.List;

import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.util.OAResult;

public interface DeptDao {
	/**
	 * 获取部门列表
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNote> getDeptList(String parentId);
	/**
	 * 新增部门
	 * @param dept
	 * @return
	 */
	OAResult create(Department dept);
	/**
	 * 删除部门
	 * @param dept
	 * @return
	 */
	OAResult delete(String ids);
	/**
	 * 编辑部门
	 * @param dept
	 * @return
	 */

	OAResult update(String DN,Department OU);

	/**
	 * 通过id获取部门信息
	 * @param id
	 * @return
	 */
	EasyUIDataGridResult geteDeptInfoById(String id);
	/**
	 * 获取最大的编号
	 * @return
	 */
	OAResult getMaxId();
	OAResult edit(Department dept);
}
