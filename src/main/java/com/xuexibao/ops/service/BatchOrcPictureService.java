package com.xuexibao.ops.service;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.dao.IOrcPictureBatchDao;
import com.xuexibao.ops.dao.IOrcPictureRecolistDao;
import com.xuexibao.ops.dao.ITranOpsDao;
import com.xuexibao.ops.enumeration.BatchOrcPictureCheckStatus;
import com.xuexibao.ops.enumeration.EnumSubject;
import com.xuexibao.ops.enumeration.OrcPictureCheckTarget;
import com.xuexibao.ops.model.OrcPictureBatch;
import com.xuexibao.ops.model.OrcPictureRecolist;
import com.xuexibao.ops.model.OrcPictureResultCount;
import com.xuexibao.ops.util.DateUtils;
import com.xuexibao.ops.util.RecoJson;

@Service
public class BatchOrcPictureService {

	@Resource
	IOrcPictureBatchDao orcPictureDao;

	@Resource
	IOrcPictureRecolistDao orcPictureRecolistDao;

	private static Logger logger = LoggerFactory
			.getLogger("machine_recognition_log");

	@Resource
	ITranOpsDao tranOpsDao;

	public OrcPictureBatch getOrcPictureById(Long pictureId) {
		OrcPictureBatch orcPicture = orcPictureDao.getById(pictureId);
		if (orcPicture != null) {
			setSubject(orcPicture);
			setStatus(orcPicture);
			String knowledge = orcPicture.getKnowledge();
			if (StringUtils.isNotEmpty(knowledge)) {
				orcPicture.setKnowledgeArray(knowledge.split(","));
			}
		}
		return orcPicture;
	}

	public OrcPictureBatch getNextPicture(String target, Long pictureId,
			String operatorName, Integer status, Date startDate, Date endDate,
			String batchId) {
		OrcPictureBatch orcPicture = orcPictureDao.getNextById(target,
				pictureId, operatorName, status, startDate, endDate, batchId);
		if (orcPicture != null) {
			setSubject(orcPicture);
			setStatus(orcPicture);
			String knowledge = orcPicture.getKnowledge();
			if (StringUtils.isNotEmpty(knowledge)) {
				orcPicture.setKnowledgeArray(knowledge.split(","));
			}
		}
		return orcPicture;
	}

	public OrcPictureBatch getLastPicture(String target, Long pictureId,
			String operatorName, Integer status, Date startDate, Date endDate,
			String batchId) {
		OrcPictureBatch orcPicture = orcPictureDao.getLastById(target,
				pictureId, operatorName, status, startDate, endDate, batchId);
		if (orcPicture != null) {
			setSubject(orcPicture);
			setStatus(orcPicture);
			String knowledge = orcPicture.getKnowledge();
			if (StringUtils.isNotEmpty(knowledge)) {
				orcPicture.setKnowledgeArray(knowledge.split(","));
			}
		}
		return orcPicture;
	}

