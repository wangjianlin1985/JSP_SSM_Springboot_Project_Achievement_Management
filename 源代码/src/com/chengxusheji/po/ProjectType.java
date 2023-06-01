package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectType {
    /*项目类别id*/
    private Integer typeId;
    public Integer getTypeId(){
        return typeId;
    }
    public void setTypeId(Integer typeId){
        this.typeId = typeId;
    }

    /*项目类别名称*/
    @NotEmpty(message="项目类别名称不能为空")
    private String typeName;
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /*备注*/
    private String remark;
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonProjectType=new JSONObject(); 
		jsonProjectType.accumulate("typeId", this.getTypeId());
		jsonProjectType.accumulate("typeName", this.getTypeName());
		jsonProjectType.accumulate("remark", this.getRemark());
		return jsonProjectType;
    }}