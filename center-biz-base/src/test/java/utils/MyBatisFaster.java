package utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.*;
import java.util.*;

/**
 * MyBatis相关结构快速生成器 Example:
 * <p>
 * <pre class="code">
 * <p>
 * public static void main( String[] args ) throws Exception {
 * MyBatisFaster faster = new MyBatisFaster ();
 * faster.setupConnection ( &quot;192.168.1.194:3306&quot;, &quot;coh_platform&quot;, &quot;dev&quot;, &quot;admin123&quot;, MyBatisFaster.MYSQL );
 * faster.tableName ( &quot;bu_department&quot; ).DAOInterfacePackage ( &quot;com.cohcapital.utils.dao&quot; ).DAOName ( &quot;IBuDepartmentDao&quot; ).domainName ( &quot;BuDepartment&quot; )
 * .domainPackage ( &quot;com.cohcapital.utils.domain&quot; );
 * <p>
 * faster.outSQLMapper ( System.getProperty ( &quot;user.dir&quot; ) + &quot;\\src\\main\\resources\\gala-sqlmap&quot; );
 * faster.outDAOInterface ( System.getProperty ( &quot;user.dir&quot; ) );
 * faster.outDomain ( System.getProperty ( &quot;user.dir&quot; ) );
 * }
 * </pre>
 *
 * @author John zhang
 * @version 1.0
 */
public class MyBatisFaster {

    public static final int SQLSERVER = 1;
    public static final int MYSQL = 2;
    public static final int ORACLE = 3;

    private String DAOInterfacePackage; // 接口包名
    private String domainPackage; // 域模型包名
    private String tableName; // 表名
    private String domainName; // 域模型名称
    private String DAOName; // DAO名称

    private String tableColumnDelimiter = "_"; // 数据表列分隔符

    private Connection mConn;

    /**
     * ftl模板生成配置
     */
    private Configuration freemarkerConf = new Configuration();

    /**
     * ftl模版默认编译编码方式
     */
    private static final String DEFAULT_TEMPLATE_ENCODING = "utf-8";

    public MyBatisFaster() throws IOException {
        freemarkerConf.setDirectoryForTemplateLoading( new File( getClass().getProtectionDomain().getCodeSource().getLocation().getPath() ) );
    }

    /**
     * 设置默认数据库连接
     *
     * @param ipport   ip+port ,eg.: 192.168.1.194:3306
     * @param dbName   databaseName ,eg. : ETForMonitor_V2
     * @param username eg.:sa
     * @param password eg. :password
     * @param type     1-SQLServer 2-MySQL 3-Oracle
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void setupConnection( String ipport, String dbName, String username, String password, int type ) throws ClassNotFoundException, SQLException {
        mConn = getConnection( ipport, dbName, username, password, type );
    }

    /**
     * 通过jdbc获取相应的数据库链接connection
     *
     * @param ipport   ip+port ,eg.: 192.168.1.161:1997
     * @param dbName   databaseName ,eg. : ETForMonitor_V2
     * @param username eg.:sa
     * @param password eg. :password
     * @param type     请看本类的静态变量
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection getConnection( String ipport, String dbName, String username, String password, int type ) throws ClassNotFoundException, SQLException {
        String jdbcString = null;
        if ( type == SQLSERVER ) {
            jdbcString = "jdbc:jtds:sqlserver://" + ipport + ";databaseName=" + dbName;
            Class.forName( "net.sourceforge.jtds.jdbc.Driver" );
        } else if ( type == MYSQL ) {
            jdbcString = "jdbc:mysql://" + ipport + "/" + dbName + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&nullCatalogMeansCurrent=true";
            Class.forName( "com.mysql.cj.jdbc.Driver" );
        } else if ( type == ORACLE ) {
            jdbcString = "jdbc:oracle:thin:@" + ipport;
            Class.forName( "oracle.jdbc.OracleDriver" );
        }

        Connection connection = null;
        connection = DriverManager.getConnection( jdbcString, username, password );
        return connection;
    }

    /**
     * 生成Domain模型
     *
     * @param rootPath 项目的跟路径
     * @throws Exception
     */
    public void outDomain( String rootPath, int type ) throws Exception {
        Assert.notNull( mConn, "请先调用setupConnect()方法对数据连接进行创建" );
        Assert.notNull( tableName, "表名不能为空" );
        Assert.notNull( domainName, "domain名称不能为空" );
        Assert.notNull( domainPackage, "domain包名不能为空" );

        String tableName = this.tableName;

        ResultSet rs = null;
        PreparedStatement ps = null;
        if ( type == SQLSERVER ) {
            ps = mConn.prepareStatement( "select top 1 * from " + tableName );
        } else if ( type == MYSQL ) {
            ps = mConn.prepareStatement( "select * from " + tableName + " limit 1" );
        } else if ( type == ORACLE ) {
            ps = mConn.prepareStatement( "select * from " + tableName + " where rownum <= 1" );
        } else {
            Assert.isTrue( false, "SQLSERVER 1, MYSQL 2, ORACLE 3" );
            return;
        }
        rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        //rs.close();
        //ps.close();

        // 数据表列名与域模型字段名映射关系
        Map<String, String> fieldDataTypes = new HashMap<String, String>();
        List<String> fields = new ArrayList<String>();
        for ( int i = 1; i <= rsmd.getColumnCount(); i++ ) {
            String columnName = rsmd.getColumnName( i );
            String fieldName = buildClassFieldName( columnName );
            fields.add( fieldName );
            System.out.println( rsmd.getColumnTypeName( i ) );
            fieldDataTypes.put( fieldName, DataBaseType.getPojoType( rsmd.getColumnTypeName( i ) ) );
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "domainName", domainName );
        params.put( "domainPackage", domainPackage );
        params.put( "fields", fields );
        params.put( "fieldDataTypes", fieldDataTypes );

        Template template = freemarkerConf.getTemplate( "domain-template.ftl", DEFAULT_TEMPLATE_ENCODING );
        Writer out = new StringWriter( 2048 );
        template.process( params, out );

        File dir = new File( rootPath + "\\src\\main\\java" + package2FilePath( this.domainPackage ) );
        if ( !dir.exists() ) {
            dir.mkdirs();
        }

        FileUtils.write2File( dir.getPath() + File.separator + this.domainName + ".java", out.toString() );
    }

