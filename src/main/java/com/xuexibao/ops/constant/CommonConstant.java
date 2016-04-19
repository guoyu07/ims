package com.xuexibao.ops.constant;

public class CommonConstant {
	public static String pre_url;
	public static String upload_pic_url;
	public static String pic_recod_url;
	public static String question_id_url;
	public void setPre_url(String pre_url) {
		CommonConstant.pre_url = pre_url;
		TEACHER_QUERYURL = CommonConstant.pre_url+"teacherMs/api/student/queryOrganization";//测试环境教师后台查询接口
		TEACHER_QUERYONEURL = CommonConstant.pre_url+"teacherMs/api/student/selectOrganizationById";//测试环境教师后台查询接口
		TEACHER_URL = CommonConstant.pre_url+"teacherMs/api/student/insertOrgQuest";//教师后台插入接口
		
	}
	public void setQuestion_id_url(String question_id_url) {
		CommonConstant.question_id_url = question_id_url;
		QUESTION_ID_URL = CommonConstant.question_id_url+"api/srv/search/docidd/";//获取题目详情接口
	}
	public void setUpload_pic_url(String upload_pic_url) {
		CommonConstant.upload_pic_url = upload_pic_url;
		UPLOAD_RECOG_URL = CommonConstant.upload_pic_url + "imgapi02";//上传识别文件的url
	}
	public void setPic_recod_url(String pic_recod_url) {
		CommonConstant.pic_recod_url = pic_recod_url + "api/srv/search/qimage2";//识别操作的url;
		RECOG_URL = CommonConstant.pic_recod_url;
	}
	public static String UPLOAD_RECOG_URL = null;
	public static String RECOG_URL = null;
	public static String TEACHER_QUERYURL = null;
	public static String TEACHER_QUERYONEURL = null;
	public static String TEACHER_URL = null;
	public static String QUESTION_ID_URL = null;
//  正式地址	
//	public static String UPLOAD_RECOG_URL = "http://imgapi02.91xuexibao.com/imgapi02"; //上传识别文件的url
//	public static String RECOG_URL = "http://service02.91xuexibao.com/api/srv/search/qimage2"; //识别操作的url;
//	public static final String TEACHER_QUERYURL = "teacher.91xuexibao.com/teacherMs/api/student/queryOrganization";//测试环境教师后台查询接口
//	public static final String TEACHER_QUERYONEURL = "teacher.91xuexibao.com/teacherMs/api/student/selectOrganizationById";//测试环境教师后台查询接口
//	public static final String TEACHER_URL = "teacher.91xuexibao.com/teacherMs/api/student/insertOrgQuest";//教师后台插入接口

	

}
