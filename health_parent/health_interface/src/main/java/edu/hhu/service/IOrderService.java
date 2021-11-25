package edu.hhu.service;

import edu.hhu.entity.Result;

import java.util.List;
import java.util.Map;

/**
 * 订单处理的业务层接口
 */
public interface IOrderService {
    //处理提交过来的订单
     Result processOrder(Map map);

     //根据订单id查询订单信息
    Map findOrderById(Integer id) throws Exception;

    //查询套餐统计数据
    List<Map<String, Object>> findSetMealCount();
}
