package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.zzhb.domain.common.Param;

public interface ParamMapper {

	public List<Param> getParams(Map<String, Object> params);

	public List<Param> getAllParams(Map<String, String> params);

	public Integer addNewParam(Param param);

	public Integer delParamById(Map<String,String> map);

	@Select("SELECT * FROM sys_t_params WHERE p_id = #{0}")
	public Param getParamById(Integer p_id);

	public Integer checkKey(Param param);

	public Integer updateParam(Param param);

}
