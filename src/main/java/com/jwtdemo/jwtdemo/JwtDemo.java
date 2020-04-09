package com.jwtdemo.jwtdemo;


import com.alibaba.fastjson.JSON;
import com.idsmanager.dingdang.jwt.DingdangUserRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class JwtDemo {
    private static Logger logger = LoggerFactory.getLogger(JwtDemo.class);



    private static String publicKey = "{\"kty\":\"RSA\",\"kid\":\"2296479403213002694\",\"alg\":\"RS256\",\"n\":\"oIcT2CUCePZ32B5tdtkdFvkkgr69M-6ahuMTQd9B1x8BGp1-TomvQ1R9f9vC7cpDWOXZ_YNotdA7YKBcLXSnWUo9ljpesAI_FzHb6wGMDMljgLPyRiYRG--360VULKR_XNVoAHM3VuheFHq-NTUX0A2c78HU-gTX3QiD-gQbXNhI-fmFIwC9uUNlcbGZKhIDJYxWvLA_umJYHgOiiTI7pKgWSZo9oAfXY6zWDkmXMzUqOte1BPIukT9CwHUG_5k52AMXgH0ToxwqliELzo9n2zR2fYZLNL_W4DeoSo38a8P87W8gG0lwHH4HLNjQw-_TIEqazI8kfaYfNiqQX9b5kw\",\"e\":\"AQAB\"}";

    @RequestMapping(value = "/jwt/sso/login")
    public String ssoUrl(@RequestParam String id_token, String redirect_url, Model model, HttpServletRequest request) {
        DingdangUserRetriever retriever = new DingdangUserRetriever(id_token, publicKey);
        DingdangUserRetriever.User user = null;
        try {
            //2.获取用户信息
            user = retriever.retrieve();
            if(user != null) {
                logger.info("applist====" + JSON.toJSONString(user.getAppListJson()));
                logger.info("user====" + JSON.toJSONString(user));
                logger.info("payLoad====" + JSON.toJSONString(user.payload()));
            }
        } catch (Exception e) {
            logger.warn("Retrieve SSO user failed" , e);
            return "error";
        }
        //3.判断用户名是否在自己系统存在isExistedUsername()方法为业务系统自行判断数据库中是否存在
        if (user != null && user.getUsername() != null) {
            logger.info(user.getUsername());
            model.addAttribute("user", user);
            return "欢迎你，" + user.getUsername();
        } else {
            //7.如果不存在,返回登录失败页面,提示用户不存在
//            model.addAttribute("error", "username { " + user.getUsername() + " } not exist");
            return "error";
        }
    }


}
