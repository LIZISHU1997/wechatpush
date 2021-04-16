package com.lizishu.wechat.wechatpush.util.redis;

import lombok.Data;

/**
 * @author by lijiacheng
 * @date 2021/4/16.
 */
@Data
public class Redis {
    /**
     * 地址
     */
    private String address;

    /**
     * 密码
     */
    private String password;

    public Redis(String address) {
        this.address = address;
    }

    public Redis(String address, String password) {
        //连接地址，是由host和port组成的
        this.address = address;
        this.password = password;
    }
}
