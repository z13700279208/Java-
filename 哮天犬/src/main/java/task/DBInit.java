package task;
import util.DBUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

public class DBInit {
    public static void init(){
        InputStream is = DBInit.class.getClassLoader().getResourceAsStream("init.sql");
        try {
            //获取数据库初始化文件的输入流
            BufferedReader in =new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = in.readLine()) != null){
                //忽略注释代码 --
                int idx = line.indexOf("--");
                if(idx != -1){
                    line = line.substring(0,idx);
                }
                sb.append(line);
            }
            String[] sqls = sb.toString().split(";");
            //System.out.println(Arrays.toString(sqls));
            Connection connection = null;
            Statement statement = null;
            try {
                for (String sql : sqls) {
                    connection = DBUtil.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate(sql);
                }
            }finally {
                DBUtil.close(connection,statement);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库初始化任务错误");
        }
    }

    public static void main(String[] args) {
        init();
    }
}
