package com.zzhb.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfUtil {

	public static void main(String[] args) {
		String tempPath = "E:/leave_temp.pdf";
		String outPdfPath = "E:/leave_bk.pdf";
		Map<String,String> data = new HashMap<>();
		data.put("ksrq_year", "2018");
		data.put("ksrq_month", "12");
		data.put("ksrq_day", "11");
		data.put("jsrq_year", "2018");
		data.put("jsrq_month", "12");
		data.put("jsrq_day", "11");
		createPdfByTemp(tempPath, outPdfPath,data);
	}

	/**
	 * 
	 * @param tempPath
	 * @param outPdfPath
	 * @param data
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String createPdfByTemp(String tempPath, String outPdfPath, Map<String, String> data) {
		// pdf模板
		ByteArrayOutputStream bos = null;
		PdfReader reader = null;
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(outPdfPath);
			reader = new PdfReader(tempPath);

			/* 将要生成的目标PDF文件名称 */
			bos = new ByteArrayOutputStream();
			PdfStamper ps = new PdfStamper(reader, bos);

			/* 使用中文字体 */
			BaseFont bf1 = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
			fontList.add(bf1);

			/* 取出报表模板中的所有字段 */
			AcroFields fields = ps.getAcroFields();
			fields.setSubstitutionFonts(fontList);
			for (String key : data.keySet()) {
				fields.setField(key, data.get(key));
			}
			/* 必须要调用这个，否则文档不会生成的 */
			ps.setFormFlattening(true);
			ps.close();

			// 生成pdf路径
			fos.write(bos.toByteArray());
			fos.flush();
			return outPdfPath;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				bos.close();
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
