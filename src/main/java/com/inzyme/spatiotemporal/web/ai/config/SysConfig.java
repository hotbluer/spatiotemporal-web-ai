package com.inzyme.spatiotemporal.web.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**   
 * @ClassName: SysConfig    
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020年3月7日 下午1:18:42    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
@Component  
@ConfigurationProperties(prefix = "spring.system")
@Data
public class SysConfig {
	private String appId;
	private String authHost;
	private String apiKey;
	private String securetKey;
	private String bodyLengthRpcUrl;
	private String verifyIdcardRpcUrl;
	private Float threshold;
	private String outputFolder;
	private Boolean drawRect;
	private Integer requestExpire;
	private String countPigRpcUrl;
	private Float countPigThreshold;
	private String ocrApiKey;
	private String ocrSecuretKey;
	private String ocrNumberRpcUrl;
	private Float threshold2;
	private String pigSizeUrl;
	private String pigCountUrl;
}
