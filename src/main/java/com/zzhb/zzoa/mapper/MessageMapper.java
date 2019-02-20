package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import com.zzhb.zzoa.domain.Message;

public interface MessageMapper {

	public Integer addMessages(List<Message> messages);
	
	public List<Message> getMessages(Map<String, String> param);
}
