package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ProjectType;

public interface ProjectTypeMapper {
	/*添加项目类别信息*/
	public void addProjectType(ProjectType projectType) throws Exception;

	/*按照查询条件分页查询项目类别记录*/
	public ArrayList<ProjectType> queryProjectType(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有项目类别记录*/
	public ArrayList<ProjectType> queryProjectTypeList(@Param("where") String where) throws Exception;

	/*按照查询条件的项目类别记录数*/
	public int queryProjectTypeCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条项目类别记录*/
	public ProjectType getProjectType(int typeId) throws Exception;

	/*更新项目类别记录*/
	public void updateProjectType(ProjectType projectType) throws Exception;

	/*删除项目类别记录*/
	public void deleteProjectType(int typeId) throws Exception;

}
