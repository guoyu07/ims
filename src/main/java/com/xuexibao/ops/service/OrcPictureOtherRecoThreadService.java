package com.xuexibao.ops.service;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.dao.ITranOpsListDao;
import com.xuexibao.ops.enumeration.OrcPictureCheckTarget;
import com.xuexibao.ops.model.TranOpsList;
import com.xuexibao.ops.util.RecoJson;

public class OrcPictureOtherRecoThreadService extends Thread {
	
	private static Logger logger = LoggerFactory.getLogger("machine_recognition_log");
    //子线程记数器,记载着运行的线程数
    private CountDownLatch runningThreadNum;
    String imageId;
    OrcPictureCheckTarget target;
    Long orcPictureId;
    ITranOpsListDao tranOpsListDao;
    
    OrcPictureOtherRecoThreadService(CountDownLatch runningThreadNum, OrcPictureCheckTarget target, String imageId, Long orcPictureId, ITranOpsListDao tranOpsListDao)
    {
        this.runningThreadNum= runningThreadNum;
        this.target = target;
        this.imageId = imageId;
        this.orcPictureId = orcPictureId;
        this.tranOpsListDao=tranOpsListDao;
    }
     
    @Override
    public void run() 
    {
		switch (this.target) {		
		case XUEBAJUN:
			getXuebaReco(this.imageId, orcPictureId);
			break;
		case XIAOYUANSOUTI:
			getXiaoyuanReco(this.imageId, orcPictureId);
			break;
		default:
			break;
		}
        runningThreadNum.countDown();//正在运行的线程数减一
    }
    

	private void getXuebaReco(String imageId, Long orcPictureId) {
		try{
			TranOpsList othertranops=new TranOpsList();
			String resultStr = RecoJson.getRecogeResult(imageId,OrcPictureCheckTarget.XUEBAJUN, 1);
			
			if(resultStr != null){
				JSONObject innerJson = JSON.parseObject(resultStr);
				if(innerJson.getInteger("status") == 0 ){
					JSONArray recoJson = innerJson.getJSONArray("result");
					if( recoJson != null && recoJson.size()>0){
						JSONObject answerObj = recoJson.getJSONObject(0);
						String value = answerObj.getString("answer");
						value=value.replaceAll("<link rel=\"stylesheet\" type=\"text/css\" href=\"(http://|https://)((\\w|=|\\?|\\.|/|&|-)+)\">", "");
						value = RecoJson.repalceURLXueba(value);		
						othertranops.setAnswer(value);
						value = answerObj.getString("stem_html");
						value = RecoJson.repalceURLXueba(value);
						value = value.replaceAll("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://bbl.xueba100.com/app/1.1.4/css/base.min.css\">", "");
						othertranops.setContent(value);
						othertranops.setTarget(OrcPictureCheckTarget.XUEBAJUN.getId());
						othertranops.setOrcPictureId(orcPictureId);
						
						tranOpsListDao.insertSelective(othertranops);
					}else{
						logger.warn("【学霸君】识别无结果  [imageId]=" + imageId + ";[orcPictureId]=" + orcPictureId);
					}
				}else{
					logger.warn("【学霸君】识别无结果  [imageId]=" + imageId + ";[orcPictureId]=" + orcPictureId + ";[msg]=" + innerJson.getString("msg"));
				}
			}
		}catch(SocketTimeoutException e){
			logger.warn("取得【学霸君】识别结果超时:"+e.getMessage());
		}catch(SocketException e){
			logger.warn("取得【学霸君】识别结果socket错误:"+e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void getXiaoyuanReco(String imageId, Long orcPictureId) {
		try{
			TranOpsList othertranops=new TranOpsList();
			String resultStr = RecoJson.getRecogeResult(imageId,OrcPictureCheckTarget.XIAOYUANSOUTI, 1);
			
			if(resultStr != null){
				JSONObject innerJson = JSON.parseObject(resultStr);			
				if(innerJson.getInteger("status") == 0) {
					JSONObject resObj = innerJson.getJSONObject("result");
					if( resObj.getInteger("failedResult") == 0 && resObj.getJSONObject("question") != null ){
					    JSONObject answerObj = resObj.getJSONObject("question");
						String value = answerObj.getString("answer");
						value = RecoJson.repalceURL(value);		
						othertranops.setAnswer(value);
						value = answerObj.getString("content");
						value = RecoJson.repalceURL(value);			
						othertranops.setContent(value);
						value = answerObj.getString("solution");
						value = RecoJson.repalceURL(value);		
						othertranops.setSolution(value);
						othertranops.setTarget(OrcPictureCheckTarget.XIAOYUANSOUTI.getId());
						othertranops.setOrcPictureId(orcPictureId);
						
						tranOpsListDao.insertSelective(othertranops);
					}else{
						logger.warn("【猿题库】识别无结果  [imageId]=" + imageId + ";[orcPictureId]=" + orcPictureId);
					}				
				}else{
					logger.warn("【猿题库】识别无结果  [imageId]=" + imageId + ";[orcPictureId]=" + orcPictureId+ ";[msg]=" + innerJson.getString("msg"));
				}
			}
			
		}catch(SocketTimeoutException e){
			logger.warn("取得【猿题库】识别结果超时:"+e.getMessage());
		}catch(SocketException e){
			logger.warn("取得【猿题库】识别结果socket错误:"+e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
	}    
}
