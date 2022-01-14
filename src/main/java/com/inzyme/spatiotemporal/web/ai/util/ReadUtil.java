package com.inzyme.spatiotemporal.web.ai.util;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * <p>
 *
 * <p>
 *
 * @author fyl
 * @since 2021-12-22 15:03
 */
public class ReadUtil {

    public static String readDoc(MultipartFile file) {
        if (file.isEmpty())return "";
        XWPFDocument wordExtractor = null;
        XWPFWordExtractor extractor = null;
        try {
            InputStream inputStream = file.getInputStream();
            wordExtractor = new XWPFDocument(inputStream);
            extractor = new XWPFWordExtractor(wordExtractor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractor.getText();
    }

    /**
     * 读取 PDF文本内容
     *
     * @Param: MultipartFile
     * @return: pdf文本内容
     */
    public static String readPdf(MultipartFile file) {
        StringBuilder content = new StringBuilder();
        try {
            InputStream is = file.getInputStream();
            PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
            parser.parse();
            // 读取文本内容
            PDDocument document = parser.getPDDocument();
            // 获取页码
            int pages = document.getNumberOfPages();
            PDFTextStripper stripper = new PDFTextStripper();
            // 设置按顺序输出
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(pages);
            content.append(stripper.getText(document));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static String readXlsx(MultipartFile file) {
        if (file.isEmpty()) return "";
        StringBuilder content = new StringBuilder();
        try {
            XSSFWorkbook excel = new XSSFWorkbook(file.getInputStream());
            //获取第一个sheet
            XSSFSheet sheet0 = excel.getSheetAt(0);
            for (Iterator rowIterator = sheet0.iterator(); rowIterator.hasNext(); ) {
                XSSFRow row = (XSSFRow) rowIterator.next();
                for (Iterator iterator = row.cellIterator(); iterator.hasNext(); ) {
                    XSSFCell cell = (XSSFCell) iterator.next();
                    //根据单元格的类型 读取相应的结果
                    if (cell.getCellType() == CellType.STRING)
                        content.append(cell.getStringCellValue() + "\t");
                    else if (cell.getCellType() == CellType.NUMERIC
                            || cell.getCellType() == CellType.FORMULA)
                        content.append(cell.getNumericCellValue() + "\t");
                    else
                        content.append("" + "\t");
                }
                content.append("" + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}
