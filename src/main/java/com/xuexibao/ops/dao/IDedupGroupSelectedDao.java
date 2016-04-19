package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.dto.NodupClickCntDto;
import com.xuexibao.ops.dto.ValidQstnLenDto;
import com.xuexibao.ops.model.DedupGroupSelected;
import com.xuexibao.ops.model.DedupStatisticsInfo;

public interface IDedupGroupSelectedDao extends IEntityDao<DedupGroupSelected> {
	
	public List<DedupStatisticsInfo> getDedupStatistics(String dateStr);
	
	public List<NodupClickCntDto> getNodupClickCnt(String dateStr);

	public List<ValidQstnLenDto> getValidQstnLen(String dateStr);

	public List<DedupGroupSelected> searchByNecessary(String questionIds, Integer groupId, Integer round, Integer tinyPhase, String UserKey, String dateStr);
	
	public void updateByRound(Integer groupId, Integer round, Integer validQstnLen, String finishDateStr);
}
