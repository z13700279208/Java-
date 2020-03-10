package util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {
    private static volatile DataSource DATASOURCE;

    private DBUtil(){
    }

    private static final String USERNAME = "root";

    private static final String PASSWORD = "jingjing";

    private static final String URL = "jdbc:mysql://localhost:3306/cash";

    //获取数据库连接池
    private static DataSource getDatasource(){
        if(DATASOURCE == null){
            synchronized (DBUtil.class){
                if(DATASOURCE == null){
                    DATASOURCE = new MysqlDataSource();
                    ((MysqlDataSource)DATASOURCE).setUrl(URL);
                    ((MysqlDataSource)DATASOURCE).setUser(USERNAME);
                    ((MysqlDataSource)DATASOURCE).setPassword(PASSWORD);
                }
            }
        }
        return DATASOURCE;
    }

    //项目当中可能会出现事务的回滚
    public static Connection getConnection(boolean autoCommit) {
        try{
            Connection connection = getDatasource().getConnection();
            connection.setAutoCommit(autoCommit);
            return connection;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("获取数据库连接失败");
        }

    }

    public static void close(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet){

        try {
            if(resultSet != null){
                resultSet.close();
            }
            if(preparedStatement != null){
                preparedStatement.close();
            }
            if(connection != null){
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("数据库释放资源错误");
        }
    }

    public static void main(String[] args) {
        DBUtil.getConnection(true);
    }

}
