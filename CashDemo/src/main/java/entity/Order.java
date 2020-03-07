package entity;

import common.OrderStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private String id;
    private Integer account_id;
    private String account_name;
    private String create_time;
    private String finish_time;
    private Integer actual_amount;
    private Integer total_money;
    private OrderStatus order_status;
    public List<OrderItem> orderItemList = new ArrayList<>();

    public double getTotal_money(){
        return total_money*1.0/100;
    }
    public int getTotal_moneyInt(){
        return total_money;
    }

    public double getActual_amount(){
        return actual_amount*1.0/100;
    }

    public int getActual_amountInt(){
        return actual_amount;
    }
    public double getDiscount() {
        return (this.getTotal_moneyInt()-this.getActual_amountInt())*1.0/100;
    }
}
