package com.xiaoliu.centerbiz.dao;

import com.xiaoliu.centerbiz.common.db.SQLOrder;
import com.xiaoliu.centerbiz.common.model.Pageable;
import com.xiaoliu.centerbiz.domain.Module;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author
 * @date
 **/
public interface IModuleDao {
	
	/**
	 * 获取详情
	 *
	 * @param id 主键
	 * @return 详情
	 */
	@Select("select * from module where id = #{id}")
	@Options(useCache = false)
	@ResultMap("module")
	Module getModuleById(Long id);

	/**
	 * 添加
	 *
	 * @param entity
	 * @return 成功条数
	 */
	int addModule(Module entity);

	/**
	 * 删除多个
	 *
	 * @param ids 主键集合
	 * @return 成功条数
	 */
	int deleteModules(@Param("ids") Long... ids);

	/**
	 * 修改
	 *
	 * @param params 修改参数
	 * @return 成功条数
	 */
	int updateModule(@Param("params") Map<String, Object> params);

	/**
	 * 列表
	 *
	 * @param pageable 分页参数
	 * @param params   查询参数
	 * @param orders   排序参数
	 * @return 列表
	 */
	List<Module> listModules(@Param("pageable") Pageable pageable, @Param("params") Map<String, Object> params, @Param("orders") LinkedHashMap<String, SQLOrder> orders);
}