package com.zzhb.zzoa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.common.Menu;
import com.zzhb.zzoa.domain.common.Result;
import com.zzhb.zzoa.mapper.MenuMapper;
import com.zzhb.zzoa.utils.Constant;

@Service
public class MenuService {

	@Autowired
	MenuMapper menuMapper;

	@Cacheable(value = "ONEMENU", key = "#r_id")
	public List<Menu> getOneMenusByRoleId(String r_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("r_id", r_id);
		params.put("m_level", "1");
		return menuMapper.getMenus(params);
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "SECONDMENU", key = "#r_id")
	public List<Map<String, Object>> getSecondMenu(String r_id, String parentid) {
		// 获取parentid下的所有子菜单ID
		List<String> childIds = menuMapper.getIdByParentId(parentid,r_id);
		Map<String, Object> params = new HashMap<>();
		if (Constant.SUPERADMIN.equals(r_id)) {
			params.put("m_ids", childIds);
		} else {
			params.put("m_ids", childIds);
			params.put("r_id", r_id);
			// 根据当前角色ID 获取所拥有的子菜单ID
			childIds = menuMapper.getIdByRoleID(params);
			params.put("m_status", "1");
		}
		List<Menu> menus = menuMapper.getMenus(params);
		Map<String, Map<String, Object>> json = new LinkedHashMap<>();
		for (int i = 0; i < menus.size(); i++) {
			Menu menu = menus.get(i);
			if (menu.getIcon().indexOf(Constant.FA) != -1) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", menu.getId());
				map.put("title", menu.getTitle());
				map.put("icon", menu.getIcon());
				if (i == 0) {
					map.put("spread", true);
				} else {
					map.put("spread", false);
				}
				json.put(menu.getId(), map);
			} else {
				Map<String, Object> map = json.get(menu.getParentid());
				Map<String, String> map1 = new HashMap<>();
				if (map.get("children") == null) {
					List<Map<String, String>> list = new ArrayList<>();
					map1.put("id", menu.getId());
					map1.put("title", menu.getTitle());
					map1.put("icon", menu.getIcon());
					map1.put("url", menu.getUrl());
					list.add(map1);
					map.put("children", list);
				} else {
					List<Map<String, String>> list = (List<Map<String, String>>) map.get("children");
					map1.put("id", menu.getId());
					map1.put("title", menu.getTitle());
					map1.put("icon", menu.getIcon());
					map1.put("url", menu.getUrl());
					list.add(map1);
				}
			}
		}
		List<Map<String, Object>> result = new ArrayList<>();
		for (Map.Entry<String, Map<String, Object>> entry : json.entrySet()) {
			result.add(entry.getValue());
		}
		return result;
	}

	public JSONObject getAllMenus(Map<String, String> params) {
		JSONObject result = new JSONObject();
		List<Menu> allMenus = menuMapper.getAllMenus(params);
		result.put("code", 0);
		result.put("msg", "");
		result.put("count", allMenus.size());
		result.put("is", true);
		result.put("data", allMenus);
		result.put("tip", "操作成功");
		return result;
	}

}
