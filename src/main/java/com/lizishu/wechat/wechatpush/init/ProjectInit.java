package com.lizishu.wechat.wechatpush.init;

import com.lizishu.wechat.wechatpush.dao.SysUserDao;
import com.lizishu.wechat.wechatpush.queue.user.UserQueue;
import com.lizishu.wechat.wechatpush.util.AccessTokenUtil;
import com.lizishu.wechat.wechatpush.util.redis.Redis;
import com.lizishu.wechat.wechatpush.util.redis.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by lijiacheng
 * @date 2021/4/16.
 */
@Component
public class ProjectInit implements ApplicationRunner{
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    @Value("${wechat.accessTokenUrl}")
    private String accessTokenUrl;
    //应用ID
    @Value("${wechat.appId}")
    private String appId;
    //开发者密钥
    @Value("${wechat.secret}")
    private String secret;

    //获取用户信息的接口地址（通过UID机制）
    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    //注入用户模块的数据层依赖
    @Autowired
    private SysUserDao userDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Redis> redisList = new ArrayList<>();
        //127.0.0.1:6379
        redisList.add(new Redis(host + ":" + port , password));
        RedisUtil.init(redisList);
        System.out.println("初始化Redis成功");

        //为工具类的相关字段，赋值
        AccessTokenUtil.accessTokenUrl =this.accessTokenUrl;
        AccessTokenUtil.appId = this.appId;
        AccessTokenUtil.secret = this.secret;

        //绑定阻塞队列的变量值
        UserQueue.userInfoUrl = this.userInfoUrl;
        UserQueue.userDao = this.userDao;
        //唤醒后台任务
        UserQueue.listen();
    }
}
