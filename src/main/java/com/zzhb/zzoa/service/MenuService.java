package com.zzhb.zzoa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zzhb.zzoa.domain.common.Menu;
import com.zzhb.zzoa.mapper.MenuMapper;
import com.zzhb.zzoa.utils.Constant;

@Service
public class MenuService {

	@Autowired
	MenuMapper menuMapper;

	@Cacheable(value = "ONEMENU", key = "#rolename")
	public List<Menu> getOneMenusByRoleName(String rolename) {
		Map<String, Object> params = new HashMap<>();
		if ("admin".equals(rolename)) {
			params.put("m_status", "1");
			params.put("m_parentid", "0");
			return menuMapper.getMenus(params);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "SECONDMENU", key = "#rolename")
	public List<Map<String, Object>> getSecondMenu(String rolename, String parentid) {
		// 获取parentid下的所有子菜单ID
		List<String> childIds = menuMapper.getIdByParentId(parentid);
		Map<String, Object> params = new HashMap<>();
		params.put("m_ids", childIds);
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

}
