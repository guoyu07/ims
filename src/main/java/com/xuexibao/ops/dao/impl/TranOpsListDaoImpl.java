package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.ITranOpsListDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.TranOpsList;

@Repository
public class TranOpsListDaoImpl extends EntityDaoImpl<TranOpsList> implements ITranOpsListDao  {

	
	@Override
	public List<TranOpsList> getByPictureId(Long pictureId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("orcPictureId", pictureId);
		List<TranOpsList> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getByPictureId", para);
		return results;
	}
	
	
}
