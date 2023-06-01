package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ProAuth {
    /*记录id*/
    private Integer authId;
    public Integer getAuthId(){
        return authId;
    }
    public void setAuthId(Integer authId){
        this.authId = authId;
    }

    /*项目*/
    private Project projectObj;
    public Project getProjectObj() {
        return projectObj;
    }
    public void setProjectObj(Project projectObj) {
        this.projectObj = projectObj;
    }

    /*用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*备注*/
    private String remark;
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /*加入时间*/
    @NotEmpty(message="加入时间不能为空")
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonProAuth=new JSONObject(); 
		jsonProAuth.accumulate("authId", this.getAuthId());
		jsonProAuth.accumulate("projectObj", this.getProjectObj().getName());
		jsonProAuth.accumulate("projectObjPri", this.getProjectObj().getProjectId());
		jsonProAuth.accumulate("userObj", this.getUserObj().getName());
		jsonProAuth.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonProAuth.accumulate("remark", this.getRemark());
		jsonProAuth.accumulate("addTime", this.getAddTime().length()>19?this.getAddTime().substring(0,19):this.getAddTime());
		return jsonProAuth;
    }}