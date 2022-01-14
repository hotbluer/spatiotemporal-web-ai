package com.inzyme.spatiotemporal.web.ai.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * @ClassName: ImageUtil
 * @Description: 图像工具类
 * @date 2020年6月16日 下午10:26:08
 * 
 * @author Q.JI
 * @version
 * @since JDK 1.8
 */
public class ImageUtil {

	/**
	 * 
	 * @Title: rotateImage
	 * @Description: 旋转图片为指定角度
	 * @param bufferedimage 目标图像
	 * @param degree        旋转角度
	 * @return BufferedImage
	 */
	public static BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
		int w = bufferedimage.getWidth();
		int h = bufferedimage.getHeight();
		int type = bufferedimage.getColorModel().getTransparency();
		BufferedImage img;
		Graphics2D graphics2d;
		(graphics2d = (img = new BufferedImage(w, h, type)).createGraphics()).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
		graphics2d.drawImage(bufferedimage, 0, 0, null);
		graphics2d.dispose();
		return img;
	}

}
