package com.oa.service;

import java.util.List;

import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.util.OAResult;

/**部门管理服务层接口 
* @author 朱树广 
* @date 2019年8月5日 上午11:20:38 
* @version 1.0 
*/
public interface DeptService {
	/**获取部门列表 
	* @author 朱树广 
	* @date 2019年8月5日 上午11:20:38 
	* @version 1.0 
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

	OAResult updateOrganization(String DN,Department OU);

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
}

