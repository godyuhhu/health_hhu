package edu.hhu.dao;

import edu.hhu.domain.Order;

import java.util.List;
import java.util.Map;

/**
 * 订单信息的持久层接口
 */
public interface IOrderDao {
    //根据条件查询订单信息
    List<Order> findOrdersByCondition(Order order);

    //插入订单消息
    void addOrder(Order order);

    Map findOrderById(Integer id);

    //统计订单数据
    List<Map<String, Object>> findSetMealCount();

    Integer findTodayOrderNumber(String today);

    Integer findTodayVisitsNumber(String today);

    Integer findThisWeekOrderNumber(String thisWeekMonday);

    Integer findThisMonthOrderNumber(String firstDayOfMonth);

    Integer findVThisWeekVisitsNumber(String thisWeekMonday);

    Integer findThisMonthVisitsNumber(String firstDayOfMonth);

    List<Map<String, Object>> findHotSetMeal();
}
