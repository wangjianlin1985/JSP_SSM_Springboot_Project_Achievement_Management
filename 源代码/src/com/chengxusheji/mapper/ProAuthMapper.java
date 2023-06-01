package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ProAuth;

public interface ProAuthMapper {
	/*添加项目人员信息*/
	public void addProAuth(ProAuth proAuth) throws Exception;

	/*按照查询条件分页查询项目人员记录*/
	public ArrayList<ProAuth> queryProAuth(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有项目人员记录*/
	public ArrayList<ProAuth> queryProAuthList(@Param("where") String where) throws Exception;

	/*按照查询条件的项目人员记录数*/
	public int queryProAuthCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条项目人员记录*/
	public ProAuth getProAuth(int authId) throws Exception;

	/*更新项目人员记录*/
	public void updateProAuth(ProAuth proAuth) throws Exception;

	/*删除项目人员记录*/
	public void deleteProAuth(int authId) throws Exception;

}
