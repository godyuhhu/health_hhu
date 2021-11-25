package edu.hhu.controller;

import edu.hhu.constant.MessageConstant;
import edu.hhu.constant.RedisConstant;
import edu.hhu.domain.Setmeal;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;
import edu.hhu.entity.Result;
import edu.hhu.service.ISetMealService;
import edu.hhu.utils.QiniuUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * 套餐管理的表示层
 */
@RestController
@RequestMapping("/setMeal")
public class SetMealController {
    @Reference
    private ISetMealService setMealService;
    @Autowired
    private JedisPool jedisPool;
    /**
     * 套餐信息的图片上传工作
     */
    @RequestMapping("/upload.do")
    public Result upload(MultipartFile imgFile) {
        try {
            //1.设置图片的上传图片的文件名
            //1.1获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //1.2截取文件名后缀的.jpg
            //1.2.1获得最后一个点的位置
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //1.2.2截取字符串 获取文件后缀名
            String suffix = originalFilename.substring(lastIndexOf - 1);
            //1.2设置新文件名
            String fileName = UUID.randomUUID().toString() + suffix;
            //2.上传文件至七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //同时在redis缓存中记录该图片
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
    /**
     * 新增套餐工作
     */
    @RequestMapping("/addSetMeal.do")
    public Result addSetMeal(@RequestBody Setmeal setmeal,Integer checkGroupIds[]){
        try {
            setMealService.addSetMeal(setmeal,checkGroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }
    /**
     * 分页查询
     */
    @RequestMapping("/findSetMealsWithPage.do")
    public PageResult findSetMealsWithPage(@RequestBody QueryPageBean queryPageBean){
        return setMealService.findSetMealsWithPage(queryPageBean);
    }
}
