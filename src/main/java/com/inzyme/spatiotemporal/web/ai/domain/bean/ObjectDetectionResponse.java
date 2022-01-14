package com.inzyme.spatiotemporal.web.ai.domain.bean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**   
 * @ClassName: ObjectDetectionResponse    
 * @Description: 物体检测响应体
 * @date 2020年3月7日 下午3:40:36    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/

@Data
public class ObjectDetectionResponse {
	private long log_id;
	private List<ResultsBean> results;

	@NoArgsConstructor
	@Data
	public static class ResultsBean {
		private LocationBean location;
		/**
		 * 分类名称
		 */
		private String name;
		
		/**
		 * 置信度
		 */
		private double score;

		@NoArgsConstructor
		@Data
		public static class LocationBean {
			/**
			 * 检测到的目标主体区域的高度
			 */
			private int height;
			
			/**
			 * 检测到的目标主体区域到图片左边界的距离
			 */
			private int left;
			
			/**
			 * 检测到的目标主体区域到图片上边界的距离
			 */
			private int top;
			
			/**
			 * 检测到的目标主体区域的宽度
			 */
			private int width;
		}
	}
}
