package com.zzhb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.domain.activiti.UserSpr;
import com.zzhb.mapper.OrgMapper;
import com.zzhb.mapper.OrgUserMapper;
import com.zzhb.mapper.UserMapper;
import com.zzhb.mapper.UserOrgJobMapper;
import com.zzhb.mapper.UserSprMapper;
import com.zzhb.utils.ZtreeUtil;

@Service
public class OrgUserService {

	private static Logger logger = LoggerFactory.getLogger(OrgUserService.class);

	@Autowired
	UserMapper userMapper;

	@Autowired
	OrgUserMapper orgUserMapper;

	@Autowired
	OrgMapper orgMapper;

	@Autowired
	UserSprMapper userSprMapper;

	@Autowired
	UserOrgJobMapper userOrgJobMapper;
	
	/**
	 * 
	 * @param chkDisabled radio 不能选择的用户ID
	 * @return
	 */
	public List<Map<String, Object>> zpOrwt(String chkDisabled) {
		List<Map<String, Object>> listOrgUser = new ArrayList<>();
		List<Map<String, Object>> listOrgUser2 = orgUserMapper.listOrgUser2();
		for (int i = 0; i < listOrgUser2.size(); i++) {
			Map<String, Object> map = listOrgUser2.get(i);
			if (i > 1) {
				Map<String, Object> map2 = listOrgUser2.get(i - 1);
				if (!map2.get("id").equals(map.get("id"))) {
					listOrgUser.add(map);
				}
			} else {
				listOrgUser.add(map);
			}
			map.put("nocheck", true);
			if (map.get("u_id") != null) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("id", map.get("id") + "#" + map.get("u_id"));
				Object name = map.get("nickname");
				if (map.get("j_name") != null) {
					name += " 【" + map.get("j_name") + "】";
				}
				map2.put("name", name);
				map2.put("parentid", map.get("id"));
				if (chkDisabled != null) {
					String userId = map.get("u_id").toString();
					List<String> lists = Arrays.asList(chkDisabled.split(","));
					if (lists.contains(userId)) {
						map2.put("chkDisabled", true);
					}
				}
				map.remove("u_id");
				map.remove("j_name");
				map.remove("nickname");
				listOrgUser.add(map2);
			}
		}
		return ZtreeUtil.getStandardJSON(listOrgUser);
	}

	public List<Map<String, Object>> sqr(String p_id) {
		List<Map<String, Object>> listOrgUser = new ArrayList<>();
		List<Map<String, Object>> listOrgUser2 = orgUserMapper.listOrgUser2();
		List<String> usersIdByPId = new ArrayList<>();
		if (p_id != null) {
			usersIdByPId = userMapper.getUsersIdByPId(p_id);
		}
		for (int i = 0; i < listOrgUser2.size(); i++) {
			Map<String, Object> map = listOrgUser2.get(i);
			if (i > 1) {
				Map<String, Object> map2 = listOrgUser2.get(i - 1);
				if (!map2.get("id").equals(map.get("id"))) {
					listOrgUser.add(map);
				}
			} else {
				listOrgUser.add(map);
			}
			if (map.get("u_id") != null) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("id", map.get("id") + "#" + map.get("u_id"));
				Object name = map.get("nickname");
				if (map.get("j_name") != null) {
					name += " 【" + map.get("j_name") + "】";
				}
				map2.put("name", name);
				map2.put("parentid", map.get("id"));
				if (!usersIdByPId.isEmpty()) {
					String oIdUid = map.get("id") + "#" + map.get("u_id");
					if (usersIdByPId.contains(oIdUid)) {
						map2.put("checked", true);
					}
				}
				map.remove("u_id");
				map.remove("j_name");
				map.remove("nickname");
				listOrgUser.add(map2);
			}
		}
		return ZtreeUtil.getStandardJSON(listOrgUser);
	}

	public List<Map<String, Object>> sprList(UserSpr userSpr) {
		List<Map<String, Object>> listOrgUser = new ArrayList<>();
		List<Map<String, Object>> listOrgUser2 = orgUserMapper.listOrgUser2();
		List<String> usersIdByPId = new ArrayList<>();
		UserSpr userSprs = userSprMapper.getUserSprs(userSpr);
		if (userSprs != null) {
			usersIdByPId = Arrays.asList(userSprs.getSprs().split(","));
		}
		List<String> userOrgs = userOrgJobMapper.getUserOrgs(userSpr.getUid());
		for (int i = 0; i < listOrgUser2.size(); i++) {
			Map<String, Object> map = listOrgUser2.get(i);
			if (i > 1) {
				Map<String, Object> map2 = listOrgUser2.get(i - 1);
				if (!map2.get("id").equals(map.get("id"))) {
					listOrgUser.add(map);
				}
			} else {
				listOrgUser.add(map);
			}
			if (map.get("u_id") != null) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("id", map.get("id") + "#" + map.get("u_id"));
				Object name = map.get("nickname");
				if (map.get("j_name") != null) {
					name += " 【" + map.get("j_name") + "】";
				}
				map2.put("name", name);
				map2.put("parentid", map.get("id"));
				if (!usersIdByPId.isEmpty()) {
					String oIdUid = map.get("id") + "#" + map.get("u_id");
					if (usersIdByPId.contains(oIdUid)) {
						map2.put("checked", true);
					}
				}
				if (!userOrgs.isEmpty()) {
					String id = map.get("id").toString();
					if (userOrgs.contains(id)) {
						map.put("open", true);
					}
				}
				map.remove("u_id");
				map.remove("j_name");
				map.remove("nickname");
				listOrgUser.add(map2);
			}
		}
		return ZtreeUtil.getStandardJSON(listOrgUser);
	}
}
