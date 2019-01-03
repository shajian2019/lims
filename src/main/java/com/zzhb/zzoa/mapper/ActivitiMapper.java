package com.zzhb.zzoa.mapper;

import java.util.List;
import java.util.Map;

import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.domain.activiti.ProcessDefinitionExt;

public interface ActivitiMapper {

	public Integer addProcessDefinitionExt(ProcessDefinitionExt pde);

	public List<ProcessDefinitionExt> getProcessDefinitionExts(Map<String, String> params);

	public Integer delProcessDefinitionExt(Map<String, String> params);
	
	public List<User> getAddUsers(Map<String, String> params);

}
