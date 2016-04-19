package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.OrcPictureRecolist;

public interface IOrcPictureRecolistDao extends IEntityDao<OrcPictureRecolist>   {

	public List<OrcPictureRecolist> getByOrcPictureBatchId(Long orcPictureId);

}