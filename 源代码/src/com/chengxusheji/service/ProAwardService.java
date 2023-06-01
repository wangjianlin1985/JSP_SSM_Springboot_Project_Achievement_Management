package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Project;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.ProAward;

import com.chengxusheji.mapper.ProAwardMapper;
@Service
public class ProAwardService {

	@Resource ProAwardMapper proAwardMapper;
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

    /*添加项目获奖记录*/
    public void addProAward(ProAward proAward) throws Exception {
    	proAwardMapper.addProAward(proAward);
    }

    /*按照查询条件分页查询项目获奖记录*/
    public ArrayList<ProAward> queryProAward(Project projectObj,String awardName,UserInfo userObj,String awardTime,String createTime,String createUser,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_proAward.projectObj=" + projectObj.getProjectId();
    	if(!awardName.equals("")) where = where + " and t_proAward.awardName like '%" + awardName + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_proAward.userObj='" + userObj.getUser_name() + "'";
    	if(!awardTime.equals("")) where = where + " and t_proAward.awardTime like '%" + awardTime + "%'";
    	if(!createTime.equals("")) where = where + " and t_proAward.createTime like '%" + createTime + "%'";
    	if(!createUser.equals("")) where = where + " and t_proAward.createUser like '%" + createUser + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return proAwardMapper.queryProAward(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ProAward> queryProAward(Project projectObj,String awardName,UserInfo userObj,String awardTime,String createTime,String createUser) throws Exception  { 
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_proAward.projectObj=" + projectObj.getProjectId();
    	if(!awardName.equals("")) where = where + " and t_proAward.awardName like '%" + awardName + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_proAward.userObj='" + userObj.getUser_name() + "'";
    	if(!awardTime.equals("")) where = where + " and t_proAward.awardTime like '%" + awardTime + "%'";
    	if(!createTime.equals("")) where = where + " and t_proAward.createTime like '%" + createTime + "%'";
    	if(!createUser.equals("")) where = where + " and t_proAward.createUser like '%" + createUser + "%'";
    	return proAwardMapper.queryProAwardList(where);
    }

    /*查询所有项目获奖记录*/
    public ArrayList<ProAward> queryAllProAward()  throws Exception {
        return proAwardMapper.queryProAwardList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Project projectObj,String awardName,UserInfo userObj,String awardTime,String createTime,String createUser) throws Exception {
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_proAward.projectObj=" + projectObj.getProjectId();
    	if(!awardName.equals("")) where = where + " and t_proAward.awardName like '%" + awardName + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_proAward.userObj='" + userObj.getUser_name() + "'";
    	if(!awardTime.equals("")) where = where + " and t_proAward.awardTime like '%" + awardTime + "%'";
    	if(!createTime.equals("")) where = where + " and t_proAward.createTime like '%" + createTime + "%'";
    	if(!createUser.equals("")) where = where + " and t_proAward.createUser like '%" + createUser + "%'";
        recordNumber = proAwardMapper.queryProAwardCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取项目获奖记录*/
    public ProAward getProAward(int awardId) throws Exception  {
        ProAward proAward = proAwardMapper.getProAward(awardId);
        return proAward;
    }

    /*更新项目获奖记录*/
    public void updateProAward(ProAward proAward) throws Exception {
        proAwardMapper.updateProAward(proAward);
    }

    /*删除一条项目获奖记录*/
    public void deleteProAward (int awardId) throws Exception {
        proAwardMapper.deleteProAward(awardId);
    }

    /*删除多条项目获奖信息*/
    public int deleteProAwards (String awardIds) throws Exception {
    	String _awardIds[] = awardIds.split(",");
    	for(String _awardId: _awardIds) {
    		proAwardMapper.deleteProAward(Integer.parseInt(_awardId));
    	}
    	return _awardIds.length;
    }
}
