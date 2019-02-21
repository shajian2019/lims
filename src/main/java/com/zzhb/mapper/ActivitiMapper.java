package com.zzhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zzhb.domain.User;
import com.zzhb.domain.activiti.ProcessDefinitionExt;
import com.zzhb.domain.activiti.ProcessDefinitionType;

public interface ActivitiMapper {

	@Select("SELECT NAME_ FROM act_ge_bytearray WHERE DEPLOYMENT_ID_ = #{0}")
	public List<String> getDeployResourceNameByDepId(String deploymentId);

	public Integer addProcessDefinitionExt(@Param("pdes") List<ProcessDefinitionExt> pdes);

	@Select("SELECT * FROM ext_act_re_procdef WHERE `key` = #{key} AND version = #{version}")
	public ProcessDefinitionExt getPreVersionProcessDefinitionExt(Map<String, String> params);

	public List<ProcessDefinitionExt> getProcessDefinitionExts(Map<String, String> params);

	public List<ProcessDefinitionExt> getProcessDefinitionExtsByUid(Map<String, String> params);

	public Integer delProcessDefinitionExt(Map<String, String> params);

	public List<User> getAddUsers(Map<String, String> params);

	@Select("SELECT * from ext_act_proctype")
	public List<ProcessDefinitionType> getProcessDefinitionTypes();

	@Select("SELECT rp.* FROM ext_act_proctype p LEFT JOIN ext_act_re_procdef rp on p.type = rp.protype WHERE p.type = #{0} and rp.key is not null")
	public List<ProcessDefinitionExt> getProcessDefinitionExtByProType(String proType);

	public Integer addProcessDefinitionType(ProcessDefinitionType processDefinitionType);

	public List<Map<String, Object>> getTodoTaskAndToClaimTask(Map<String, Object> params);

	public Map<String, String> getBusinessByBk(Map<String, String> params);

	@Update("UPDATE ACT_RU_TASK SET CLAIM_TIME_ = null WHERE ID_ = #{taskId}")
	public void updateRuTaskWhenUnclaim(Map<String, String> params);

	@Update("UPDATE ACT_HI_TASKINST SET CLAIM_TIME_ = null WHERE ID_ = #{taskId}")
	public void updateHiRuTaskWhenUnclaim(Map<String, String> params);

	@Update("UPDATE ACT_HI_TASKINST SET OWNER_ = #{owner}, DESCRIPTION_ = #{description}, ASSIGNEE_ = #{assignee} WHERE ID_ = #{taskId}")
	public Integer updateHiTaskInst(Map<String, String> params);

	public void delHiActInst(@Param("lists") List<String> list);
}
