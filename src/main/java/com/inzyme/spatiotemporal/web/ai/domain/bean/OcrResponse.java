package com.inzyme.spatiotemporal.web.ai.domain.bean;

import java.util.List;

import com.inzyme.spatiotemporal.web.ai.domain.bean.NumberResponse.Location;
import com.inzyme.spatiotemporal.web.ai.domain.bean.NumberResponse.WordsResult;

import lombok.Data;
import lombok.NoArgsConstructor;

/**   
 * @ClassName: OcrResponse    
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2021年1月17日 下午2:44:50    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
@Data
public class OcrResponse {
    private List<WordsResult> words_result;
    private long log_id;
    private int words_result_num;
    
	@NoArgsConstructor
	@Data
	public static class WordsResult {
		private String words;
	}
}