	public boolean editOrcPicture(Long id, String approvor,
			Integer operatorTeamId, Integer status, String info) {
		return updateOrcPicture(id, approvor, operatorTeamId, status, info);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	private synchronized boolean updateOrcPicture(Long id, String operatorName,
			Integer operatorTeamId, Integer status, String info) {
		// 通过id查询tranOps，不存在则退出此次循环
		final OrcPictureBatch orcPicture = orcPictureDao.getById(id);
		if (orcPicture == null) {
			return false;
		}
		// final TranOps tranOpsN = tranOpsDao.getByPictureId(id);
		// if( tranOpsN != null)
		// {
		// return false;
		// }
		// //如果状态是识别错误需要人工录入，则插入识别的修正结果
		// if(status == OrcPictureCheckStatusConstant.ERROR_RECORD && status ==
		// OrcPictureCheckStatusConstant.USOLVE_ERROR){
		// if(tranOps != null){
		// //插入试题列表tranOps
		// tranOpsDao.insertSelective(tranOps);
		// }else{
		// return false;
		// }
		// }
		// 更新OrcPicture
		orcPicture.setStatus(status);
		orcPicture.setOperatorName(operatorName);
		orcPicture.setOperatorTeamId(operatorTeamId);
		orcPicture.setApproveTime(new Date());
		orcPicture.setRecoAdditionalInfo(info);
		orcPictureDao.updateIfNecessary(orcPicture);
		return true;
	}

	public long searchCount(String target, String pictureId, String userKey,
			Integer status, Date startDate, Date endDate, String fileName) {
		return orcPictureDao.searchCount(target, pictureId, userKey, status,
				startDate, endDate, fileName);
	}

	public List<OrcPictureBatch> searchList(String target, String pictureId,
			String userKey, Integer status, Date startDate, Date endDate,
			Long page, Long limit, String fileName) {
		List<OrcPictureBatch> orcPictureList = orcPictureDao.searchList(target,
				pictureId, userKey, status, startDate, endDate, page, limit,
				fileName);
		for (OrcPictureBatch orcPicture : orcPictureList) {
			// setSubject(orcPicture);
			setStatus(orcPicture);
		}
		return orcPictureList;
	}

	private void setStatus(OrcPictureBatch picture) {
		Integer status = picture.getStatus();
		if (status != null) {
			for (BatchOrcPictureCheckStatus pictureStatus : BatchOrcPictureCheckStatus
					.values()) {
				if (status.equals(pictureStatus.getId())) {
					picture.setStatusStr(pictureStatus.getDesc());
					break;
				}
			}
		}

		Integer target = Integer.parseInt(picture.getTarget());
		if (target != null) {
			for (OrcPictureCheckTarget pictureTarget : OrcPictureCheckTarget
					.values()) {
				if (target.equals(pictureTarget.getId())) {
					picture.setTargetStr(pictureTarget.getDesc());
					break;
				}
			}
		}
	}

	private void setSubject(OrcPictureBatch picture) {
		Integer subject = picture.getRealSubject();
		if (subject != null) {
			for (EnumSubject enumsubject : EnumSubject.values()) {
				if (subject.equals(enumsubject.getId())) {
					picture.setSubject(enumsubject.getChineseName());
					break;
				}
			}
		}
	}

	public int getRecogResultAndSave(String image_id, String userKey,
			String batchId, String target) {
		int ret = 0;
		try {

			OrcPictureBatch orcPicture = null;

			JSONObject recogObj = RecoJson.getRecogeResult(image_id);

			if (recogObj == null) {
				logger.error("getRecogResultAndSave failed, no data.");
				return -1;
			} else if (recogObj.getInteger("status") != 0) {
				orcPicture = setNoresOrcPicture(recogObj, userKey, batchId,
						target);
			} else {
				orcPicture = newOrcPictureData(recogObj, userKey, batchId,
						target);
			}

			if (orcPicture != null) {
				orcPictureDao.insertSelective(orcPicture);
			}

		} catch (Exception e) {
			logger.error("getRecogResultAndSave failed,", e);
			return -1;
		}

		return ret;
	}

	@SuppressWarnings("static-access")
	public int getRecogResultByBatchIdOnly(String batchId, String target) {
		int ret = 0;
		try {
			List<OrcPictureBatch> orcPictureList = orcPictureDao
					.getPictureIdsByBatchId(batchId, Integer
							.toString(BatchOrcPictureCheckStatus.URECO.getId()));

			if (StringUtils.isEmpty(target)) {
				target = orcPictureList.get(0).getTarget();
			}

			if (orcPictureList != null) {

				switch (OrcPictureCheckTarget.getEnum(Integer.parseInt(target))) {
				case XUEXIBAO:
					final OrcPictureXuexibaoResult lockThread = new OrcPictureXuexibaoResult(
							orcPictureList);

					int threadNum = (orcPictureList.size() + 4) / 5; // 线程数
					// 定义正在运行的线程数
					CountDownLatch runningThreadNum = new CountDownLatch(
							threadNum);
					// 创建多个子线程
					for (int i = 0; i < threadNum; i++) {
						new OrcPictureMultServiceThread(lockThread,
								runningThreadNum, batchId, "0", orcPictureDao,
								orcPictureRecolistDao).start();
					}
					// 等待子线程都执行完了再执行主线程剩下的动作
					runningThreadNum.await();
					break;
				case ZUOYEBANG:
				case XUEBAJUN:
				case XIAOYUANSOUTI: {
					boolean sleep_flag = false;
					OrcPictureBatch orcPicture = null;
					for (OrcPictureBatch orc : orcPictureList) {

						// 第一张图片不等待
						if (sleep_flag) {
							Random r = new Random();
							int rand = r.nextInt(10);
							Long sleepTime = 1000 * 30 + 1000 * rand * 1L;
							Thread.currentThread().sleep(sleepTime);
						}else{
							sleep_flag = true;
						}

						if("2".equals(target)){
							try{
								String recoStr = RecoJson.getRecogeResult(orc.getPictureId(),OrcPictureCheckTarget.XIAOYUANSOUTI, 0);
								if (recoStr != null) {
									JSONObject recogObjYuantiku = JSON.parseObject(recoStr);
									orcPicture = updateOrcPictureDataFromYuantiku(recogObjYuantiku, batchId, target,orc.getId());
								}								
							}catch(SocketTimeoutException e){
								logger.warn("取得【猿题库】识别结果超时.");
							}catch(SocketException e){
								logger.warn("取得【猿题库】识别结果socket错误:"+e.getMessage());
							}catch(Exception e){
								logger.warn("取得【猿题库】识别结果失败.");
							}
						}else if("1".equals(target)){
							try{
								String recoStr = RecoJson.getRecogeResult(orc.getPictureId(),OrcPictureCheckTarget.XUEBAJUN, 0);
								if (recoStr != null) {
									JSONObject recogObjXueba = JSON.parseObject(recoStr);
									orcPicture = updateOrcPictureDataFromXueba(recogObjXueba, batchId, target, orc.getId());
								}									
							}catch(SocketTimeoutException e){
								logger.warn("取得【学霸君】识别结果超时.");
							}catch(SocketException e){
								logger.warn("取得【学霸君】识别结果socket错误:"+e.getMessage());
							}catch(Exception e){
								logger.warn("取得【学霸君】识别结果失败.");
							}
						}

						// 当返回无结果，设定识别
						if (orcPicture == null) {
							orcPicture = new OrcPictureBatch();
							orcPicture.setId(orc.getId());
							orcPicture.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT.getId());
						}
						orcPictureDao.updateIfNecessary(orcPicture);
					}
					break;
				}
				default:
					break;
				}

			}
		} catch (Exception e) {
			logger.error("getRecogResultByBatchIdOnly failed,", e);
			return -1;
		}

		return ret;
	}

