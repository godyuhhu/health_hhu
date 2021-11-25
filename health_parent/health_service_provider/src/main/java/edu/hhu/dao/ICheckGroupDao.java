package edu.hhu.dao;

import com.github.pagehelper.Page;
import edu.hhu.domain.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * 检查组的持久层接口
 */
public interface ICheckGroupDao {

    //新增检查项基本信息
    void addCheckGroup(CheckGroup checkGroup);

    //新增检查项和检查组的关联关系
    void addCheckGroupAndCheckItemRel(Map<String, Integer> map);

    //分页查询
    Page<CheckGroup> findCheckGroupsWithPage(String queryString);

    //检查组基本信息回显
    CheckGroup findCheckGroupById(Integer checkGroupId);

    //检查组所关联的检查项回显
    List<Integer> findCheckItemsByCheckGroupId(Integer checkGroupId);

    //编辑检查组基本信息
    void editCheckGroup(CheckGroup checkGroup);

    //删除原有检查项的关联关系
    void deleteCheckItemAndCheckGroupRel(Integer id);

    //删除检查组基本信息
    void deleteCheckGroupById(Integer id);

    //查询所有检查组信息
    List<CheckGroup> findAllCheckGroups();
}
