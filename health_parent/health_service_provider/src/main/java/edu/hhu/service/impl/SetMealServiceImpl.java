package edu.hhu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import edu.hhu.constant.RedisConstant;
import edu.hhu.dao.ISetMealDao;
import edu.hhu.domain.Setmeal;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;
import edu.hhu.service.ISetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐类的实现类接口
 */
@Service
public class SetMealServiceImpl implements ISetMealService {
    @Autowired
    private ISetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;//注入页面静态化的配置类
    @Value("${out_put_path}")
    private String outPutPath;//从属性文件中读取需要生成的HTML目录


    /**
     * 新增套餐信息，同时需要引入静态页面技术
     * 当套餐数据发生改变时，需要重新生成静态页面
     */
    @Override
    public void addSetMeal(Setmeal setmeal, Integer[] checkGroupIds) {
        //1.基本信息插入
        setMealDao.addSetMeal(setmeal);
        //1.2在redis中存储该对应相应的图片名
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        //2.新建关联关系
        if (checkGroupIds != null && checkGroupIds.length > 0){
            //2.1建立一个map集合用于存放关联关系
            Map<String,Integer> map = new HashMap<>();
            //2.2.获取套餐id
            Integer setMealId = setmeal.getId();
            map.put("setMealId",setMealId);
            //2.3 存入检查组id
            for (Integer checkGroupId: checkGroupIds) {
                map.put("checkGroupId",checkGroupId);
                //存入该关联关系
                setMealDao.addSetMealAndCheckGroupRel(map);
            }
        }
        //3.生成静态页面
        generateMobileStaticHtml();


    }

    /**
     * 分页查询
     */
    @Override
    public PageResult findSetMealsWithPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //对查询条件进行修改以便进行模糊查询
        if (queryString != null && queryString.length()>0){
            queryString = "%" + queryString + "%";
        }
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setMealDao.findSetMealsWithPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 加载套餐列表
     */
    @Override
    public List<Setmeal> findAllSetMeals() {
        return setMealDao.findAllSetMeals();
    }

    /**
     * 显示套餐的详细信息
     */
    @Override
    public Setmeal findSetMealById(Integer id) {
        return setMealDao.findSetMealById(id);
    }

    /**
     * 生成需要生成的静态页面
     */
    public void  generateMobileStaticHtml(){
        //1.准备数据
        List<Setmeal> setMeals = setMealDao.findAllSetMeals();
        //2.生成套餐列表的静态页面
        generateMobileSetmealListHtml(setMeals);
        //3.生成套餐详情的静态页面
        generateMobileSetmealDetailHtml(setMeals);
    }
    /**
     * 生成套餐列表静态页面
     */
    public void  generateMobileSetmealListHtml(List<Setmeal> setmeals){
        //向map数据中存储该数据 注意key值与模板中相对应
        Map<String,Object> map = new HashMap<>();
        map.put("setmealList",setmeals);
        //调用通用生成方法
        generateHTML("mobile_setmeal.ftl","m_setmeal.html",map);
    }
    /**
     * 生成套餐详情的静态页面
     */
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmeals){
        //向map数据中存储该数据 注意key值与模板中相对应
        Map<String,Object> map = new HashMap<>();
        for (Setmeal setmeal : setmeals) {
            //注意需要关联查询 则 需要调用findSetMealById方法获得封装好的Map集合
            map.put("setmeal",setMealDao.findSetMealById(setmeal.getId()));
            //调用通用生成方法
            generateHTML("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }
    /**
     * 通用生成静态页面
     */
    public void generateHTML(String templateName,String htmlPageName,Map<String,Object> map){
        Writer out = null;
        try {
            //1.生成配置类
            Configuration configuration = freeMarkerConfig.getConfiguration();
            //2.加载模板
            Template template = configuration.getTemplate(templateName);
            //3.创建write对象
            out = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            //4输出
            template.process(map,out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
