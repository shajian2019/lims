package com.zzhb.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.domain.Message;
import com.zzhb.mapper.MessageMapper;
import com.zzhb.utils.LayUiUtil;
import com.zzhb.websocket.MessageWebSocket;

@Service
public class MessageService {

	private static Logger logger = LoggerFactory.getLogger(MessageService.class);

	@Autowired
	MessageMapper messageMapper;

	public JSONObject getMessages(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Message> messages = messageMapper.getMessages(params);
		PageInfo<Message> pageInfo = new PageInfo<Message>(messages);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public void saveMessage(List<Message> messages) {
		messageMapper.addMessages(messages);
	}

	@Autowired
	MessageWebSocket messageWebSocket;

	@Transactional
	public void updateMessage(Map<String, Object> params) {
		List<String> m_ids = Arrays.asList(params.get("m_ids").toString().split(","));
		params.put("m_ids", m_ids);
		messageMapper.updateMessage(params);
		Integer countMessages = messageMapper.countMessages(params.get("u_id").toString());
		try {
			messageWebSocket.sendtoUser(countMessages + "", params.get("u_id").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void deleteMessage(Map<String, Object> params) {
		List<String> m_ids = Arrays.asList(params.get("m_ids").toString().split(","));
		params.put("m_ids", m_ids);
		messageMapper.deleteMessage(params);
	}
}
