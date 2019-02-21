package com.zzhb.shiro.filter;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zzhb.shiro.domain.SysPermissionInit;
import com.zzhb.shiro.mapper.SysPermissionInitMapper;

@Component
public class FilterChainDefinitionMapBuilder {

	@Autowired
	private SysPermissionInitMapper sysPermissionInitMapper;

	public LinkedHashMap<String, String> buildFilterChainDefinitionMap() {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		List<SysPermissionInit> list = sysPermissionInitMapper.selectAll();
		for (SysPermissionInit sysPermissionInit : list) {
			map.put(sysPermissionInit.getUrl(), sysPermissionInit.getQxlj());
		}
		return map;
	}

}