	public int getRecogResultByBatchIdOnly(String batchId) {
		int ret = 0;
		try {
			List<OrcPictureBatch> orcPictureList = orcPictureDao
					.getPictureIdsByBatchId(batchId, Integer
							.toString(BatchOrcPictureCheckStatus.URECO.getId()));
			OrcPictureBatch orcPicture = null;
			String target = null;

			if (orcPictureList != null) {

				for (OrcPictureBatch orc : orcPictureList) {

					target = orc.getTarget();
					switch (OrcPictureCheckTarget.getEnum(Integer
							.parseInt(target))) {
					case XUEXIBAO:
						orcPicture = null;
						JSONObject recogObj = RecoJson.getRecogeResult(orc.getPictureId());

						if (recogObj != null) {
							orcPicture = updateOrcPictureData(recogObj, batchId, target, orc.getId());
						}

						// 当返回无结果，设定
						if (orcPicture == null) {
							orcPicture = new OrcPictureBatch();
							orcPicture.setId(orc.getId());
							orcPicture
									.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT
											.getId());
						}
						orcPictureDao.updateIfNecessary(orcPicture);
						break;

					case XIAOYUANSOUTI: {
						orcPicture = null;
						try{
							String recoStr = RecoJson.getRecogeResult(orc.getPictureId(),OrcPictureCheckTarget.XIAOYUANSOUTI, 0);
							if (recoStr != null) {
								JSONObject recogObjYuantiku = JSON.parseObject(recoStr);
								orcPicture = updateOrcPictureDataFromYuantiku(recogObjYuantiku, batchId, target, orc.getId());
							}
						}catch(SocketTimeoutException e){
							logger.warn("取得【猿题库】识别结果超时:"+e.getMessage());
						}catch(SocketException e){
							logger.warn("取得【猿题库】识别结果socket错误:"+e.getMessage());
						}catch(Exception e){
							logger.warn("取得【猿题库】识别结果失败:"+e.getMessage());
						}

						// 当返回无结果，设定识别
						if (orcPicture == null) {
							orcPicture = new OrcPictureBatch();
							orcPicture.setId(orc.getId());
							orcPicture.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT.getId());
						}
						orcPictureDao.updateIfNecessary(orcPicture);
						break;
					}
					case XUEBAJUN: {
						orcPicture = null;
						try{
							String recoStr = RecoJson.getRecogeResult(orc.getPictureId(),OrcPictureCheckTarget.XUEBAJUN, 0);
							if (recoStr != null) {
								JSONObject recogObjXueba = JSON.parseObject(recoStr);
								orcPicture = updateOrcPictureDataFromXueba(recogObjXueba, batchId, target, orc.getId());
							}
						}catch(SocketTimeoutException e){
							logger.warn("取得【学霸君】识别结果超时:"+e.getMessage());
						}catch(SocketException e){
							logger.warn("取得【学霸君】识别结果socket错误:"+e.getMessage());
						}catch(Exception e){
							logger.warn("取得【学霸君】识别结果失败:"+e.getMessage());
						}

						// 当返回无结果，设定识别
						if (orcPicture == null) {
							orcPicture = new OrcPictureBatch();
							orcPicture.setId(orc.getId());
							orcPicture.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT.getId());
						}
						orcPictureDao.updateIfNecessary(orcPicture);
						break;
					}

					case ZUOYEBANG:
						break;

					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error("getRecogResultAndSave failed,", e);
			return -1;
		}

		return ret;
	}

