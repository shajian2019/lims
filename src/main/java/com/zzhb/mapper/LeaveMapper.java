package com.zzhb.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.zzhb.domain.activiti.Leave;

public interface LeaveMapper {

	public Integer addLeave(Leave leave);

	public Integer delLeave(Map<String, Object> params);

	@Select("SELECT * FROM oa_t_leave WHERE bk = #{bk}")
	public Leave getLeave(Map<String, String> params);

}
