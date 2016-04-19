package com.xuexibao.ops.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.Books;
import com.xuexibao.ops.service.BooksService;
import com.xuexibao.ops.service.OrcPictureService;
import com.xuexibao.ops.util.FileUtil;

/**
 * 
 * @author 赵超群
 * 
 */
@SuppressWarnings("restriction")
@RestController
@RequestMapping(value = "/fileupload")
public class FileUploadController extends AbstractController {
	private static Logger logger = LoggerFactory.getLogger("file_upload_log");
	@Autowired
	protected OrcPictureService orcPictureService;
	@Autowired
	protected BooksService booksService;
	@RequestMapping(value = "/uploadLocalImage")
	public ResponseResult uploadLocalImage(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
		try{
			if(null == file)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "本地文件为空，请重新上传");
			String fileName = file.getOriginalFilename();
			File localFile = copyFile(file.getInputStream(), fileName);
			BufferedImage bi = ImageIO.read(file.getInputStream());
			String downloadUrl = FileUtil.upload(localFile);
			localFile.delete();
			JSONObject result = new JSONObject();
			result.put("imageUrl", downloadUrl);
			result.put("width", bi.getWidth());
			result.put("height", bi.getHeight());
			logger.info("成功上传图片地址："+result);
			return successJson(result);
		} catch (Exception e) {
			logger.info("时间："+new Date()+"失败上传图片："+file);
			logger.error("失败上传图片:upload_time=" + new Date(),e);
			return errorJson(EnumResCode.SERVER_ERROR.value(), "读取文件失败，请重新上传");
		}
	}

	@RequestMapping(value = "/batchRecognitionBook")
	public ResponseResult batchRecognitionBook(HttpServletRequest request, HttpServletResponse response, MultipartFile file, Long bookId) {
		try {
			Books book = booksService.getById(bookId);
			if (book == null)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "书籍不能为空，选择书籍");
			if (file == null)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "本地文件为空，请重新上传");
			String fileName = file.getOriginalFilename();
			File localFile = copyFile(file.getInputStream(), fileName);
			String image_id = FileUtil.uploadToBatch(localFile);
			localFile.delete();
			if (StringUtils.isEmpty(image_id)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "读取文件失败，请重新上传");
			}
			TimeUnit.SECONDS.sleep(3);
			JSONObject result = new JSONObject();
			result.put("image_id", image_id);

			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);

			if(!orcPictureService.getRecogResultAndSave(image_id, userKey, bookId))
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片识别失败！");
			return successJson(result);
		} catch (Exception e) {
			logger.error("失败上传图片:upload_time=" + new Date(),e);
			logger.info("时间："+new Date()+"失败上传图片："+file);
			return errorJson(EnumResCode.SERVER_ERROR.value(), "读取文件失败，请重新上传");
		}
	}
	
	public static File copyFile(InputStream inStream, String fileName) throws IOException {
		FileOutputStream outStream = null;
		try {
			String filePath = System.getProperty("java.io.tmpdir");
			File imageFile = new File(filePath);
			if (imageFile.exists() == false) {
				imageFile.mkdirs();
			}
			imageFile = new File(filePath, fileName);
			outStream = new FileOutputStream(imageFile);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			return imageFile;
		} finally {
			if(inStream != null) {
				inStream.close();
			}
			if(outStream != null) {
				outStream.close();
			}
		}
	}
	
	@RequestMapping(value = "/uploadRemoteImage")
	public ResponseResult uploadRemoteImage(HttpServletResponse response, String remoteUrl) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(StringUtils.isNotEmpty(remoteUrl)) {
				String fileName = this.getFileName();
				URL url = new URL(remoteUrl);
				URLConnection conn = url.openConnection();
				conn.setReadTimeout(10 * 1000);
				InputStream inStream = conn.getInputStream();
				File file = copyFile(inStream, fileName); 
				String downloadUrl = FileUtil.upload(file);

				file.delete();
				BufferedImage image = getBufferedImage(downloadUrl); 
				if (image != null) { 
					JSONObject result = new JSONObject();
					result.put("imageUrl", downloadUrl);
					result.put("width", image.getWidth());
					result.put("height", image.getHeight());
					return successJson(result);
				} else { 
					return errorJson(EnumResCode.SERVER_ERROR.value(), "图片网址不存在图片，请重新上传"); 
				}			
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片网址为空，请重新上传");
			}
		} catch (Exception e) {
			logger.error("失败上传图片:upload_time=" + new Date(),e);
			logger.info("时间："+new Date()+"失败上传图片地址："+remoteUrl);
			return errorJson(EnumResCode.SERVER_ERROR.value(), "读取文件失败，请重新上传");
		}
	}
	
	@RequestMapping(value = "/uploadRemoteImages")
	public ResponseResult uploadRemoteImages(HttpServletResponse response, String[] remoteUrls) {
		int length = remoteUrls.length;
		if(remoteUrls != null && length > 0) {
			String[] downloadUrls = new String[length];
			JSONArray results = new JSONArray();
			response.setHeader("Access-Control-Allow-Origin", "*");
			String remoteUrl = null;
			Pattern pattern = Pattern.compile("^data:image/\\w{0,10};base64,.*");
			Matcher matcher = null;
			for(int i = 0; i < length; i++) {
				try {
					remoteUrl = remoteUrls[i];
					if(StringUtils.isNotEmpty(remoteUrl)) {
						remoteUrl = URLDecoder.decode(remoteUrls[i], "utf-8");
						matcher = pattern.matcher(remoteUrl);
						File file = null;
						if(matcher.matches()) {
							remoteUrl = remoteUrl.replaceFirst("^data:image/\\w{0,10};base64,", "");
							file = this.saveBase64Image(remoteUrl);
						} else {
							String fileName = this.getFileName();
							URL url = new URL(remoteUrl);
							URLConnection conn = url.openConnection();
							conn.setReadTimeout(10 * 1000);
							InputStream inStream = conn.getInputStream();
							file = copyFile(inStream, fileName);
						}
						downloadUrls[i] = FileUtil.upload(file);
						BufferedImage image = getBufferedImage(downloadUrls[i]); 
						JSONObject result = new JSONObject();
						if (image != null) { 
							result.put("imageUrl", downloadUrls[i]);
							result.put("width", image.getWidth());
							result.put("height", image.getHeight());
							results.add(result);
						} else { 
							results.add(new JSONObject());
						}					
						file.delete();
					} else {
						results.add(new JSONObject());
					}
				} catch (Exception e) {
					logger.error("失败上传图片:upload_time=" + new Date(),e);
					logger.info("时间："+new Date()+"失败上传图片地址："+remoteUrl);
					results.add(new JSONObject());
					
				}
			}
			return successJson(results);
		} else {
			return errorJson(EnumResCode.SERVER_ERROR.value(), "图片网址为空，请重新上传");
		}
	}
	
	public File saveBase64Image(String imgStr) throws IOException {   //对字节数组字符串进行Base64解码并生成图片
		OutputStream outStream = null;
		try {
	        BASE64Decoder decoder = new BASE64Decoder();
	        //Base64解码
	        byte[] b = decoder.decodeBuffer(imgStr);
	        for(int i = 0; i < b.length; ++i) {
	            if(b[i] < 0)  {//调整异常数据
	                b[i] += 256;
	            }
	        }
	        //生成jpeg图片
	        String filePath = System.getProperty("java.io.tmpdir");
			File imageFile = new File(filePath);
			if (imageFile.exists() == false) {
				imageFile.mkdirs();
			}
			imageFile = new File(filePath, UUID.randomUUID().toString());
			outStream = new FileOutputStream(imageFile);    
			outStream.write(b);
			outStream.flush();
	        return imageFile;
		} finally {
			if(outStream != null) {
				outStream.close();
			}
		}
    }
	private String getFileName() {
		return UUID.randomUUID().toString();
	}
	 /**
     * 
     * @param imgUrl 图片地址
     * @return 
     */ 
    public static BufferedImage getBufferedImage(String imgUrl) { 
        URL url = null; 
        InputStream is = null; 
        BufferedImage img = null; 
        try { 
            url = new URL(imgUrl); 
            is = url.openStream(); 
            img = ImageIO.read(is); 
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
               
            try { 
                is.close(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        return img; 
    } 
}
