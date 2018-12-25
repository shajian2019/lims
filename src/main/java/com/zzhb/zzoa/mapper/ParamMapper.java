package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import com.zzhb.zzoa.domain.common.Param;
import org.apache.ibatis.annotations.Select;

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
