package com.oa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.pojo.Department;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
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
	public EasyUIDataGridResult geteDeptInfoById(String id) {
		EasyUIDataGridResult result = deptService.geteDeptInfoById(id);
		return result;
	}

	@RequestMapping("/department/delete")
	@ResponseBody

	public OAResult deleteContent(String ids){
		OAResult result = deptService.delete(ids);

		return result;
	}



	@RequestMapping("/department/gen/id")
	@ResponseBody
	public OAResult genId() {
		OAResult result = deptService.getMaxId();
		return result;
	}

	
	//------------------修改部门test-start-----------
	  String DN="ou=市场部,o=分部";
	//将市场部的属性l的值变成“托尔斯泰”	
		
		private Department getDept() {
			Department ou=new Department();
			
			
			ou.setDeptName("托尔斯泰");
			
		    return ou;
		}
		
		@RequestMapping("/tes")//网站访问后会报404，但是在ldap已经修改了
		public void updateTest(){
			
			Department ou=getDept();
			
			deptService.updateOrganization(DN,ou);
		}
		
		//------------------修改部门test-end-----------


}
