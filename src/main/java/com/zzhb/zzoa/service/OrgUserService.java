package com.zzhb.zzoa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.Org;
import com.zzhb.zzoa.domain.activiti.UserSpr;
import com.zzhb.zzoa.mapper.OrgMapper;
import com.zzhb.zzoa.mapper.OrgUserMapper;
import com.zzhb.zzoa.mapper.UserSprMapper;
import com.zzhb.zzoa.utils.ZtreeUtil;

@Service
public class OrgUserService {

	private static Logger logger = LoggerFactory.getLogger(OrgUserService.class);

	@Autowired
	OrgUserMapper orgUserMapper;

	@Autowired
	OrgMapper orgMapper;

	@Autowired
	UserSprMapper userSprMapper;

	public List<Map<String, Object>> list() {
		List<Map<String, Object>> listOrgUser = new ArrayList<>();
		List<Map<String, Object>> listOrgUser2 = orgUserMapper.listOrgUser2();
		for (Map<String, Object> map : listOrgUser2) {
			listOrgUser.add(map);
			if (map.get("u_id") != null) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("id", map.get("id") + "#" + map.get("u_id"));
				Object name = map.get("nickname");
				if (map.get("j_name") != null) {
					name += " 【" + map.get("j_name") + "】";
				}
				map2.put("name", name);
				map2.put("parentid", map.get("id"));
				map.remove("u_id");
				map.remove("j_name");
				map.remove("nickname");
				listOrgUser.add(map2);
			}
		}
		return ZtreeUtil.getStandardJSON(listOrgUser);
	}

	public JSONObject sprList(Map<String, String> params) {
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		List<Org> orgs = orgMapper.getOrgs(null);
		UserSpr userSpr = userSprMapper.getUserSprs(params);
		String uId = params.get("u_id");
		for (Org org : orgs) {
			JSONObject parent = new JSONObject();
			parent.put("id", org.getId());
			parent.put("title", org.getName());
			parent.put("level", "1");
			parent.put("parentId", "0");
			parent.put("spread", false);
			List<Map<String, String>> listOrgUser = orgUserMapper.listOrgUser(org.getId());
			parent.put("isLast", false);
			JSONObject checkP = new JSONObject();
			checkP.put("type", "0");
			checkP.put("isChecked", "0");
			JSONArray checkArrP = new JSONArray();
			checkArrP.add(checkP);
			parent.put("checkArr", checkArrP);
			JSONArray children = new JSONArray();
			Integer checkFlag = 0;
			for (Map<String, String> user : listOrgUser) {
				if (uId.equals(String.valueOf(user.get("u_id")))) {
					parent.put("spread", true);
				}
				JSONObject child = new JSONObject();
				child.put("id", String.valueOf(user.get("u_id")));
				String title = user.get("nickname");
				String j_name = user.get("j_name");
				if (j_name != null) {
					title += "【" + j_name + "】";
				}
				child.put("title", title);
				child.put("isLast", true);
				child.put("parentId", org.getId());
				child.put("level", "2");
				JSONObject checkC = new JSONObject();
				checkC.put("type", "0");
				checkC.put("isChecked", "0");
				if (userSpr != null && userSpr.getSprs().indexOf(String.valueOf(user.get("u_id"))) != -1) {
					checkC.put("isChecked", "1");
					checkFlag += 1;
				}
				JSONArray checkArrC = new JSONArray();
				checkArrC.add(checkC);
				child.put("checkArr", checkArrC);
				children.add(child);
			}
			if (checkFlag > 0) {
				parent.getJSONArray("checkArr").getJSONObject(0).put("isChecked", "1");
			}
			parent.put("children", children);
			array.add(parent);
		}
		result.put("data", array);
		JSONObject status = new JSONObject();
		status.put("code", 200);
		status.put("message", "操作成功");
		result.put("status", status);
		return result;
	}
}
