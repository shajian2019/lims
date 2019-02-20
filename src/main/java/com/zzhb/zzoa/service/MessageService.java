package com.zzhb.zzoa.service;

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
import com.zzhb.zzoa.domain.Message;
import com.zzhb.zzoa.mapper.MessageMapper;
import com.zzhb.zzoa.utils.LayUiUtil;

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
}
