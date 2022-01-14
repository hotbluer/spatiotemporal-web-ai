package com.inzyme.spatiotemporal.web.ai.service.impl;

import com.inzyme.spatiotemporal.web.ai.config.SysConfig;
import com.inzyme.spatiotemporal.web.ai.dao.AiDictAgeWeightDao;
import com.inzyme.spatiotemporal.web.ai.dao.MeasureLogDao;
import com.inzyme.spatiotemporal.web.ai.dao.MeasureResultDao;
import com.inzyme.spatiotemporal.web.ai.domain.bean.NumberResponse;
import com.inzyme.spatiotemporal.web.ai.domain.bean.ObjectClassifyResponse;
import com.inzyme.spatiotemporal.web.ai.domain.bean.ObjectClassifyResponse.ClassifyResults;
import com.inzyme.spatiotemporal.web.ai.domain.bean.ObjectDetectionResponse;
import com.inzyme.spatiotemporal.web.ai.domain.bean.ObjectDetectionResponse.ResultsBean;
import com.inzyme.spatiotemporal.web.ai.domain.bean.ResponseTokenBean;
import com.inzyme.spatiotemporal.web.ai.domain.entity.AiDictAgeWeight;
import com.inzyme.spatiotemporal.web.ai.domain.entity.MeasureLog;
import com.inzyme.spatiotemporal.web.ai.domain.entity.MeasureResult;
import com.inzyme.spatiotemporal.web.ai.service.IAipService;
import com.inzyme.spatiotemporal.web.ai.service.IAuthService;
import com.inzyme.spatiotemporal.web.ai.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;

/**
 * @ClassName: AipServiceImpl
 * @Description: AI服务能力实现
 * @date 2020年3月8日 下午10:32:02
 * 
 * @author Q.JI
 * @version
 * @since JDK 1.8
 */
@Slf4j
@Service
public class AipServiceImpl implements IAipService {

	@Autowired
	private IAuthService authService;

	@Autowired
	private SysConfig syscfg;

	@Autowired
	private AiDictAgeWeightDao aiDictAgeWeightDao;

	@Autowired
	private MeasureResultDao measureResultDao;

