package edu.hhu.service.impl;

import edu.hhu.dao.IOrderSettingDao;
import edu.hhu.domain.OrderSetting;
import edu.hhu.service.IOrderSettingService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderSettingServiceImpl implements IOrderSettingService {
    @Autowired
    private IOrderSettingDao orderSettingDao;

    @Override
    public void addOrderSetting(List<OrderSetting> orderSettings) {
        if (orderSettings != null && orderSettings.size() > 0){
            for (OrderSetting orderSetting : orderSettings) {
                //根据日期来检查该天的数据是否有导入过
               long count =  orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
               if (count > 0){
                   //表示该天的日期之前已经设置过 所以仅更新该天的信息即可
                   orderSettingDao.updateOrderSetting(orderSetting);
               }else{
                   //向数据库表中插入该数据
                   orderSettingDao.addOrderSetting(orderSetting);
               }
            }
        }
    }

    @Override
    public List<Map> findOrderSettingsByMonth(String date) {//date 形式 'yyyy-MM'
        //对日期进行处理
        String begin = date + "-01";
        String end = date + "-31";  //由于任何一个月日期都不可能超过31天 对范围日期查找无需按月份复杂处理
        Map<String,String> map = new HashMap<>(); // 封装方法参数
        map.put("begin",begin);
        map.put("end",end);
        List<OrderSetting> orderSettings = orderSettingDao.findOrderSettingsByMonth(map);
        //对orderSettings中的数据进行相应修改封装到map中去
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettings) {
            Map orderSettingsMap = new HashMap();
            orderSettingsMap.put("date",orderSetting.getOrderDate().getDate());
            orderSettingsMap.put("number",orderSetting.getNumber());
            orderSettingsMap.put("reservations",orderSetting.getReservations());
            data.add(orderSettingsMap);
        }
        return data;
    }

    /**
     * 根据日期设置预约人数
     * 查询该日期是否设置过预约人数
     * if 设置过 进行更新操作 ，没有进行插入操作
     */
    @Override
    public void updateNumberByOrderDate(OrderSetting orderSetting) {
        //根据日期来检查该天的数据是否有导入过
        long count =  orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0){
            //表示该天的日期之前已经设置过 所以仅更新该天的信息即可
            orderSettingDao.updateOrderSetting(orderSetting);
        }else{
            //向数据库表中插入该数据
            orderSettingDao.addOrderSetting(orderSetting);
        }
    }
}
