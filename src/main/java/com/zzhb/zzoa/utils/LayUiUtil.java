package com.zzhb.zzoa.utils;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

public class LayUiUtil {

	public static <T> JSONObject pagination(PageInfo<T> pageInfo) {
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("msg", "");
		result.put("count", pageInfo.getTotal());
		result.put("data", pageInfo.getList());
		return result;
	}

	public static <T> JSONObject pagination(long count, List<T> list) {
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("msg", "");
		result.put("count", count);
		result.put("data", list);
		return result;
	}
}
