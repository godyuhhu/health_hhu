package edu.hhu.dao;

import edu.hhu.domain.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置的表示层接口
 */
public interface IOrderSettingDao {


    //根据日期判断该天的数据有没有被设置过
    long findCountByOrderDate(Date orderDate);

    //更新预约设置
    void updateOrderSetting(OrderSetting orderSetting);

    //新增预约设置
    void addOrderSetting(OrderSetting orderSetting);

    //根据日期查询相应的预约条件
    List<OrderSetting> findOrderSettingsByMonth(Map<String, String> map);

    //根据日期查询当天的预约信息
    OrderSetting findOrderSettingByOrderDate(Date orderDate);

    //当预约成功后更新当天的可预约人数
    void updateReservations(OrderSetting orderSetting);
}
