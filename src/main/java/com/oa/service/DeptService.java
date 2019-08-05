package com.oa.service;

import java.util.List;

import com.oa.pojo.EasyUITreeNote;

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
}

