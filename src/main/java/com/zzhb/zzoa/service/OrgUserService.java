package com.zzhb.zzoa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.zzoa.domain.activiti.UserSpr;
import com.zzhb.zzoa.mapper.OrgMapper;
import com.zzhb.zzoa.mapper.OrgUserMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.mapper.UserSprMapper;
import com.zzhb.zzoa.utils.ZtreeUtil;

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

	public List<Map<String, Object>> list(String p_id) {
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
}
