package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.RecognitionPictureMedium;

public interface IRecognitionPictureMediumDao extends IEntityDao<RecognitionPictureMedium> {
	void clearDatabase();

	List<RecognitionPictureMedium> getAnalysisTable();
}
