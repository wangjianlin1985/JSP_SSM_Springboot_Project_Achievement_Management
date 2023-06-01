package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ProjectData;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.DataAuth;

import com.chengxusheji.mapper.DataAuthMapper;
@Service
public class DataAuthService {

	@Resource DataAuthMapper dataAuthMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加资料权限记录*/
    public void addDataAuth(DataAuth dataAuth) throws Exception {
    	dataAuthMapper.addDataAuth(dataAuth);
    }

    /*按照查询条件分页查询资料权限记录*/
    public ArrayList<DataAuth> queryDataAuth(ProjectData dataObj,UserInfo userObj,String requestTime,String shenHeState,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != dataObj && dataObj.getDataId()!= null && dataObj.getDataId()!= 0)  where += " and t_dataAuth.dataObj=" + dataObj.getDataId();
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_dataAuth.userObj='" + userObj.getUser_name() + "'";
    	if(!requestTime.equals("")) where = where + " and t_dataAuth.requestTime like '%" + requestTime + "%'";
    	if(!shenHeState.equals("")) where = where + " and t_dataAuth.shenHeState like '%" + shenHeState + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return dataAuthMapper.queryDataAuth(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<DataAuth> queryDataAuth(ProjectData dataObj,UserInfo userObj,String requestTime,String shenHeState) throws Exception  { 
     	String where = "where 1=1";
    	if(null != dataObj && dataObj.getDataId()!= null && dataObj.getDataId()!= 0)  where += " and t_dataAuth.dataObj=" + dataObj.getDataId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_dataAuth.userObj='" + userObj.getUser_name() + "'";
    	if(!requestTime.equals("")) where = where + " and t_dataAuth.requestTime like '%" + requestTime + "%'";
    	if(!shenHeState.equals("")) where = where + " and t_dataAuth.shenHeState like '%" + shenHeState + "%'";
    	return dataAuthMapper.queryDataAuthList(where);
    }

    /*查询所有资料权限记录*/
    public ArrayList<DataAuth> queryAllDataAuth()  throws Exception {
        return dataAuthMapper.queryDataAuthList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(ProjectData dataObj,UserInfo userObj,String requestTime,String shenHeState) throws Exception {
     	String where = "where 1=1";
    	if(null != dataObj && dataObj.getDataId()!= null && dataObj.getDataId()!= 0)  where += " and t_dataAuth.dataObj=" + dataObj.getDataId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_dataAuth.userObj='" + userObj.getUser_name() + "'";
    	if(!requestTime.equals("")) where = where + " and t_dataAuth.requestTime like '%" + requestTime + "%'";
    	if(!shenHeState.equals("")) where = where + " and t_dataAuth.shenHeState like '%" + shenHeState + "%'";
        recordNumber = dataAuthMapper.queryDataAuthCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取资料权限记录*/
    public DataAuth getDataAuth(int authId) throws Exception  {
        DataAuth dataAuth = dataAuthMapper.getDataAuth(authId);
        return dataAuth;
    }

    /*更新资料权限记录*/
    public void updateDataAuth(DataAuth dataAuth) throws Exception {
        dataAuthMapper.updateDataAuth(dataAuth);
    }

    /*删除一条资料权限记录*/
    public void deleteDataAuth (int authId) throws Exception {
        dataAuthMapper.deleteDataAuth(authId);
    }

    /*删除多条资料权限信息*/
    public int deleteDataAuths (String authIds) throws Exception {
    	String _authIds[] = authIds.split(",");
    	for(String _authId: _authIds) {
    		dataAuthMapper.deleteDataAuth(Integer.parseInt(_authId));
    	}
    	return _authIds.length;
    }
}
