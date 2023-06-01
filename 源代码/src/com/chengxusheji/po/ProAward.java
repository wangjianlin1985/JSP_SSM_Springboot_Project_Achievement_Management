package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ProAward {
    /*获奖id*/
    private Integer awardId;
    public Integer getAwardId(){
        return awardId;
    }
    public void setAwardId(Integer awardId){
        this.awardId = awardId;
    }

    /*获奖项目*/
    private Project projectObj;
    public Project getProjectObj() {
        return projectObj;
    }
    public void setProjectObj(Project projectObj) {
        this.projectObj = projectObj;
    }

    /*获奖名称*/
    @NotEmpty(message="获奖名称不能为空")
    private String awardName;
    public String getAwardName() {
        return awardName;
    }
    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    /*级别*/
    @NotEmpty(message="级别不能为空")
    private String level;
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    /*获奖用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*获奖时间*/
    @NotEmpty(message="获奖时间不能为空")
    private String awardTime;
    public String getAwardTime() {
        return awardTime;
    }
    public void setAwardTime(String awardTime) {
        this.awardTime = awardTime;
    }

    /*创建时间*/
    @NotEmpty(message="创建时间不能为空")
    private String createTime;
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /*创建人*/
    @NotEmpty(message="创建人不能为空")
    private String createUser;
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /*附件信息*/
    private String accessory;
    public String getAccessory() {
        return accessory;
    }
    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonProAward=new JSONObject(); 
		jsonProAward.accumulate("awardId", this.getAwardId());
		jsonProAward.accumulate("projectObj", this.getProjectObj().getName());
		jsonProAward.accumulate("projectObjPri", this.getProjectObj().getProjectId());
		jsonProAward.accumulate("awardName", this.getAwardName());
		jsonProAward.accumulate("level", this.getLevel());
		jsonProAward.accumulate("userObj", this.getUserObj().getName());
		jsonProAward.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonProAward.accumulate("awardTime", this.getAwardTime().length()>19?this.getAwardTime().substring(0,19):this.getAwardTime());
		jsonProAward.accumulate("createTime", this.getCreateTime().length()>19?this.getCreateTime().substring(0,19):this.getCreateTime());
		jsonProAward.accumulate("createUser", this.getCreateUser());
		jsonProAward.accumulate("accessory", this.getAccessory());
		return jsonProAward;
    }}