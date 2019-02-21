package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.zzhb.domain.common.Dict;

public interface DictMapper {

	public List<Dict> getDicts(Map<String, String> params);

	@Select("SELECT d_id,name,value,remark,gtype,g_id,sort,status,createtime,updatetime FROM sys_t_dict where d_id = #{d_id}")
	public Dict getDict(Map<String, String> params);

	@Select("SELECT d_id,name,value,remark,gtype,g_id,sort,status,createtime,updatetime FROM sys_t_dict where gtype = #{gtype}")
	public Dict getDictByGtype(Map<String, String> params);

	public Integer updateDict(Dict dict);

	public Integer checkDictGtype(Dict dict);

	public Integer addDict(Dict dict);

	@Delete("DELETE FROM sys_t_dict WHERE d_id = #{0}")
	public Integer delGDict(String d_id);

	@Delete("DELETE FROM sys_t_dict WHERE g_id = #{0}")
	public Integer delDicts(String g_id);

}
