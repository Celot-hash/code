package com.zjq.javacode.controller;

import com.zjq.javacode.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "/login.html";
    }

    @RequestMapping("/login")//HttpServletRequest request,HttpSession session
    @ResponseBody
    public String login(HttpServletRequest req, HttpServletResponse rep,HttpSession session, Model model){
        String name = req.getParameter("name");
        String passwd = req.getParameter("password");
        if("admin".equals(name)&&"123456".equals(passwd)) {
            User user=new User();
            user.setName(name);
            user.setPasswd(passwd);
            session.setAttribute("User",user);
            return "success";
        }
        else
            return "error";
    }

}
