package com.oa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.pojo.EasyUIDataGridResult;
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
		
		return null;
	}
	
	@RequestMapping("/staff/delete")
	@ResponseBody
	public OAResult empDelete(Employee emp){
		
		return null;
	}
	
	@RequestMapping("/staff/query/info")
	@ResponseBody
	public EasyUIDataGridResult getStaffInfoById(String id){
		return employeeService.getEmpInfoById(id);
	}
	

}

