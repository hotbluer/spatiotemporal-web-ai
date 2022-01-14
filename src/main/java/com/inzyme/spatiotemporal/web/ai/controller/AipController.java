package com.inzyme.spatiotemporal.web.ai.controller;

import com.inzyme.spatiotemporal.web.ai.domain.entity.MeasureResult;
import com.inzyme.spatiotemporal.web.ai.domain.vo.PizeEVo;
import com.inzyme.spatiotemporal.web.ai.service.IAipService;
import com.inzyme.spatiotemporal.web.ai.util.EsUtil;
import com.inzyme.spatiotemporal.web.ai.util.ExcelUtil;
import com.inzyme.spatiotemporal.web.ai.util.ReadUtil;
import com.inzyme.spatiotemporal.web.ai.util.SysUtil;
import com.inzyme.spatiotemporal.web.core.constants.enums.ReturnEnum;
import com.inzyme.spatiotemporal.web.core.domain.vo.ReturnVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**   
 * @ClassName: AipController    
 * @Description: AI服务
 * @date 2020年3月7日 下午2:20:44    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/

@RestController
@CrossOrigin
@RequestMapping("/app/aip")
@Api(tags = "AI服务能力")
public class AipController extends BaseRestController{
	
	@Autowired
	private IAipService aipService;

	@Resource
	RestHighLevelClient client;
	@ApiOperation(value = "测试es", notes = "无")
	@PostMapping("/aa")
	@Transactional
	public String getAA(@ApiParam("keyWord") String keyWord){
//		new EsUtil().eventsSaveOrUpdateDocs(client);
		new EsUtil().topicsSearch(keyWord,client);
		return "成功";
	}

	@ApiOperation(value = "测试docx", notes = "无")
	@PostMapping("/AiOcrInfoByEncry2")
	@Transactional
	public String getAiOcrInfoByEncry2(@RequestParam("imgFile") @ApiParam("待测算照片") MultipartFile imgFile){
		return ReadUtil.readXlsx(imgFile);
	}

