package com.inzyme.spatiotemporal.web.ai.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.inzyme.spatiotemporal.web.ai.domain.entity.MeasureResult;

/**
 * 
 * @ClassName: IAipService    
 * @Description: AI服务能力
 * @date 2020年3月7日 下午10:06:10    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
 */
public interface IAipService {

	/**
	 *
	 * @Title: calcBodyLength2
	 * @Description: 据图算体长体重,python
	 * @param filePath
	 * @return
	 * String
	 */
	public Object[] calcBodyLength2(String appId, MultipartFile imgFile);

	/**
	 *
	 * @Title: count4pig2
	 * @Description: 智能点数（猪）,python
	 * @param imgFile
	 * @return
	 * Object[]
	 */
	public Object[] count4pig2(MultipartFile imgFile);

	/**
	 * 
	 * @Title: calcBodyLength    
	 * @Description: 据图算体长体重
	 * @param filePath
	 * @return  
	 * String
	 */
	public Object[] calcBodyLength(String appId, MultipartFile imgFile);
	
	/**
	 * 
	 * @Title: count4pig    
	 * @Description: 智能点数（猪）
	 * @param imgFile
	 * @return  
	 * Object[]
	 */
	public Object[] count4pig(MultipartFile imgFile);
	
	/**
	 * 
	 * @Title: ocrnumber    
	 * @Description: 耳标数字识别
	 * @param imgFile
	 * @return  
	 * Object[]
	 */
	public Object[] ocrnumber(MultipartFile imgFile);
	
	/**
	 * 
	 * @Title: fixCorrect    
	 * @Description: 订正为正确的值 
	 * @param para
	 * @return  
	 * int
	 */
	public int fixCorrect(Map para);
	
	/**
	 * 
	 * @Title: verifyIdcard    
	 * @Description: 验证是否有身份证
	 * @param imgFile
	 * @return  非、正面、反面
	 * String
	 */
	public String verifyIdcard(MultipartFile imgFile);
}
