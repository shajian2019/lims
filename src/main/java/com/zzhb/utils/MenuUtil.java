package com.zzhb.utils;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class MenuUtil {

	public static List<Map<String, Object>> getStandardJSON(List<Map<String, Object>> queryList) {
		// 根据不同框架获取对应的List数据
		List<Map<String, Object>> parentList = Lists.newArrayList();
		List<Map<String, Object>> childList = Lists.newArrayList();
		for (Map<String, Object> map : queryList) {
			if ("0".equals(map.get("parentid").toString())) {
				parentList.add(map);
			} else {
				childList.add(map);
			}
		}
		recursionChildren(parentList, childList);
		return parentList;
	}

	// 递归获取子节点数据
	public static void recursionChildren(List<Map<String, Object>> parentList, List<Map<String, Object>> childList) {
		for (Map<String, Object> parentMap : parentList) {
			List<Map<String, Object>> childrenList = Lists.newArrayList();
			for (Map<String, Object> allMap : childList) {
				if (allMap.get("parentid").toString().equals(parentMap.get("id").toString())) {
					childrenList.add(allMap);
				}
			}
			if (!childrenList.isEmpty()) {
				parentMap.put("children", childrenList);
				recursionChildren(childrenList, childList);
			}
		}
	}
}