package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.DedupMark;

public interface IDedupMarkDao  extends IEntityDao<DedupMark>{

	List<DedupMark> getBlockIds();
	List<DedupMark> searchIds(Integer teamId);
}
