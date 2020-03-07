package Servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Goods;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/browseGoods")
public class GoodsBrowseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Goods> list = new ArrayList<>();

        try {
            String sql = "select id, name, introduce, stock, unit, price, discount from goods";
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Goods goods = this.extractGoods(resultSet);
                if (goods != null) {
                    list.add(goods);
                }
            }
            System.out.println("browseGoods" + list);

            ObjectMapper mapper = new ObjectMapper();
            PrintWriter pw = resp.getWriter();
            mapper.writeValue(pw,list);
            Writer writer = resp.getWriter();
            writer.write(pw.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
    }
    private Goods extractGoods(ResultSet resultSet) throws SQLException {
        Goods goods = new Goods();
        goods.setId(resultSet.getInt("id"));
        goods.setName(resultSet.getString("name"));
        goods.setDiscount(resultSet.getInt("discount"));
        goods.setIntroduce(resultSet.getString("introduce"));
        goods.setStock(resultSet.getInt("stock"));
        goods.setPrice(resultSet.getInt("price"));
        goods.setUnit(resultSet.getString("unit"));
        return goods;
    }
}
