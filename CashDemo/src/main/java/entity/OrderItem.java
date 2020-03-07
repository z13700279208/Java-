package entity;

import lombok.Data;

/**
 * @ClassName OrderItem
 * @Description TODO
 * @Author 付小雷
 * @Date 2020/2/1315:55
 * @Version1.0
 **/
@Data
public class OrderItem {
    private Integer id;
    private String order_id;
    private Integer goods_id;
    private String goods_name;
    private String goods_introduce;
    private Integer goods_num;
    private String goods_unit;
    private Integer goods_price;
    private Integer goods_discount;

    public double getGoods_Price(){
        return goods_price*1.0/100;
    }
    public int getIntoods_Price(){
        return goods_price;
    }
}
