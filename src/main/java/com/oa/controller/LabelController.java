package com.oa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Label;
import com.oa.service.LabelService;
import com.oa.util.OAResult;

/**
 * @author 朱树广
 * @date 2019年8月22日 上午11:08:38
 * @version 1.0
 */
@Controller(value = "labelController")
public class LabelController {
	@Autowired
	private LabelService labelService;

	/**
	 * 查询所有标签
	 * 
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/label/list")
	@ResponseBody
	public List<EasyUITreeNote> getLabelList(@RequestParam(value = "id", defaultValue = "46") String parentId) {
		return labelService.getLabelList(parentId);
	}

	/**
	 * 查询指定标签信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/label/query/info")
	@ResponseBody
	public EasyUIDataGridResult getMembers(String id) {
		return labelService.getMembers(id);
	}

	/**
	 * 删除标签
	 * 
	 * @param id
	 *            标签id
	 * @return
	 */
	@RequestMapping("/label/delete")
	@ResponseBody
	public OAResult delete(String id) {
		return null;
	}

	/**
	 * 新增标签
	 * 
	 * @param label
	 * @return
	 */
	@RequestMapping("/label/save")
	@ResponseBody
	public OAResult create(Label label) {
		return null;
	}

	/**
	 * 添加标签中的人员
	 * 
	 * @param id
	 *            人员id
	 * @param labelId
	 *            标签id
	 * @return
	 */
	@RequestMapping("/label/addMember")
	@ResponseBody
	public OAResult addMember(String labelId, String id) {
		return null;
	}

	/**
	 * 重命名标签
	 * 
	 * @param id
	 *            标签id
	 * @param text
	 *            新标签名
	 * @return
	 */
	@RequestMapping("/rest/label/edit")
	@ResponseBody
	public OAResult update(String id, String name) {
		
		return labelService.update(id, name);
	}

	/**
	 * 删除标签中的人员
	 * 
	 * @param id
	 *            人员id
	 * @param labelId
	 *            标签id
	 * @return
	 */
	@RequestMapping("/label/deleteMember")
	@ResponseBody
	public OAResult deleteMember(String labelId, String id) {
		return null;
	}
}
