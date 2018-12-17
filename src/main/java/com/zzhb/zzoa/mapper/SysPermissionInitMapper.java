package com.zzhb.zzoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.shiro.domain.SysPermissionInit;

public interface SysPermissionInitMapper {

	@Select("SELECT * FROM sys_t_permission WHERE sfsx = '1' ORDER BY sort")
	List<SysPermissionInit> selectAll();
}