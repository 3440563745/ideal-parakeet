package com.sky.Task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
//@Scheduled(cron = "1/5 * * * * ?")
public void processTimeoutOrder(){
     log.info("处理支付超时订单:{}", LocalDateTime.now());
     LocalDateTime time=LocalDateTime.now().plusMinutes(-15);
        List<Orders> orderList=orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);
        if(orderList!=null&& !orderList.isEmpty()){
            for(Orders orders:orderList){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
    @Scheduled(cron = "0 0 1 * * ?")
//@Scheduled(cron = "0/5 * * * * ?")
    public  void processDeliveryOrder(){
        log.info("处理处于待派单状态的订单:{}",LocalDateTime.now());
        LocalDateTime time=LocalDateTime.now().plusMinutes(-60);
        List<Orders> orderList=orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,time);
        if(orderList!=null&& !orderList.isEmpty()){
            for(Orders orders:orderList){
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
