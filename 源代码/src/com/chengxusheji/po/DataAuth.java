package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class DataAuth {
    /*授权id*/
    private Integer authId;
    public Integer getAuthId(){
        return authId;
    }
    public void setAuthId(Integer authId){
        this.authId = authId;
    }

    /*项目资料*/
    private ProjectData dataObj;
    public ProjectData getDataObj() {
        return dataObj;
    }
    public void setDataObj(ProjectData dataObj) {
        this.dataObj = dataObj;
    }

    /*用户*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*申请时间*/
    @NotEmpty(message="申请时间不能为空")
    private String requestTime;
    public String getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    /*申请状态*/
    @NotEmpty(message="申请状态不能为空")
    private String shenHeState;
    public String getShenHeState() {
        return shenHeState;
    }
    public void setShenHeState(String shenHeState) {
        this.shenHeState = shenHeState;
    }

    /*审核回复*/
    @NotEmpty(message="审核回复不能为空")
    private String shenHeReply;
    public String getShenHeReply() {
        return shenHeReply;
    }
    public void setShenHeReply(String shenHeReply) {
        this.shenHeReply = shenHeReply;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonDataAuth=new JSONObject(); 
		jsonDataAuth.accumulate("authId", this.getAuthId());
		jsonDataAuth.accumulate("dataObj", this.getDataObj().getDataname());
		jsonDataAuth.accumulate("dataObjPri", this.getDataObj().getDataId());
		jsonDataAuth.accumulate("userObj", this.getUserObj().getName());
		jsonDataAuth.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonDataAuth.accumulate("requestTime", this.getRequestTime().length()>19?this.getRequestTime().substring(0,19):this.getRequestTime());
		jsonDataAuth.accumulate("shenHeState", this.getShenHeState());
		jsonDataAuth.accumulate("shenHeReply", this.getShenHeReply());
		return jsonDataAuth;
    }}