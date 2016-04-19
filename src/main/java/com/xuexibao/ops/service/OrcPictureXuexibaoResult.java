package com.xuexibao.ops.service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.xuexibao.ops.model.OrcPictureBatch;

//public class OrcPictureMultThreadService implements Runnable {
public class OrcPictureXuexibaoResult  {

	List<OrcPictureBatch> _picList=null;
	Integer totalCNT=0;
	Integer currentCNT = 0;
	private Lock lock = new ReentrantLock();// 锁对象
	
	public OrcPictureXuexibaoResult(List<OrcPictureBatch> pictureList)
	{
		_picList = pictureList;
		totalCNT = _picList.size();
	}
	
	public OrcPictureBatch getOneOrcPictureBatch()
	{
		OrcPictureBatch orcPicture = null;
        lock.lock();// 得到锁 
        try { 
        	if(currentCNT < totalCNT ){
            	orcPicture = _picList.get(currentCNT);
            	currentCNT++;        		
        	}
        } finally { 
            lock.unlock();// 释放锁 
        } 
	            
        return orcPicture;
	}

}



