package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import com.zzhb.zzoa.domain.common.Param;

public interface ParamMapper {

	public List<Param> getParams(Map<String, Object> params);

}
