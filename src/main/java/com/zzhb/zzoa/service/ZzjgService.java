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
import com.zzhb.zzoa.mapper.OrgUserMapper;
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

	@Autowired
	OrgUserMapper orgUserMapper;

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

	public JSONArray zzjgZtreeList() {
		JSONArray result = new JSONArray();
		Map<String, String> params = new HashMap<>();
		params.put("parentid", "0");
		List<Org> list = orgMapper.getOrgs(params);
		for (Org org : list) {
			JSONObject orgJ = new JSONObject();
			orgJ.put("id", org.getId());
			orgJ.put("tId", org.getId());
			orgJ.put("name", org.getName());
			orgJ.put("checked", false);
			orgJ.put("level", org.getLevel());
			orgJ.put("parentid", "0");

			params.put("parentid", org.getId());
			JSONArray childrenOrj = new JSONArray();
			list = orgMapper.getOrgs(params);
			for (Org orgC : list) {
				JSONObject orgCJ = new JSONObject();
				orgCJ.put("id", orgC.getId());
				orgCJ.put("tId", orgC.getId());
				orgCJ.put("name", orgC.getName());
				orgCJ.put("checked", false);
				orgCJ.put("level", orgC.getLevel());
				orgCJ.put("parentid", org.getId());
				childrenOrj.add(orgCJ);
			}
			orgJ.put("children", childrenOrj);
			result.add(orgJ);
		}
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

	public JSONArray zwglZtreeList() {
		List<Job> jobs = jobMapper.getJobs(null);
		JSONArray result = new JSONArray();
		for (Job job : jobs) {
			JSONObject orgJ = new JSONObject();
			orgJ.put("id", job.getId());
			orgJ.put("name", job.getName());
			result.add(orgJ);
		}
		return result;
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
