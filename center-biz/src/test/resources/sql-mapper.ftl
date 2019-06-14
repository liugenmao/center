<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${DAOInterfacePackage}.${DAOName}">
	<!-- 实体 -->
	<resultMap id="${resultMapId}" type="${domainPackage}.${domainName}">
		<#if primaryKey?exists>
		<id column="${primaryKey}" property="${primaryKeyMapping}" />
		</#if>
		<#list ordinaryColumns as columnName>
		<result column="${columnName}" property="${mappings[columnName]}" />
		</#list>
	</resultMap>
	
	<!-- 分页查询并排序 -->
	<select id="list${domainName}s" resultMap="${resultMapId}">
		SELECT * FROM ${tableName} a
		<where>
			<#list ordinaryColumns as columnName>
				<if test="params.containsKey('${mappings[columnName]}') and params.${mappings[columnName]} != null">AND ${columnName} = ${r'#{params.'}${mappings[columnName]}${r'}'}</if>
			</#list>
		</where>
		<if test="orders != null">
			order by
			<foreach item="value" separator="," index="key" collection="orders">
				a.${r'${key}'} ${r'${value}'}
			</foreach>
		</if>
	</select>
	
	<!-- 新增 -->
	<#if primaryKey?exists>
	<insert id="add${domainName}" useGeneratedKeys="true" keyProperty="${primaryKeyMapping}"> 
	<#else>
	<insert id="add${domainName}">
	</#if>
		INSERT INTO ${tableName} 
		(<#assign len = mappings?size, i = 0><#list ordinaryColumns as columnName><#assign i = i + 1>${columnName}<#if i != len>,</#if></#list>) 
		VALUES
		(<#assign len = mappings?size, i = 0><#list ordinaryColumns as columnName><#assign i = i + 1>${r'#{'}${mappings[columnName]}${r'}'}<#if i != len>,</#if></#list>)
	</insert>
	
	<!-- 修改 -->
	<update id="update${domainName}">
		UPDATE ${tableName}
		<set>
			<#list ordinaryColumns as columnName>
			<if test="params.containsKey('${mappings[columnName]}') and params.${mappings[columnName]} != null">${columnName} = ${r'#{params.'}${mappings[columnName]}${r'}'},</if>
			</#list>
		</set>
		<#if primaryKey?exists>
		where ${primaryKey} = ${r'#{params.'}${primaryKey}${r'}'}
		</#if>
	</update>
	
	<#if primaryKey?exists>
	<!-- 删除 -->
	<delete id="delete${domainName}s">
		DELETE FROM ${tableName}
		<where>
			${primaryKey} in
			<foreach collection="ids" item="id" separator="," open="(" close=")">
				${r'#{id}'}
			</foreach>
		</where>
	</delete>
	</#if>
</mapper>