	private OrcPictureBatch setNoresOrcPicture(JSONObject recogObj,
			String userKey, String batchId, String target) {

		if (recogObj.getString("image_url_jpg") == null) {
			return null;
		}

		OrcPictureBatch orcPicture = new OrcPictureBatch();
		orcPicture.setStatus(4);
		orcPicture.setBatchId(batchId);
		orcPicture.setUserKey(userKey);
		orcPicture.setCreateTime(new Date());
		orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg")); // for
																			// test,"http://xfsr.91xuexibao.com/7,13c8959e91c96e";
		orcPicture.setTarget(target);
		return orcPicture;
	}


	private OrcPictureBatch newOrcPictureData(JSONObject recogObj,
			String userKey, String batchId, String target) {
		OrcPictureBatch orcPicture = new OrcPictureBatch();
		JSONObject resultObj = recogObj.getJSONObject("result");

		JSONObject answerObj = resultObj.getJSONArray("answers").getJSONObject(
				0);
		orcPicture.setStatus(0);
		orcPicture.setUserKey(userKey);
		orcPicture.setCreateTime(new Date());
		orcPicture.setAnswer(answerObj.getString("question_answer"));
		orcPicture.setContent(answerObj.getString("question_body_html"));
		orcPicture.setSolution(answerObj.getString("answer_analysis"));
		orcPicture.setQuestionId(Long.parseLong(answerObj
				.getString("question_id")));
		orcPicture.setKnowledge(answerObj.getString("question_tag"));
		orcPicture.setRealSubject(Integer.parseInt(answerObj
				.getString("subject")));

		orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg"));
		orcPicture.setBatchId(batchId);
		orcPicture.setTarget(target);

		orcPicture.setRawText(recogObj.getString("raw_text"));

		return orcPicture;
	}

