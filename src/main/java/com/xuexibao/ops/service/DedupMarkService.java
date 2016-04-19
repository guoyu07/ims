package com.xuexibao.ops.service;


//import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.dao.IDedupMarkDao;
import com.xuexibao.ops.dao.impl.DedupCheckDetailRecordDaoImpl;
import com.xuexibao.ops.dao.impl.DedupMarkDao;
import com.xuexibao.ops.dao.impl.DedupMarkMongoDao;
import com.xuexibao.ops.model.DedupMark;
import com.xuexibao.ops.model.DedupMarkMongo;

@Service
public class DedupMarkService {
	
	private static final int default_block_value = 500;
	
	private static double score = 0.95;
	
	@Resource
	DedupMarkDao dedupMarkDao;
	
	@Resource
	IDedupMarkDao dedupMarkDaos;
	
	@Resource
	DedupMarkMongoDao dedupMarkMongoDao;
	
	@Resource
	DedupCheckDetailRecordDaoImpl dedupCheckDetailRecordDao;
	
	static Object lock_obj = new Object();
	
	public void fromMongoToMysql() {
		System.out.println("start");
		long start = System.currentTimeMillis();
		DedupMarkMongo base = new DedupMarkMongo();
		String last_id = "start";
		String lastBaseId = null;
		long count = 0;
		List<DedupMark> list = null;
		DedupMark mark = null;
		for(int i = 1; base != null; i++) {
			long current = System.currentTimeMillis();
			list = new ArrayList<>();
			for(int j = 1; j <= default_block_value && base != null;) {
				if(i == 1 && j == 1)
					last_id = null;
				base = dedupMarkMongoDao.getMarkedBase(last_id);
				if(base == null)
					break;
				last_id = base.get_id();
				lastBaseId = base.getBase_id();
				count = dedupMarkMongoDao.needMarkedCount(lastBaseId, score);
				if(count > 0) {
					mark = new DedupMark(base, i);
					list.add(mark);
					j++;
				}
			}
			dedupMarkDao.insertBatch(list);
			System.out.println("GROUP " + i + "，SIZE: " + list.size() + "，时长: " + (System.currentTimeMillis() - current) + "ms");
		}
		System.out.println("总时长:" + (System.currentTimeMillis() - start));
	}
	
	public int getUnfinishedCount(String userKey) {
		return dedupMarkDao.getUnfinishedCount(userKey);
	}
	
	public synchronized Map<String, Object> assignNewBlock(String userKey) {
		Map<String, Object> result = new HashMap<>();
		int unfinishedCount = dedupMarkDao.getUnfinishedCount(userKey);
		if(unfinishedCount == 0) {
			Integer minBlockNotAssigned = dedupMarkDao.minBlockNotAssigned();
			if(minBlockNotAssigned != null) {
				dedupMarkDao.assignNewBlock(userKey, minBlockNotAssigned);
				result.put("success", true);
				result.put("msg", "题目分配成功！");
			} else {
				result.put("success", false);
				result.put("msg", "题目已全部分配完成，感谢您的辛勤工作！");
			}
		} else {
			result.put("success", false);
			result.put("msg", "当前您还有" + unfinishedCount + "道题目未完成！");
		}
		return result;
		
	}
	
	public DedupMark getOneDedupMark(String userKey){
		
		return dedupMarkDao.selectOne(userKey, 0, null);
	}
	
	public List<DedupMarkMongo> getOneGroupQuestion(DedupMark dedupMark){
		List<DedupMarkMongo> dedupList = new ArrayList<>();
		
		DedupMarkMongo  baseOne = dedupMarkMongoDao.getMarkBase(dedupMark.getBaseId());
		if(baseOne !=null) dedupList.add(baseOne);
		
		List<DedupMarkMongo>  undups = dedupMarkMongoDao.getMarkList(dedupMark.getBaseId(),score);
		if(undups != null && undups.size()>0) dedupList.addAll(undups);
		
		return dedupList;
	}
	//获取最佳题目
	public List<DedupMarkMongo> getCheckBestOneQuestion(DedupMark dedupMark){
		List<DedupMarkMongo> dedupList = new ArrayList<>(); 
		DedupMarkMongo  baseOne = dedupMarkMongoDao.getMarkBase(dedupMark.getBaseId());
		if(baseOne !=null) dedupList.add(baseOne);
		return dedupList;
	}
	
