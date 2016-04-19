package com.xuexibao.ops.service;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.dao.IOrcPictureBatchDao;
import com.xuexibao.ops.dao.IOrcPictureRecolistDao;
import com.xuexibao.ops.enumeration.BatchOrcPictureCheckStatus;
import com.xuexibao.ops.model.OrcPictureBatch;
import com.xuexibao.ops.model.OrcPictureRecolist;
import com.xuexibao.ops.util.RecoJson;

public class OrcPictureMultServiceThread extends Thread{
		 
	private static Logger logger = LoggerFactory.getLogger("machine_recognition_log");
	
    //子线程记数器,记载着运行的线程数
    private CountDownLatch runningThreadNum;
    OrcPictureXuexibaoResult _service;
    String batchId;
    String target;
    IOrcPictureBatchDao orcPictureDao;
    IOrcPictureRecolistDao orcPictureRecolistDao;
    
    OrcPictureMultServiceThread(OrcPictureXuexibaoResult service,CountDownLatch runningThreadNum, String batchId, String target,IOrcPictureBatchDao orcPictureDao, IOrcPictureRecolistDao orcPictureRecolistDao)
    {
        this.runningThreadNum= runningThreadNum;
        this._service = service;
        this.batchId = batchId;
        this.target = target;
        this.orcPictureDao=orcPictureDao;
        this.orcPictureRecolistDao = orcPictureRecolistDao;
    }
     
    @Override
    public void run() 
    {
    	OrcPictureBatch orc = null;
    	do{
    		orc = _service.getOneOrcPictureBatch();
    		try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if(orc != null)
    		{
				JSONObject recogObj = RecoJson.getRecogeResult(orc.getPictureId());
				OrcPictureBatch orcPicture =null;
				
				if(recogObj == null ){
					logger.error("getRecogResultAndSave failed, no data.");
				}else{
					orcPicture = updateOrcPictureData(recogObj, batchId,target,orc.getId());								
				}
				
				if(orcPicture != null){
					orcPictureDao.updateIfNecessary(orcPicture);
				}else{
					orcPicture = new OrcPictureBatch();
					orcPicture.setId(orc.getId());
					orcPicture.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT.getId());	
				}

    		}else{
    			break;
    		}
    	}while(true);
    	
        runningThreadNum.countDown();//正在运行的线程数减一
    }
    
	//update
	private OrcPictureBatch updateOrcPictureData(JSONObject recogObj,String batchId,String target,long id){
		
		OrcPictureBatch orcPicture = new OrcPictureBatch();
		try{
			JSONObject resultObj = recogObj.getJSONObject("result");
			JSONObject answerObj;
			
			if(recogObj.getInteger("status") == 0){
				Integer recoCount = resultObj.getJSONArray("answers").size();
			    answerObj = resultObj.getJSONArray("answers").getJSONObject(0);
			    orcPicture.setId(id);
				orcPicture.setStatus(0);
				orcPicture.setRecolistCount(recoCount);
				orcPicture.setAnswer(answerObj.getString("question_answer"));
				orcPicture.setContent(answerObj.getString("question_body_html"));
				orcPicture.setSolution(answerObj.getString("answer_analysis"));
				orcPicture.setQuestionId(Long.parseLong(answerObj.getString("question_id")));		
				orcPicture.setKnowledge(answerObj.getString("question_tag"));
				orcPicture.setRealSubject(Integer.parseInt(answerObj.getString("subject")));
				orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg"));
				orcPicture.setRawText(recogObj.getString("raw_text"));
				
				for(int i=1; i < recoCount; i++){
					OrcPictureRecolist orcPictureReco = new OrcPictureRecolist();
					
				    answerObj = resultObj.getJSONArray("answers").getJSONObject(i);
				    orcPictureReco.setOrcPictureBatchId(id);
				    orcPictureReco.setRecoIndex(i);
				    orcPictureReco.setAnswer(answerObj.getString("question_answer"));
				    orcPictureReco.setContent(answerObj.getString("question_body_html"));
				    orcPictureReco.setSolution(answerObj.getString("answer_analysis"));
				    orcPictureReco.setQuestionId(Long.parseLong(answerObj.getString("question_id")));		
				    orcPictureReco.setKnowledge(answerObj.getString("question_tag"));
				    orcPictureReco.setRealSubject(Integer.parseInt(answerObj.getString("subject")));
				    orcPictureReco.setRawText(recogObj.getString("raw_text"));
				    orcPictureReco.setCreateTime(new Date());
				    
				    orcPictureRecolistDao.insertSelective(orcPictureReco);
				}
					
			}else{
				
//				if(recogObj.getString("image_url_jpg") == null){
//					return null;
//				}
				
				orcPicture.setId(id);
				orcPicture.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT.getId());
				orcPicture.setRecolistCount(0);
				orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg")); 
			
			}
		}catch(Exception e){
			logger.error("updateOrcPictureData failed,", e);
			return null;
		}
		
		return orcPicture;		
	}		
    
}
