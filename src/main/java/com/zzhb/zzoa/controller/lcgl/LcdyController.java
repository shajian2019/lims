package com.zzhb.zzoa.controller.lcgl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zzhb.zzoa.service.ActivitiService;

//部署管理
@Controller
@RequestMapping("/lcgl/lcdy")
public class LcdyController {

	@GetMapping("/lcdy")
	public String lcdy() {
		return "lcgl/lcdy/lcdy";
	}

	@Autowired
	ActivitiService activitiService;

	@GetMapping("/lcdy/list")
	public String lcdyList(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return null;
	}

	public void deploy(@RequestParam("file") MultipartFile file, String name) {
		String fileName = file.getOriginalFilename(); 
		InputStream in = null;
		try {
			 in = file.getInputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	

}
