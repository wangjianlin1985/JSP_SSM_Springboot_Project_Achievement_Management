package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Project;
import com.chengxusheji.po.ProjectData;

import com.chengxusheji.mapper.ProjectDataMapper;
@Service
public class ProjectDataService {

	@Resource ProjectDataMapper projectDataMapper;
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

    /*添加项目资料记录*/
    public void addProjectData(ProjectData projectData) throws Exception {
    	projectDataMapper.addProjectData(projectData);
    }

    /*按照查询条件分页查询项目资料记录*/
    public ArrayList<ProjectData> queryProjectData(Project projectObj,String dataname,String createuser,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_projectData.projectObj=" + projectObj.getProjectId();
    	if(!dataname.equals("")) where = where + " and t_projectData.dataname like '%" + dataname + "%'";
    	if(!createuser.equals("")) where = where + " and t_projectData.createuser like '%" + createuser + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return projectDataMapper.queryProjectData(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ProjectData> queryProjectData(Project projectObj,String dataname,String createuser) throws Exception  { 
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_projectData.projectObj=" + projectObj.getProjectId();
    	if(!dataname.equals("")) where = where + " and t_projectData.dataname like '%" + dataname + "%'";
    	if(!createuser.equals("")) where = where + " and t_projectData.createuser like '%" + createuser + "%'";
    	return projectDataMapper.queryProjectDataList(where);
    }

    /*查询所有项目资料记录*/
    public ArrayList<ProjectData> queryAllProjectData()  throws Exception {
        return projectDataMapper.queryProjectDataList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Project projectObj,String dataname,String createuser) throws Exception {
     	String where = "where 1=1";
    	if(null != projectObj && projectObj.getProjectId()!= null && projectObj.getProjectId()!= 0)  where += " and t_projectData.projectObj=" + projectObj.getProjectId();
    	if(!dataname.equals("")) where = where + " and t_projectData.dataname like '%" + dataname + "%'";
    	if(!createuser.equals("")) where = where + " and t_projectData.createuser like '%" + createuser + "%'";
        recordNumber = projectDataMapper.queryProjectDataCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取项目资料记录*/
    public ProjectData getProjectData(int dataId) throws Exception  {
        ProjectData projectData = projectDataMapper.getProjectData(dataId);
        return projectData;
    }

    /*更新项目资料记录*/
    public void updateProjectData(ProjectData projectData) throws Exception {
        projectDataMapper.updateProjectData(projectData);
    }

    /*删除一条项目资料记录*/
    public void deleteProjectData (int dataId) throws Exception {
        projectDataMapper.deleteProjectData(dataId);
    }

    /*删除多条项目资料信息*/
    public int deleteProjectDatas (String dataIds) throws Exception {
    	String _dataIds[] = dataIds.split(",");
    	for(String _dataId: _dataIds) {
    		projectDataMapper.deleteProjectData(Integer.parseInt(_dataId));
    	}
    	return _dataIds.length;
    }
}
