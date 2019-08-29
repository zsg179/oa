package com.oa.dao;

/** 
* @author 朱树广 
* @date 2019年8月21日 上午10:02:37 
* @version 1.0 
*/
import java.util.List;

import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Label;
import com.oa.util.OAResult;

public interface LabelDao {
	/**
	 * 获取所有标签
	 * 
	 * @return
	 */
	List<EasyUITreeNote> getLabelList(String parentId);

	/**
	 * 获取指定标签下的全部成员
	 * 
	 * @param id
	 *            标签id
	 * @return
	 */
	EasyUIDataGridResult getMembers(String id);

	/**
	 * 新增标签
	 * 
	 * @param label
	 * @return
	 */
	OAResult create(Label label);

	/**
	 * 新增员工
	 * 
	 * @param id
	 *            员工id
	 * @return
	 */
	OAResult addMember(String id);

	/**
	 * 删除标签
	 * 
	 * @param id
	 *            标签id
	 * @return
	 */
	OAResult delete(String id);

	/**
	 * 删除员工
	 * 
	 * @param id
	 *            员工id
	 * @return
	 */
	OAResult deleteMember(String labelId, String id);

	/**
	 * 重命名标签
	 * 
	 * @param id
	 *            标签id
	 * @param text
	 *            新标签名
	 * @return
	 */
	OAResult update(String id, String text);
}
