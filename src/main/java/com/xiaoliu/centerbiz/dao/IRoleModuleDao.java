package com.xiaoliu.centerbiz.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import com.xiaoliu.centerbiz.common.db.SQLOrder;
import com.xiaoliu.centerbiz.common.model.Pageable;

import com.xiaoliu.centerbiz.domain.RoleModule;

import java.util.LinkedHashMap;

/**
 *
 *
 * @author
 * @date
 **/
public interface IRoleModuleDao {
	
	/**
	 * 获取详情
	 *
	 * @param id 主键
	 * @return 详情
	 */
	@Select("select * from role_module where id = #{id}")
	@Options(useCache = false)
	@ResultMap("roleModule")
	RoleModule getRoleModuleById( Long id );
	
	/**
	 * 添加
	 *
	 * @param entity
	 * @return 成功条数
	 */
	int addRoleModule( RoleModule entity );
	
	/**
	 * 删除多个
	 *
	 * @param ids 主键集合
	 * @return 成功条数
	 */
	int deleteRoleModules( @Param("ids") Long... ids );

	/**
	 * 修改
	 *
	 * @param params 修改参数
	 * @return 成功条数
	 */
	int updateRoleModule( @Param("params") Map<String, Object> params );
	
	/**
	 * 列表
	 *
	 * @param pageable 分页参数
	 * @param params   查询参数
	 * @param orders   排序参数
	 * @return 列表
	 */
	List<RoleModule> listRoleModules( @Param("pageable") Pageable pageable, @Param("params") Map<String, Object> params, @Param("orders") LinkedHashMap<String, SQLOrder> orders );
}