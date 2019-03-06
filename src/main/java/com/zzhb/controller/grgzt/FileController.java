package com.zzhb.controller.grgzt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.shiro.codec.Base64;
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
import com.zzhb.config.Props;
import com.zzhb.utils.Constant;
import com.zzhb.utils.FileUtil;
import com.zzhb.utils.ZipUtils;

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
		filename = Constant.ATTACH_FILE + "&" + bk + "&" + filename;
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
	public Set<String> taskAttachments(@PathVariable("taskId") String taskId) {
		Set<String> fileNames = new HashSet<>();
		Task ruTask = taskService.createTaskQuery().taskId(taskId).singleResult();
		List<Attachment> taskAttachments = taskService.getProcessInstanceAttachments(ruTask.getProcessInstanceId());
		String dir = props.getTempPath();
		if (!new File(dir).exists()) {
			new File(dir).mkdir();
		}
		for (Attachment attachment : taskAttachments) {
			String filename = attachment.getType() + "&" + attachment.getDescription() + "&" + attachment.getName();
			FileUtil.saveFileFromInputStream(taskService.getAttachmentContent(attachment.getId()), dir, filename);
			fileNames.add(filename);
		}
		return fileNames;
	}

	@PostMapping("/downloadAttachment")
	@ResponseBody
	public String downloadAttachment(@RequestParam("bk") String bk) {
		String dir = props.getTempPath();
		String fileName = bk + ".zip";
		String outFileFullName = dir + File.separator + fileName;
		File files = new File(dir);
		File[] listFiles = files.listFiles();
		List<File> fileArr = new ArrayList<File>();
		for (File file : listFiles) {
			String filename = file.getName();
			if (filename.indexOf("&" + bk + "&") != -1) {
				FileUtil.copyFile(dir + File.separator + filename,
						dir + File.separator + filename.split("&" + bk + "&")[1]);
				File newFile = new File(dir + File.separator + filename.split("&" + bk + "&")[1]);
				fileArr.add(newFile);
			}
		}
		try {
			ZipUtils.toZip(fileArr, new FileOutputStream(new File(outFileFullName)), true);
		} catch (FileNotFoundException | RuntimeException e) {
			e.printStackTrace();
		}
		return fileName;
	}

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
}
