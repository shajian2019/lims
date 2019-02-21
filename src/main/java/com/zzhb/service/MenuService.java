package com.zzhb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.domain.common.Menu;
import com.zzhb.mapper.MenuMapper;
import com.zzhb.mapper.RoleMapper;
import com.zzhb.utils.Constant;
import com.zzhb.utils.MenuUtil;

@Service
public class MenuService {

	@Autowired
	CacheService cacheService;

	@Autowired
	MenuMapper menuMapper;

	@Autowired
	RoleMapper roleMapper;

	@Cacheable(value = "ALLMENUS", key = "#r_id", condition = "#r_id !='superadmin'")
	public List<Map<String, Object>> menus(String r_id) {
		Map<String, String> params = new HashMap<>();
		params.put("r_id", r_id);
		List<Map<String, Object>> menus2 = menuMapper.getMenus(params);
		List<Map<String, Object>> standardJSON = MenuUtil.getStandardJSON(menus2);
		return standardJSON;
	}

	@CacheEvict(value = "ALLMENUS", allEntries = true)
	public void flushAllMenu() {
	}

	@Cacheable(value = "ONEMENU", key = "#r_id", condition = "#r_id !='superadmin'")
	public List<Map<String, Object>> getOneMenusByRoleId(String r_id) {
		Map<String, String> params = new HashMap<>();
		params.put("r_id", r_id);
		params.put("m_level", "1");
		if (!Constant.SUPERADMIN.equals(r_id)) {
			params.put("m_status", "1");
		}
		return menuMapper.getMenus(params);
	}

	@CacheEvict(value = "ONEMENU", allEntries = true)
	public void flushOnemenu() {
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "SECONDMENU", key = "#cachKey", condition = "#r_id !='superadmin'")
	public List<Map<String, Object>> getSecondMenu(String cachKey, String r_id, String parentid) {
		// 获取parentid下的所有子菜单ID
		List<Map<String, Object>> menus = menus(r_id);
		for (Map<String, Object> map : menus) {
			if (map.get("id").toString().equals(parentid)) {
				menus = (List<Map<String, Object>>) map.get("children");
				break;
			}
		}
		return menus;
	}

	@CacheEvict(value = "SECONDMENU", allEntries = true)
	public void flushSecondmenu() {
	}

	@Cacheable(value = "TREEMENUS", key = "#level", condition = "#r_id !='superadmin'")
	public JSONArray initMenuTree(String level, String r_id, Map<String, String> params) {
		JSONArray result = new JSONArray();
		params.put("m_level", "1");
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

	@CacheEvict(value = "TREEMENUS", allEntries = true)
	public void flushTreemenus() {
	}

	public JSONObject initDTree(Map<String, String> params) {
		JSONObject result = new JSONObject();
		JSONObject status = new JSONObject();
		JSONArray data = new JSONArray();
		params.put("m_level", "1");
		List<Menu> allMenus = menuMapper.getAllMenus(params);
		List<String> midsByRId = new ArrayList<>();
		if (!"".equals(params.get("r_id"))) {
			midsByRId = roleMapper.getMidsByRId(params);
		}
		params.clear();
		for (int i = 0; i < allMenus.size(); i++) {
			JSONObject json = new JSONObject();
			Menu menu = allMenus.get(i);
			json.put("id", menu.getId());
			json.put("title", menu.getTitle());
			json.put("checkArr", getCheckArr(midsByRId, menu.getId()));
			json.put("parentId", menu.getParentid());

			JSONArray childrenS = new JSONArray();
			params.put("m_parentid", menu.getId());
			List<Menu> secondMenus = menuMapper.getAllMenus(params);
			params.clear();
			for (int j = 0; j < secondMenus.size(); j++) {
				JSONObject jsonS = new JSONObject();
				Menu menuS = secondMenus.get(j);
				jsonS.put("id", menuS.getId());
				jsonS.put("title", menuS.getTitle());
				jsonS.put("checkArr", getCheckArr(midsByRId, menuS.getId()));
				jsonS.put("parentId", menuS.getParentid());
				JSONArray childrenT = new JSONArray();
				params.put("m_parentid", menuS.getId());
				List<Menu> thirdMenus = menuMapper.getAllMenus(params);
				params.clear();
				for (int k = 0; k < thirdMenus.size(); k++) {
					JSONObject jsonT = new JSONObject();
					Menu menuT = thirdMenus.get(k);
					jsonT.put("id", menuT.getId());
					jsonT.put("title", menuT.getTitle());
					jsonT.put("checkArr", getCheckArr(midsByRId, menuT.getId()));
					jsonT.put("parentId", menuT.getParentid());
					childrenT.add(jsonT);
				}
				jsonS.put("children", childrenT);
				childrenS.add(jsonS);
			}
			json.put("children", childrenS);
			data.add(json);
		}
		status.put("code", 200);
		status.put("message", "success");
		result.put("status", status);
		result.put("data", data);
		return result;
	}

	public JSONArray getCheckArr(List<String> midsByRId, String id) {
		JSONArray result = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("type", "0");
		json.put("isChecked", "0");
		if (midsByRId.contains(id)) {
			json.put("isChecked", "1");
		}
		result.add(json);
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
		String id = menu.getId();
		if (id != null) {
			updateMenu = menuMapper.updateMenu(menu);
			List<String> childIds = menuMapper.getIdByParentId(id, Constant.SUPERADMIN);
			for (String string : childIds) {
				Menu menus = new Menu();
				menus.setId(string);
				menus.setStatus(menu.getStatus());
				updateMenu += menuMapper.updateMenu(menus);
			}
		} else {
			updateMenu = menuMapper.insertMenu(menu);
		}
		cacheService.flushMenus();
		return updateMenu;
	}

	@Transactional
	public Integer updateSort(JSONArray array) {
		Integer updateMenu = 0;
		for (int i = 0; i < array.size(); i++) {
			JSONObject job = array.getJSONObject(i);
			Menu menu = new Menu();
			menu.setId(job.getString("id"));
			menu.setSort(job.getString("sort"));
			updateMenu += menuMapper.updateMenu(menu);
		}
		cacheService.flushMenus();
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
		cacheService.flushMenus();
		return menuMapper.delMenus(params);
	}

}
