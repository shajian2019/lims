package com.zzhb.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.mapper.ActivitiMapper;

@Service
public class DbsxService {

	private static Logger logger = LoggerFactory.getLogger(DbsxService.class);

	@Autowired
	ActivitiMapper activitiMapper;

	public Map<String, String> getBusinessByBks(Map<String, String> params) {
		Map<String, String> data = activitiMapper.getBusinessByBk(params);
		return data;
	}
}
