package edu.hhu.service;

import edu.hhu.domain.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置的业务层接口
 */
public interface IOrderSettingService {
    //导入预约信息
    void addOrderSetting(List<OrderSetting> orderSettings);

    //在日历控件上查找对应的预约信息
    List<Map> findOrderSettingsByMonth(String date);

    //在日历控件上修改相应的预约信息
    void updateNumberByOrderDate(OrderSetting orderSetting);
}
