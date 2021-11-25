package edu.hhu.controller;

import edu.hhu.constant.MessageConstant;
import edu.hhu.domain.OrderSetting;
import edu.hhu.entity.Result;
import edu.hhu.service.IOrderSettingService;
import edu.hhu.utils.POIUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置的表示层
 */
@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {
    @Reference
    private IOrderSettingService orderSettingService;

    /**
     * 向数据库中导入excel文件中的信息
     */
    @RequestMapping("/upload.do")
    public Result upload(MultipartFile excelFile) {
        //读取该文件
        try {
            //读取数据行中的每一行
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size() > 0){
                List<OrderSetting> orderSettings = new ArrayList<>();
                for (String[] strings : list) {
                    //strings中保存每一行的数据
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    //把这一行的数据添加到list集合中
                    orderSettings.add(orderSetting);
                }
                orderSettingService.addOrderSetting(orderSettings);
            }
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 向前端页面展示对应的预约信息
     */
    @RequestMapping("/findOrderSettingsByMonth")
    public Result findOrderSettingsByMonth(String date){ //参数类型是 yyyy-MM
        /**
         * 此处存储 map集合 而不存储orderList
         * leftObj json对应的字符串  { date: 1, number: 120, reservations: 1 } 与 orderSetting 类无法对应
         */
        try {
            List<Map> orderSettings = orderSettingService.findOrderSettingsByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,orderSettings);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    /**
     * 在页面上更改可预约人数
     */
    @RequestMapping("/updateNumberByOrderDate")
    public Result updateNumberByOrderDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.updateNumberByOrderDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
