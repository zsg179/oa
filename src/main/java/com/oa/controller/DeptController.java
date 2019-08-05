package com.oa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.pojo.EasyUITreeNote;
import com.oa.service.DeptService;

/** 
* @author 朱树广 
* @date 2019年8月5日 上午11:26:43 
* @version 1.0 
*/
@Controller
public class DeptController {
	@Autowired
	private DeptService deptService;
	
	@RequestMapping("/department/list")
	@ResponseBody
	public List<EasyUITreeNote> getDeptList(@RequestParam(value="id",defaultValue="0")  String parentId){
		List<EasyUITreeNote> result = deptService.getDeptList(parentId);
		return result;
	}
}

