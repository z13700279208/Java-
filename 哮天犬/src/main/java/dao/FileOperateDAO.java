package dao;

import app.FileMeta;
import util.DBUtil;
import util.Pinyin4jUtil;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileOperateDAO {

    public static List<FileMeta> query(String dirpath) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<FileMeta> metas = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select name,path,size,last_modified,is_directory from file_meta " +
                    "where path = ?";
            statement = connection.prepareStatement(sql);

            statement.setString(1,dirpath);

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                long size = resultSet.getLong("size");
                long last_modified = resultSet.getLong("last_modified");
                boolean is_directory = resultSet.getBoolean("is_directory");

                FileMeta meta = new FileMeta(name,path,size,last_modified,is_directory);
                metas.add(meta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return metas;
    }


    public static void insert(FileMeta localMeta) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            try {
                //1.获取数据库连接
                connection = DBUtil.getConnection();
                String sql = "insert into file_meta (name,path,size,last_modified,pinyin," +
                        "pinyin_first,is_directory) values (?,?,?,?,?,?,?)";
                //2.获取操作命令对象
                statement = connection.prepareStatement(sql);
                //填充占位符
                statement.setString(1,localMeta.getName());
                statement.setString(2,localMeta.getPath());
                statement.setBoolean(3,localMeta.getDirectory());
                statement.setTimestamp(4,new Timestamp(localMeta.getLastModified()));

                String pinyin = null;
                String pinyin_first = null;



                //System.out.println("insert: "+localMeta.getName());

                //包含中文字符时需要保存全拼和拼音首字母
                if (Pinyin4jUtil.containsChinese(localMeta.getName())) {
                    String[] pinyins = Pinyin4jUtil.get(localMeta.getName());
                    pinyin = pinyins[0];
                    pinyin_first = pinyins[1];
                }
                statement.setString(5,pinyin);
                statement.setString(6,pinyin_first);
                statement.setBoolean(7,localMeta.getDirectory());

                //3.执行sql语句
                statement.executeUpdate();
            } finally {
                DBUtil.close(connection,statement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delect(FileMeta meta) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //1.获取数据库连接
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            String sql = "delete from file_meta where name = ? " +
                    "and path = ? and is_directory = ?";
            System.out.println(sql);
            //2.获取操作命令对象
            statement = connection.prepareStatement(sql);
            //填充占位符
            statement.setString(1,meta.getName());
            statement.setString(2,meta.getPath());
            statement.setBoolean(3,meta.getDirectory());

            //3.执行sql语句
            statement.executeUpdate();
            //删除子文件/子文件夹
            if(meta.getDirectory()) {
                sql = "delete from file_meta where path=? or path like ?";
                System.out.println(sql);
                statement = connection.prepareStatement(sql);
                String path = meta.getPath() + File.separator + meta.getName();
                statement.setString(1,path);

                statement.setString(2, path + File.separator +  "%");

                System.out.println("delext: " + meta.getPath() + File.separator + meta.getName());
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    //出现异常就回滚
                    connection.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            DBUtil.close(connection,statement);
        }
    }

    public static void main(String[] args) {
        delect(new FileMeta("16040226224王思颖","E:\\",4096L,
                1558494310748L,true));
    }


    public static List<FileMeta> search(String dir, String text) {
        List<FileMeta> metas = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            boolean empty = dir == null || dir.trim().length() == 0;
            String sql = "select name,path,size,last_modified,is_directory from file_meta " +
                    "where (name like ? or pinyin like ? or pinyin_first like ?) " +
                    (empty ? "" : "and (path = ? or path like ?)");
            statement = connection.prepareStatement(sql);

            statement.setString(1,"%" + text + "%");
            statement.setString(2,"%" + text + "%");
            statement.setString(3,"%" + text + "%");
            if (!empty) {
                statement.setString(4,dir);
                statement.setString(5,dir + File.separator + "%");

            }

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String path1 = resultSet.getString("path");
                long size = resultSet.getLong("size");
                long last_modified = resultSet.getLong("last_modified");
                boolean is_directory = resultSet.getBoolean("is_directory");
                FileMeta meta = new FileMeta(name,path1,size,last_modified,is_directory);
                metas.add(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return  metas;
    }
}
