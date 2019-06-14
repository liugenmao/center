package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 
 * @author onedear
 * @data:2010-11-15 下午07:54:23
 */
public class DataBaseType {

	private static Map<String, String> map;

	static {
		map = new HashMap<String, String> ();
		map.put ( "varchar", "String" );
		map.put ( "int", "Integer" );
		map.put ( "integer", "Integer" );
		map.put ( "datetime", "Date" );
		map.put ( "date", "Date" );
		map.put ( "nvarchar", "String" );
		map.put ( "char", "String" );
		map.put ( "uniqueidentifier", "String" );
		map.put ( "bigint", "Long" );
		map.put ( "tinyint", "Boolean" );
		map.put ( "float", "Float" );
		map.put ( "decimal", "BigDecimal" );
		map.put ( "smallint", "Integer" );
		map.put ( "varchar2", "String" );
		map.put ( "number", "Integer" );
	}

	public static String getPojoType( String dataType ) {
		String tmp = dataType.toLowerCase ();
		StringTokenizer st = new StringTokenizer ( tmp );
		return map.get ( st.nextToken () );
	}
}