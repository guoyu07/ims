package com.xuexibao.ops.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.CommonConstant;
import com.xuexibao.ops.constant.RecognitionBatchConstant;
import com.xuexibao.ops.enumeration.OrcPictureCheckTarget;

public class RecoJson {

	// 它对象
	public static String getRecogeResult(String pictureId, OrcPictureCheckTarget target, Integer type) throws SocketTimeoutException,
			SocketException, Exception {
		String url = null;
		GetMethod postMethod = null;
		String resObj = null;
		try {
			switch (target) {
			case XUEBAJUN:
			  if( type == 1 ){
			    url = RecognitionBatchConstant.XUEBAServiceURL + pictureId;
			  }else{
			    url = RecognitionBatchConstant.XUEBAServiceURLForAUTO + pictureId;
			  }
				break;
			case XIAOYUANSOUTI:
			  if( type == 1 ){
			    url = RecognitionBatchConstant.YuantikuServiceURL + pictureId;
			  }else{
          url = RecognitionBatchConstant.YuantikuServiceURLForAUTO + pictureId;
        }
				break;
			default:
				break;
			}
			postMethod = new GetMethod(url);
			HttpClient httpclient = new HttpClient();
			httpclient.getParams().setIntParameter("http.socket.timeout",
					50 * 1000);
			postMethod.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);

			int statusCode = httpclient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			InputStream responseBody = postMethod.getResponseBodyAsStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					responseBody, "utf-8"));
			String line = reader.readLine();
			String all_line = "";
			while (line != null) {
				all_line += line;
				line = reader.readLine();
			}

			resObj = all_line;
		} finally {
			if (postMethod != null)
				postMethod.releaseConnection();
		}

		return resObj;
	}

	// 学习宝
	public static JSONObject getRecogeResult(String image_id) {
		PostMethod postMethod = new UTF8PostMethod(CommonConstant.RECOG_URL);// "http://service02.91xuexibao.com/api/srv/search/qimage2"
		JSONObject resObj = new JSONObject();
		try {
			HttpClient httpclient = new HttpClient();
			NameValuePair[] data = { new NameValuePair("image_id", image_id) };
			postMethod.setRequestBody(data);
			postMethod.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);

			int statusCode = httpclient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			InputStream responseBody = postMethod.getResponseBodyAsStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					responseBody, "utf-8"));
			String line = reader.readLine();
			String all_line = "";
			while (line != null) {
				all_line += line;
				line = reader.readLine();
			}

			resObj = JSON.parseObject(all_line);
		} catch (Exception e) {
			e.printStackTrace();
			resObj = null;
		} finally {
			postMethod.releaseConnection();
		}

		return resObj;
	}

	private static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}
	}

	public static String repalceURL(String inStr) {
		String regex = "([-\\w\\d]+\\.(?:jpg|gif|png))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inStr);
		String replace = null;
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			replace = FileUtil.uploadRemoteImage(FileUtil.xiaoyuanHost
					+ matcher.group(0));
			matcher.appendReplacement(sb, replace);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String repalceURLXueba(String inStr) {
		String regex = "(/question_bank/[/\\.\\w\\d]+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inStr);
		String replace = null;
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			replace = FileUtil.uploadRemoteImage(FileUtil.xuebaHost
					+ matcher.group(0));
			matcher.appendReplacement(sb, replace);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static File copyFile(InputStream inStream, String fileName,
			String imagePath) throws IOException {
		FileOutputStream outStream = null;
		try {
			String filePath;
			if (imagePath == null || imagePath == "") {
				filePath = System.getProperty("java.io.tmpdir");
			} else {
				filePath = imagePath;
			}

			File imageFile = new File(filePath);
			if (imageFile.exists() == false) {
				imageFile.mkdirs();
			}
			imageFile = new File(filePath, fileName);
			outStream = new FileOutputStream(imageFile);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			return imageFile;
		} finally {
			if (inStream != null) {
				inStream.close();
			}
			if (outStream != null) {
				outStream.close();
			}
		}
	}
}
