package com.zzhb.mapper;

import org.apache.ibatis.annotations.Select;

import com.zzhb.domain.Table;

public interface TableMapper {

	@Select("SELECT * FROM sys_t_table WHERE procdefkey = #{0}")
	public Table getTableByProcdefkey(String procdefkey);

}
