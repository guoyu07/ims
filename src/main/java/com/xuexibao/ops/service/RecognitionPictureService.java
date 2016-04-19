package com.xuexibao.ops.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.constant.RecognitionPictureResultsConstant;
import com.xuexibao.ops.constant.RecognitionPictureStatusConstant;
import com.xuexibao.ops.dao.IRecognitionAnalysisBydateDao;
import com.xuexibao.ops.dao.IRecognitionPictureDao;
import com.xuexibao.ops.dao.IUserInfoDao;
import com.xuexibao.ops.enumeration.EnumRecognitionResult;
import com.xuexibao.ops.model.RecognitionAnalysisBydate;
import com.xuexibao.ops.model.RecognitionPicture;
import com.xuexibao.ops.task.ObtainRemainPictureTask;
import com.xuexibao.ops.util.UNZIPUtil;
import com.xuexibao.ops.web.RecognitionHistoryController;

@Service
public class RecognitionPictureService {

	private static Logger logger = LoggerFactory.getLogger("artificial_recognition_log");

	private static final String BMP_FORMAT = ".bmp";

	private static final String DOWNLOAD_PATH = "download";

	private static final int INSERT_PER_SIZE = 3000;

	@Resource
	IRecognitionPictureDao recognitionPictureDao;

	@Resource
	IRecognitionAnalysisBydateDao recognitionAnalysisBydateDao;

	@Resource
	IUserInfoDao userInfoDao;

	@Autowired
	protected RecognitionHistoryService recognitionHistoryService;
	@Autowired
	protected RecognitionAnalysisService recognitionAnalysisService;

	public List<RecognitionPicture> searchList(Long pictureId, Integer status, Date startDate, Date endDate, Long page, int limit) {
		List<RecognitionPicture> recognitionpictureList = recognitionPictureDao.searchList(pictureId, status, startDate, endDate, page, limit);
		if (recognitionpictureList != null) {
			Iterator<RecognitionPicture> recognitionPictureIterator = recognitionpictureList.iterator();
			while (recognitionPictureIterator.hasNext()) {
				this.setUnRecognizedReason(recognitionPictureIterator.next());
			}
		}
		return recognitionpictureList;
	}

	public List<RecognitionAnalysisBydate> searchCountList(String operator, Date startDate, Date endDate, Long page, int limit) {
		List<RecognitionAnalysisBydate> recogPicCountList = recognitionAnalysisBydateDao.searchCountList(operator, startDate, endDate, page, limit);
		return recogPicCountList;
	}

	public List<RecognitionAnalysisBydate> searchCountList(String operator, Date startDate, Date endDate) {
		List<RecognitionAnalysisBydate> recogPicCountList = recognitionAnalysisBydateDao.searchCountList(operator, startDate, endDate);
		return recogPicCountList;
	}

	public long searchUserCount() {
		return recognitionAnalysisBydateDao.searchRecognitionUserCount();
	}

	public List<RecognitionPicture> obtainRemainPictureList(String operator, int limit) {
		List<RecognitionPicture> result = new ArrayList<>();
		RecognitionPicture picture = null;
		for (int i = 0; i < limit;) {
			picture = ObtainRemainPictureTask.pictureList.poll();
			if (picture != null) {
				if (RecognitionPictureStatusConstant.ONE_CHECKED.equals(picture.getStatus()) && operator.equals(picture.getOperator1())) {
					continue;
				}
				result.add(picture);
				i++;
			} else {
				break;
			}
		}
		return result;
	}

	public long searchCount(Long pictureId, Integer status, Date startDate, Date endDate) {
		return recognitionPictureDao.searchCount(pictureId, status, startDate, endDate);
	}

	// 总识别图片数量
	public long CountTotalRecognizedNum() {
		return recognitionPictureDao.CountTotalRecognizedNum();
	}

	// 今日识别图片数量
	public long CountTodayNum(String operator, Date today, Date tomorrow) {
		return recognitionPictureDao.CountTodayNum(operator, today, tomorrow);
	}

	// 待识别图片数量
	public long CountRemainNum(String operator, Date today) {
		return recognitionPictureDao.CountRemainNum(operator, today);
	}

