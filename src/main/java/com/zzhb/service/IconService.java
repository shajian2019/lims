package com.zzhb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.domain.common.Icon;
import com.zzhb.mapper.IconMapper;
import com.zzhb.utils.LayUiUtil;

@Service
public class IconService {

	@Autowired
	IconMapper iconMapper;

	public JSONObject tbglList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Icon> icons = iconMapper.getIcons(params);
		PageInfo<Icon> pageInfo = new PageInfo<Icon>(icons);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer tbglPodAdd(Icon icon) {
		Integer addIcon = 0;
		if (icon.getId() == null) {
			addIcon = iconMapper.addIcon(icon);
		} else {
			addIcon = iconMapper.updateIcon(icon);
		}
		return addIcon;
	}

	@Transactional
	public Integer tbglPodDel(String id) {
		return iconMapper.delIcon(id);
	}
}
