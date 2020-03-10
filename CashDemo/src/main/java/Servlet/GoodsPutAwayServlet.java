package Servlet;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/inbound")
public class GoodsPutAwayServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html : charset=UTF-8");

        String name = req.getParameter("name");
        String introduce = req.getParameter("introduce");
        String stock = req.getParameter("stock");
        String unit = req.getParameter("unit");

        String price = req.getParameter("price");
        Double doublePrice = Double.valueOf(price) * 100;
        int IntPrice = doublePrice.intValue() ;

        String discount = req.getParameter("discount");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            String sql = "insert into goods(name,introduce,stock,unit,price,discount)" +
                    " values (?,?,?,?,?,?)";

            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,name);
            preparedStatement.setString(2,introduce);
            preparedStatement.setInt(3,Integer.parseInt(stock));
            preparedStatement.setString(4,unit);
            preparedStatement.setInt(5,IntPrice);
            preparedStatement.setInt(6,Integer.parseInt(discount));

            int ret = preparedStatement.executeUpdate();

            if(ret == 1){
                resp.sendRedirect("index.html");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,null);
        }
    }
    

}
