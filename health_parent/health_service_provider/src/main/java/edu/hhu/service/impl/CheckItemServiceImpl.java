package edu.hhu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import edu.hhu.dao.ICheckItemDao;
import edu.hhu.domain.CheckItem;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;
import edu.hhu.service.ICheckItemService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 检查项的业务层实现类
 */
@Service//dubbo 远程注册服务
public class CheckItemServiceImpl implements ICheckItemService {
    @Autowired
    private ICheckItemDao checkItemDao;

    /**
     * 新增一个检查项
     */
    @Override
    public void addCheckItem(CheckItem checkItem) {
        checkItemDao.addCheckItem(checkItem);
    }

    @Override
    public PageResult findCheckItemsWithPage(QueryPageBean queryPageBean) {
        //获取当前页码
        Integer currentPage = queryPageBean.getCurrentPage();
        //获取每页记录数
        Integer pageSize = queryPageBean.getPageSize();
        //获取查询条件
        String queryString = queryPageBean.getQueryString();
        //对查询条件进行判断 以便可以模糊查询
        if (queryString != null && queryString.length() > 0){
            queryString = "%" + queryString + "%";
        }
        //调用pageHelper完成分页查询
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.findCheckItemsWithPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void deleteCheckItem(Integer id) {
        //1.先检查该检查项是否存在关联关系，if存在关联关系不能删除该检查项
        Integer CheckGroupId = checkItemDao.findCheckItemRelation(id);
        if (CheckGroupId != null){
            //存在关联关系
            throw new RuntimeException();
        }else{
            checkItemDao.deleteCheckItemById(id);
        }
    }

    @Override
    public CheckItem findCheckItemById(Integer checkItemId) {
        return checkItemDao.findCheckItemById(checkItemId);
    }

    /**
     * 修改检查项信息
     */
    @Override
    public void editCheckItem(CheckItem checkItem) {
        checkItemDao.editCheckItem(checkItem);
    }

    /**
     * 查询所有检查项信息
     */
    @Override
    public List<CheckItem> findAllCheckItems() {
        return checkItemDao.findAllCheckItems();
    }
}
