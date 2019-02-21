package com.zzhb.controller.lcgl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.config.Props;
import com.zzhb.domain.activiti.ProcessDefinitionType;
import com.zzhb.service.ActivitiService;

@Controller
@RequestMapping("/lcgl/lcdy")
public class LcdyController {

	@Autowired
	Props props;

	@GetMapping("")
	public String lcdy() {
		return "lcgl/lcdy/lcdy";
	}

	@Autowired
	ActivitiService activitiService;

	@GetMapping("/list")
	@ResponseBody
	public JSONObject lcdyList(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "0x7fffffff") Integer limit, @RequestParam Map<String, String> params) {
		return activitiService.lcdyList(page, limit, params);
	}

	@GetMapping("/lcfl/list")
	@ResponseBody
	public JSONObject lcflList() {
		return activitiService.lcflList();
	}

	@GetMapping("/lcfl/listByid")
	@ResponseBody
	public JSONObject lcflListByUid() {
		return activitiService.lcflListByUid();
	}

	@PostMapping("/lcfl/add")
	@ResponseBody
	public Integer lcflAdd(String name) {
		return activitiService.lcflAdd(name);
	}

	@PostMapping("/lcfl/edit")
	@ResponseBody
	public Integer lcflEdit(ProcessDefinitionType pt) {
		return activitiService.lcflEdit(pt);
	}

	@PostMapping("/del")
	@ResponseBody
	public Long lcdyDel(@RequestParam Map<String, String> params) {
		return activitiService.lcdyDel(params);
	}

	@PostMapping("/deploy")
	@ResponseBody
	public JSONObject deploy(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
		JSONObject json = new JSONObject();
		String fileName = file.getOriginalFilename();
		if (fileName.indexOf(".zip") == -1) {
			json.put("code", "-1");
			json.put("msg", "文件类型错误，仅支持zip文件");
		} else {
			try {
				Integer deploy = activitiService.deploy(params, file);
				json.put("code", deploy);
				json.put("msg", "success");
			} catch (IOException e) {
				e.printStackTrace();
				json.put("code", "-2");
				json.put("msg", e.getMessage());
			}
		}
		return json;
	}

	@PostMapping("/download")
	@ResponseBody
	public String download(@RequestParam Map<String, String> params) {
		return activitiService.download(params);
	}

	@PostMapping("/preview")
	@ResponseBody
	public String preview(@RequestParam Map<String, String> params) {
		return activitiService.preview(params);
	}
<<<<<<< HEAD

	@PostMapping("/downloadZip")
	public void downloadZip(String fileName, HttpServletRequest request, HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		String filePath = props.getTempPath() + "/" + fileName;
		try {
			is = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			response.reset();
			String agent = request.getHeader("USER-AGENT");
			if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
				fileName = "=?UTF-8?B?" + (new String(Base64.encode(fileName.getBytes("UTF-8")))) + "?=";
			} else {
				fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			}
			String contentDisposition = "attachment;filename=" + fileName;
			response.addHeader("Content-Disposition", contentDisposition);
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
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
=======
>>>>>>> refs/heads/chj
}
