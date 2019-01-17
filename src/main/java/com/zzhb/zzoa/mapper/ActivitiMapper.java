package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.domain.activiti.ProcessDefinitionExt;
import com.zzhb.zzoa.domain.activiti.ProcessDefinitionType;

public interface ActivitiMapper {

	public Integer addProcessDefinitionExt(ProcessDefinitionExt pde);

	public List<ProcessDefinitionExt> getProcessDefinitionExts(Map<String, String> params);

	public List<ProcessDefinitionExt> getProcessDefinitionExtsByUid(Map<String, String> params);

	public Integer delProcessDefinitionExt(Map<String, String> params);

	public List<User> getAddUsers(Map<String, String> params);

	@Select("SELECT * from ext_act_proctype")
	public List<ProcessDefinitionType> getProcessDefinitionTypes();

	@Select("SELECT rp.* FROM ext_act_proctype p LEFT JOIN ext_act_re_procdef rp on p.type = rp.protype WHERE p.type = #{0} and rp.key is not null")
	public List<ProcessDefinitionExt> getProcessDefinitionExtByProType(String proType);

	public Integer addProcessDefinitionType(ProcessDefinitionType processDefinitionType);

}