	public List<RecognitionPicture> selectUnRecList(String operater, Integer limit) {
		List<RecognitionPicture> recognitionpictureList = recognitionPictureDao.selectUnRecList(operater, limit);
		return recognitionpictureList;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public int[] updatePicture(String operator, Long[] idArray, String[] results, Integer[] statusArray, String rootPath) {

		List<RecognitionPicture> recognitionPictureList = new ArrayList<>();
		for (int i = 0; i < idArray.length; i++) {
			RecognitionPicture recognitionPictureId = new RecognitionPicture(idArray[i]);
			RecognitionPicture oldRecogPicture = recognitionPictureDao.getById(recognitionPictureId);

			// 识别成功
			if (RecognitionPictureResultsConstant.RECOGNIZED.equals(statusArray[i])) {
				// 第1次识别成功
				if (oldRecogPicture.getStatus().equals(RecognitionPictureStatusConstant.REMAIN_CHECK))
					recognitionPictureList.add(new RecognitionPicture(idArray[i], operator, null, new Date(),
							RecognitionPictureStatusConstant.ONE_CHECKED));

				// 第1次识别成功 + 第2次识别成功
				else if (oldRecogPicture.getStatus().equals(RecognitionPictureStatusConstant.ONE_CHECKED)
						&& !operator.equals(oldRecogPicture.getOperator1())) {
					// 2次识别结果一致
					if (oldRecogPicture.getRecognitionHistory().getResult().equals(results[i])) {
						recognitionPictureList.add(new RecognitionPicture(idArray[i], null, operator, new Date(),
								RecognitionPictureStatusConstant.TWO_CHECKED));
						// 保存识别一致的图片
						outputSuccessPicture(results[i], oldRecogPicture.getFilePath(), rootPath);
						// 2次识别结果不同
					} else
						recognitionPictureList.add(new RecognitionPicture(idArray[i], null, operator, new Date(),
								RecognitionPictureStatusConstant.CHECK_DISUNITY));
					// 第1次无法识别 + 第2次识别成功
				} else if (RecognitionPictureStatusConstant.CANNOT_CHECK.equals(oldRecogPicture.getStatus())
						&& !operator.equals(oldRecogPicture.getOperator1()))
					recognitionPictureList.add(new RecognitionPicture(idArray[i], null, operator, new Date(),
							RecognitionPictureStatusConstant.CHECK_DISUNITY));
				// 被3个人请求到，不进行插入处理
				else {
					logger.info("【重复】识别成功");
					logger.info("operator1={}:", oldRecogPicture.getOperator1());
					logger.info("operator2={}:", oldRecogPicture.getOperator2());
					logger.info("operator3={}:", operator);
					logger.info("Picture ID: {}", oldRecogPicture.getId());
					logger.info("oldRecogPicture.getStatus():{}", oldRecogPicture.getStatus());
					logger.info("oldRecogPicture.getRequest_num():{}", oldRecogPicture.getRequest_num());

					// 将ID置空，不做处理
					idArray[i] = null;
				}
				// 无法识别
			} else if (statusArray[i].equals(RecognitionPictureResultsConstant.NOT_RECOGNIZED)) {
				// 第1次无法识别
				if (oldRecogPicture.getStatus().equals(RecognitionPictureStatusConstant.REMAIN_CHECK))
					recognitionPictureList.add(new RecognitionPicture(idArray[i], operator, null, new Date(),
							RecognitionPictureStatusConstant.CANNOT_CHECK));
				// 第1次识别成功 + 第2次无法识别
				else if (oldRecogPicture.getStatus().equals(RecognitionPictureStatusConstant.ONE_CHECKED)
						&& !operator.equals(oldRecogPicture.getOperator1()))
					recognitionPictureList.add(new RecognitionPicture(idArray[i], null, operator, new Date(),
							RecognitionPictureStatusConstant.CHECK_DISUNITY));
				// 第1次无法识别 + 第2次无法识别
				else if (oldRecogPicture.getStatus().equals(RecognitionPictureStatusConstant.CANNOT_CHECK)
						&& !operator.equals(oldRecogPicture.getOperator1()))
					recognitionPictureList.add(new RecognitionPicture(idArray[i], null, operator, new Date(),
							RecognitionPictureStatusConstant.CANNOT_CHECK));
				else {
					logger.info("【重复】无法识别");
					logger.info("operator1={}:", oldRecogPicture.getOperator1());
					logger.info("operator2={}:", oldRecogPicture.getOperator2());
					logger.info("operator3={}:", operator);
					logger.info("Picture ID: {}", oldRecogPicture.getId());
					logger.info("oldRecogPicture.getStatus():{}", oldRecogPicture.getStatus());
					logger.info("oldRecogPicture.getRequest_num():{}", oldRecogPicture.getRequest_num());
					// 将ID置空，不做处理
					idArray[i] = null;
				}
			}

		}
		recognitionHistoryService.updatePicture(operator, idArray, results, statusArray);
		RecognitionPicture[] recognitionPictures = new RecognitionPicture[recognitionPictureList.size()];
		for (RecognitionPicture recognitionPicture : recognitionPictureList)
			recognitionPicture.setRequest_num(0);
		return recognitionPictureDao.updateIfNecessary(recognitionPictureList.toArray(recognitionPictures));

	}

	private void outputSuccessPicture(String result, String oldFilePath, String rootPath) {
		synchronized (result.intern()) {
			try {
				Calendar calendar = Calendar.getInstance();
				// 拿到绝对路径
				String filePath = rootPath + File.separator + DOWNLOAD_PATH + File.separator
						+ com.xuexibao.ops.util.DateUtils.formatDate(new Date(), "yyyy-MM-dd") + File.separator + calendar.get(Calendar.HOUR_OF_DAY)
						+ File.separator + result;
				File folder = new File(filePath);
				if (!folder.exists())
					folder.mkdirs();
				int index = folder.list().length;
				// 解_00000、解_00001、解_00002、解_00003、解_00004
				DecimalFormat dcf = new DecimalFormat("000000");
				dcf.setGroupingUsed(false);
				String fileName = result + "_" + dcf.format(index) + BMP_FORMAT;
				// File file = new File(filePath + File.separator + fileName);
				oldFilePath = rootPath + File.separator + oldFilePath;
				File oldFile = new File(oldFilePath);
				FileInputStream fis = new FileInputStream(oldFile);
				copyFile(fis, fileName, filePath);
			} catch (IOException e) {
				logger.error("保存识别成功图片出错:", e);
			}
		}
	}

	public void savePicture(RecognitionPicture[] recognitionPictures) {
		recognitionPictureDao.insertBatch(Arrays.asList(recognitionPictures));
	}

	public void DecompressAndSave(InputStream is, String fileName, String filePath) throws Exception {
		logger.info("拷贝文件夹到临时目录:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
		File zipFile = copyFile(is, fileName, System.getProperty("java.io.tmpdir"));
		// 1.解压
		File[] files = UNZIPUtil.deCompress(zipFile.getAbsolutePath(), filePath, true, fileName);
		logger.info("完成解压:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
		// 2.存
		File file= null;
		RecognitionPicture[] recognitionPictures = new RecognitionPicture[files.length];
		int index = 0;
		int i = 0;
		logger.info("开始插入数据库:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
		for (int length = files.length / INSERT_PER_SIZE; i < length;) {
			for (int j = 0; j < INSERT_PER_SIZE; j++) {
				index = i * INSERT_PER_SIZE + j;
				file = files[index];
				recognitionPictures[index] = new RecognitionPicture(file.getName(), RecognitionHistoryController.UPLOAD_PATH + File.separator
						+ file.getParentFile().getParentFile().getName() + File.separator + file.getParentFile().getName() + File.separator
						+ file.getName(), new Date());
			}
			RecognitionPicture[] recognPictures = Arrays.copyOfRange(recognitionPictures, i * INSERT_PER_SIZE, ++i * INSERT_PER_SIZE);
			this.savePicture(recognPictures);
		}
		int length = files.length;
		int startPos = length - length % INSERT_PER_SIZE;
		int k = startPos;
		for (; k < length; k++) {
			file = files[k];
			recognitionPictures[k] = new RecognitionPicture(file.getName(), RecognitionHistoryController.UPLOAD_PATH + File.separator
					+ file.getParentFile().getParentFile().getName() + File.separator + file.getParentFile().getName() + File.separator
					+ file.getName(), new Date());
		}
		RecognitionPicture[] recognPictures = Arrays.copyOfRange(recognitionPictures, startPos, length);
		this.savePicture(recognPictures);
		logger.info("插入数据库完成:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
		zipFile.delete();
	}

	private File copyFile(InputStream inStream, String fileName, String filePath) throws IOException {
		FileOutputStream outStream = null;
		try {
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

	public Workbook save2Excel(List<RecognitionAnalysisBydate> recognitionList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		sheet.setDefaultColumnWidth(20);
		int rows = recognitionList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("识别人");
		cell = row.createCell(1);
		cell.setCellValue("识别数量");
		cell = row.createCell(2);
		cell.setCellValue("识别正确数量");
		cell = row.createCell(3);
		cell.setCellValue("标记无法识别数量");
		cell = row.createCell(4);
		cell.setCellValue("识别不一致数量");
		cell = row.createCell(5);
		cell.setCellValue("总识别数量");

		for (int i = 1; i <= rows; i++) {
			RecognitionAnalysisBydate recogAnalysisBydate = recognitionList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);
			cell.setCellValue(recogAnalysisBydate.getOperator());
			cell = row.createCell(1);
			cell.setCellValue(recogAnalysisBydate.getRecognitionCount());
			cell = row.createCell(2);
			cell.setCellValue(recogAnalysisBydate.getRecognitionCorrectCount());
			cell = row.createCell(3);
			cell.setCellValue(recogAnalysisBydate.getRecognitionUnrecognitionCount());
			cell = row.createCell(4);
			cell.setCellValue(recogAnalysisBydate.getRecognitionDisunityCount());
			cell = row.createCell(5);
			cell.setCellValue(recogAnalysisBydate.getRecognitionCount());
		}

		return workBook;
	}

	private void setUnRecognizedReason(RecognitionPicture unRecognitionReason) {
		Integer status = unRecognitionReason.getStatus();
		if (status != null) {
			for (EnumRecognitionResult unRecognitionReasonStatus : EnumRecognitionResult.values()) {
				if (status.equals(unRecognitionReasonStatus.getId())) {
					unRecognitionReason.setUnRecognizedReason(unRecognitionReasonStatus.getDesc());
					break;
				}

			}
		}
	}
}