	// update
	private OrcPictureBatch updateOrcPictureData(JSONObject recogObj,
			String batchId, String target, long id) {

		OrcPictureBatch orcPicture = new OrcPictureBatch();
		JSONObject resultObj = recogObj.getJSONObject("result");

		if (recogObj.getInteger("status") == 0) {
			Integer recoCount = resultObj.getJSONArray("answers").size();
			JSONObject answerObj = resultObj.getJSONArray("answers")
					.getJSONObject(0);
			orcPicture.setId(id);
			orcPicture.setStatus(0);
			orcPicture.setRecolistCount(recoCount);
			orcPicture.setAnswer(answerObj.getString("question_answer"));
			orcPicture.setContent(answerObj.getString("question_body_html"));
			orcPicture.setSolution(answerObj.getString("answer_analysis"));
			orcPicture.setQuestionId(Long.parseLong(answerObj
					.getString("question_id")));
			orcPicture.setKnowledge(answerObj.getString("question_tag"));
			orcPicture.setRealSubject(Integer.parseInt(answerObj
					.getString("subject")));
			orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg"));
			orcPicture.setRawText(recogObj.getString("raw_text"));

			for (int i = 1; i < recoCount; i++) {
				OrcPictureRecolist orcPictureReco = new OrcPictureRecolist();

				answerObj = resultObj.getJSONArray("answers").getJSONObject(i);
				orcPictureReco.setOrcPictureBatchId(id);
				orcPictureReco.setRecoIndex(i);
				orcPictureReco
						.setAnswer(answerObj.getString("question_answer"));
				orcPictureReco.setContent(answerObj
						.getString("question_body_html"));
				orcPictureReco.setSolution(answerObj
						.getString("answer_analysis"));
				orcPictureReco.setQuestionId(Long.parseLong(answerObj
						.getString("question_id")));
				orcPictureReco
						.setKnowledge(answerObj.getString("question_tag"));
				orcPictureReco.setRealSubject(Integer.parseInt(answerObj
						.getString("subject")));
				orcPictureReco.setRawText(recogObj.getString("raw_text"));
				orcPictureReco.setCreateTime(new Date());

				orcPictureRecolistDao.insertSelective(orcPictureReco);
			}
		} else {

			// if(recogObj.getString("image_url_jpg") == null){
			// return null;
			// }

			orcPicture.setId(id);
			orcPicture.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT
					.getId());
			orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg"));

		}

