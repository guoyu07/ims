package com.xuexibao.ops.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.CommonConstant;
import com.xuexibao.ops.web.FileUploadController;

@Component
public class FileUtil {
	
	private static String file_upload_url;
	public static String file_download_url;
	public static String xuebaHost;
	public static String xiaoyuanHost;
	
	public void setXiaoyuanHost(String xiaoyuanHost) {
		FileUtil.xiaoyuanHost = xiaoyuanHost;
	}
	public void setXuebaHost(String xuebaHost) {
		FileUtil.xuebaHost = xuebaHost;
	}
	public void setFile_upload_url(String file_upload_url) {
		FileUtil.file_upload_url = file_upload_url;
	}
	public void setFile_download_url(String file_download_url) {
		FileUtil.file_download_url = file_download_url;
	}
	
	public static String upload(File file) throws HttpException, IOException {
		String url = file_upload_url;
        String downUrl = file_download_url;
        PostMethod filePost = new PostMethod(url);
        Part[] parts = new Part[]{
            new FilePart(file.getName(), file)
        };
        filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
        int status = client.executeMethod(filePost);
        if (200 == status || 201 == status) {
            String responseStr = filePost.getResponseBodyAsString();
            if(StringUtils.isNotBlank(responseStr)){
                JSONObject responseMap = JSON.parseObject(responseStr);
                return downUrl + responseMap.get("fid").toString();
            }
        }
        return null;
	}
	
	public static String uploadToBatch(File file) throws HttpException, IOException {
		String url = CommonConstant.UPLOAD_RECOG_URL; //"http://imgapi02.91xuexibao.com/imgapi02";
        PostMethod filePost = new PostMethod(url);
        Part[] parts = {
            new FilePart(file.getName(), file),
            new StringPart("pushapi", "pushapi3.91xuexibao.com"),
            new StringPart("ver_client", "2")
        };
        
        filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

        filePost.setRequestHeader("cookie", "liveaa_club=b906b62db68edc6326deba4e10d8999e335824a780fa9b7aee347ce24c27d3a88b002fdc068611c835be46471b29b420d2c241eeb481485d441c5cdf5b52e7d45b360032e430d6e1952165ea0a7f0f2d600a3c07536d060ec9564f66faf370415de354f3a46c48de7009aa80b276c0e92be97552971e8bf642f3db427dda7f04; Max-Age=2592000; Path=/; Expires=Tue, 19 Aug 2014 05: 24:43 GMT");
           
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
        int status = client.executeMethod(filePost);
        if (200 == status) {
            String responseStr = filePost.getResponseBodyAsString();
            if(StringUtils.isNotBlank(responseStr)){
                JSONObject responseMap = JSON.parseObject(responseStr);
                return responseMap.get("image_id").toString();
            }
        }
        return null;
	}
	public static String uploadRemoteImage(String remoteUrl) {
		if(StringUtils.isNotEmpty(remoteUrl)) {
			try {
				String fileName = getFileName();
				URL url = new URL(remoteUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				InputStream inStream = conn.getInputStream();
				File file = FileUploadController.copyFile(inStream, fileName);
				String downloadUrl = FileUtil.upload(file);
				file.delete();
				return downloadUrl;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	public static String getFileName(String picFileName) {
		return UUID.randomUUID().toString() + getExtention(picFileName);
	}
	
	public static String getFileName() {
		return UUID.randomUUID().toString();
	}
	
	private static String getExtention(String fileName) {
		if(StringUtils.isEmpty(fileName))
			return "";
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}
}
