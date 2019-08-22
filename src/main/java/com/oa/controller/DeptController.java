package com.oa.controller;



import java.util.List;

import javax.naming.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.dao.impl.EmployeeDaoImpl;
import com.oa.mapper.DepartmentAttributeMapper;
import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.service.DeptService;
import com.oa.util.OAResult;

/**
 * @author 朱树广
 * @date 2019年8月5日 上午11:26:43
 * @version 1.0
 */
@Controller(value = "deptController")
public class DeptController {
	@Autowired
	private DeptService deptService;

	@RequestMapping("/department/list")
	@ResponseBody
	public List<EasyUITreeNote> getDeptList(@RequestParam(value = "id", defaultValue = "-1") String parentId) {
		List<EasyUITreeNote> result = deptService.getDeptList(parentId);
		return result;
	}

	@RequestMapping("/department/query/info")
	@ResponseBody
	public EasyUIDataGridResult geteDeptInfoById(@RequestParam(value = "id", defaultValue = "-1") String id) {
		EasyUIDataGridResult result = deptService.geteDeptInfoById(id);
		return result;
	}

	@RequestMapping("/department/delete")
	@ResponseBody

	public OAResult deleteContent(String ids) {
		OAResult result = deptService.delete(ids);

		return result;
	}

	@RequestMapping("/department/gen/id")
	@ResponseBody
	public OAResult genId() {
		OAResult result = deptService.getMaxId();
		return result;
	}

	@RequestMapping("/department/save")
	@ResponseBody
	public OAResult deptCreate(Department dept) {
		return deptService.create(dept);
	}
	
	@RequestMapping("/rest/department/edit")
	@ResponseBody
	public OAResult deptEdit(Department dept){
		return deptService.edit(dept);
	}
	

}
