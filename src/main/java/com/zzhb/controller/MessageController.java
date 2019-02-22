package com.zzhb.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.service.MessageService;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	MessageService messageService;

	@GetMapping("/message")
	public String message() {
		return "message/message";
	}

	@GetMapping("/list")
	@ResponseBody
	public JSONObject getMessages(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return messageService.getMessages(page, limit, params);
	}

	@PostMapping("/update")
	@ResponseBody
	public void updateMessage(@RequestParam Map<String, Object> params) {
		messageService.updateMessage(params);
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public void deleteMessage(@RequestParam Map<String, Object> params) {
		messageService.deleteMessage(params);
	}
}
