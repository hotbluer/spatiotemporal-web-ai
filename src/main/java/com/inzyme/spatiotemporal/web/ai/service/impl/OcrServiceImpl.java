package com.inzyme.spatiotemporal.web.ai.service.impl;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.inzyme.spatiotemporal.web.ai.domain.bean.OcrResponse;
import com.inzyme.spatiotemporal.web.ai.domain.bean.ResponseTokenBean;
import com.inzyme.spatiotemporal.web.ai.util.Base64Util;
import com.inzyme.spatiotemporal.web.ai.util.FileUtil;
import com.inzyme.spatiotemporal.web.ai.util.GsonUtils;
import com.inzyme.spatiotemporal.web.ai.util.HttpUtil;

/**   
 * @ClassName: OcrServiceImpl    
 * @Description: 通用文字
 * @date 2021�?1�?17�? 下午2:20:50    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
public class OcrServiceImpl {
	// 高精
//	private static final String ocrurl = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
	// 标准
	private static final String ocrurl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
	private static final String ak = "i4GgG9PxGjmDYQw9U1xwIBQT";
	private static final String sk = "GGT7lMSSqGkYUTc6939HgsWKQc3SbCAv";
	private static final String authHost = "https://aip.baidubce.com/oauth/2.0/token";
	
	/**
	 * [调用鉴权接口获取的token]
	 */
	private static final String accessToken = "24.184a31da1de0d381f8fe6e50dfba77f0.2592000.1613457380.282335-20435822";
	public static String token() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("grant_type", "client_credentials");
        map.put("client_id", ak);
        map.put("client_secret", sk);    
        
        String result = HttpUtil.get(authHost, map);  
        ResponseTokenBean tokenBean = GsonUtils.fromJson(result, ResponseTokenBean.class);
        return tokenBean.getAccess_token();
	}

	public static void ocr(String filePath) throws Exception{		
		byte[] imgData = FileUtil.readFileByBytes(filePath);
        String imgStr = Base64Util.encode(imgData);
        String imgParam = URLEncoder.encode(imgStr, "UTF-8");
        
        String param = "image=" + imgParam;
        
        String result = HttpUtil.post(ocrurl, accessToken, param);
        
        OcrResponse ocrRes = GsonUtils.fromJson(result, OcrResponse.class);
        System.out.println(ocrRes);
	}	
	
	public static void downImg() throws Exception{
		List<String> list = readFile();
		for(String fileId : list) {
			URL url = new URL("http://220.163.112.85:9529/oss/get/" + fileId );			
			//打开链接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//设置请求方式"GET"
			conn.setRequestMethod("GET");
			//超时响应时间
			conn.setConnectTimeout(5 * 1000);
			//通过输入流获取图片
			InputStream inStream = conn.getInputStream();
			org.apache.commons.io.FileUtils.copyInputStreamToFile(inStream, new File("c:\\tmp\\" + fileId + ".jpg"));
			inStream.close();
		}
	}
	
	public static List<String> readFile() throws Exception{
		List<String> list = org.apache.commons.io.FileUtils.readLines(new File("c:\\a.txt"), StandardCharsets.UTF_8);
		return list;
	}
	
	
	public static void ocr2() throws Exception{		
		List<String> list = readFile();
		
		// QPS限制2个
		ExecutorService pool = Executors.newFixedThreadPool(2);
		
		for(String fileId : list) {
			pool.execute(() -> {
				File imgFile = new File("c:\\tmp\\" + fileId + ".jpg");
				try {
					org.apache.commons.io.FileUtils.copyURLToFile(new URL("http://220.163.112.85:9529/oss/get/" + fileId ), imgFile);
					
					byte[] imgData = FileUtil.readFileByBytes(imgFile.getAbsolutePath());
			        String imgStr = Base64Util.encode(imgData);
			        String imgParam = URLEncoder.encode(imgStr, "UTF-8");		        
			        String param = "image=" + imgParam;		        
			        String result = HttpUtil.post(ocrurl, accessToken, param);		        
//			        OcrResponse ocrRes = GsonUtils.fromJson(result, OcrResponse.class);
//			        System.out.println(ocrRes);				
			        
			        imgFile.delete();
					
				} catch (Exception e) {					
					e.printStackTrace();
				} 				
			});
		}
		
		pool.shutdown();
		System.out.println("pool submit done.");
	}
	
	public static void main(String[] args) throws Exception {
//		System.out.println(token());
		
//		downImg();
		
//		String filePath = "c:\\11111.jpg";
//		ocr(filePath);
		
		long start = System.currentTimeMillis();
		
		ocr2();		
		
		System.out.println( (System.currentTimeMillis() - start ) / 1000 + "s, done.");		
	}	
}
