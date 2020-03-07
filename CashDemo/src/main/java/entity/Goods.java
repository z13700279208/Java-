package entity;

import lombok.Data;

/**
 * @ClassName Goods
 * @Description TODO
 * @Author 付小雷
 * @Date 2020/2/129:07
 * @Version1.0
 **/
@Data
public class Goods {
    private Integer id;
    private String name ;
    private String introduce;
    private Integer stock;
    private String unit;
    private Integer price;
    private Integer discount;
    private Integer buyGoodsNum;
    public double getPrice(){
        return price*1.0/100;
    }
    public int getPriceInt(){
        return price;
    }
}
