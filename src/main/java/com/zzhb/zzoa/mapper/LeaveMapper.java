package com.zzhb.zzoa.mapper;

import java.util.Map;

import com.zzhb.zzoa.domain.activiti.Leave;

public interface LeaveMapper {

	public Integer addLeave(Leave leave);

	public Integer delLeave(Map<String, Object> params);

}
