package com.inzyme.spatiotemporal.web.ai.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author fyl
 * @since 2021-12-21 16:12
 */
public class ExcelUtil {

   /* public static void export(String sheetTitle, String[] title, List<Object> list, HttpServletResponse response) {

        HSSFWorkbook wb = new HSSFWorkbook();//创建excel表
        HSSFSheet sheet = wb.createSheet(sheetTitle);
        sheet.setDefaultColumnWidth(20);//设置默认行宽

        //表头样式（加粗，水平居中，垂直居中）
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //设置边框样式
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFFont fontStyle = wb.createFont();
        fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        cellStyle.setFont(fontStyle);

        //标题样式（加粗，垂直居中）
        HSSFCellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cellStyle2.setFont(fontStyle);

        //设置边框样式
        cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        //字段样式（垂直居中）
        HSSFCellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

        //设置边框样式
        cellStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        //创建表头
        HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(20);//行高

        HSSFCell cell = row.createCell(0);
        cell.setCellValue(sheetTitle);
        cell.setCellStyle(cellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0,0,0,(title.length-1)));

        //创建标题
        HSSFRow rowTitle = sheet.createRow(1);
        rowTitle.setHeightInPoints(20);

        HSSFCell hc;
        for (int i = 0; i < title.length; i++) {
            hc = rowTitle.createCell(i);
            hc.setCellValue(title[i]);
            hc.setCellStyle(cellStyle2);
        }

        byte result[] = null;

        ByteArrayOutputStream out = null;

        try {
            //创建表格数据
            Field[] fields;
            int i = 2;

            for (Object obj : list) {
                fields = obj.getClass().getDeclaredFields();

                HSSFRow rowBody = sheet.createRow(i);
                rowBody.setHeightInPoints(20);

                int j = 0;
                for (Field f : fields) {

                    f.setAccessible(true);

                    Object va = f.get(obj);
                    if (null == va) {
                        va = "";
                    }

                    hc = rowBody.createCell(j);
                    hc.setCellValue(va.toString());
                    hc.setCellStyle(cellStyle3);

                    j++;
                }

                i++;
            }

            response.setHeader("Content-Disposition", "attachment;Filename=" + System.currentTimeMillis() + ".xls");
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.close();
        } catch (Exception ex) {
                ex.printStackTrace();
        }

    }*/

    /**
     * 构造文件流输出
     * @param response
     * @param fileName
     * @param tClass
     * @param exportVOList
     * @throws Exception
     */
    public static void fileStreamOutputBuilder (HttpServletResponse response, String fileName, Class<?> tClass, List<?> exportVOList) throws UnsupportedEncodingException {
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(fileName, fileName, ExcelType.XSSF), tClass, exportVOList);
        response.reset();
//        response.setContentType("application/msexcel;charset=UTF-8");
        // 下载文件能正常显示中文
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String encode = URLEncoder.encode(fileName + ".xlsx", "UTF-8");
        //空格变加号问题
        encode = encode.replaceAll("\\+","%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encode );
        //设置不要缓存
        response.setHeader("Pragma", "No-cache");
        //设置http状态码，如果为200时，则前端解析该文件数据。
        response.setStatus(200);
        OutputStream ouputStream = null;
        try {
            ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            if (ouputStream != null) {
                ouputStream.flush();
                ouputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ouputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
