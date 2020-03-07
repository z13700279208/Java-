package util;

import  org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库工具类
 */

public class DBUtil {

    private static volatile DataSource DATA_SOURCE;
    private DBUtil(){

    }

    /**
     * 获取SQLLite数据库本地文件路径（target目录下）
     * @return
     * @throws URISyntaxException
     */
    private static String getUrl() throws URISyntaxException {
        String dbName = "哮天犬.db";
        URL url = DBUtil.class.getClassLoader().getResource(".");
        return "jdbc:sqlite://" + new File(url.toURI()).getParent() + File.separator + dbName;
    }

    //获取数据库连接池
    private static DataSource getDataSource() throws URISyntaxException {
        if(DATA_SOURCE == null){
            synchronized (DBUtil.class){
                if(DATA_SOURCE == null){
                    //sqlite日期格式 : yyyy-mm-dd HH:mm:ss:SSS
                    //mysql日期格式 ： yyyy-MM-dd HH:mm:ss
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATE_PATTERN);
                    DATA_SOURCE  = new SQLiteDataSource(config);
                    ((SQLiteDataSource) DATA_SOURCE).setUrl(getUrl());
                }
            }
        }
        return  DATA_SOURCE;
    }
    /**
     * 获取数据库连接
     * 1.class.forName().DriverManager.getConnection()
     * 2.DataSourse
     */
    public static Connection getConnection(){
        try {
            return  getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接获取失败");
        }
    }

    /**
     * 释放数据库资源
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void close(Connection connection,Statement statement,ResultSet resultSet){
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw  new RuntimeException("释放数据库资源错误");
        }
    }

    public static void close(Connection connection,Statement statement){
        close(connection,statement,null);
    }
    public static void main(String[] args) throws URISyntaxException {
        Connection connection = getConnection();

    }
}
