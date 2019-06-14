package com.xiaoliu.centerbiz.dao;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import com.yuouhui.oc.assist.common.db.SQLOrder;
import com.yuouhui.oc.assist.common.model.Pageable;

import com.xiaoliu.centerbiz.domain.Module;

/**
 *
 *
 * @author
 * @version
 **/
public interface IModuleDao {
	
	/**
	 * 通过id获取XX信息
	 * @param id 主键
	 * @return
	 */
	@Select("select * from module where id = #{id}")
	@Options(useCache = false)
	@ResultMap("module")
	Module getModuleById( Long id );
	
	/**
	 * 添加一个XX
	 * @param entity XXX
	 * @return
	 */
	int addModule( Module entity );
	
	/**
	 * 删除多个XX记录
	 * @param ids 主键集合
	 * @return
	 */
	int deleteModules( @Param("ids") Long... ids );

	/**
	 * 修改XX
	 * @param params - 请在此处添加上可提供的查询参数
	 * @return
	 */
	int updateModule( @Param("params") Map<String, Object> params );
	
	/**
	 * 列表XX
	 * @param pageable 分页参数
	 * @param params 查询参数 - 请在此处添加上可提供的查询参数
	 * @param orders 排序参数 - 请在此处添加上可提供的排序参数
	 * @return
	 */
	List<Module> listModules( @Param("pageable") Pageable pageable, @Param("params") Map<String, Object> params, @Param("orders") LinkedHashMap<String, SQLOrder> orders );
}