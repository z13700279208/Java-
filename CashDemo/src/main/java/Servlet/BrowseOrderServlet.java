package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.OrderStatus;
import entity.Account;
import entity.Order;
import entity.OrderItem;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/browseOrder")
public class BrowseOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        System.out.println("browseOrder");
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("user");
        Integer accountId = account.getId();
        List<Order> orderList = queryOrder(accountId);


        if(orderList==null){
            System.out.println("当前用户暂无订单");
        }else{
            System.out.println("订单" + orderList);
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter pw = resp.getWriter();
            mapper.writeValue(pw,orderList);
            Writer writer = resp.getWriter();
            writer.write(pw.toString());
        }
    }

    private List<Order> queryOrder(Integer accountId) {
        List<Order> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = this.getSql("query_order_by_account.sql");
            connection = DBUtil.getConnection(true);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,accountId);
            resultSet = preparedStatement.executeQuery();
            Order order = null;
            while(resultSet.next()){
                if(order==null){
                    order = new Order();
                    this.extractOrder(order,resultSet);
                    list.add(order);
                }
                String orderId = resultSet.getString("order_id");
                if(!order.getId().equals(orderId)){
                    order = new Order();
                    this.extractOrder(order,resultSet);
                    list.add(order);
                }
                OrderItem orderItem =  this.extractOrderItem(resultSet);
                order.orderItemList.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return list;
    }

    private String getSql(String s) {

        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream("script/"+s);

        if(is==null){
            throw new RuntimeException("加载SQL语句失败");
        }else{
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader bufferedReader = new BufferedReader(isr);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(bufferedReader.readLine());
                String line ;
                while((line = bufferedReader.readLine())!=null){
                    sb.append(" ").append(line);
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("转化SQL语句发生异常");
            }
        }
    }

    private Order extractOrder(Order order , ResultSet resultSet) throws SQLException {

        order.setOrder_status(OrderStatus.valueOf(resultSet.getInt("order_status")));
        order.setFinish_time(resultSet.getString("finish_time"));
        order.setActual_amount(resultSet.getInt("actual_amount"));
        order.setTotal_money(resultSet.getInt("total_money"));
        order.setCreate_time(resultSet.getString("create_time"));
        order.setAccount_name(resultSet.getString("account_name"));
        order.setAccount_id(resultSet.getInt("account_id"));
        order.setId(resultSet.getString("order_id"));

        return order;
    }
    private OrderItem extractOrderItem(ResultSet resultSet) throws SQLException {

        OrderItem orderItem = new OrderItem();
        orderItem.setGoods_discount(resultSet.getInt("goods_discount"));
        orderItem.setGoods_unit(resultSet.getString("goods_unit"));
        orderItem.setGoods_price(resultSet.getInt("goods_price"));
        orderItem.setGoods_num(resultSet.getInt("goods_num"));
        orderItem.setGoods_introduce(resultSet.getString("goods_introduce"));
        orderItem.setGoods_name(resultSet.getString("goods_name"));
        orderItem.setGoods_id(resultSet.getInt("goods_id"));
        orderItem.setId(resultSet.getInt("item_id"));
        return orderItem;
    }
}
