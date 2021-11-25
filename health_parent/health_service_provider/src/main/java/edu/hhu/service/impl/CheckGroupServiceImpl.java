package edu.hhu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import edu.hhu.dao.ICheckGroupDao;
import edu.hhu.dao.ICheckItemDao;
import edu.hhu.domain.CheckGroup;
import edu.hhu.domain.CheckItem;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;
import edu.hhu.service.ICheckGroupService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckGroupServiceImpl implements ICheckGroupService {

    @Autowired
    ICheckGroupDao checkGroupDao;

    /**
     * 新增检查组 (多个检查项的集合)
     * 1.新增基本信息 2.新增关联关系
     */
    @Override
    public void addCheckGroup(Integer[] checkItemIds, CheckGroup checkGroup) {
        //新增基本信息
        checkGroupDao.addCheckGroup(checkGroup);
        //当检查信息不为空时,新增关联关系
        if (checkItemIds != null && checkItemIds.length > 0) {
            //获取当前检查组的id值
            Integer checkGroupId = checkGroup.getId();
            //定义一个Map集合存储检查组和检查项的关联关系
            Map<String, Integer> map = new HashMap<>();
            map.put("checkGroupId", checkGroupId);
            //新增关联关系
            for (Integer checkItemId : checkItemIds) {
                map.put("checkItemId", checkItemId);
                checkGroupDao.addCheckGroupAndCheckItemRel(map);
            }
        }

    }

    /**
     * 分页查询
     */
    @Override
    public PageResult findCheckGroupsWithPage(QueryPageBean queryPageBean) {
        //获取当前页码
        Integer currentPage = queryPageBean.getCurrentPage();
        //获取每页记录数
        Integer pageSize = queryPageBean.getPageSize();
        //获取查询条件
        String queryString = queryPageBean.getQueryString();
        //对查询条件进行判断 以便可以模糊查询
        if (queryString != null && queryString.length() > 0) {
            queryString = "%" + queryString + "%";
        }
        //调用pageHelper完成分页查询
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.findCheckGroupsWithPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 检查组基本信息回显
     */
    @Override
    public CheckGroup findCheckGroupById(Integer checkGroupId) {
        return checkGroupDao.findCheckGroupById(checkGroupId);
    }

    /**
     * 所关联的检查项信息回显
     */
    @Override
    public List<Integer> findCheckItemsByCheckGroupId(Integer checkGroupId) {
        return checkGroupDao.findCheckItemsByCheckGroupId(checkGroupId);
    }

    /**
     * 编辑检查项
     */
    @Override
    public void editCheckGroup(CheckGroup checkGroup, Integer[] checkItemIds) {
        //1.编辑基本信息
        checkGroupDao.editCheckGroup(checkGroup);
        //2.编辑该检查组所包含的检查项信息
        //2.1删除原有的关联关系
        checkGroupDao.deleteCheckItemAndCheckGroupRel(checkGroup.getId());
        if (checkItemIds != null && checkItemIds.length > 0) {
            //2.2建立新的关联关系
            //获取当前检查组的id值
            Integer checkGroupId = checkGroup.getId();
            //定义一个Map集合存储检查组和检查项的关联关系
            Map<String, Integer> map = new HashMap<>();
            map.put("checkGroupId", checkGroupId);
            //新增关联关系
            for (Integer checkItemId : checkItemIds) {
                map.put("checkItemId", checkItemId);
                checkGroupDao.addCheckGroupAndCheckItemRel(map);
            }
        }
    }

    /**
     * 删除检查组基本信息
     */
    @Override
    public void deleteCheckGroup(Integer id) {
        //1.删除该检查组的关联信息
        checkGroupDao.deleteCheckItemAndCheckGroupRel(id);
        //2.删除基本信息
        checkGroupDao.deleteCheckGroupById(id);
    }

    /**
     * 查询所有检查组信息
     */
    @Override
    public List<CheckGroup> findAllCheckGroups() {
        return checkGroupDao.findAllCheckGroups();
    }
}
