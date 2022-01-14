package com.inzyme.spatiotemporal.web.ai.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**   
 * @ClassName: NoUtils    
 * @Description: 自定义类型序号工具
 * @date 2020年3月8日 上午11:56:38    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
public class NoUtils {
	private static final long ONE_STEP = 100;
	private static final Lock LOCK = new ReentrantLock();
	private static long lastTime = System.currentTimeMillis();
	private static short lastCount = 0;
	private static int count = 0;
	
	@SuppressWarnings("finally")
	public static String generateNo(String type) {
		LOCK.lock();
		try {
			long now = System.currentTimeMillis();
			if(now == lastTime) {
				count++;
				Thread.currentThread();
				Thread.sleep(1);
			}else {
				count = 0;
				lastTime = now;
			}
		} finally {
			String no = type + transferLongToDate(lastTime) + String.format("%03d", count);
			LOCK.unlock();
			return no;
		}
	}
	
	
	@SuppressWarnings("finally")
	public static String generateNo2(String type) {
		LOCK.lock();
		try {
			if (lastCount == ONE_STEP) {
				boolean done = false;
				while (!done) {
					long now = System.currentTimeMillis();
					if (now == lastTime) {
						try {
							Thread.currentThread();
							Thread.sleep(1);
						} catch (java.lang.InterruptedException e) {
						}
						continue;
					} else {
						lastTime = now;
						lastCount = 0;
						done = true;
					}
				}
			}
			count = lastCount++;
		} finally {
			String no = type + transferLongToDate(lastTime) + String.format("%03d", count);
			LOCK.unlock();
			return no;
		}
	}
	
	private static String transferLongToDate(Long millSec) {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date(millSec));
	}
}
