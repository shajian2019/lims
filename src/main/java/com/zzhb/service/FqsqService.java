package com.zzhb.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzhb.mapper.UserMapper;

@Service
public class FqsqService {

	@Autowired
	UserMapper userMapper;

	@Transactional
	public Integer bindUser(String p_id, String u_ids) {
		userMapper.delUserProcdef(p_id, "");
		List<String> uIds = Arrays.asList(u_ids.split(","));
		Map<String, Object> params = new HashMap<>();
		params.put("p_id", p_id);
		params.put("u_ids", uIds);
		Integer addUserProcdef = userMapper.addUserProcdef(params);
		return addUserProcdef;
	}

}
