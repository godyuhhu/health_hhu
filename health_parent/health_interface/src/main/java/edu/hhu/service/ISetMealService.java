package edu.hhu.service;

import edu.hhu.domain.Setmeal;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;

import java.util.List;

/**
 * 套餐的业务层接口
 */
public interface ISetMealService {
    //新增套餐
    void addSetMeal(Setmeal setmeal,Integer setMealIds[]);

    //分页查询
    PageResult findSetMealsWithPage(QueryPageBean queryPageBean);

    //查询所有套餐信息
    List<Setmeal> findAllSetMeals();

    //根据套餐id查询套餐的详细信息同时将所包含的检查组和检查项信息都查询出来
    Setmeal findSetMealById(Integer id);
}
