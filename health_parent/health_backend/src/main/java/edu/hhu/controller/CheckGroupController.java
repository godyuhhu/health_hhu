package edu.hhu.controller;

import edu.hhu.constant.MessageConstant;
import edu.hhu.domain.CheckGroup;
import edu.hhu.entity.PageResult;
import edu.hhu.entity.QueryPageBean;
import edu.hhu.entity.Result;
import edu.hhu.service.ICheckGroupService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    ICheckGroupService checkGroupService;

    @RequestMapping("/addCheckGroup.do")
    public Result addCheckGroup(Integer checkItemIds[], @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.addCheckGroup(checkItemIds,checkGroup);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }
    /**
     * 分页查询
     */
    @RequestMapping("/findCheckGroupsWithPage.do")
    public PageResult findCheckGroupsWithPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.findCheckGroupsWithPage(queryPageBean);
    }
    /**
     * 检查组基本信息回显
     */
    @RequestMapping("/findCheckGroupById.do")
    public Result findCheckGroupById(Integer checkGroupId){
        try {
            CheckGroup checkGroup = checkGroupService.findCheckGroupById(checkGroupId);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    /**
     * 检查组所包含的检查项信息回显
     */
    @RequestMapping("/findCheckItemsByCheckGroupId.do")
    public Result findCheckItemsByCheckGroupId(Integer checkGroupId){
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemsByCheckGroupId(checkGroupId);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 编辑检查项信息
     */
    @RequestMapping("/editCheckGroup.do")
    public Result editCheckGroup(@RequestBody CheckGroup checkGroup, Integer checkItemIds[]){
        try {
            checkGroupService.editCheckGroup(checkGroup,checkItemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 删除检查项
     */
    @RequestMapping("/deleteCheckGroup.do")
    public Result deleteCheckGroup(Integer id){
        try {
            checkGroupService.deleteCheckGroup(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }
    /**
     * 查询所有检查项
     */
    @RequestMapping("/findAllCheckGroups.do")
    public Result findAllCheckGroups(){
        try {
            List<CheckGroup> checkGroups = checkGroupService.findAllCheckGroups();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