    /**
     * 生成SQL-Mapper.xml文件
     *
     * @param path sql-mapper.xml的目录
     * @throws Exception
     */
    public void outSQLMapper( String path, int type ) throws Exception {
        afterPropertySet();

        String tableName = this.tableName;

        ResultSet rs = null;
        PreparedStatement ps = null;
        if ( type == SQLSERVER ) {
            ps = mConn.prepareStatement( "select top 1 * from " + tableName );
        } else if ( type == MYSQL ) {
            ps = mConn.prepareStatement( "select * from " + tableName + " limit 1" );
        } else if ( type == ORACLE ) {
            ps = mConn.prepareStatement( "select * from " + tableName + " where rownum <= 1" );
        } else {
            Assert.isTrue( false, "SQLSERVER 1, MYSQL 2, ORACLE 3" );
            return;
        }
        rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        //rs.close();
        //ps.close();

        // 数据表列名与域模型字段名映射关系

        String primaryKey = getPrimaryKey( mConn, tableName );
        String primaryKeyMapping = "";

        Map<String, String> mappings = new HashMap<String, String>();
        List<String> ordinaryColumns = new ArrayList<String>();
        for ( int i = 1; i <= rsmd.getColumnCount(); i++ ) {
            String columnName = rsmd.getColumnName( i );
            if ( !columnName.equals( primaryKey ) ) {
                ordinaryColumns.add( columnName );
                mappings.put( columnName, buildClassFieldName( columnName ) );
            } else {
                primaryKeyMapping = buildClassFieldName( columnName );
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "DAOInterfacePackage", DAOInterfacePackage );
        params.put( "DAOName", DAOName );
        params.put( "resultMapId", buildClassFieldName( tableName ) );
        params.put( "domainName", domainName );
        params.put( "domainPackage", domainPackage );
        params.put( "tableName", tableName );
        params.put( "primaryKey", primaryKey );
        params.put( "primaryKeyMapping", primaryKeyMapping );
        params.put( "ordinaryColumns", ordinaryColumns );
        params.put( "mappings", mappings );

        Template template = freemarkerConf.getTemplate( "sql-mapper.ftl", DEFAULT_TEMPLATE_ENCODING );
        Writer out = new StringWriter( 2048 );
        template.process( params, out );

        String sqlMapName = tableName + "-mapper.xml";

        File dir = new File( path );
        if ( !dir.exists() ) {
            dir.mkdirs();
        }

        FileUtils.write2File( path + File.separator + sqlMapName, out.toString() );
    }

    /**
     * 自动生成DAO接口文件
     *
     * @param rootPath 项目根路径
     * @throws Exception
     */
    public void outDAOInterface( String rootPath, int type ) throws Exception {
        afterPropertySet();

        String tableName = this.tableName;

        ResultSet rs = null;
        PreparedStatement ps = null;
        if ( type == SQLSERVER ) {
            ps = mConn.prepareStatement( "select top 1 * from " + tableName );
        } else if ( type == MYSQL ) {
            ps = mConn.prepareStatement( "select * from " + tableName + " limit 1" );
        } else if ( type == ORACLE ) {
            ps = mConn.prepareStatement( "select * from " + tableName + " where rownum <= 1" );
        } else {
            Assert.isTrue( false, "SQLSERVER 1, MYSQL 2, ORACLE 3" );
            return;
        }
        rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        //rs.close();
        //ps.close();

        // 数据表列名与域模型字段名映射关系
        Map<String, String> mappings = new LinkedHashMap<>();
        for ( int i = 1; i <= rsmd.getColumnCount(); i++ ) {
            String columnName = rsmd.getColumnName( i );
            mappings.put( columnName, buildClassFieldName( columnName ) );
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "DAOInterfacePackage", DAOInterfacePackage );
        params.put( "DAOName", DAOName );
        params.put( "resultMapId", buildClassFieldName( tableName ) );
        params.put( "domainName", domainName );
        params.put( "domainPackage", domainPackage );
        params.put( "tableName", tableName );
        params.put( "primaryKey", getPrimaryKey( mConn, tableName ) );
        params.put( "mappings", mappings );

        Template template = freemarkerConf.getTemplate( "DAO-template.ftl", DEFAULT_TEMPLATE_ENCODING );
        Writer out = new StringWriter( 2048 );
        template.process( params, out );

        File dir = new File( rootPath + "\\src\\main\\java" + package2FilePath( this.DAOInterfacePackage ) );
        if ( !dir.exists() ) {
            dir.mkdirs();
        }

        FileUtils.write2File( dir.getPath() + File.separator + this.DAOName + ".java", out.toString() );
    }

    /**
     * 将数据库字段转换为类字段名格式（驼峰式）
     *
     * @param columnName 数据库列名
     * @return
     */
    private String buildClassFieldName( String columnName ) {
        if ( ValidatorUtils.isEmpty( columnName ) ) {
            throw new IllegalArgumentException( "columnName不能为空" );
        }
        String fieldName = "";
        String[] columnWordGroup = columnName.split( tableColumnDelimiter );
        for ( int i = 0, len = columnWordGroup.length; i < len; i++ ) {
            String word = columnWordGroup[i].toLowerCase();
            if ( i == 0 ) {
                fieldName += word;
            } else if ( word.length() > 0 ) {
                fieldName += ( word.charAt( 0 ) > 96 && word.charAt( 0 ) < 123 ) ? ( word.substring( 0, 1 ).toUpperCase() + ( word.length() > 1 ? word.substring( 1 ) : "" ) ) : word;
            }
        }
        return fieldName;
    }

    /**
     * 获取表的主键
     *
     * @param connection
     * @param table
     * @return
     * @throws SQLException
     */
    private String getPrimaryKey( Connection connection, String table ) throws SQLException {
        String keyName = null;
        DatabaseMetaData dbmd = connection.getMetaData();
        ResultSet rs = dbmd.getPrimaryKeys( null, null, table );
        while ( rs.next() )
            keyName = rs.getString( 4 );
        rs.close();
        return keyName;
    }

    private void afterPropertySet() {
        Assert.notNull( mConn, "请先调用setupConnect()方法对数据连接进行创建" );
        Assert.notNull( tableName, "表名不能为空" );
        Assert.notNull( DAOInterfacePackage, "DAO报名不能为空" );
        Assert.notNull( DAOName, "DAO名称不能为空" );
        Assert.notNull( domainName, "domain名称不能为空" );
        Assert.notNull( domainPackage, "domain包名不能为空" );
    }

    private String package2FilePath( String packageName ) {
        if ( packageName.indexOf( "." ) != -1 ) {
            String[] names = packageName.split( "\\." );
            String filepath = "";
            for ( String name : names ) {
                filepath += File.separator + name;
            }
            return filepath;
        }
        return packageName;
    }

    public MyBatisFaster colunmDelimiter( String tableColumnDelimiter ) {
        this.tableColumnDelimiter = tableColumnDelimiter;
        return this;
    }

    public MyBatisFaster DAOInterfacePackage( String DAOInterfacePackage ) {
        this.DAOInterfacePackage = DAOInterfacePackage;
        return this;
    }

    public MyBatisFaster domainPackage( String domainPackage ) {
        this.domainPackage = domainPackage;
        return this;
    }

    public MyBatisFaster tableName( String tableName ) {
        this.tableName = tableName;
        return this;
    }

    public MyBatisFaster domainName( String domainName ) {
        this.domainName = domainName;
        return this;
    }

    public MyBatisFaster DAOName( String DAOName ) {
        this.DAOName = DAOName;
        return this;
    }

}
