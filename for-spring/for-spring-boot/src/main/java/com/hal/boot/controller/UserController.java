package com.hal.boot.controller;

import com.hal.boot.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") Integer id){
         User user = User.getInstance();
         user.setId(id);
         return user;
    }
}
