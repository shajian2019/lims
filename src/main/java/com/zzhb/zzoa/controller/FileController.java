package com.zzhb.zzoa.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@Autowired
	TaskService taskService;

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
	
	@GetMapping("/taskAttachments/{taskId}")
	@ResponseBody
	public List<String> taskAttachments(@PathVariable("taskId") String taskId) {
		List<String> fileNames = new ArrayList<>();
		List<Attachment> taskAttachments = taskService.getTaskAttachments(taskId);
		String dir = props.getTempPath();
		if (!new File(dir).exists()) {
			new File(dir).mkdir();
		}
		for (Attachment attachment : taskAttachments) {
			String filename = attachment.getTaskId()+"@"+attachment.getName();
			FileUtil.saveFileFromInputStream(taskService.getAttachmentContent(attachment.getId()), dir, attachment.getTaskId()+"@"+attachment.getName());
			fileNames.add(filename);
		}
		return fileNames;
	}
	
}
