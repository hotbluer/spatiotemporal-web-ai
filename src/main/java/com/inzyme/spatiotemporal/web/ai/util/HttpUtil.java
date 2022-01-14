package com.inzyme.spatiotemporal.web.ai.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.inzyme.spatiotemporal.web.ai.service.impl.AipServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: HttpUtil    
 * @Description: http 工具类
 * @date 2020年3月5日 下午4:23:44    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
 */
@Slf4j
public class HttpUtil {

    public static String post(String requestUrl, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.postGeneralUrl(requestUrl, contentType, params, "UTF-8");
    }

    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, contentType, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params) throws Exception {
    	return postGeneralUrl(generalUrl, contentType, params, "UTF-8");
    }
    
    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
//        for (String key : headers.keySet()) {
//            System.err.println(key + "--->" + headers.get(key));
//        }
        
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        log.info("[POST] result:" + result);
        return result;
    }
    
	public static String get(String url, Map<String, Object> param) throws Exception {
		String params = map2String(param);
		URL realUrl = new URL(url + params);
		// 打开和URL之间的连接
		HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		// 定义 BufferedReader输入流来读取URL的响应
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String result = "";
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
//		System.err.println("[GET] result:" + result);
		return result;
	}
	
	
    private static String map2String(Map<String, Object> param) throws UnsupportedEncodingException {
        if (param != null && param.size() > 0) {
            StringBuilder params = new StringBuilder("?");
            for (String key : param.keySet()) {
                params.append(key).append("=").append(URLEncoder.encode(param.get(key).toString(), "UTF-8")).append("&");
            }
            return params.toString().substring(0, params.toString().length() - 1);
        } else return "";
    }
}
