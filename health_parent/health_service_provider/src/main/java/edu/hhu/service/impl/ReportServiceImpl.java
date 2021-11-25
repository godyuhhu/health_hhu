package edu.hhu.service.impl;

import edu.hhu.dao.IMemberDao;
import edu.hhu.dao.IOrderDao;
import edu.hhu.service.IReportService;
import edu.hhu.utils.DateUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 获得运营统计数据
 * Map数据格式：
 * today -> string
 * todayNewMember -> number
 * totalMember -> number
 * thisWeekNewMember -> number
 * thisMonthNewMember -> number
 * todayOrderNumber -> number
 * todayVisitsNumber -> number
 * thisWeekOrderNumber -> number
 * thisWeekVisitsNumber -> number
 * thisMonthOrderNumber -> number
 * thisMonthVisitsNumber -> number
 * hotSetmeal -> List<Setmeal>
 */
@Service
public class ReportServiceImpl implements IReportService {
    @Autowired
    private IMemberDao memberDao;
    @Autowired
    private IOrderDao orderDao;
    //获取运营统计数据
    @Override
    public Map<String, Object> findBusinessReportData() throws Exception {
        Map<String,Object> map = new HashMap<>();
        //获取当天的日期(yyyy-MM-dd)
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //获取今天新增的会员数
        Integer todayNewMember = memberDao.findTodayNewMember(today);
        //获取总会员数
        Integer totalMember = memberDao.findTotalMember();
        //获取这周新增的会员数(获取本周的第一天)
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        Integer thisWeekNewMember = memberDao.findThisWeekNewMember(thisWeekMonday);
        //获取本月新增的会员数(获取本月的第一天);
        String firstDayOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer thisMonthNewMember = memberDao.findThisMonthNewMember(firstDayOfMonth);
        //获取今天预约数
        Integer todayOrderNumber = orderDao.findTodayOrderNumber(today);
        //今日到诊数
        Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(today);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findThisWeekOrderNumber(thisWeekMonday);
        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findThisMonthOrderNumber(firstDayOfMonth);
        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVThisWeekVisitsNumber(thisWeekMonday);
        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findThisMonthVisitsNumber(firstDayOfMonth);
        //获取本月热销套餐(取前三)
        List<Map<String,Object>> hotSetMeal = orderDao.findHotSetMeal();
        map.put("reportDate",today);
        map.put("todayNewMember",todayNewMember);
        map.put("totalMember",totalMember);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("thisMonthNewMember",thisMonthNewMember);
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("hotSetMeal",hotSetMeal);



        return map;
    }

}
