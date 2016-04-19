package com.xuexibao.ops.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.base.EntityMongoDaoImpl;
import com.xuexibao.ops.model.DedupMarkMongo;


@Repository
public class DedupMarkMongoDao extends EntityMongoDaoImpl<DedupMarkMongo> {
	public DedupMarkMongo getMarkedBase(String last_id) {
		Query query = new Query();
		Criteria criteria = Criteria.where("is_base").is(1);
		if(StringUtils.isNotEmpty(last_id)) {
			criteria.and("_id").gt(new ObjectId(last_id));
		}
		query.addCriteria(criteria);
		query.with(new Sort(Sort.Direction.ASC, "_id"));
		query.limit(1);
		return getMongoTemplate().findOne(query, DedupMarkMongo.class, "dedup_mark");
	}
	
	public long needMarkedCount(String lastBaseId, double score) {
		Query query = new Query();
		Criteria criteria = Criteria.where("base_id").is(lastBaseId);
		criteria.and("score").lt(score);
		query.addCriteria(criteria);
		return getMongoTemplate().count(query, "dedup_mark");
	}
	
	public List<DedupMarkMongo> getMarkList(String baseQuestionId, double score) {
		Query query = new Query();
		Criteria criteria = Criteria.where("base_id").is(baseQuestionId);
		criteria.and("score").lt(score);
		query.addCriteria(criteria);
		return getMongoTemplate().find(query, DedupMarkMongo.class, "dedup_mark");
	}	
	
	public DedupMarkMongo getMarkBase(String baseQuestionId) {
		Query query = new Query();
		Criteria criteria = Criteria.where("is_base").is(1);
		if(StringUtils.isNotEmpty(baseQuestionId)) {
			criteria.and("question_id").is(baseQuestionId);
		}
		query.addCriteria(criteria);
		return getMongoTemplate().findOne(query, DedupMarkMongo.class, "dedup_mark");
	}	
	
}
