package com.inzyme.spatiotemporal.web.ai.util;

import java.math.BigDecimal;

/**   
 * @ClassName: SysUtil    
 * @Description: 小工具
 * @date 2020年3月7日 下午9:24:20    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
public class SysUtil {
	
	/**
	 * 
	 * @Title: nvl    
	 * @Description: 对象转字符
	 * @param input
	 * @return  
	 * String
	 */
	public static String nvl(Object input) {
		if (input == null) {
			return "";
		} else {
			return input.toString();
		}
	}
	
	/**
	 * 
	 * @Title: round2    
	 * @Description: 四舍五入取整
	 * @param value
	 * @param inum
	 * @return  
	 * double
	 */
	public static int trunc(double value) {
		BigDecimal big = new BigDecimal(Double.toString(value)).setScale(0, BigDecimal.ROUND_HALF_UP);
		return big.intValue();
	}
	
	/**
	 * 
	 * round:(保留小数的四舍五入). <br/>
	 * 
	 * @param value
	 * @param inum
	 * @return float
	 * @since 1.0
	 */
	public static float round(float value, int inum) {
		double num = Math.pow(10, inum);
		return (float) (Math.round(value * num) / num);
	}

	/**
	 * 
	 * round:(保留小数的四舍五入). <br/>
	 * 
	 * @param value
	 * @param inum
	 * @return double
	 * @since 1.0
	 */
	public static double round(double value, int inum) {
		double num = Math.pow(10, inum);
		return (double) (Math.round(value * num) / num);
	}
}
