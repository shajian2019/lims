package com.zzhb.zzoa.domain.activiti;

public class ProcessDefinitionExt {

	private String id;
	private String name;
	private String key;
	private Integer version;
	private String deployment_id;
	private String resource_name;
	private String dgrm_resource_name;
	private String description;
	private String createtime;
	private String updatetime;
	private String createuser;
	private String updateuser;
	private String protype;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDeployment_id() {
		return deployment_id;
	}

	public void setDeployment_id(String deployment_id) {
		this.deployment_id = deployment_id;
	}

	public String getResource_name() {
		return resource_name;
	}

	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	public String getDgrm_resource_name() {
		return dgrm_resource_name;
	}

	public void setDgrm_resource_name(String dgrm_resource_name) {
		this.dgrm_resource_name = dgrm_resource_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	public String getProtype() {
		return protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	@Override
	public String toString() {
		return "ProcessDefinitionExt [id=" + id + ", name=" + name + ", key=" + key + ", version=" + version
				+ ", deployment_id=" + deployment_id + ", resource_name=" + resource_name + ", dgrm_resource_name="
				+ dgrm_resource_name + ", description=" + description + ", createtime=" + createtime + ", updatetime="
				+ updatetime + ", createuser=" + createuser + ", updateuser=" + updateuser + ", protype=" + protype
				+ "]";
	}

}
