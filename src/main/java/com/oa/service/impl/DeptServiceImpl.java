package com.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.oa.dao.DeptDao;
import com.oa.pojo.EasyUITreeNote;
import com.oa.service.DeptService;

/**部门管理服务层实现类 
* @author 朱树广 
* @date 2019年8月5日 上午11:17:22 
* @version 1.0 
*/
public class DeptServiceImpl implements DeptService {
	@Autowired
	private DeptDao deptDao;

	@Override
	public List<EasyUITreeNote> getDeptList(String parentId) {
		List<EasyUITreeNote> list = deptDao.getDeptList(parentId);
		return list;
	}

}

