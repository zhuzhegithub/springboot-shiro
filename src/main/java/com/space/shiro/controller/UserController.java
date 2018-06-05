package com.space.shiro.controller;

import com.space.shiro.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhuzhe
 * @date 2018/6/5 14:43
 * @email 1529949535@qq.com
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @RequiresPermissions("user:add")
    @PostMapping("/add")
    public String add(User user){
        log.info(user.toString());
        return "add-ok";
    }

    @RequiresPermissions("user:update")
    @PostMapping("/update")
    public String update(User user){
        log.info(user.toString());
        return "update-ok";
    }

    @RequiresPermissions("user:delete")
    @GetMapping("/delete/{id}")
    public String add(@PathVariable("id") Integer id){
        log.info(id.toString());
        return "delete-ok";
    }

    @RequiresPermissions("user:query")
    @GetMapping("/list")
    public String getAll(){
        return "getAll-ok";
    }
}
