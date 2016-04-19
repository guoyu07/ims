package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IOrcPictureRecolistDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.OrcPictureRecolist;


@Repository
public class OrcPictureRecoDaoImpl extends EntityDaoImpl<OrcPictureRecolist> implements IOrcPictureRecolistDao  {

	@Override
	public List<OrcPictureRecolist> getByOrcPictureBatchId(Long orcPictureId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("orcPictureBatchId", orcPictureId);
		List<OrcPictureRecolist> recoList = getSqlSessionTemplate().selectList(getNameSpace() + ".getByOrcPictureBatchId", para);
		return recoList;
	}		
}