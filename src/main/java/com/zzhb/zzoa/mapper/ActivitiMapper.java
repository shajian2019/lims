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

	public Integer addProcessDefinitionType(ProcessDefinitionType processDefinitionType);

}