		return orcPicture;
	}

	// update
	private OrcPictureBatch updateOrcPictureDataFromYuantiku(
			JSONObject recogObjInner, String batchId, String target, long id) {

		OrcPictureBatch orcPicture = new OrcPictureBatch();
		String value = "";
		try {
			//初始设为失败，满足条件后置为成功
			orcPicture.setId(id);
			orcPicture.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT.getId());			
			if (recogObjInner.getInteger("status") == 0 ) {
				JSONObject recogObj = recogObjInner.getJSONObject("result");
				if( recogObj.getInteger("failedResult") == 0) {
					JSONObject answerObj = recogObj.getJSONObject("question");// .getJSONObject(0);
					orcPicture.setId(id);
					orcPicture.setStatus(0);
	
					value = answerObj.getString("answer");
					value = RecoJson.repalceURL(value);
					orcPicture.setAnswer(value);
	
					value = answerObj.getString("content");
					value = RecoJson.repalceURL(value);
					orcPicture.setContent(value);
	
					value = answerObj.getString("solution");
					value = RecoJson.repalceURL(value);
					orcPicture.setSolution(value);
	
					orcPicture.setQuestionId(Long.parseLong(answerObj.getString("id")));
				}
			}else{
				logger.warn("【学霸君】识别无结果  [batchId]=" + batchId + ";[id]=" + id + ";[msg]=" + recogObjInner.getString("msg"));
			}
		} catch (Exception e) {
			logger.error("updateOrcPictureDataFromYuantiku failed,", e);
			return null;
		}
		return orcPicture;
	}

	// update
	private OrcPictureBatch updateOrcPictureDataFromXueba(JSONObject recogObjInner,
			String batchId, String target, long id) {

		OrcPictureBatch orcPicture = new OrcPictureBatch();
		String value = "";
		try {
			//初始设为失败，满足条件后置为成功
			orcPicture.setId(id);
			orcPicture.setStatus(BatchOrcPictureCheckStatus.ERROR_RECO_NORESULT.getId());					
			if (recogObjInner.getInteger("status") == 0) {
				JSONArray recogObj = recogObjInner.getJSONArray("result");
				if( recogObj.size() > 0) {
					JSONObject answerObj = recogObj.getJSONObject(0);
					orcPicture.setId(id);
					orcPicture.setStatus(0);
					orcPicture.setRecolistCount(recogObj.size());
	
					value = answerObj.getString("answer");
					value = RecoJson.repalceURLXueba(value);
					orcPicture.setAnswer(value);
	
					value = answerObj.getString("stem_html");
					value = RecoJson.repalceURLXueba(value);
					orcPicture.setContent(value);
	
					orcPicture.setQuestionId(Long.parseLong(answerObj
							.getString("aid")));
	
					for (int i = 1; i < recogObj.size(); i++) {
						OrcPictureRecolist orcPictureReco = new OrcPictureRecolist();
	
						answerObj = recogObj.getJSONObject(i);
						orcPictureReco.setOrcPictureBatchId(id);
						orcPictureReco.setRecoIndex(i);
	
						value = answerObj.getString("answer");
						value = RecoJson.repalceURLXueba(value);
						orcPictureReco.setAnswer(value);
	
						value = answerObj.getString("stem_html");
						value = RecoJson.repalceURLXueba(value);
						orcPictureReco.setContent(value);
	
						orcPicture.setQuestionId(Long.parseLong(answerObj
								.getString("aid")));
						orcPictureReco.setCreateTime(new Date());
	
						orcPictureRecolistDao.insertSelective(orcPictureReco);
					}
				}
			}else{
				logger.warn("【学霸君】识别无结果  [batchId]=" + batchId + ";[id]=" + id + ";[msg]=" + recogObjInner.getString("msg"));
			}
		} catch (Exception e) {
			logger.error("updateOrcPictureDataFromXueba failed,", e);
			return null;
		}
		return orcPicture;
	}



	public long getHasAnswerCount(Long questionId) {
		return orcPictureDao.getHasAnswerCount(questionId);
	}

	public List<OrcPictureBatch> searchHasAnswerList(Long questionId) {
		List<OrcPictureBatch> orcPictureList = orcPictureDao
				.searchHasAnswerList(questionId);
		for (OrcPictureBatch orcPicture : orcPictureList) {
			setStatus(orcPicture);

		}
		return orcPictureList;
	}

	public List<OrcPictureBatch> computeRecPercent(String target,
			String batchId, String userKey, Integer status, Date startDate,
			Date endDate) {
		List<OrcPictureBatch> counts = orcPictureDao.computeRecPercent(target,
				batchId, userKey, status, startDate, endDate);
		return counts;
	}

	public OrcPictureBatch setUploadInfo(String target, String userKey,
			String batchId, String downloadUrl, String pictureId, String fileName,
			BatchOrcPictureCheckStatus status) {
		try {
			OrcPictureBatch orcPicture = new OrcPictureBatch();
			orcPicture.setTarget(target);
			orcPicture.setUserKey(userKey);
			orcPicture.setBatchId(batchId);
			orcPicture.setOrcPictureUrl(downloadUrl);
			orcPicture.setPictureId(pictureId);
			orcPicture.setOriginalFileName(fileName);
			orcPicture.setStatus(status.getId());
			orcPicture.setCreateTime(new Date());
			return orcPictureDao.insertSelective(orcPicture);
		} catch (Exception e) {
			logger.error("getRecogResultAndSave failed,", e);
			return null;
		}
	}

	public Workbook saveExcel(List<OrcPictureBatch> OrcPictureBatchList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		int rows = OrcPictureBatchList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("测试应用");
		cell = row.createCell(1);
		cell.setCellValue("图片名称");
		cell = row.createCell(2);
		cell.setCellValue("文件名");
		cell = row.createCell(3);
		cell.setCellValue("提交人");
		cell = row.createCell(4);
		cell.setCellValue("提交时间");
		cell = row.createCell(5);
		cell.setCellValue("识别结果");
		cell = row.createCell(6);
		cell.setCellValue("备注");

		for (int i = 1; i <= rows; i++) {
			OrcPictureBatch OrcPicture = OrcPictureBatchList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);
			cell.setCellValue(OrcPicture.getTargetStr());
			cell = row.createCell(1);
			cell.setCellValue(OrcPicture.getId());
			cell = row.createCell(2);
			cell.setCellValue(OrcPicture.getOriginalFileName());
			cell = row.createCell(3);
			cell.setCellValue(OrcPicture.getUserKey());
			cell = row.createCell(4);
			cell.setCellValue(DateUtils.formatDate(OrcPicture.getCreateTime()));
			cell = row.createCell(5);
			cell.setCellValue(OrcPicture.getStatusStr());
			cell = row.createCell(6);
			cell.setCellValue(OrcPicture.getRecoAdditionalInfo());
		}

		return workBook;
	}

	public Workbook save2Excel(Long Max, Long totalCount) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("当前检索条件数据超过" + Max + "条(" + totalCount
				+ "条)，无法生成文件，请进一步输入检索条件。");

		return workBook;
	}

	public List<OrcPictureRecolist> getOrcpictureRecolist(Long pictureId) {
		List<OrcPictureRecolist> orcPictureRecolist = orcPictureRecolistDao
				.getByOrcPictureBatchId(pictureId);

		for (OrcPictureRecolist reco : orcPictureRecolist) {
			if (reco != null) {
				String knowledge = reco.getKnowledge();
				if (StringUtils.isNotEmpty(knowledge)) {
					reco.setKnowledgeArray(knowledge.split(","));
				}
			}
		}

		return orcPictureRecolist;
	}

	public List<OrcPictureBatch> getBatchRecoResult(String batchId) {

		List<OrcPictureBatch> orcPictureList = orcPictureDao
				.getPictureIdsByBatchId(batchId, null);

		for (OrcPictureBatch orcPicture : orcPictureList) {
			setSubject(orcPicture);
			setStatus(orcPicture);
			String knowledge = orcPicture.getKnowledge();
			if (StringUtils.isNotEmpty(knowledge)) {
				orcPicture.setKnowledgeArray(knowledge.split(","));
			}

			// 取得多个识别结果
			if (orcPicture.getTarget().equals(
					Integer.toString(OrcPictureCheckTarget.XUEXIBAO.getId()))) {

			}
		}

		return orcPictureList;
	}

	public List<OrcPictureResultCount> computeRecPercentPerMonth(String target,
			String userKey, String searchMonth) {
		List<OrcPictureResultCount> countsList = orcPictureDao
				.computeRecPercentPerMonth(target, userKey, searchMonth);
		return countsList;
	}

}
