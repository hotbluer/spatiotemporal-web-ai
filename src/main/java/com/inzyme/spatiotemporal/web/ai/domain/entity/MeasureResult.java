package com.inzyme.spatiotemporal.web.ai.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.inzyme.spatiotemporal.web.core.domain.entity.CommonEntity;

import lombok.Data;

/**   
 * @ClassName: MeasureResult    
 * @Description: 一拍知长知重测算结果
 * @date 2020年3月7日 下午9:04:04    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/

@TableName("ai_measure_result")
@Data
public class MeasureResult extends CommonEntity implements Serializable{
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = 6966428698693078517L;

	/**
	 * 图像自定义ID
	 */
	private String imgId;
	
	/**
	 * 牲口主体类型
	 */
	private String mainType;
	
	/**
	 * 体长cm
	 */
	private Float length;
	
	/**
	 * 体重kg
	 */
	private Double weight;
	
	/**
	 * 蓄龄（日龄）
	 */
	private Integer age;
	
	/**
	 * 图像文件
	 */
	private String imgFile;
	
	/**
	 * 修正后对的体长cm
	 */
	private Float correctLength;
	
	/**
	 * 修正后对的体重kg
	 */
	private Double correctWeight;
	
	/**
	 * 修正后对的蓄龄（日龄）
	 */
	private Integer correctAge;

}
