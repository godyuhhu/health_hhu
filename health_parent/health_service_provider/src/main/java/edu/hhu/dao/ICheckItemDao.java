package edu.hhu.dao;

import com.github.pagehelper.Page;
import edu.hhu.domain.CheckItem;

import java.util.List;

/**
 * 检查项的持久层接口
 */
public interface ICheckItemDao {
    //新增检查项
    void addCheckItem(CheckItem checkItem);

    //查询检查项
    Page<CheckItem> findCheckItemsWithPage(String queryString);

    //查询关联关系
    Integer findCheckItemRelation(Integer id);

    //删除检查项
    void deleteCheckItemById(Integer id);

    //根据id查询检查项
    CheckItem findCheckItemById(Integer checkItemId);

    //编辑检查项信息
    void editCheckItem(CheckItem checkItem);

    //查询所有检查项信息
    List<CheckItem> findAllCheckItems();
}
