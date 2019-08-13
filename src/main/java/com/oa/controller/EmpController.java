package com.oa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.dao.impl.EmployeeDaoImpl;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Employee;
import com.oa.service.EmployeeService;
import com.oa.util.OAResult;

/** 
* @author 朱树广 
* @date 2019年8月12日 上午10:04:44 
* @version 1.0 
*/
@Controller(value = "empController")
public class EmpController {
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping("/staff/save")
	@ResponseBody
	public OAResult empCreate(Employee emp){
		
		return null;
	}
	
	
	@RequestMapping("/rest/staff/edit")
	@ResponseBody
	public OAResult empEdit(Employee emp){
		
		return employeeService.edit(emp);
	}
	
	@RequestMapping("/staff/delete")
	@ResponseBody
	public OAResult empDelete(String ids){
		OAResult result = employeeService.delete(ids);

		return OAResult.ok();
		
	}
	
	@RequestMapping("/staff/query/info")
	@ResponseBody
	public EasyUIDataGridResult getStaffInfoById(String id){
		return employeeService.getEmpInfoById(id);
	}
	
	@RequestMapping("/staff/list")
	@ResponseBody
	public List<EasyUITreeNote> getDeptList(@RequestParam(value = "id", defaultValue = "-1") String parentId) {
		List<EasyUITreeNote> result = employeeService.getEmpList(parentId);
		return result;
	}
	
	
	String DN="cn=李四,ou=test,o=分部";
	Employee person=new Employee();
	public void W(){
		
		person.setFullName("test");
		person.setLastName("test");
		person.setTitle("test");
		person.setEmail("test@qq.com");
		person.setPhone("1234567890");
		person.setLabel("test");
		person.setParentId("1");
		person.setId("1");
	}
	
	@RequestMapping("/W")
	public OAResult updateContent(){
		W();
		
		return employeeService.update(DN, person);

	}


}

