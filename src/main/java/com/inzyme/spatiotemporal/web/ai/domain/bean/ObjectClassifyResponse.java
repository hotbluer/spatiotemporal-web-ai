package com.inzyme.spatiotemporal.web.ai.domain.bean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ObjectClassifyResponse
 * @Description: 物体分类返回体
 * @date 2020年3月11日 下午1:52:23
 * 
 * @author Q.JI
 * @version
 * @since JDK 1.8
 */
@Data
public class ObjectClassifyResponse {
	private long log_id;
	private List<ClassifyResults> results;

	@NoArgsConstructor
	@Data
	public static class ClassifyResults {
		/**
		 * 分类名称
		 */
		private String name;

		/**
		 * 置信度
		 */
		private double score;
	}
}
