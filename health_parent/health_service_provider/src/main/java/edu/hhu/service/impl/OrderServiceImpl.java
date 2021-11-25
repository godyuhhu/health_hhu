package edu.hhu.service.impl;

import edu.hhu.constant.MessageConstant;
import edu.hhu.dao.IMemberDao;
import edu.hhu.dao.IOrderDao;
import edu.hhu.dao.IOrderSettingDao;
import edu.hhu.domain.Member;
import edu.hhu.domain.Order;
import edu.hhu.domain.OrderSetting;
import edu.hhu.entity.Result;
import edu.hhu.service.IOrderService;
import edu.hhu.utils.DateUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderSettingDao orderSettingDao;
    @Autowired
    private IMemberDao memberDao;
    @Autowired
    private IOrderDao orderDao;

    /**
     * 对提交来的订单进行校验处理
     */
    @Override
    public Result processOrder(Map map) {
        try {
            //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
            // 根据预约日期查询当天的预约设置情况如果不存在则返回相应的结果
            String s_orderDate = (String) map.get("orderDate"); //原数据 yyyy-MM-dd 需要将其转化为 date类型
            Date orderDate = DateUtils.parseString2Date(s_orderDate);//获得预约时间(date类型)
            OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(orderDate);
            if (orderSetting == null) {
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
            int reservations = orderSetting.getReservations();//当前的预约人数
            int number = orderSetting.getNumber();//当天的最大人数
            if (reservations >= number) {
                return new Result(false, MessageConstant.ORDER_FULL);
            }
            // 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
            //3.1检查该用户是否为会员
            String telephone = (String) map.get("telephone");
            Member member = memberDao.findMemberByTelephone(telephone);
            //3.2if存在该会员 根据用户id,套餐id,预定时间进行查询如果有该预定信息 则提示重复预约
            Integer setmealId = Integer.parseInt((String) map.get("setmealId"));//获取套餐id
            if (member != null) {
                Integer memberId = member.getId();//获取会员id
                Order order = new Order(memberId, orderDate, setmealId);
                //根据该组合信息查询该订单是否存在
                List<Order> orders = orderDao.findOrdersByCondition(order);
                if (orders != null && orders.size() > 0) {
                    //已经存在该订单 则提示重复预约
                    return new Result(false, MessageConstant.HAS_ORDERED);
                }
            } else {
                //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
                //4.1该用户不是会员
                //自动注册该用户信息
                member = new Member();
                String name = (String) map.get("name");//用户名信息
                String sex = (String) map.get("sex"); // 用户性别
                String idCard = (String) map.get("idCard");//用户身份证
                member.setName(name);
                member.setIdCard(idCard);
                member.setSex(sex);
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberDao.addMember(member);
            }
            //5、预约成功,保存订单信息
            Order order = new Order(member.getId(), orderDate, Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO, setmealId);
            orderDao.addOrder(order);
            //更新当日的已预约人数
            orderSetting.setReservations(orderSetting.getReservations() + 1);
            orderSettingDao.updateReservations(orderSetting);
            return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SYSTEM_ERROR);
        }


    }

    //  根据id查询相应的订单信息
    @Override
    public Map findOrderById(Integer id) throws Exception {
        Map map = orderDao.findOrderById(id);
        if (map != null) {
            //对日期进行处理
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }

    //统计订单数据
    @Override
    public List<Map<String, Object>> findSetMealCount() {
        return orderDao.findSetMealCount();
    }
}
