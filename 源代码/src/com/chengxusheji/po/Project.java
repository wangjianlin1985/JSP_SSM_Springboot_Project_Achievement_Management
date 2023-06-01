package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Project {
    /*项目id*/
    private Integer projectId;
    public Integer getProjectId(){
        return projectId;
    }
    public void setProjectId(Integer projectId){
        this.projectId = projectId;
    }

    /*项目类别*/
    private ProjectType projectTyoeObj;
    public ProjectType getProjectTyoeObj() {
        return projectTyoeObj;
    }
    public void setProjectTyoeObj(ProjectType projectTyoeObj) {
        this.projectTyoeObj = projectTyoeObj;
    }

    /*项目编号*/
    @NotEmpty(message="项目编号不能为空")
    private String serialnumber;
    public String getSerialnumber() {
        return serialnumber;
    }
    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    /*项目名称*/
    @NotEmpty(message="项目名称不能为空")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*项目内容*/
    @NotEmpty(message="项目内容不能为空")
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*开始时间*/
    @NotEmpty(message="开始时间不能为空")
    private String beginTime;
    public String getBeginTime() {
        return beginTime;
    }
    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    /*结束时间*/
    @NotEmpty(message="结束时间不能为空")
    private String endTime;
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /*合同金额*/
    @NotNull(message="必须输入合同金额")
    private Float money;
    public Float getMoney() {
        return money;
    }
    public void setMoney(Float money) {
        this.money = money;
    }

    /*项目创建人*/
    @NotEmpty(message="项目创建人不能为空")
    private String createuser;
    public String getCreateuser() {
        return createuser;
    }
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonProject=new JSONObject(); 
		jsonProject.accumulate("projectId", this.getProjectId());
		jsonProject.accumulate("projectTyoeObj", this.getProjectTyoeObj().getTypeName());
		jsonProject.accumulate("projectTyoeObjPri", this.getProjectTyoeObj().getTypeId());
		jsonProject.accumulate("serialnumber", this.getSerialnumber());
		jsonProject.accumulate("name", this.getName());
		jsonProject.accumulate("content", this.getContent());
		jsonProject.accumulate("beginTime", this.getBeginTime().length()>19?this.getBeginTime().substring(0,19):this.getBeginTime());
		jsonProject.accumulate("endTime", this.getEndTime().length()>19?this.getEndTime().substring(0,19):this.getEndTime());
		jsonProject.accumulate("money", this.getMoney());
		jsonProject.accumulate("createuser", this.getCreateuser());
		return jsonProject;
    }}