package com.zzhb.zzoa.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.Job;
import com.zzhb.zzoa.domain.Org;
import com.zzhb.zzoa.mapper.ActivitiMapper;
import com.zzhb.zzoa.mapper.JobMapper;
import com.zzhb.zzoa.mapper.OrgMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.utils.LayUiUtil;

@Service
public class ZzjgService {

	@Autowired
	IdentityService identityService;

	@Autowired
	ActivitiMapper activitiMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	OrgMapper orgMapper;

	public JSONObject zzjgList(Map<String, String> params) {
		JSONObject result = new JSONObject();
		List<Org> list = orgMapper.getOrgs(params);
		result.put("code", 0);
		result.put("msg", "");
		result.put("count", list.size());
		result.put("is", true);
		result.put("data", list);
		result.put("tip", "操作成功");
		return result;
	}

	public JSONObject zzjgDtreeList() {
		Map<String, String> params = new HashMap<>();
		params.put("parentid", "0");
		List<Org> list = orgMapper.getOrgs(params);
		JSONObject result = new JSONObject();
		JSONArray data = new JSONArray();
		JSONArray checkArr = new JSONArray();
		JSONObject checked = new JSONObject();
		checked.put("type", "0");
		checked.put("isChecked", "0");
		checkArr.add(checked);
		for (Org org : list) {
			JSONObject groupJ = new JSONObject();
			groupJ.put("id", org.getId());
			groupJ.put("title", org.getName());
			groupJ.put("parentId", org.getParentid());
			groupJ.put("checkArr", checkArr);
			params.put("parentid", org.getId());
			JSONArray children = new JSONArray();
			list = orgMapper.getOrgs(params);
			for (Org orgC : list) {
				JSONObject orgJC = new JSONObject();
				orgJC.put("id", orgC.getId());
				orgJC.put("title", orgC.getName());
				orgJC.put("parentId", orgC.getParentid());
				orgJC.put("checkArr", checkArr);
				children.add(orgJC);
			}
			groupJ.put("children", children);
			data.add(groupJ);
		}
		result.put("data", data);
		JSONObject status = new JSONObject();
		status.put("code", 200);
		status.put("message", "操作成功");
		result.put("status", status);
		return result;
	}

	public JSONObject zzjgUserList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Map<String, String>> list = orgMapper.getUsers(params);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
		return LayUiUtil.pagination(pageInfo);
	}

	public JSONObject zzjgAddUserList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Map<String, String>> addUsers = orgMapper.getAddUsers(params);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(addUsers);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer zzjgUserDel(String u_id) {
		return orgMapper.delUserOrgByUid(u_id);
	}

	@Transactional
	public Integer zzjgAdd(Org org) {
		Map<String, String> params = new HashMap<>();
		Integer addOrg = 0;
		String id = org.getId();
		if (id == null) {
			params.put("name", org.getName());
			params.put("parentid", org.getParentid());
			List<Org> orgs = orgMapper.getOrgs(params);
			if (orgs.isEmpty()) {
				addOrg = orgMapper.addOrg(org);
			} else {
				return -1;
			}
		} else {
			addOrg = orgMapper.checkOrgName(org);
			if (addOrg > 0) {
				return -1;
			} else {
				addOrg = orgMapper.updateOrg(org);
			}
		}
		return addOrg;
	}

	@Transactional
	public Integer updateZzjgSort(JSONArray array) {
		Integer updateOrg = 0;
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = array.getJSONObject(i);
			Org org = new Org();
			org.setId(json.getString("id"));
			org.setSort(json.getString("sort"));
			updateOrg += orgMapper.updateOrg(org);
		}
		return updateOrg;
	}

	@Transactional
	public Integer zzjgEdit(Org org) {
		Integer updateOrg = orgMapper.updateOrg(org);
		return updateOrg;
	}

	@Transactional
	public Integer zzjgDel(Org org) {
		Map<String, String> params = new HashMap<>();
		params.put("parentid", org.getId());
		List<Org> orgs = orgMapper.getOrgs(params);
		orgs.add(org);
		orgMapper.delUserOrgByOid(orgs);
		return orgMapper.delOrg(orgs);
	}

	@Transactional
	public Integer zzjgUserAdd(String o_id, String u_ids) {
		List<String> asList = Arrays.asList(u_ids.split("\\|"));
		Map<String, Object> params = new HashMap<>();
		params.put("u_ids", asList);
		params.put("o_id", o_id);
		return orgMapper.addUserOrg(params);
	}

	@Autowired
	JobMapper jobMapper;

	public JSONObject zwglList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Job> list = jobMapper.getJobs(params);
		PageInfo<Job> pageInfo = new PageInfo<>(list);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer zwglAddOrUpdate(Job job) {
		Integer addjob = 0;
		Map<String, String> params = new HashMap<>();
		if (job.getId() == null) {
			params.put("name", job.getName());
			List<Job> jobs = jobMapper.getJobs(params);
			if (jobs.isEmpty()) {
				addjob = jobMapper.addJob(job);
			} else {
				return -1;
			}
		} else {
			addjob = jobMapper.checkJobName(job);
			if (addjob > 0) {
				return -1;
			} else {
				addjob = jobMapper.updateJob(job);
			}
		}
		return addjob;
	}

	@Transactional
	public Integer zwglEdit(Job job) {
		Integer updatejob = jobMapper.updateJob(job);
		return updatejob;
	}

	public JSONObject zwglUserList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Map<String, String>> list = jobMapper.getUsers(params);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer zwglUserDel(String u_id) {
		return jobMapper.delUserJobByUid(u_id);
	}

	public JSONObject zwglAddUserList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Map<String, String>> addUsers = jobMapper.getAddUsers(params);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(addUsers);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer zwglUserAdd(String j_id, String u_ids) {
		List<String> asList = Arrays.asList(u_ids.split("\\|"));
		Map<String, Object> params = new HashMap<>();
		params.put("u_ids", asList);
		params.put("j_id", j_id);
		return jobMapper.addUserJob(params);
	}

	@Transactional
	public Integer zwglDel(Job job) {
		jobMapper.delUserJobByJid(job);
		return jobMapper.delJob(job);
	}

	@Transactional
	public Integer updateZwglSort(JSONArray array) {
		Integer updateOrg = 0;
		for (int i = 0; i < array.size(); i++) {
			JSONObject json = array.getJSONObject(i);
			Job job = new Job();
			job.setId(json.getString("id"));
			job.setSort(json.getString("sort"));
			updateOrg += jobMapper.updateJob(job);
		}
		return updateOrg;
	}
}
