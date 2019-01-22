package com.zzhb.zzoa.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.Org;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.OrgMapper;
import com.zzhb.zzoa.mapper.OrgUserMapper;

@Service
public class OrgUserService {

	private static Logger logger = LoggerFactory.getLogger(OrgUserService.class);

	@Autowired
	OrgUserMapper orgUserMapper;

	@Autowired
	OrgMapper orgMapper;

	public JSONObject list(String p_id) {
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		List<Org> orgs = orgMapper.getOrgs(null);
		for (Org org : orgs) {
			JSONObject parent = new JSONObject();
			parent.put("id", org.getId());
			parent.put("title", org.getName());
			parent.put("level", "1");
			parent.put("parentId", "0");
			List<Map<String,String>> listOrgUser = orgUserMapper.listOrgUser(org.getId());
			parent.put("isLast", false);
			JSONObject checkP = new JSONObject();
			checkP.put("type", "0");
			checkP.put("isChecked", "0");
			JSONArray checkArrP = new JSONArray();
			checkArrP.add(checkP);
			parent.put("checkArr", checkArrP);
			JSONArray children = new JSONArray();
			Integer chenckFlag = 0;
			for (Map<String,String> user : listOrgUser) {
				JSONObject child = new JSONObject();
				child.put("id", String.valueOf(user.get("u_id")));
				String title = user.get("nickname");
				String j_name = user.get("j_name");
				if(j_name != null) {
					title +="【"+j_name+"】";
				}
				child.put("title", title);
				child.put("isLast", true);
				child.put("parentId", org.getId());
				child.put("level", "2");
				JSONObject checkC = new JSONObject();
				checkC.put("type", "0");
				Integer countProcDefByUidAndPid = orgUserMapper.countProcDefByUidAndPid(String.valueOf(user.get("u_id")), p_id);
				chenckFlag += countProcDefByUidAndPid;
				checkC.put("isChecked", countProcDefByUidAndPid + "");
				JSONArray checkArrC = new JSONArray();
				checkArrC.add(checkC);
				child.put("checkArr", checkArrC);
				children.add(child);
			}
			if (chenckFlag > 0) {
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
