package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ProjectData;

public interface ProjectDataMapper {
	/*添加项目资料信息*/
	public void addProjectData(ProjectData projectData) throws Exception;

	/*按照查询条件分页查询项目资料记录*/
	public ArrayList<ProjectData> queryProjectData(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有项目资料记录*/
	public ArrayList<ProjectData> queryProjectDataList(@Param("where") String where) throws Exception;

	/*按照查询条件的项目资料记录数*/
	public int queryProjectDataCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条项目资料记录*/
	public ProjectData getProjectData(int dataId) throws Exception;

	/*更新项目资料记录*/
	public void updateProjectData(ProjectData projectData) throws Exception;

	/*删除项目资料记录*/
	public void deleteProjectData(int dataId) throws Exception;

}
