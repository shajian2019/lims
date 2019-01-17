package com.zzhb.zzoa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.Org;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.OrgMapper;
import com.zzhb.zzoa.mapper.UserMapper;

@Service
public class OrgUserService {
	
	@Autowired
	OrgMapper orgMapper;
	
	@Autowired
	UserMapper userMapper;
	
	public JSONObject getOrgUsers() {
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		List<Org> orgs = orgMapper.getOrgs(null);
		for (Org org : orgs) {
			JSONObject orgJ = new JSONObject();
			orgJ.put("id", org.getId());
			orgJ.put("deptname", org.getName());
			JSONArray children = new JSONArray();
			List<User> users = userMapper.getUsersByOid(org.getId());
			for (User user : users) {
				JSONObject userJ = new JSONObject();
				userJ.put("id", user.getU_id());
				userJ.put("deptname", user.getNickname());
				userJ.put("children", new JSONArray());
				children.add(userJ);
			}
			orgJ.put("children", children);
			array.add(orgJ);
		}
		result.put("data", array);
		return result;
	}
}