	@Autowired
	private MeasureLogDao measureLogDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.inzyme.spatiotemporal.web.ai.service.IBodyLengthService#calcBodyLength(
	 * java.lang.String)
	 */
	@Override
	public Object[] calcBodyLength2(String appId, MultipartFile imgFile) {

		Object[] result = new Object[2];
		result[0] = new String("知长知重AI测算失败，请重试！");
		result[1] = null;

		try {
			String filePath = upload(imgFile);

			if (StringUtils.isNotEmpty(filePath)) {
				ResponseTokenBean token = authService.getAuth(syscfg.getApiKey(), syscfg.getSecuretKey());

				if (null != token) {
					String base64Img = Base64Util.encode(FileUtil.readFileByBytes(filePath));

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("image", base64Img);
					map.put("threshold", syscfg.getThreshold());
					String param = GsonUtils.toJson(map);

					String postResult = HttpUtil.post(syscfg.getPigSizeUrl(),param);
//					String postResult = HttpUtil.post(syscfg.getBodyLengthRpcUrl(),token.getAccess_token(),param);

					result = calcRect(appId, filePath, GsonUtils.fromJson(postResult, ObjectDetectionResponse.class));
				} else {
					result[0] = new String("知长知重AI鉴权失败，请重试！");
					log.info("知长知重AI鉴权失败！");
				}
			} else {
				result[0] = new String("知长知重AI测算必须上传图像文件！");
				log.info("一拍知长知重必须上传图像文件！");
			}
		} catch (Exception ex) {
			log.error("一拍知长知重测算出现异常！", ex);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.inzyme.spatiotemporal.web.ai.service.IAipService#count4pig(org.
	 * springframework.web.multipart.MultipartFile)
	 */
	public Object[] count4pig2(MultipartFile imgFile) {
		Object[] result = new Object[2];
		result[0] = new String("智能点数（猪）失败，请重试！");
		result[1] = null;

		try {
			String filePath = upload(imgFile);

			if (StringUtils.isNotEmpty(filePath)) {
				ResponseTokenBean token = authService.getAuth(syscfg.getApiKey(), syscfg.getSecuretKey());

				if (null != token) {
					String base64Img = Base64Util.encode(FileUtil.readFileByBytes(filePath));

/*					BASE64Encoder encoder = new BASE64Encoder();
					String base64Img = encoder.encode(FileUtil.readFileByBytes(filePath));*/

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("image", base64Img);
					map.put("threshold", 0.6);
					String param = GsonUtils.toJson(map);

					String postResult = HttpUtil.post(syscfg.getPigCountUrl(), param);
//					String postResult = HttpUtil.post(syscfg.getCountPigRpcUrl(),token.getAccess_token(),param);

					result = coutRect(filePath, GsonUtils.fromJson(postResult, ObjectDetectionResponse.class));
				} else {
					result[0] = new String("智能点数（猪）鉴权失败，请重试！");
					log.info("智能点数（猪）鉴权失败！");
				}
			} else {
				result[0] = new String("智能点数（猪）必须上传图像文件！");
				log.info("智能点数（猪）必须上传图像文件！");
			}
		} catch (Exception ex) {
			log.error("智能点数（猪）测算出现异常！", ex);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.inzyme.spatiotemporal.web.ai.service.IBodyLengthService#calcBodyLength(
	 * java.lang.String)
	 */
	@Override
	public Object[] calcBodyLength(String appId, MultipartFile imgFile) {

		Object[] result = new Object[2];
		result[0] = new String("知长知重AI测算失败，请重试！");
		result[1] = null;

		try {
			String filePath = upload(imgFile);

			if (StringUtils.isNotEmpty(filePath)) {
				ResponseTokenBean token = authService.getAuth(syscfg.getApiKey(), syscfg.getSecuretKey());

				if (null != token) {
					String base64Img = Base64Util.encode(FileUtil.readFileByBytes(filePath));

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("image", base64Img);
					map.put("threshold", syscfg.getThreshold());
					String param = GsonUtils.toJson(map);
//					Map<String, Object> map = new HashMap<String, Object>();
//					map.put("image", base64Img);
//					map.put("threshold", 0.8);
//					String param = GsonUtils.toJson(map);

//					String postResult = HttpUtil.post(syscfg.getBodyLengthRpcUrl(), token.getAccess_token(), param);
//					String postResult = HttpUtil.postGeneralUrl ("http://192.168.60.123:80/pig_size","application/x-www-form-urlencoded", param, "UTF-8");
					String postResult = HttpUtil.post(syscfg.getBodyLengthRpcUrl(),param);

					result = calcRect(appId, filePath, GsonUtils.fromJson(postResult, ObjectDetectionResponse.class));
				} else {
					result[0] = new String("知长知重AI鉴权失败，请重试！");
					log.info("知长知重AI鉴权失败！");
				}
			} else {
				result[0] = new String("知长知重AI测算必须上传图像文件！");
				log.info("一拍知长知重必须上传图像文件！");
			}
		} catch (Exception ex) {
			log.error("一拍知长知重测算出现异常！", ex);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inzyme.spatiotemporal.web.ai.service.IAipService#count4pig(org.
	 * springframework.web.multipart.MultipartFile)
	 */
	public Object[] count4pig(MultipartFile imgFile) {
		Object[] result = new Object[2];
		result[0] = new String("智能点数（猪）失败，请重试！");
		result[1] = null;

		try {
			String filePath = upload(imgFile);

			if (StringUtils.isNotEmpty(filePath)) {
				ResponseTokenBean token = authService.getAuth(syscfg.getApiKey(), syscfg.getSecuretKey());

				if (null != token) {
					String base64Img = Base64Util.encode(FileUtil.readFileByBytes(filePath));

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("image", base64Img);
					map.put("threshold", 0.2);
					String param = GsonUtils.toJson(map);

//					String postResult = HttpUtil.post(syscfg.getCountPigRpcUrl(), token.getAccess_token(), param);
//					String postResult = HttpUtil.postGeneralUrl ("http://192.168.60.123:80/pig_count","application/x-www-form-urlencoded", param, "UTF-8");
					String postResult = HttpUtil.post(syscfg.getCountPigRpcUrl(), param);

					result = coutRect(filePath, GsonUtils.fromJson(postResult, ObjectDetectionResponse.class));
				} else {
					result[0] = new String("智能点数（猪）鉴权失败，请重试！");
					log.info("智能点数（猪）鉴权失败！");
				}
			} else {
				result[0] = new String("智能点数（猪）必须上传图像文件！");
				log.info("智能点数（猪）必须上传图像文件！");
			}
		} catch (Exception ex) {
			log.error("智能点数（猪）测算出现异常！", ex);
		}

		return result;
	}

	/**
	 * 
	 * @Title: upload
	 * @Description: 文件落地
	 * @param multipartFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException           String
	 */
	private String upload(MultipartFile multipartFile) throws FileNotFoundException, IOException {
		return upload("", multipartFile);
	}

	/**
	 * 
	 * @Title: upload
	 * @Description: 带子目录的文件落地
	 * @param multipartFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException           String
	 */
	private String upload(String subFolder, MultipartFile multipartFile) throws FileNotFoundException, IOException {
		String outputFileName = "";
		String type = multipartFile.getContentType();
		if (type.indexOf("image") > -1) {
			File uploadOutFile = new File(syscfg.getOutputFolder() + subFolder + File.separator);
			if (!uploadOutFile.exists())
				uploadOutFile.mkdirs();

			String oriFileName = multipartFile.getOriginalFilename();
			int end = oriFileName.lastIndexOf(".");
			String filename = UUID.randomUUID().toString() + oriFileName.substring(end);
			outputFileName = uploadOutFile.getAbsolutePath() + File.separator + filename;
			FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(outputFileName));
		}

		return outputFileName;
	}

	/**
	 * 
	 * @Title: calcRect
	 * @Description: 测算体长
	 * @param file
	 * @param resBean
	 * @throws Exception void
	 */
	private Object[] calcRect(String appId, String file, ObjectDetectionResponse resBean) throws Exception {
		String message = "知长知重AI测算失败，请重试！";
		MeasureResult result = null;
		Object[] returnObj = new Object[] { message, result };

		if (null != resBean) {
			// 主体、参照物类型
			String mainType = "", referType = "";
			// 主体长、宽
			int mainWidth = 0, mainHeight = 0;
			// 参照物长、宽
			int referWidth = 0, referHeight = 0;
			// 测算操作结果
			Integer opRes = 0;

			File outFile = null;
			BufferedImage image = null;
			Graphics graphics = null;
			Font font = null;
			if (syscfg.getDrawRect()) {
				int end = file.lastIndexOf(".");
				String verFile = file.substring(0, end) + "_verify.jpg";
				outFile = new File(verFile);
				image = ImageIO.read(new File(file));
				graphics = image.getGraphics();
				font = new Font("宋体", Font.BOLD, 14);
			}

			List<ResultsBean> list = resBean.getResults();
			if (null != list && list.size() > 0) {
				for (ResultsBean res : list) {
					log.info("原图中发现 {} ，其外接矩形长：{}px， 宽：{}px.", res.getName(), res.getLocation().getWidth(),
							res.getLocation().getHeight());

					if (!res.getName().equalsIgnoreCase("coin") && res.getName().indexOf("card") < 0) {
						mainType = res.getName();
						mainWidth = res.getLocation().getWidth();
						mainHeight = res.getLocation().getHeight();
					} else {
						referType = res.getName();
						referWidth = res.getLocation().getWidth();
						referHeight = res.getLocation().getHeight();
					}

					if (syscfg.getDrawRect()) {
						graphics.setColor(Color.BLUE);
						graphics.setFont(font);
						graphics.drawString(res.getName(), res.getLocation().getLeft(), res.getLocation().getTop());
						graphics.drawRect(res.getLocation().getLeft(), res.getLocation().getTop(),
								res.getLocation().getWidth(), res.getLocation().getHeight());
					}
				}

				if (syscfg.getDrawRect()) {
					FileOutputStream out = new FileOutputStream(outFile);
					ImageIO.write(image, "jpg", out);
					if (null != out) {
						out.close();
					}
					log.info("用于检查的外接矩形图已生到 " + outFile.getAbsolutePath() + " ，请查看。");
				}

				if (StringUtils.isNotEmpty(mainType) && mainWidth > 0 && mainHeight > 0
						&& StringUtils.isNotEmpty(referType) && referWidth > 0 && referHeight > 0) {
					Float realReferWid = 0F;
					Float realReferHei = 0F;
					if (referType.indexOf("card") > -1) {
						realReferWid = AiConstants.CARD_WIDTH;
						realReferHei = AiConstants.CARD_HEIGHT;
					} else if (referType.equals("coin")) {
						realReferWid = AiConstants.COIN_DIAM;
						realReferHei = AiConstants.COIN_DIAM;
					}

					Float realWidth = SysUtil.round((realReferWid * mainWidth) / referWidth / 10, 1); // 真实体长cm
					Float realHeight = SysUtil.round((realReferHei * mainHeight) / referHeight / 10, 1);

					Double weight = 0d; // 体重 kg

					if (mainType.equals("pig")) {
						// 猪体重算法(kg)：(体长cm * 胸围cm) / 142或156或162 营养好：142， 营养中等：156， 营养不良：162
						weight = SysUtil.round((realWidth * realHeight * 1.5) / 156, 2);
					} else if (mainType.equals("cattle")) {
						// 牛体重算法(kg)：采用肉或肉乳兼用牛算法 ， 胸围m * 胸围m * 体长m * 100
						weight = SysUtil.round(Math.pow((realHeight / 10 * 1.5), 2) * (realWidth / 10) * 100, 2);
					} else {
						opRes = 4;
						message = "暂不支持测算的牲畜类型，知长知重AI无法测算，请联系管理员！";
						log.info("暂不支持测算的牲畜类型：{}", mainType);
					}

					if (realWidth > 0 && weight > 0) { // 已算出长、重
						result = new MeasureResult();

						result.setMainType(mainType);
						result.setImgId(NoUtils.generateNo(mainType.substring(0, 1)));
						result.setLength(realWidth);
						result.setWeight(weight);
						int end = file.lastIndexOf(File.separator);
						result.setImgFile(file.substring(end + 1));
						result.setCreateUser("AI");
						result.setCreateTime(new Date());

						List<AiDictAgeWeight> dicList = aiDictAgeWeightDao.queryByMainTypeWeight(result.getMainType(),
								result.getWeight());
						if (!dicList.isEmpty()) {
							result.setAge(dicList.get(0).getAge());
						}
//						measureResultDao.insert(result);
						opRes = 0;
/*						log.info("测算成功 - 牲畜类型:{}, imgId:{}, 体长:{}cm, 体重:{}kg, 畜龄:{}日.", mainType, result.getImgId(),
								result.getLength(), result.getWeight(), result.getAge());*/
						log.info("-------测算成功 - 牲畜类型:{}, imgId:{}, 体长:{}cm, 体重:{}kg, 畜龄:{}日.", mainType, result.getImgFile(),
								result.getLength(), result.getWeight(), result.getAge());

				/*		File aa = new File("c:\\portal\\ai\\" + "11.txt");
						byte[] buff1 = new byte[]{};
							String message2= "牲畜类型:" + mainType +" imgId:" + result.getImgFile()+" 体长:cm:"  + result.getLength()+
									" 体重:{}kg:" +result.getWeight()+" 畜龄:{}日:" +result.getAge()+"\r\n";
							buff1 = message2.getBytes();
							FileOutputStream out = new FileOutputStream(aa, true);
							out.write(buff1);*/

					} else {
						opRes = 5;
						message = "知长知重AI测算失败，请重拍！";
						log.info("知长知重AI测算失败，体长：{}cm， 体重：{}kg.", realWidth, weight);
					}
				} else {
					if (StringUtils.isEmpty(mainType) || mainWidth <= 0 || mainHeight <= 0) {
						opRes = 2;
						message = "未发现牲畜主体，知长知重AI无法测算，请重拍！";
					}
					if (StringUtils.isEmpty(referType) || referWidth <= 0 || referHeight <= 0) {
						opRes = 3;
						message = "未发现参照物（银行卡或1元硬币），知长知重AI无法测算，请重拍！";
					}
					log.warn(
							"计算参数缺失，知长知重AI无法测算，请检查图片！mainType:{}, mainWidth:{}, mainHeight:{}, 参照采用-referType:{}, referWidth:{}, referHeight:{}.",
							mainType, mainWidth, mainHeight, referType, referWidth, referHeight);
				}
			} else {
				opRes = 1;
				message = "图中未发现任何牲畜或参照物，请重拍！";
				log.warn(message);
			}

			MeasureLog measureLog = new MeasureLog();
			measureLog.setAppId(appId);
			measureLog.setImgId(
					null != result ? result.getImgId() : file.substring(file.lastIndexOf(File.separator) + 1));
			measureLog.setMainType(mainType);
			measureLog.setMainWidth(mainWidth);
			measureLog.setMainHeight(mainHeight);
			measureLog.setReferType(referType);
			measureLog.setReferWidth(referWidth);
			measureLog.setReferHeight(referHeight);
			measureLog.setCreateUser(null != result ? result.getCreateUser() : "AI");
			measureLog.setCreateTime(null != result ? result.getCreateTime() : new Date());
			measureLog.setOpRes(opRes);
//			measureLogDao.insert(measureLog);
		}

		returnObj[0] = message;
		returnObj[1] = result;

		return returnObj;
	}

	/**
	 * 
	 * @Title: coutRect
	 * @Description: 智能点数（猪）
	 * @param file
	 * @param resBean
	 * @return
	 * @throws Exception Object[]
	 */
	private Object[] coutRect(String file, ObjectDetectionResponse resBean) throws Exception {
		String message = "智能点数（猪）失败，请重试！";
		List<int[][]> countList = new ArrayList<int[][]>();
		Object[] returnObj = new Object[] { message, countList };

		if (null != resBean) {
			File outFile = null;
			BufferedImage image = null;
			Graphics graphics = null;
			Font font = null;
			if (syscfg.getDrawRect()) {
				int end = file.lastIndexOf(".");
				String verFile = file.substring(0, end) + "_cntverify.jpg";
				outFile = new File(verFile);
				image = ImageIO.read(new File(file));
				graphics = image.getGraphics();
				font = new Font("宋体", Font.BOLD, 14);
			}

			List<ResultsBean> list = resBean.getResults();
			if (null != list && list.size() > 0) {

				for (ResultsBean res : list) {
					log.info("原图中发现 {} ，其外接矩形长：{}px， 宽：{}px.", res.getName(), res.getLocation().getWidth(),
							res.getLocation().getHeight());
					if (res.getName().equalsIgnoreCase("pig")) {
						int x = res.getLocation().getLeft() + res.getLocation().getWidth() / 2;
						int y = res.getLocation().getTop() + res.getLocation().getHeight() / 2;
						countList.add(new int[][] { { x, y } });
						if (syscfg.getDrawRect()) {
							graphics.setColor(Color.BLUE);
							graphics.setFont(font);
							graphics.drawString("●", x, y);
							graphics.drawRect(res.getLocation().getLeft(), res.getLocation().getTop(),
									res.getLocation().getWidth(), res.getLocation().getHeight());
						}
					}
				}

				if (null == countList || countList.isEmpty()) {
					message = "图中未发现可计数的猪，请重拍！";
				}

				if (syscfg.getDrawRect()) {
					FileOutputStream out = new FileOutputStream(outFile);
					ImageIO.write(image, "jpg", out);
					if (null != out) {
						out.close();
					}
					log.info("用于检查的外接矩形图已生到 " + outFile.getAbsolutePath() + " ，请查看。");
				}
				log.info("智能点数（猪）完成，共计 {} 头。", countList.size());

			} else {
				message = "图中未发现可识别的牲畜，请重拍！";
				log.warn(message);
			}
		}

		returnObj[0] = message;
		returnObj[1] = countList;

		return returnObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.inzyme.spatiotemporal.web.ai.service.IBodyLengthService#fixCorrect(java.
	 * util.Map)
	 */
	@Override
	public int fixCorrect(Map para) {
		int flg = measureResultDao.updateForImgId(para);
		log.info("imgId:{}，修订牲畜体长、体重、畜龄成功条目数：{}。", SysUtil.nvl(para.get("imgId")), flg);
		return flg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inzyme.spatiotemporal.web.ai.service.IAipService#verifyIdcard(org.
	 * springframework.web.multipart.MultipartFile)
	 */
	@Override
	public String verifyIdcard(MultipartFile imgFile) {
		String result = null;

		try {
			String filePath = upload("card", imgFile);

			if (StringUtils.isNotEmpty(filePath)) {
				ResponseTokenBean token = authService.getAuth(syscfg.getApiKey(), syscfg.getSecuretKey());

				if (null != token) {
					String base64Img = Base64Util.encode(FileUtil.readFileByBytes(filePath));

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("image", base64Img);
					map.put("top_num", 1); // 只取最高置信度那条
					String param = GsonUtils.toJson(map);

					String postResult = HttpUtil.post(syscfg.getVerifyIdcardRpcUrl(), token.getAccess_token(), param);
					ObjectClassifyResponse classify = GsonUtils.fromJson(postResult, ObjectClassifyResponse.class);
					if (null != classify) {
						List<ClassifyResults> list = classify.getResults();
						if (list.size() > 0) {
							result = AiConstants.IDCARD_RES.get(list.get(0).getName());
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error("验证是否为身份证出现异常！", ex);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inzyme.spatiotemporal.web.ai.service.IAipService#ocrnumber(org.
	 * springframework.web.multipart.MultipartFile)
	 */
	@Override
	public Object[] ocrnumber(MultipartFile imgFile) {
		Object[] result = new Object[2];
		result[0] = new String("数字标号识别失败，请重试！");
		result[1] = null;

		try {
			String filePath = upload(imgFile);

			if (StringUtils.isNotEmpty(filePath)) {
				ResponseTokenBean token = authService.getAuth(AiConstants.AITOKEN_OCRAPPKEY, syscfg.getOcrApiKey(),
						syscfg.getOcrSecuretKey());

				if (null != token) {
					String[] words = new String[] {null, null};
					
					String base64Img = Base64Util.encode(FileUtil.readFileByBytes(filePath));
					String imgParam = URLEncoder.encode(base64Img, "UTF-8");
					String param = "image=" + imgParam;
//				     param = param + "&detect_direction=true";
					
					String postResult = HttpUtil.post(syscfg.getOcrNumberRpcUrl(), token.getAccess_token(), param);
					NumberResponse number1 = GsonUtils.fromJson(postResult, NumberResponse.class);
					if (null != number1 && number1.getWords_result_num() > 0 && !number1.getWords_result().isEmpty()) {						
						words[0] = number1.getWords_result().get(0).getWords();
					}					
					
					BufferedImage image = ImageIO.read(new File(filePath));
					image = ImageUtil.rotateImage(image, -90);
					String tmpFile = syscfg.getOutputFolder() + UUID.randomUUID().toString() + ".jpg";
					ImageIO.write(image, "jpg", new File(tmpFile));		
					
					base64Img = Base64Util.encode(FileUtil.readFileByBytes(tmpFile));
					imgParam = URLEncoder.encode(base64Img, "UTF-8");
					param = "image=" + imgParam;
					
					postResult = HttpUtil.post(syscfg.getOcrNumberRpcUrl(), token.getAccess_token(), param);
					NumberResponse number2 = GsonUtils.fromJson(postResult, NumberResponse.class);
					
					if (null != number2 && number2.getWords_result_num() > 0 && !number2.getWords_result().isEmpty()) {
						words[1] = number2.getWords_result().get(0).getWords();
					}
					
					if(!StringUtils.isEmpty(words[0]) || !StringUtils.isEmpty(words[1])) {
						result[1] = words;
					}else {
						result[0] = new String("没有检测到数字标号，请重试！");
						log.info("没有检测到数字标号，请重试！");
					}
					
					FileUtils.forceDelete(new File(filePath));
					FileUtils.forceDelete(new File(tmpFile));
				} else {
					result[0] = new String("数字标号识别失败，请重试！");
					log.info("数字标号识别失败，请重试！");
				}
			} else {
				result[0] = new String("数字标号识别必须上传图像文件！");
				log.info("数字标号识别必须上传图像文件！");
			}
		} catch (Exception ex) {
			log.error("数字标号识别出现异常！", ex);
		}

		return result;
	}
}
