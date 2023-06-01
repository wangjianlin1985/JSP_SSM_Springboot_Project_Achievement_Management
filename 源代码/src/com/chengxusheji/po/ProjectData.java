package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectData {
    /*资料id*/
    private Integer dataId;
    public Integer getDataId(){
        return dataId;
    }
    public void setDataId(Integer dataId){
        this.dataId = dataId;
    }

    /*项目*/
    private Project projectObj;
    public Project getProjectObj() {
        return projectObj;
    }
    public void setProjectObj(Project projectObj) {
        this.projectObj = projectObj;
    }

    /*资料名称*/
    @NotEmpty(message="资料名称不能为空")
    private String dataname;
    public String getDataname() {
        return dataname;
    }
    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    /*资料介绍*/
    @NotEmpty(message="资料介绍不能为空")
    private String datacontent;
    public String getDatacontent() {
        return datacontent;
    }
    public void setDatacontent(String datacontent) {
        this.datacontent = datacontent;
    }

    /*文件路径*/
    private String dataurl;
    public String getDataurl() {
        return dataurl;
    }
    public void setDataurl(String dataurl) {
        this.dataurl = dataurl;
    }

    /*排序号*/
    @NotNull(message="必须输入排序号")
    private Integer sortNumber;
    public Integer getSortNumber() {
        return sortNumber;
    }
    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    /*备注*/
    private String remark;
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /*创建人*/
    @NotEmpty(message="创建人不能为空")
    private String createuser;
    public String getCreateuser() {
        return createuser;
    }
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonProjectData=new JSONObject(); 
		jsonProjectData.accumulate("dataId", this.getDataId());
		jsonProjectData.accumulate("projectObj", this.getProjectObj().getName());
		jsonProjectData.accumulate("projectObjPri", this.getProjectObj().getProjectId());
		jsonProjectData.accumulate("dataname", this.getDataname());
		jsonProjectData.accumulate("datacontent", this.getDatacontent());
		jsonProjectData.accumulate("dataurl", this.getDataurl());
		jsonProjectData.accumulate("sortNumber", this.getSortNumber());
		jsonProjectData.accumulate("remark", this.getRemark());
		jsonProjectData.accumulate("createuser", this.getCreateuser());
		return jsonProjectData;
    }}