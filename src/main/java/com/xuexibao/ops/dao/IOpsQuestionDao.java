package com.xuexibao.ops.dao;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.OpsQuestion;

public interface IOpsQuestionDao extends IEntityDao<OpsQuestion> {

	public OpsQuestion getById(Long Id);
}
