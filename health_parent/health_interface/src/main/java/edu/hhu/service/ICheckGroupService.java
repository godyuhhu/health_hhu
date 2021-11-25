package edu.hhu.service;

import edu.hhu.domain.CheckGroup;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;

import java.util.List;

/**
 * 检查组的业务层接口
 */
public interface ICheckGroupService {
    //新增检查组
    void addCheckGroup(Integer[] checkItemIds, CheckGroup checkGroup);

    //分页查询
    PageResult findCheckGroupsWithPage(QueryPageBean queryPageBean);

    //检查组基本信息回显
    CheckGroup findCheckGroupById(Integer checkGroupId);

    //查询所关联的检查项id
    List<Integer> findCheckItemsByCheckGroupId(Integer checkGroupId);

    //编辑检查组信息
    void editCheckGroup(CheckGroup checkGroup, Integer[] checkItemIds);

    //删除检查组
    void deleteCheckGroup(Integer id);

    //查询所有检查组
    List<CheckGroup> findAllCheckGroups();
}
