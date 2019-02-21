package com.zzhb.utils;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ZtreeUtil {

	@SuppressWarnings("unchecked")
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
		for (Map<String, Object> map : parentList) {
			List<Map<String, Object>> children = (List<Map<String, Object>>) map.get("children");
			if (children != null) {
				for (Map<String, Object> map2 : children) {
					Object checked = map2.get("checked");
					Object open = map2.get("open");
					if (checked != null) {
						map.put("checked", true);
					}
					if (open != null) {
						map.put("open", true);
					}
				}
			}
		}
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
				for (Map<String, Object> map : childrenList) {
					Object object = map.get("checked");
					if (object != null) {
						parentMap.put("checked", true);
						break;
					}
				}
				parentMap.put("children", childrenList);
				recursionChildren(childrenList, childList);
			}
		}
	}
}
