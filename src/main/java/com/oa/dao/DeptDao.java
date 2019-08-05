package com.oa.dao;

import java.util.List;

import com.oa.pojo.EasyUITreeNote;

public interface DeptDao {
	/**
	 * 获取部门列表
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNote> getDeptList(String parentId);
}
