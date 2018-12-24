package com.zzhb.zzoa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.zzoa.domain.common.Param;
import com.zzhb.zzoa.mapper.ParamMapper;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

@Service
public class InitService {

	@Autowired
	ParamMapper paramMapper;

	public void initParams(Configuration configuration) throws TemplateModelException {
		List<Param> params2 = paramMapper.getParams(null);
		for (Param param : params2) {
			configuration.setSharedVariable(param.getKey(), param.getValue());
		}
	}
}
