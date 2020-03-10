package common;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum OrderStatus {
    PLAYING(1,"待支付"),OK(2,"支付完成");
    int flg;
    String desc;
    OrderStatus(int flg,String desc){
        this.flg=flg;
        this.desc=desc;
    }
    public static OrderStatus valueOf(int flg){
        for (OrderStatus orderStatus:OrderStatus.values()) {
            if(orderStatus.flg==flg){
                return orderStatus;
            }
        }
        throw new RuntimeException("OrderStatus is not fount");
    }
}
