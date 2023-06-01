package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Project;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.ProAuth;

import com.chengxusheji.mapper.ProAuthMapper;
@Service
public class ProAuthService {

	@Resource ProAuthMapper proAuthMapper;
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

    /*添加项目人员记录*/
    public void addProAuth(ProAuth proAuth) throws Exception {
    	proAuthMapper.addProAuth(proAuth);
    }

    /*按照查询条件分页查询项目人员记录*/
    public ArrayList<ProAuth> queryProAuth(Project projectObj,UserInfo userObj,String addTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_proAuth.projectObj=" + projectObj.getProjectId();
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_proAuth.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_proAuth.addTime like '%" + addTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return proAuthMapper.queryProAuth(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ProAuth> queryProAuth(Project projectObj,UserInfo userObj,String addTime) throws Exception  { 
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_proAuth.projectObj=" + projectObj.getProjectId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_proAuth.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_proAuth.addTime like '%" + addTime + "%'";
    	return proAuthMapper.queryProAuthList(where);
    }

    /*查询所有项目人员记录*/
    public ArrayList<ProAuth> queryAllProAuth()  throws Exception {
        return proAuthMapper.queryProAuthList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Project projectObj,UserInfo userObj,String addTime) throws Exception {
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_proAuth.projectObj=" + projectObj.getProjectId();
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_proAuth.userObj='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) where = where + " and t_proAuth.addTime like '%" + addTime + "%'";
        recordNumber = proAuthMapper.queryProAuthCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取项目人员记录*/
    public ProAuth getProAuth(int authId) throws Exception  {
        ProAuth proAuth = proAuthMapper.getProAuth(authId);
        return proAuth;
    }

    /*更新项目人员记录*/
    public void updateProAuth(ProAuth proAuth) throws Exception {
        proAuthMapper.updateProAuth(proAuth);
    }

    /*删除一条项目人员记录*/
    public void deleteProAuth (int authId) throws Exception {
        proAuthMapper.deleteProAuth(authId);
    }

    /*删除多条项目人员信息*/
    public int deleteProAuths (String authIds) throws Exception {
    	String _authIds[] = authIds.split(",");
    	for(String _authId: _authIds) {
    		proAuthMapper.deleteProAuth(Integer.parseInt(_authId));
    	}
    	return _authIds.length;
    }
}
