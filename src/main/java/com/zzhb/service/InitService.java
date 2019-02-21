package com.zzhb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.domain.common.Param;
import com.zzhb.mapper.ParamMapper;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

@Service
public class InitService {

	@Autowired
	ParamMapper paramMapper;

	@Autowired
	private Configuration configuration;

	public void initParams() throws TemplateModelException {
		List<Param> params2 = paramMapper.getParams(null);
		for (Param param : params2) {
			configuration.setSharedVariable(param.getKey(), param.getValue());
		}
	}
}
