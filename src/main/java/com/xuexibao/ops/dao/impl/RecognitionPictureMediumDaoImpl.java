package com.xuexibao.ops.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IRecognitionPictureMediumDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.RecognitionPictureMedium;

@Repository
public class RecognitionPictureMediumDaoImpl extends EntityDaoImpl<RecognitionPictureMedium> implements IRecognitionPictureMediumDao {

	@Override
	public void clearDatabase() {
		getSqlSessionTemplate().delete(getNameSpace() + ".deleteAllData");
	}

	@Override
	public List<RecognitionPictureMedium> getAnalysisTable() {
		return getSqlSessionTemplate().selectList(getNameSpace() + ".getAnalysisTable");
	}

}
