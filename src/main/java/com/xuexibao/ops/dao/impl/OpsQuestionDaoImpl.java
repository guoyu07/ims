package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IOpsQuestionDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.OpsQuestion;


@Repository
public class OpsQuestionDaoImpl extends EntityDaoImpl<OpsQuestion> implements IOpsQuestionDao {
	
	
	@Override
	public OpsQuestion getById(Long Id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("Id", Id);
		OpsQuestion opsquestion = getSqlSessionTemplate().selectOne(getNameSpace() + ".getById", para);		
		return opsquestion;
	}
	
}
