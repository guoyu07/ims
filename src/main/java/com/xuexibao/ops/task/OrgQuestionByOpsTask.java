package com.xuexibao.ops.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.CommonConstant;
import com.xuexibao.ops.dao.IOpsQuestionDao;
import com.xuexibao.ops.dao.IOrcPictureDao;
import com.xuexibao.ops.dao.IOrgQuestDao;
import com.xuexibao.ops.dao.IQuestionDao;
import com.xuexibao.ops.dao.ITranOpsDao;
import com.xuexibao.ops.model.OrcPicture;
import com.xuexibao.ops.util.http.HttpSubmit;
import com.xuexibao.ops.web.AbstractController;

@Component
public class OrgQuestionByOpsTask extends AbstractController {

	private static Logger logger = LoggerFactory.getLogger("org_question_ops_log");


	@Resource
	IOrcPictureDao orcPictureDao;
	@Resource
	IOpsQuestionDao opsQuestionDao;
	@Resource
	ITranOpsDao tranOpsDao;
	@Resource
	IOrgQuestDao orgQuestDao;
	@Resource
	IQuestionDao questionDao;

	public static Date getDay(Date date, int flag) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, flag);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}
	// 每50秒执行一次
	@Scheduled(cron = "0/50 * *  * * ? ")
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	private void orcPictureYesterdayData() {
		logger.info("开始执行一天扫描批量识别图片录制题目拥有来源id的题目orc_picture_sourceId:{}", System.currentTimeMillis());
		Map<String, String> MSG_sParaTemp = null;
		String latex = null;
		String Result="";
		//第一步，先查询出昨天录制的完整的（题干，答案完整的）题目 具有图片id，关联orc_picture表，
		//查询出bookid 不为空的，关联books表查询出 source_id 不为空的数据。确定好这部分数据是来自机构的题目

		//List<OrcPicture> orcPictureList=orcPictureDao.orcPictureByDate(todayDawn, yesterdayDawn);

		//第四步，查询识别正确的题目，直接进行推送
		//List<OrcPicture> orcPictureLists=orcPictureDao.orcPictureRightByDate(todayDawn, yesterdayDawn);//按时间查询推送
		List<OrcPicture> orcPictureLists=orcPictureDao.orcPictureRightByDate(null, null);//不按时间，只按状态推送去查询
		if(orcPictureLists != null){
			for(OrcPicture orcpic:orcPictureLists){
				//读取判断正确的直接 插入org_quest ,
				orcpic.getBooks_source_id();
				orcpic.getTran_ops_id();//正确返回的题目id
				OrcPicture orcpi=orcPictureDao.getById(orcpic.getOrc_picture_id());
				if(orcpi != null){
					//调用教师后台接口，传入要插入的题目机构关系和题目详细 
					MSG_sParaTemp = new HashMap<String, String>();
					JSONObject resObj = new JSONObject();

				
					if(orcpi.getSubject() != null){
						resObj.put("subject", orcpi.getSubject());
					}else{
						resObj.put("subject", "");
					}
					if(orcpi.getLatex() != null){
						resObj.put("latex", orcpi.getLatex());
					}else{
//						try {
//							url=new URL(CommonConstant.QUESTION_ID_URL+orcpi.getQuestionId());
//							Object obj = url.getContent(); 
//							conn = (HttpURLConnection) url.openConnection();
//							input = new InputStreamReader(conn.getInputStream(),"utf-8");
//							Scanner inputStream = new Scanner(input);   
//							StringBuffer sb = new StringBuffer();
//							while (inputStream.hasNext()) {    
//								sb.append(inputStream.nextLine());
//							}
//							jsonObj = JSON.parseObject(sb.toString());
//							JSONObject recogObj = jsonObj;
//							latex = ((JSONObject)(((JSONArray)((JSONObject)recogObj.get("result")).get("answers")).get(0))).get("question_body").toString();
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						resObj.put("latex", "");
					}
					if(orcpi.getContent() != null){
						resObj.put("content", orcpi.getContent());
					}else{
						resObj.put("content", "");
					}
					if(orcpi.getKnowledge() != null){
						resObj.put("knowledge", orcpi.getKnowledge());
					}else{
						resObj.put("knowledge", "");
					}
					if(orcpi.getAnswer() != null){
						resObj.put("answer", orcpi.getAnswer());
					}else{
						resObj.put("answer", "");
					}

					if(orcpi.getSolution() != null){
						resObj.put("solution", orcpi.getSolution());
					}else{
						resObj.put("solution", "");
					}

					resObj.put("learnPhase", "");
					if(orcpi.getRealSubject() != null){
						resObj.put("realSubject", orcpi.getRealSubject());
					}else{
						resObj.put("realSubject", "");
					}
					if(orcpi.getQuestionId() != null){
						resObj.put("realId", orcpi.getQuestionId());
					}else{
						resObj.put("realId", "");
					}
					resObj.put("source", "3");
					MSG_sParaTemp.put("question",resObj.toString());
					MSG_sParaTemp.put("orgIds", orcpic.getBooks_source_id());		


					try {	
						Result=HttpSubmit.sendPostOrGetInfo(MSG_sParaTemp, CommonConstant.TEACHER_URL, "POST");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.info("请求时间："+new Date()+"--url:"+CommonConstant.TEACHER_URL+"--Result:"+ Result);
					if(StringUtils.isNotEmpty(Result)){				
						resObj=JSON.parseObject(Result);
						//推送插入成功-备份本地关系表orc_ops_sources-一道题去question 查询是否已经 推送  如果已经存在 则继续，不存在则插入							
						final OrcPicture orcPicture = orcPictureDao.getById(orcpic.getOrc_picture_id());
						orcPicture.setPushstatus(1);
						orcPicture.setLatex(latex);
						orcPictureDao.updateIfNecessary(orcPicture);
						logger.info("查询识别正确的图片id:"+orcpic.getOrc_picture_id()+"，直接进行推送Time:"+new Date()+"返回成功："+successJson(resObj.toString()));														
					}
				}					
			}
		}
		//人工录入题目推题过程
		//		if(orcPictureList != null){
		//			for(OrcPicture orcpic:orcPictureList){
		//			orcpic.getTran_ops_id();
		//			//一道题去线上查询上线id是否存在
		//			OpsQuestion opsQusetion=opsQuestionDao.getById(orcpic.getTran_ops_id());
		//			if(opsQusetion != null){
		//				//第二步，根据第一步查询出的数据 去线上questionid和opsid 关系表中查出这些数据的对应关系，插入到教师端机构题目关系表 同时插入本库orc_ops_sources做备份
		//				Long realquestion=opsQusetion.getQuestionid();
		//				String souce_id=orcpic.getBooks_source_id();
		//				//如果存在多个来源id循环插入题目来源对应表
		//				String[] sourceIdArray=souce_id.split(",");
		//				if(sourceIdArray.length>0){
		//				for(String i:sourceIdArray){
		//					OrgQuest orgquest=new OrgQuest(Integer.valueOf(i),realquestion);
		//					OrgQuest orgquestInfo=orgQuestDao.getByIdKey(Integer.valueOf(i),realquestion);
		//					if(orgquestInfo != null){
		//						continue;
		//					}else{
		//						OrgQuest orgquests=orgQuestDao.insertSelective(orgquest);
		//						
		//						logger.info("Time:"+new Date()+"成功插入org_quest 表中："+successJson(orgquest));
		//						if(orgquests != null){
		//							//第三步，同时把这些查询出来的opsid 的题目详细插入question 表中，标记机构题
		//							TranOps tranops=tranOpsDao.getById(orcpic.getTran_ops_id());
		//							Question questionInfo=questionDao.getByRealId(realquestion);						
		//							if(tranops != null && questionInfo == null){
		//								Question question=new Question(tranops.getSubject(),tranops.getKnowledge(),
		//										tranops.getLearnphase(),(byte)tranops.getRealSubject().intValue(),
		//										realquestion,Integer.valueOf(0),
		//										(byte)0, Integer.valueOf(0),new Date(),
		//										tranops.getLatex(),tranops.getContent(),
		//										tranops.getAnswer(),tranops.getSolution(),
		//										(byte)0,  (byte)1,
		//							    		 new Date(),  String.valueOf(3));							
		//								questionDao.insert(question);

		//			                    tranops.setOrcpushstatus(1);
		//			                    tranOpsDao.updateIfNecessary(tranops);
		//								logger.info("Time:"+new Date()+"成功插入question 表中："+successJson(question));	
		//							}
		//						}
		//					}							
		//				}							
		//			  }
		//			}
		//		}
		//		}
	}
}
