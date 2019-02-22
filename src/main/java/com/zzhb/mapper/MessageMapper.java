package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.zzhb.domain.Message;

public interface MessageMapper {

	public Integer addMessages(List<Message> messages);

	public List<Message> getMessages(Map<String, String> param);

	@Select("SELECT count(1) FROM sys_t_message WHERE m_status = '0' AND m_recipientId = #{0}")
	public Integer countMessages(String u_id);

	public Integer updateMessage(Map<String, Object> params);

	public Integer deleteMessage(Map<String, Object> params);
}
