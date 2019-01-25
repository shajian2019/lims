package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zzhb.zzoa.domain.activiti.UserSpr;

public interface UserSprMapper {

	public UserSpr getUserSprs(Map<String, String> params);

	public Integer replaceSprsById(Map<String,String> params);
	
	public Integer updateSprs(Map<String,String> params);
	
	public List<Map<String,String>> getSprs(@Param("u_ids")List<String> uIds);
	
	public void delSprs(Map<String,String> params);
}
