package com.xuexibao.ops.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.BatchOrcPictureCheckStatus;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.enumeration.OrcPictureCheckTarget;
import com.xuexibao.ops.model.OrcPictureBatch;
import com.xuexibao.ops.service.BatchOrcPictureService;
import com.xuexibao.ops.util.FileUtil;

/**
 * 
 * @author 赵超群
 * 
 */
@RestController
@RequestMapping(value = "/batchfileupload")
public class BatchFileUploadController extends AbstractController {
	
	@Autowired
	protected BatchOrcPictureService orcPictureService;
	
	@RequestMapping(value = "/fileUploadApi")
	public ResponseResult fileUpload(HttpServletRequest request, HttpServletResponse response, MultipartFile file, String batchId, String target) {
		
		JSONObject result=new JSONObject();
		DiskFileItem fi = null;
		try{
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			String downloadUrl = null;
			if(null != file) {
				String fileName = file.getOriginalFilename();
				CommonsMultipartFile cf= (CommonsMultipartFile)file; 
		        fi = (DiskFileItem)cf.getFileItem(); 
		        File f = fi.getStoreLocation();
		        String pictureId=null;
		        String newFileName=FileUtil.getFileName(fileName);
		        BatchOrcPictureCheckStatus status = BatchOrcPictureCheckStatus.URECO;
		        
				switch(OrcPictureCheckTarget.getEnum(Integer.parseInt(target))) {
				case XUEXIBAO:
			        File newfile =  copyFile(fi.getInputStream(),newFileName,null);
					pictureId = FileUtil.uploadToBatch(newfile);
					newfile.delete();
					//上传文件失败也登数据库
					if(StringUtils.isEmpty(pictureId)){
						status = BatchOrcPictureCheckStatus.ERROR_UPLOAD_FAILURE;
					}					
					break;
				case XIAOYUANSOUTI:
				case XUEBAJUN:
					File local = FileUploadController.copyFile(file.getInputStream(), fileName);
					downloadUrl = FileUtil.upload(local);
					local.delete();
					pictureId = downloadUrl.replace(FileUtil.file_download_url, "");						
					break;
				case ZUOYEBANG:
					break;	
					
				default:
					break;
				}
				f.delete();
				OrcPictureBatch ret = orcPictureService.setUploadInfo(target, userKey, batchId, downloadUrl, pictureId, fileName, status);
				
				if(ret == null){
					return errorJson(EnumResCode.SERVER_ERROR.value(), "数据库插入失败，请重新上传！");
				}
								
				result.put("image_id", pictureId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return successJson(result);
	}

	@RequestMapping(value = "/getBatchResult")
	public ResponseResult getBatchResult(HttpServletRequest request, HttpServletResponse response,String batchId, String[] pictureIds,String target) throws IOException {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			String paraBatchId = batchId;
			
			int ret = orcPictureService.getRecogResultByBatchIdOnly(paraBatchId,target);
			
			if(ret != 0){
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片识别失败！");
			}
			
			JSONObject result = new JSONObject();
			result.put("result", "ok");				
			return successJson(result);

		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "读取文件失败，请重新上传");
		}
	}	
	
	private File copyFile(InputStream inStream, String fileName, String imagePath) throws IOException {
		FileOutputStream outStream = null;
		try {
			String filePath;
			if(imagePath==null || imagePath ==""){
				filePath = System.getProperty("java.io.tmpdir");
			}else{
				filePath=imagePath;
			}
				
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
				String fileName = FileUtil.getFileName();
					URL url = new URL(remoteUrl);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5 * 1000);
					InputStream inStream = conn.getInputStream();
					File file = copyFile(inStream, fileName,null);
					
					String downloadUrl = FileUtil.upload(file);
					file.delete();
					JSONObject result = new JSONObject();
					result.put("imageUrl", downloadUrl);
					return successJson(result);
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片网址为空，请重新上传");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "读取文件失败，请重新上传");
		}
	}
	
	@RequestMapping(value = "/uploadLocalImage")
	public ResponseResult uploadLocalImage(HttpServletRequest request, HttpServletResponse response, String localImageFileName) throws IOException {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			String fileName = FileUtil.getFileName(localImageFileName);
			InputStream inStream = request.getInputStream();
			if(inStream != null) {
				File file = copyFile(inStream, fileName,null);
				
				String downloadUrl = FileUtil.upload(file);
				file.delete();
				JSONObject result = new JSONObject();
				result.put("imageUrl", downloadUrl);
				return successJson(result);
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "本地文件为空，请重新上传");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "读取文件失败，请重新上传");
		}
	}

}
