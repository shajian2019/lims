package com.zzhb.zzoa;

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

public class PdfTemp {

	public static void main(String[] args) throws Exception {
		test();
	}

	public static void test() throws IOException, DocumentException {
		String tempPath = "E:/leavetemp.pdf";
		String outPdfPath = "E:/pdfresult.pdf";
		// pdf模板
		PdfReader reader = new PdfReader(tempPath);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		/* 将要生成的目标PDF文件名称 */
		PdfStamper ps = new PdfStamper(reader, bos);

		/* 使用中文字体 */
		BaseFont bf1 = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
		fontList.add(bf1);

		/* 取出报表模板中的所有字段 */
		AcroFields fields = ps.getAcroFields();
		fields.setSubstitutionFonts(fontList);
		fillData(fields, data());

		/* 必须要调用这个，否则文档不会生成的 */
		ps.setFormFlattening(true);
		ps.close();

		// 生成pdf路径
		OutputStream fos = new FileOutputStream(outPdfPath);
		fos.write(bos.toByteArray());
		fos.flush();
		fos.close();
		bos.close();
	}

	public static void fillData(AcroFields fields, Map<String, String> data) throws IOException, DocumentException {
		for (String key : data.keySet()) {
			// 为字段赋值,注意字段名称是区分大小写的  
			fields.setField(key, data.get(key));
		}
	}

	public static Map<String, String> data() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("sqr", "李磊");
		data.put("orgname", "项目科");
		data.put("ksrq", "2018年12月08日");
		data.put("jsrq", "2018年12月18日");
		data.put("qjlx", "事假");
		data.put("qjly", "家里有事112121212");
		
		data.put("ks_spyj", "同意  好的0");
		data.put("ks_spr", "陈浩杰");
		data.put("ks_year", "2018");
		data.put("ks_month", "11");
		data.put("ks_day", "21");
		
		data.put("fg_spyj", "同意  好的1");
		data.put("fg_spr", "陈浩杰姐");
		data.put("fg_year", "2018");
		data.put("fg_month", "11");
		data.put("fg_day", "21");
		
		data.put("zg_spyj","同意  好的2");
		data.put("zg_spr", "陈浩杰姐");
		data.put("zg_year", "2018");
		data.put("zg_month", "11");
		data.put("zg_day", "21");
		
		return data;
	}

}
