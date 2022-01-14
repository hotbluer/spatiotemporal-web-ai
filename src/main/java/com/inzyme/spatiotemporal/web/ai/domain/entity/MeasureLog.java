package com.inzyme.spatiotemporal.web.ai.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.inzyme.spatiotemporal.web.core.domain.entity.CommonEntity;

import lombok.Data;


/**
 * 
 * @ClassName: MeasureLog    
 * @Description: 一拍知长知重测算结果日志
 * @date 2020年3月10日 下午3:26:13    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
 */

@TableName("ai_measure_log")
@Data
public class MeasureLog extends CommonEntity implements Serializable{
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = -3478613428244331794L;

	/**
	 * appID
	 */
	private String appId;

	/**
	 * 图像自定义ID
	 */
	private String imgId;
	
	/**
	 * 牲口主体类型
	 */
	private String mainType;
	
	private Integer mainWidth;
	
	private Integer mainHeight;
	
	private String referType;
	
	private Integer referWidth;
	
	private Integer referHeight;
	
	/**
	 * 测算操作结果（0：测算成功；1：无标的物；2：缺牲畜主体；3：缺参照物；4：不支持的牲畜类型；5：测算失败）
	 */
	private Integer opRes;
}
