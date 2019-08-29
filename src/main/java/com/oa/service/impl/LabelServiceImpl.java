package com.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oa.dao.LabelDao;
import com.oa.pojo.EasyUIDataGridResult;
import com.oa.pojo.EasyUITreeNote;
import com.oa.pojo.Label;
import com.oa.service.LabelService;
import com.oa.util.OAResult;

/** 
* @author 朱树广 
* @date 2019年8月21日 上午10:35:13 
* @version 1.0 
*/
@Service(value="labelService")
public class LabelServiceImpl implements LabelService {
	
	@Autowired
	private LabelDao labelDao;

	@Override
	public List<EasyUITreeNote> getLabelList(String parentId) {
		return labelDao.getLabelList(parentId);
	}

	@Override
	public EasyUIDataGridResult getMembers(String id) {
		return labelDao.getMembers(id);
	}

	@Override
	public OAResult create(Label label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult addMember(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAResult delete(String id) {
		// TODO Auto-generated method stub
		return labelDao.delete(id);
	}

	@Override
	public OAResult  deleteMember(String labelId, String id) {
		// TODO Auto-generated method stub
		return labelDao.deleteMember(labelId, id);
	}

	@Override
	public OAResult update(String id, String text) {
		return labelDao.update(id, text);
	}
}

