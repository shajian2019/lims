package com.zzhb.zzoa.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zzhb.zzoa.config.Props;

@Controller
@RequestMapping("/pdf")
public class PdfController {

	@Autowired
	Props props;

	@GetMapping("/preview/{fileName}")
	public void preview(@PathVariable("fileName") String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		String filePath = props.getTempPath() + "/" + fileName;
		if (!filePath.endsWith(".pdf")) {
			filePath += ".pdf";
		}
		try {
			is = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			response.reset();
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/pdf");
			os.write(buffer);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
