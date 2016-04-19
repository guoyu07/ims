package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.TranOpsList;
public interface ITranOpsListDao extends IEntityDao<TranOpsList>   {

	public TranOpsList insertSelective(TranOpsList tranOps);
	
	public List<TranOpsList> getByPictureId(Long pictureId);
}
