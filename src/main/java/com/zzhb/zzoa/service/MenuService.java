package com.zzhb.zzoa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.common.Menu;
import com.zzhb.zzoa.mapper.MenuMapper;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.utils.Constant;

@Service
public class MenuService {

	@Autowired
	MenuMapper menuMapper;

	@Autowired
	RoleMapper roleMapper;

	@Cacheable(value = "ONEMENU", key = "#r_id")
	public List<Menu> getOneMenusByRoleId(String r_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("r_id", r_id);
		params.put("m_level", "1");
		if (!Constant.SUPERADMIN.equals(r_id)) {
			params.put("m_status", "1");
		}
		return menuMapper.getMenus(params);
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "SECONDMENU", key = "#r_id")
	public List<Map<String, Object>> getSecondMenu(String r_id, String parentid) {
		// 获取parentid下的所有子菜单ID
		List<String> childIds = menuMapper.getIdByParentId(parentid, r_id);
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

	@Cacheable(value = "TREEMENUS")
	public JSONArray initMenuTree(Map<String, String> params) {
		JSONArray result = new JSONArray();
		params.put("m_level", "1");
		String level = params.get("level");
		List<Menu> allMenus = menuMapper.getAllMenus(params);
		params.clear();
		for (int i = 0; i < allMenus.size(); i++) {
			JSONObject json = new JSONObject();
			Menu menu = allMenus.get(i);
			String m_id = menu.getId();
			json.put("id", m_id);
			json.put("name", menu.getTitle());
			json.put("open", true);
			params.put("m_parentid", m_id);
			JSONArray childrenS = new JSONArray();
			List<Menu> secondMenus = menuMapper.getAllMenus(params);
			params.clear();
			if (!"2".equals(level)) {
				for (int j = 0; j < secondMenus.size(); j++) {
					JSONObject secondJson = new JSONObject();
					Menu secondmenu = secondMenus.get(j);
					String sm_id = secondmenu.getId();
					secondJson.put("id", sm_id);
					secondJson.put("name", secondmenu.getTitle());
					secondJson.put("open", false);
					params.put("m_parentid", sm_id);
					JSONArray childrenT = new JSONArray();
					List<Menu> thirdMenus = menuMapper.getAllMenus(params);
					params.clear();
					if (!"3".equals(level)) {
						for (int k = 0; k < thirdMenus.size(); k++) {
							JSONObject thirdJson = new JSONObject();
							Menu thirdmenu = thirdMenus.get(k);
							String tm_id = thirdmenu.getId();
							thirdJson.put("id", tm_id);
							thirdJson.put("name", thirdmenu.getTitle());
							thirdJson.put("open", false);
							childrenT.add(thirdJson);
						}
					}
					secondJson.put("children", childrenT);
					childrenS.add(secondJson);
				}
			} else {
				json.put("open", false);
			}
			json.put("children", childrenS);
			result.add(json);
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

	@Transactional
	public Integer updateMenu(Menu menu) {
		Integer updateMenu = 0;
		String parentid = menu.getParentid();
		if (parentid == null) {
			updateMenu = menuMapper.updateMenu(menu);
		} else {
			updateMenu = menuMapper.insertMenu(menu);
		}
		return updateMenu;
	}

	@Transactional
	public Integer delMenus(Map<String, String> param) {
		Map<String, Object> params = new HashMap<>();
		List<String> idByParentId = menuMapper.getIdByParentId(param.get("id"), Constant.SUPERADMIN);
		idByParentId.add(param.get("id"));
		params.put("m_ids", idByParentId);
		// TODO 删除权限角色中间表中的记录
		roleMapper.delRoleMenu(params);
		return menuMapper.delMenus(params);
	}
}
