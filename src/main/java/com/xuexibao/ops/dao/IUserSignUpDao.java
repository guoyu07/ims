package com.xuexibao.ops.dao;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.UserSignUp;



public interface IUserSignUpDao extends IEntityDao<UserSignUp> {
	
	@Override
	public UserSignUp insertSelective(UserSignUp userSignUp);
	
}
