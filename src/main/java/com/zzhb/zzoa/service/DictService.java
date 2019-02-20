package com.zzhb.zzoa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.common.Dict;
import com.zzhb.zzoa.mapper.DictMapper;
import com.zzhb.zzoa.utils.LayUiUtil;

@Service
public class DictService {

	private static Logger logger = LoggerFactory.getLogger(DictService.class);

	@Autowired
	DictMapper dictMapper;

	@Autowired
	CacheService cacheService;

	public JSONObject listDicts(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Dict> dicts = dictMapper.getDicts(params);
		PageInfo<Dict> pageInfo = new PageInfo<Dict>(dicts);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer saveorupdateDict(Dict dict, String flag) {
		Integer checkDictGtype = dictMapper.checkDictGtype(dict);
		if (checkDictGtype == 0) {
			if ("edit".equals(flag)) {
				checkDictGtype = dictMapper.updateDict(dict);
			} else {
				if (dict.getValue() == null) {
					dict.setStatus("1");
				}
				checkDictGtype = dictMapper.addDict(dict);
			}
			cacheService.flushDict();
			return checkDictGtype;
		} else {
			return -1;
		}
	}

	@Transactional
	public Integer delDict(String d_id) {
		dictMapper.delDicts(d_id);
		Integer delGDict = dictMapper.delGDict(d_id);
		cacheService.flushDict();
		return delGDict;
	}

	@Cacheable(value = "DICT", key = "#gtype")
	public List<Dict> getDictByGtype(String gtype) {
		Map<String, String> params = new HashMap<>();
		params.put("gtype", gtype);
		Dict dictByGtype = dictMapper.getDictByGtype(params);
		params.clear();
		params.put("g_id", dictByGtype.getD_id() + "");
		List<Dict> dicts = dictMapper.getDicts(params);
		return dicts;
	}

	@CacheEvict(value = "DICT", allEntries = true)
	public void flushDicts() {
	}

}