	public DedupMark getdedupMarkById(Long markId){
		return  dedupMarkDao.getById(markId);
	}
	
	//获取重复题目
	public List<DedupMarkMongo> getCheckSameOneGroupQuestion(DedupMark dedupMark){
		List<DedupMarkMongo> dedupList = new ArrayList<>();
	    
		List<DedupMarkMongo>  undups = dedupMarkMongoDao.getMarkList(dedupMark.getBaseId(),score);	
		String result = dedupMark.getResult();
		
		if(result != null && !"".endsWith(result)){
			if(undups != null && undups.size()>0){
				String[] questionIDArray = result.split(",");
				for(String temp:questionIDArray){
					if(!StringUtils.isEmpty(temp))
					{
						//如果去重结果有去重题目id
						for(DedupMarkMongo dedupMarkMongo : undups){							
							if(temp.equals(dedupMarkMongo.getQuestion_id())){
								dedupList.add(dedupMarkMongo);
								break;
							}
						}				
					}
				}
			}
		}	
		return dedupList;
	}
	//获取不重复题目
	public List<DedupMarkMongo> getCheckDifOneGroupQuestion(DedupMark dedupMark){
		List<DedupMarkMongo> dedupList = new ArrayList<>();

		List<DedupMarkMongo>  undups = dedupMarkMongoDao.getMarkList(dedupMark.getBaseId(),score);	
		String result = dedupMark.getResult();

		if(result != null && !"".endsWith(result)){
			String[] questionIDArray = result.split(",");
			for(DedupMarkMongo dedupMarkMongo : undups){
				boolean flag=true;
				for(String temp:questionIDArray){				
					if(temp.equals(dedupMarkMongo.getQuestion_id())){
						flag = false;
						break;
					}
				}
				if(flag){
					dedupList.add(dedupMarkMongo);
				}
			}		
		}else{
			dedupList.addAll(undups);
		}
		
		return dedupList;
	}
	
	public List<DedupMark> getBlockIds() {
		List<DedupMark> teamsList = dedupMarkDaos.getBlockIds();
		return teamsList;
	}
	
	
	public void updateMarkStatus(String userKey, String baseId, String dupQuestionIds,String block){
		dedupMarkDao.updateDedupMarkStatus(null, baseId, 1,  Integer.parseInt(block), dupQuestionIds);
	}
	
	public void updateMarkBlock(String userKey, String block, String baseId){
		DedupMark dedupMark = dedupMarkDao.selectOne(userKey, 0, null);
		if(dedupMark == null){
			dedupMarkDao.updateDedupMarkFinished(Integer.parseInt(block));
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public void disableBlock(String block){
		List<DedupMark> newBlocks = new ArrayList<>();
		DedupMark mark;
	
		List<DedupMark> dedupMarks = dedupMarkDao.searchBlockList(Integer.parseInt(block));
		if(dedupMarks != null && dedupMarks.get(0).getStatus() != (byte)2){
			synchronized(lock_obj){
				Integer newblock = dedupMarkDao.getBiggestBlockId();
				newblock +=1;
				for(DedupMark dedupMark : dedupMarks){
					mark = new DedupMark(dedupMark.getBaseId(), newblock);
					newBlocks.add(mark);
				}
				dedupMarkDao.insertBatch(newBlocks);
			}
			
			dedupMarkDao.updateDedupMarkStatus(null, null, 2, Integer.parseInt(block), null);
			
			dedupCheckDetailRecordDao.updateStatus(Integer.parseInt(block), 2);
			
		}
	}	
	
}
