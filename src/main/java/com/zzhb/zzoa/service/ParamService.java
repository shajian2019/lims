package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.common.Param;
import com.zzhb.zzoa.mapper.ParamMapper;
import com.zzhb.zzoa.utils.LayUiUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public class ParamService {

	@Autowired
	InitService initService;

	@Autowired
	ParamMapper paramMapper;

	public JSONObject getAllParams(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Param> paramList = paramMapper.getAllParams(params);
		PageInfo<Param> pageInfo = new PageInfo<Param>(paramList);
		return LayUiUtil.pagination(pageInfo);
	}

	
	@Transactional
	public Integer saveParam(Param param, String flag) {
		Integer result = 0;
		Integer checkKey = paramMapper.checkKey(param);
		if (checkKey == 0) {
			if (flag.equals("edit")) {// 修改
				result = paramMapper.updateParam(param);
			} else {// 新增
				result = paramMapper.addNewParam(param);
			}
			try {
				initService.initParams();
			} catch (TemplateModelException e) {
				e.printStackTrace();
			}
		} else {
			result = -1;
		}
		return result;
	}
	
	@Transactional
	public Integer delParam(Map<String, String> map) {
		Integer delParamById = paramMapper.delParamById(map);
		try {
			initService.initParams();
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
		return delParamById;
	}

}
