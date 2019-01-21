package com.zzhb.zzoa.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.config.Props;
import com.zzhb.zzoa.utils.FileUtil;

@Controller
@RequestMapping("/file")
public class FileController {

	@Autowired
	Props props;

	@PostMapping("/upload")
	@ResponseBody
	public JSONObject upload(@RequestParam("file") MultipartFile file, String bk) throws Exception {
		JSONObject result = new JSONObject();
		String filename = file.getOriginalFilename();

		String dir = props.getTempPath();
		if (!new File(dir).exists()) {
			new File(dir).mkdir();
		}
		filename = bk + "&" + filename;
		FileUtil.saveFileFromInputStream(file.getInputStream(), dir, filename);
		result.put("filename", filename);
		return result;
	}

	@GetMapping("del")
	@ResponseBody
	public boolean del(@RequestParam("filename") String filename) throws Exception {
		String dir = props.getTempPath();
		return FileUtil.delete(dir + File.separator + filename);
	}
}
