package edu.hhu.controller;

import edu.hhu.constant.MessageConstant;
import edu.hhu.domain.CheckItem;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;
import edu.hhu.entity.Result;
import edu.hhu.service.ICheckItemService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.zookeeper.Op;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组控制器
 */
@RestController //等价于 @Controller + @ResponseBody
@RequestMapping("/checkItem")
public class CheckItemController {
    @Reference //从注册中心中远程调用服务
    private ICheckItemService checkItemService;
    /**
     * 新增一个检查项
     */
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")//权限校验
    @RequestMapping("/addCheckItem.do")
    public Result addCheckItem(@RequestBody CheckItem checkItem){ //@RequestBody 获取前端传过来的Json数据格式
        try {
            checkItemService.addCheckItem(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
    /**
     * 分页查询
     */
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findCheckItemsWithPage.do")
    public PageResult findCheckItemsWithPage(@RequestBody QueryPageBean queryPageBean){
        return checkItemService.findCheckItemsWithPage(queryPageBean);
    }
    /**
     * 删除检查项
     */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @RequestMapping("/deleteCheckItem.do")
    public Result deleteCheckItem(Integer id){
        try {
            checkItemService.deleteCheckItem(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }
    /**
     * 根据id查询检查项信息
     */
    @RequestMapping("/findCheckItemById.do")
    public Result findCheckItemById(Integer checkItemId){
        try {
            CheckItem checkItem = checkItemService.findCheckItemById(checkItemId);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    /**
     * 编辑检查项信息
     */
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @RequestMapping("/editCheckItem.do")
    public Result editCheckItem(@RequestBody CheckItem checkItem){
        try {
            checkItemService.editCheckItem(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }
    /**
     * 查询所有检查项信息
     */
    @RequestMapping("/findAllCheckItems.do")
    public Result findAllCheckItems(){
        try {
            List<CheckItem> checkItems = checkItemService.findAllCheckItems();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
