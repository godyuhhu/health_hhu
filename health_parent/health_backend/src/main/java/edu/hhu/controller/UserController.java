package edu.hhu.controller;

import edu.hhu.constant.MessageConstant;
import edu.hhu.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/getUsername")
    public Result getUsername(){
        try {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null){
                String username = user.getUsername();
                return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
            }else{
                return new Result(false, MessageConstant.GET_USERNAME_FAIL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_ERROR);
        }
    }
}
