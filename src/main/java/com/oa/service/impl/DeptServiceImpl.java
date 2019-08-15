package com.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.dao.DeptDao;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.service.DeptService;
import com.oa.util.OAResult;

/**部门管理服务层实现类 
* @author 朱树广 
* @date 2019年8月5日 上午11:17:22 
* @version 1.0 
*/
@Service(value="deptService")
public class DeptServiceImpl implements DeptService {
	@Autowired
	private DeptDao deptDao;

	@Override
	public List<EasyUITreeNote> getDeptList(String parentId) {
		List<EasyUITreeNote> list = deptDao.getDeptList(parentId);
		return list;
	}

	@Override
	public OAResult create(Department dept) {
		return deptDao.create(dept);
	}

	@Override
	public OAResult delete(String ids) {
		OAResult result = deptDao.delete(ids);
		return result;
	}


	@Override
	public OAResult edit(Department dept) {
		return deptDao.edit(dept);
	}



	@Override
	
	public OAResult update(Department DN,Department OU){
    	 
		OAResult result = deptDao.update(DN, OU);
		return result;
	}

	
	@Override
	public EasyUIDataGridResult geteDeptInfoById(String id) {
		EasyUIDataGridResult result = deptDao.geteDeptInfoById(id);
		List<Department> list = result.getRows();
		for (Department dept : list) {
			if(dept.getO()==null){
				dept.setO("无上级部门");
			}
		}
		result.setRows(list);
		return result;
	}

	@Override
	public OAResult getMaxId() {
		return deptDao.getMaxId();

	}

}

