package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ProAward;

public interface ProAwardMapper {
	/*添加项目获奖信息*/
	public void addProAward(ProAward proAward) throws Exception;

	/*按照查询条件分页查询项目获奖记录*/
	public ArrayList<ProAward> queryProAward(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有项目获奖记录*/
	public ArrayList<ProAward> queryProAwardList(@Param("where") String where) throws Exception;

	/*按照查询条件的项目获奖记录数*/
	public int queryProAwardCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条项目获奖记录*/
	public ProAward getProAward(int awardId) throws Exception;

	/*更新项目获奖记录*/
	public void updateProAward(ProAward proAward) throws Exception;

	/*删除项目获奖记录*/
	public void deleteProAward(int awardId) throws Exception;

}
