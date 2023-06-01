package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.DataAuth;

public interface DataAuthMapper {
	/*添加资料权限信息*/
	public void addDataAuth(DataAuth dataAuth) throws Exception;

	/*按照查询条件分页查询资料权限记录*/
	public ArrayList<DataAuth> queryDataAuth(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有资料权限记录*/
	public ArrayList<DataAuth> queryDataAuthList(@Param("where") String where) throws Exception;

	/*按照查询条件的资料权限记录数*/
	public int queryDataAuthCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条资料权限记录*/
	public DataAuth getDataAuth(int authId) throws Exception;

	/*更新资料权限记录*/
	public void updateDataAuth(DataAuth dataAuth) throws Exception;

	/*删除资料权限记录*/
	public void deleteDataAuth(int authId) throws Exception;

}
