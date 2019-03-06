package com.zzhb.service;

import java.util.ArrayList;
import java.util.Arrays;
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
	 * @param chkDisabled
	 *            radio 不能选择的用户ID
	 * @return
	 */
	public List<Map<String, Object>> zpOrwtZtree(String chkDisabled) {
		List<String> lists = new ArrayList<>();
		if (chkDisabled != null) {
			lists = Arrays.asList(chkDisabled.split(","));
		}
		List<Map<String, Object>> listOrgUser2 = orgUserMapper.listOrgUser();
		for (int i = 0; i < listOrgUser2.size(); i++) {
			Map<String, Object> map = listOrgUser2.get(i);
			String name = map.get("name").toString();
			map.put("nocheck", true);
			if (name.indexOf("#") != -1) {
				map.put("nocheck", false);
				String login = name.split("#")[2].equals("0") ? "离线" : "在线";
				name = name.split("#")[0] + "【" + name.split("#")[1] + "】" + login;
				map.put("name", name);
				String userId = map.get("id").toString().split("#")[1];
				if (lists.contains(userId)) {
					map.put("chkDisabled", true);
				}
			}
		}
		return ZtreeUtil.getStandardJSON(listOrgUser2);
	}

	public List<Map<String, Object>> sqrZtree(String p_id) {
		List<Map<String, Object>> listOrgUser2 = orgUserMapper.listOrgUser();
		List<String> usersIdByPId = new ArrayList<>();
		if (p_id != null) {
			usersIdByPId = userMapper.getUsersIdByPId(p_id);
		}
		for (int i = 0; i < listOrgUser2.size(); i++) {
			Map<String, Object> map = listOrgUser2.get(i);
			String name = map.get("name").toString();
			if (name.indexOf("#") != -1) {
				String login = name.split("#")[2].equals("0") ? "离线" : "在线";
				name = name.split("#")[0] + "【" + name.split("#")[1] + "】" + login;
				map.put("name", name);
				if (!usersIdByPId.isEmpty()) {
					String oIdUid = map.get("id").toString();
					if (usersIdByPId.contains(oIdUid)) {
						map.put("checked", true);
					}
				}
			}
		}
		return ZtreeUtil.getStandardJSON(listOrgUser2);
	}

	public List<Map<String, Object>> sprZtree(UserSpr userSpr) {
		List<Map<String, Object>> listOrgUser2 = orgUserMapper.listOrgUser();
		List<String> usersIdByPId = new ArrayList<>();
		UserSpr userSprs = userSprMapper.getUserSprs(userSpr);
		if (userSprs != null) {
			usersIdByPId = Arrays.asList(userSprs.getSprs().split(","));
		}
		List<String> userOrgs = userOrgJobMapper.getUserOrgs(userSpr.getUid());
		for (int i = 0; i < listOrgUser2.size(); i++) {
			Map<String, Object> map = listOrgUser2.get(i);
			String name = map.get("name").toString();
			if (name.indexOf("#") != -1) {
				String login = name.split("#")[2].equals("0") ? "离线" : "在线";
				name = name.split("#")[0] + "【" + name.split("#")[1] + "】" + login;
				map.put("name", name);
				String oIdUid = map.get("id").toString();
				if (!usersIdByPId.isEmpty()) {
					if (usersIdByPId.contains(oIdUid)) {
						map.put("checked", true);
					}
				}
				if (!userOrgs.isEmpty()) {
					oIdUid = oIdUid.split("#")[1];
					if (userOrgs.contains(oIdUid)) {
						map.put("open", true);
					}
				}
			}
		}
		return ZtreeUtil.getStandardJSON(listOrgUser2);
	}

}
