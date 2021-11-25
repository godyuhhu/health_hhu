package edu.hhu.controller;

import edu.hhu.constant.MessageConstant;
import edu.hhu.domain.Setmeal;
import edu.hhu.entity.Result;
import edu.hhu.service.ISetMealService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端的套餐预约的展示层
 */
@RestController
@RequestMapping("/setMeal")
public class SetMealController {
    @Reference
    private ISetMealService setMealService;
    /**
     * 加载套餐列表
     */
    @RequestMapping("/findAllSetMeals")
    public Result findAllSetMeals(){
        try {
            List<Setmeal> setMeals = setMealService.findAllSetMeals();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setMeals);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
    /**
     * 展示套餐的详细信息
     * 包括检查组和检查项信息
     */
    @RequestMapping("/findSetMealById")
    public Result findSetMealById(Integer id){
        try {
            Setmeal setmeal = setMealService.findSetMealById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
