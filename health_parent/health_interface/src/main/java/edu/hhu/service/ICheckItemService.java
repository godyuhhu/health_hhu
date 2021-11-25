package edu.hhu.service;

import edu.hhu.domain.CheckItem;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;

import java.util.List;

/**
 * 检查项的业务层接口
 */
public interface ICheckItemService {
    /**
     * 新增一个检查项
     */
    void addCheckItem(CheckItem checkItem);

    /**
     * 分页条件查询
     */
    PageResult findCheckItemsWithPage(QueryPageBean queryPageBean);

    /**
     * 删除检查项
     */
    void deleteCheckItem(Integer id);

    /**
     * 根据id查询对应的检查项
     * @return
     */
    CheckItem findCheckItemById(Integer checkItemId);

    /**
     * 修改检查项信息
     * @return
     */
    void editCheckItem(CheckItem checkItem);

    /**
     * 查询所有检查项信息
     */
    List<CheckItem> findAllCheckItems();
}
