package com.xuexibao.ops.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.IUserSignUpDao;
import com.xuexibao.ops.model.UserSignUp;

@Service
public class UserSignUpService {
	
	@Resource
	IUserSignUpDao userSignUpDao;


	public UserSignUp insertSelective(UserSignUp userSignUp) {
	   UserSignUp userSignUps = userSignUpDao.insertSelective(userSignUp);
	   return userSignUps;
   }
   
}