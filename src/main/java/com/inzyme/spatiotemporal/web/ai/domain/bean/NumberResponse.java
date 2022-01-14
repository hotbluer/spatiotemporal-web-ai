package com.inzyme.spatiotemporal.web.ai.domain.bean;

import java.util.List;



import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: NumberResponse
 * @Description: 数字识别返回值
 * @date 2020年6月16日 下午11:27:03
 * 
 * @author Q.JI
 * @version
 * @since JDK 1.8
 */

@Data
public class NumberResponse {
	private long log_id;
	private int direction;
	private int words_result_num;
	private List<WordsResult> words_result;

	@NoArgsConstructor
	@Data
	public static class WordsResult {
		private Location location;
		private String words;
	}

	@NoArgsConstructor
	@Data
	public static class Location {
		private int width;
		private int top;
		private int left;
		private int height;
	}
}
