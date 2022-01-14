package com.inzyme.spatiotemporal.web.ai.util;

import java.util.HashMap;
import java.util.Map;

/**   
 * @ClassName: AiConstants    
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020年3月7日 下午11:32:12    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
public class AiConstants {
	
	/**
	 * 1元硬币直径（mm）
	 */
	public static final Float COIN_DIAM = 25F;
	
	/**
	 * 固定大小卡片宽度（mm）身份证、银行卡等
	 */
	public static final Float CARD_WIDTH = 85.5F;
	
	/**
	 * 固定大小卡片高度（mm）
	 */
	public static final Float CARD_HEIGHT = 54f;
	
	/**
	 * AI服务-appid应用缓存标识
	 */
	public static final String SPATIOTEMPORAL_AI_APPID = "spatiotemporal:ai:appid";
	
	/**
	 * AI服务-token缓存标识
	 */
	public static final String SPATIOTEMPORAL_AI_TOKEN = "spatiotemporal:ai:token";
	
	/**
	 * AI服务-请求间隔
	 */
	public static final String SPATIOTEMPORAL_AI_Expire = "spatiotemporal:ai:expire";
	
	/**
	 * AI应用标识
	 */
	public static final String AITOKEN_BDAPPKEY = "aibdservices";
	
	/**
	 * OCR应用标识
	 */
	public static final String AITOKEN_OCRAPPKEY = "ocrbdservices";
	
	/**
	 * 身份证识别结果
	 */
	public static final Map<String, String> IDCARD_RES = new HashMap<String, String>() {
		{
			put("zm", "front");
			put("fm", "reverse");
			put("[default]", "not");
		}
	};

}
