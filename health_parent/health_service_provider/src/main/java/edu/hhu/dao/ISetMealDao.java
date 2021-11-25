package edu.hhu.dao;

import com.github.pagehelper.Page;
import edu.hhu.domain.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐的持久层接口
 */
public interface ISetMealDao {

    //1.新增套餐基本信息
    void addSetMeal(Setmeal setmeal);
    //2.新增关联关系
    void addSetMealAndCheckGroupRel(Map<String, Integer> map);
    //3.分页查询
    Page<Setmeal> findSetMealsWithPage(String queryString);
    //4.查询所有套餐
    List<Setmeal> findAllSetMeals();
    //5.显示套餐的详细信息
    Setmeal findSetMealById(Integer id);
}