	@ApiOperation(value = "知长知重/智能点数-py", notes = "无")
	@PostMapping("/AiOcrInfoByEncry")
	@Transactional
	public ReturnVO getAiOcrInfoByEncry(HttpServletRequest request, HttpServletResponse response,@RequestParam("appId") @ApiParam("appId") String appId,
										@RequestParam("apiCode") @ApiParam("apiCode")String apiCode, @RequestParam("imgFiles") @ApiParam("待测算照片") MultipartFile[] imgFiles) {
//		int res = verifyAppId(request, appId, "measure");
		int res = 0;
		Object[] obj = null;
		List<PizeEVo> aa = new ArrayList<>();
		if(res == 0) {
			if(apiCode.equalsIgnoreCase("PredictWeight")){
				for (MultipartFile imgFile : imgFiles) {
					obj = aipService.calcBodyLength2(appId, imgFile);
					MeasureResult p = (MeasureResult)obj[1];
					PizeEVo s = new PizeEVo();
					BeanUtils.copyProperties(p, s);
					aa.add(s);
				}

//				String sheetTitle = "知长知重";
//				String [] title = new String[]{"体长","体重","畜龄","图片地址"};
//				ExcelUtil.export(sheetTitle,title,aa,response);
				try {
					ExcelUtil.fileStreamOutputBuilder(response,System.currentTimeMillis()+"",PizeEVo.class,aa);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				MeasureResult result = (MeasureResult)obj[1];

				if(null != result) {
					return new ReturnVO<MeasureResult>(ReturnEnum.SUCCESS, result);
				}else {
					return new ReturnVO<MeasureResult>("400", SysUtil.nvl(obj[0]));
				}

			}else if(apiCode.equalsIgnoreCase("CheckNumber")){
				for (MultipartFile imgFile : imgFiles) {
					obj = aipService.count4pig2(imgFile);
				}
				List<int[][]> result = (List<int[][]>)obj[1];

				if(null != result && !result.isEmpty()) {
					return new ReturnVO<List<int[][]>>(ReturnEnum.SUCCESS, result);
				}else {
					return new ReturnVO<List<int[][]>>("400", SysUtil.nvl(obj[0]));
				}
			}else {
				return new ReturnVO<MeasureResult>(ReturnEnum.ERROR_POST_PARAM);
			}
/*			MeasureResult result = (MeasureResult)obj[1];
			if(null != result) {
				return new ReturnVO<MeasureResult>(ReturnEnum.SUCCESS, result);
			}else {
				return new ReturnVO<MeasureResult>("400", SysUtil.nvl(obj[0]));
			}*/
		}else if (res == 2){
			return new ReturnVO<MeasureResult>("400", "您的操作过于频繁，请稍后再试。");
		}else {
			return new ReturnVO<MeasureResult>(ReturnEnum.ERROR_PERMISSION_DENIED);
		}
	}

	@ApiOperation(value = "一拍知长知重-py", notes = "无")
	@PostMapping("/measure2")
	@Transactional
	public ReturnVO<MeasureResult> measure2(HttpServletRequest request, @RequestParam("appId") @ApiParam("appId") String appId,
										   @RequestParam("imgFile") @ApiParam("待测算照片") MultipartFile imgFile) {
//		int res = verifyAppId(request, appId, "measure");
		int res = 0;
		if(res == 0) {
			Object[] obj = aipService.calcBodyLength2(appId, imgFile);
			MeasureResult result = (MeasureResult)obj[1];
			if(null != result) {
				return new ReturnVO<MeasureResult>(ReturnEnum.SUCCESS, result);
			}else {
				return new ReturnVO<MeasureResult>("400", SysUtil.nvl(obj[0]));
			}
		}else if (res == 2){
			return new ReturnVO<MeasureResult>("400", "您的操作过于频繁，请稍后再试。");
		}else {
			return new ReturnVO<MeasureResult>(ReturnEnum.ERROR_PERMISSION_DENIED);
		}
	}

	@ApiOperation(value = "智能点数（猪）-py", notes = "无")
	@PostMapping("/count2")
	@Transactional
	public ReturnVO<List<int[][]>> count4pig2(HttpServletRequest request, @RequestParam("appId") @ApiParam("appId") String appId,
											 @RequestParam("imgFile") @ApiParam("待计数猪照片") MultipartFile imgFile) {
//		int res = verifyAppId(request, appId, "count4pig");
		int res = 0;
		if(res == 0) {
			Object[] obj = aipService.count4pig2(imgFile);
			List<int[][]> result = (List<int[][]>)obj[1];
			if(null != result && !result.isEmpty()) {
				return new ReturnVO<List<int[][]>>(ReturnEnum.SUCCESS, result);
			}else {
				return new ReturnVO<List<int[][]>>("400", SysUtil.nvl(obj[0]));
			}
		}else if (res == 2){
			return new ReturnVO<List<int[][]>>("400", "您的操作过于频繁，请稍后再试。");
		}else {
			return new ReturnVO<List<int[][]>>(ReturnEnum.ERROR_PERMISSION_DENIED);
		}
	}
	
	@ApiOperation(value = "一拍知长知重", notes = "无")
	@PostMapping("/measure")
	@Transactional
	public ReturnVO<MeasureResult> measure(HttpServletRequest request, @RequestParam("appId") @ApiParam("appId") String appId, 
			                               @RequestParam("imgFile") @ApiParam("待测算照片") MultipartFile imgFile) {
//		int res = verifyAppId(request, appId, "measure");
		int res = 0;
		if(res == 0) {
			Object[] obj = aipService.calcBodyLength(appId, imgFile); 
			MeasureResult result = (MeasureResult)obj[1];
			if(null != result) {
				return new ReturnVO<MeasureResult>(ReturnEnum.SUCCESS, result);
			}else {
				return new ReturnVO<MeasureResult>("400", SysUtil.nvl(obj[0]));
			}
		}else if (res == 2){
			return new ReturnVO<MeasureResult>("400", "您的操作过于频繁，请稍后再试。");
		}else {
			return new ReturnVO<MeasureResult>(ReturnEnum.ERROR_PERMISSION_DENIED);
		}
	}
	
	
	@ApiOperation(value = "智能点数（猪）", notes = "无")
	@PostMapping("/count")
	@Transactional
	public ReturnVO<List<int[][]>> count4pig(HttpServletRequest request, @RequestParam("appId") @ApiParam("appId") String appId, 
			                               @RequestParam("imgFile") @ApiParam("待计数猪照片") MultipartFile imgFile) {
//		int res = verifyAppId(request, appId, "count4pig");
		int res = 0;
		if(res == 0) {
			Object[] obj = aipService.count4pig(imgFile); 
			List<int[][]> result = (List<int[][]>)obj[1];
			if(null != result && !result.isEmpty()) {
				return new ReturnVO<List<int[][]>>(ReturnEnum.SUCCESS, result);
			}else {
				return new ReturnVO<List<int[][]>>("400", SysUtil.nvl(obj[0]));
			}
		}else if (res == 2){
			return new ReturnVO<List<int[][]>>("400", "您的操作过于频繁，请稍后再试。");
		}else {
			return new ReturnVO<List<int[][]>>(ReturnEnum.ERROR_PERMISSION_DENIED);
		}
	}
	
	
	@ApiOperation(value = "耳标号数字识别", notes = "无")
	@PostMapping("/ocrnumber")
	@Transactional
	public ReturnVO<String[]> ocrnumber(HttpServletRequest request, @RequestParam("appId") @ApiParam("appId") String appId, 
			                               @RequestParam("imgFile") @ApiParam("待识别耳标照片") MultipartFile imgFile) {
		int res = verifyAppId(request, appId, "ocrnumber");		
		if(res == 0) {
			Object[] obj = aipService.ocrnumber(imgFile); 
			String[] result = (String[])obj[1];
			if(null != result && result.length > 0) {
				return new ReturnVO<String[]>(ReturnEnum.SUCCESS, result);
			}else {
				return new ReturnVO<String[]>("400", SysUtil.nvl(obj[0]));
			}
		}else if (res == 2){
			return new ReturnVO<String[]>("400", "您的操作过于频繁，请稍后再试。");
		}else {
			return new ReturnVO<String[]>(ReturnEnum.ERROR_PERMISSION_DENIED);
		}
	}
	

	@ApiOperation(value = "订正测算结果", notes = "无")
	@PostMapping("/correct/{imgId}")
	@Transactional
	public ReturnVO<Void> correct(@RequestParam("appId") @ApiParam("appId") String appId, 
			                      @RequestParam("imgId") @ApiParam("测算照片ID") String imgId,
								  @RequestParam("length") @ApiParam(value = "体长CM",example = "1") Float length,
								  @RequestParam("weight") @ApiParam(value = "体重kg",example = "1") Double weight,
								  @RequestParam("age") @ApiParam(value = "育龄（日）",example = "1") Integer age) {

		if(verifyAppId(appId)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("imgId", imgId);
			map.put("length", length);
			map.put("weight", weight);
			map.put("age", age);			
			int update = aipService.fixCorrect(map);
			if(update > 0) {
				return new ReturnVO<>(ReturnEnum.SUCCESS);
			}else {
				return new ReturnVO<>(ReturnEnum.ERROR_NOT_FOUND);
			}			
		}else {
			return new ReturnVO<Void>(ReturnEnum.ERROR_PERMISSION_DENIED);
		}
	}
	
	
	@ApiOperation(value = "验证是否身份证", notes = "无")
	@PostMapping("/verifycard")
	@Transactional
	public ReturnVO<String> verifyIdcard(@RequestParam("appId") @ApiParam("appId") String appId, 
			                               @RequestParam("imgFile") @ApiParam("待验证照片") MultipartFile imgFile) {
		
		if(verifyAppId(appId)) {
			String result = aipService.verifyIdcard(imgFile);
			if(StringUtils.isNotEmpty(result)) {
				return new ReturnVO<String>(ReturnEnum.SUCCESS, result);
			}else {
				return new ReturnVO<String>(ReturnEnum.ERROR_POST_PARAM);
			}
		}else {
			return new ReturnVO<String>(ReturnEnum.ERROR_PERMISSION_DENIED);
		}
	}	
}
